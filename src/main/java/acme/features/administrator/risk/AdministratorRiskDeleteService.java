
package acme.features.administrator.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.services.AbstractService;
import acme.entities.risks.Risk;

@Service
public class AdministratorRiskDeleteService extends AbstractService<Administrator, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRiskRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Risk risk;

		masterId = super.getRequest().getData("id", int.class);
		risk = this.repository.findOneRiskById(masterId);
		status = risk != null && super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Risk object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneRiskById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Risk object) {
		assert object != null;

		super.bind(object, "reference", "identificationDate", "impact", "probability", "description", "link");
	}

	@Override
	public void validate(final Risk object) {
		assert object != null;
	}

	@Override
	public void perform(final Risk object) {
		assert object != null;

		this.repository.delete(object);
	}

	// The unbind method is not overrode since it will never be called. There is no validation beyond the authorisation.

}
