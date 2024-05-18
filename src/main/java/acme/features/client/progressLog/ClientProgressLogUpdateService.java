
package acme.features.client.progressLog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.Configuration;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;
import spam_detector.SpamDetector;

@Service
public class ClientProgressLogUpdateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Client client;
		ProgressLog pl;

		id = super.getRequest().getData("id", int.class);
		pl = this.repository.findOneProgressLogById(id);

		client = pl == null ? null : pl.getContract().getClient();
		status = pl != null && !pl.isPublished() && super.getRequest().getPrincipal().hasRole(client);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProgressLogById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog object) {
		assert object != null;

		Date registrationMoment;
		registrationMoment = MomentHelper.getCurrentMoment();

		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("contract", int.class);
		contract = this.repository.findOneContractById(contractId);

		object.setRegistrationMoment(registrationMoment);
		object.setContract(contract);
		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;

		Configuration config = this.repository.findConfiguration();
		String spamTerms = config.getSpamTerms();
		Double spamThreshold = config.getSpamThreshold();
		SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);

		if (!super.getBuffer().getErrors().hasErrors("contract"))
			super.state(!object.getContract().isPublished(), "contract", "validation.progresslog.published.contract-is-published");

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLog isCodeUnique;
			isCodeUnique = this.repository.findProgressLogByCodeDifferentId(object.getRecordId(), object.getId());
			super.state(isCodeUnique == null, "recordId", "validation.progresslog.code.duplicate");

			if (!super.getBuffer().getErrors().hasErrors("published"))
				super.state(!object.isPublished(), "published", "validation.progresslog.published");
		}

		if (!super.getBuffer().getErrors().hasErrors("comment"))
			super.state(!spamHelper.isSpam(object.getComment()), "comment", "client.progresslog.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("responsiblePerson"))
			super.state(!spamHelper.isSpam(object.getResponsiblePerson()), "responsiblePerson", "client.progresslog.form.error.spam");

	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		SelectChoices contracts;
		Dataset dataset;

		int clientId;
		clientId = super.getRequest().getPrincipal().getActiveRoleId();

		Collection<Contract> allContracts = this.repository.findAllMyContracts(clientId);
		contracts = SelectChoices.from(allContracts, "code", object.getContract());

		dataset = super.unbind(object, "recordId", "completeness", "comment", "responsiblePerson", "published");
		dataset.put("contract", contracts.getSelected().getKey());
		dataset.put("contracts", contracts);

		super.getResponse().addData(dataset);
	}

}
