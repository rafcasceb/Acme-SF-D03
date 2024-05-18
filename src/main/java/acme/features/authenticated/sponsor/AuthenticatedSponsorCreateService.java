
package acme.features.authenticated.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorCreateService extends AbstractService<Authenticated, Sponsor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedSponsorRepository repository;

	// AbstractService interface ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Sponsor object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Sponsor();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsor object) {
		assert object != null;

		super.bind(object, "name", "expectedBenefitsList", "email", "link");
	}

	@Override
	public void validate(final Sponsor object) {
		assert object != null;
	}

	@Override
	public void perform(final Sponsor object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsor object) {
		Dataset dataset;

		dataset = super.unbind(object, "name", "expectedBenefitsList", "email", "link");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
