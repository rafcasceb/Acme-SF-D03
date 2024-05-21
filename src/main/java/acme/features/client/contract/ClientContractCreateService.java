
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
import acme.components.SpamDetector;
import acme.entities.configuration.Configuration;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Contract object;
		Client client;
		Date instantiationMoment;
		instantiationMoment = MomentHelper.getCurrentMoment();

		client = this.repository.findOneClientById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Contract();
		object.setClient(client);
		object.setInstantiationMoment(instantiationMoment);
		object.setPublished(false);

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
			Contract contractSameCode;
			contractSameCode = this.repository.findContractByCode(object.getCode());
			super.state(contractSameCode == null, "code", "client.contract.form.error.duplicate");
		}
		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() >= 0., "budget", "client.contract.form.error.budgetPositive");

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() <= 1000000., "budget", "client.contract.form.error.budgetRange");

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			String currencies = this.repository.findAcceptedCurrencies();
			String[] acceptedCurrencies = currencies.split(",");
			Stream<String> streamCurrencies = Arrays.stream(acceptedCurrencies);
			super.state(object.getBudget() != null && streamCurrencies.anyMatch(currency -> currency.trim().equals(object.getBudget().getCurrency())), "budget", "client.contract.form.error.currency");
		}

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
