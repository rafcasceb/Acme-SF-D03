
package acme.features.authenticated.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.configuration.Configuration;
import acme.roles.Client;
import acme.roles.ClientType;
import spam_detector.SpamDetector;

@Service
public class AuthenticatedClientCreateService extends AbstractService<Authenticated, Client> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedClientRepository repository;

	// AbstractService interface ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Client object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Client();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Client object) {
		assert object != null;

		super.bind(object, "identification", "companyName", "type", "email", "link");
	}

	@Override
	public void validate(final Client object) {
		assert object != null;

		Configuration config = this.repository.findConfiguration();
		String spamTerms = config.getSpamTerms();
		Double spamThreshold = config.getSpamThreshold();
		SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);

		if (!super.getBuffer().getErrors().hasErrors("identification")) {
			Client isCodeUnique;
			isCodeUnique = this.repository.findClientByCode(object.getIdentification());
			super.state(isCodeUnique == null, "identification", "validation.client.code.duplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("companyName"))
			super.state(!spamHelper.isSpam(object.getCompanyName()), "companyName", "client.form.error.spam");
	}

	@Override
	public void perform(final Client object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Client object) {
		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(ClientType.class, object.getType());
		dataset = super.unbind(object, "identification", "companyName", "type", "email", "link");
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
