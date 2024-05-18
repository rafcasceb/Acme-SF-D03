
package acme.features.client.contract;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.Configuration;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;
import spam_detector.SpamDetector;

@Service
public class ClientContractUpdateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService<Auditor, CodeAudit> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Client client;
		Contract contract;

		id = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractById(id);

		client = contract == null ? null : contract.getClient();
		status = contract != null && !contract.isPublished() && super.getRequest().getPrincipal().hasRole(client);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;
		Date instantiationMoment;
		instantiationMoment = MomentHelper.getCurrentMoment();

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(id);
		object.setInstantiationMoment(instantiationMoment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		object.setProject(project);
		super.bind(object, "code", "providerName", "customerName", "goals", "budget");
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

		Configuration config = this.repository.findConfiguration();
		String spamTerms = config.getSpamTerms();
		Double spamThreshold = config.getSpamThreshold();
		SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(!object.getProject().isPublished(), "project", "validation.contract.published.project-is-published");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract isCodeUnique;
			isCodeUnique = this.repository.findContractByCodeDifferentId(object.getCode(), object.getId());
			super.state(isCodeUnique == null, "code", "client.contract.form.error.duplicate");
		}
		if (!super.getBuffer().getErrors().hasErrors("published"))
			super.state(!object.isPublished(), "published", "client.contract.form.error.already-published");

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() >= 0., "budget", "client.contract.form.error.budgetPositive");

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() <= 1000000., "budget", "client.contract.form.error.budgetRange");

		String currencies = this.repository.findAcceptedCurrencies();
		String[] acceptedCurrencies = currencies.split(",");
		Stream<String> streamCurrencies = Arrays.stream(acceptedCurrencies);
		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget() != null && streamCurrencies.anyMatch(currency -> currency.equals(object.getBudget().getCurrency())), "budget", "client.contract.form.error.currency");

		if (!super.getBuffer().getErrors().hasErrors("providerName"))
			super.state(!spamHelper.isSpam(object.getProviderName()), "providerName", "client.contract.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("customerName"))
			super.state(!spamHelper.isSpam(object.getCustomerName()), "customerName", "client.contract.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(!spamHelper.isSpam(object.getGoals()), "goals", "client.contract.form.error.spam");
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		SelectChoices projects;
		Dataset dataset;

		Collection<Project> allProjects = this.repository.findAllProjects();
		projects = SelectChoices.from(allProjects, "code", object.getProject());

		dataset = super.unbind(object, "published", "code", "providerName", "customerName", "goals", "budget");
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);

		super.getResponse().addData(dataset);
	}
}
