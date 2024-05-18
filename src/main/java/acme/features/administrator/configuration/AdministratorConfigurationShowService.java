
package acme.features.administrator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;

@Service
public class AdministratorConfigurationShowService extends AbstractService<Administrator, Configuration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorConfigurationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Configuration object;

		object = this.repository.findConfiguration();

		super.getBuffer().addData(object);
	}

	@Override
	public void validate(final Configuration object) {
		assert object != null;
	}

	@Override
	public void unbind(final Configuration object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "defaultCurrency", "acceptedCurrencies", "spamTerms", "spamThreshold");

		super.getResponse().addData(dataset);
	}

}
