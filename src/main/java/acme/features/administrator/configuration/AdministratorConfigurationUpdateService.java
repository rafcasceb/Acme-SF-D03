
package acme.features.administrator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;

@Service
public class AdministratorConfigurationUpdateService extends AbstractService<Administrator, Configuration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorConfigurationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Configuration configuration;

		configuration = this.repository.findConfiguration();
		status = configuration != null && super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Configuration object;

		object = this.repository.findConfiguration();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Configuration object) {
		assert object != null;

		super.bind(object, "defaultCurrency", "acceptedCurrencies", "spamTerms", "spamThreshold");
	}

	@Override
	public void validate(final Configuration object) {
		assert object != null;
	}

	@Override
	public void perform(final Configuration object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Configuration object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "defaultCurrency", "acceptedCurrencies", "spamTerms", "spamThreshold");

		super.getResponse().addData(dataset);
	}

}
