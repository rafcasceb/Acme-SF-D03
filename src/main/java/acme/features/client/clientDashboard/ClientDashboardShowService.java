
package acme.features.client.clientDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int clientId;
		ClientDashboard dashboard;

		clientId = super.getRequest().getPrincipal().getActiveRoleId();
		int percentil1;
		int percentil2;
		int percentil3;
		int percentil4;
		Double avgBudget;
		Double stddevBudget;
		Double maxBudget;
		Double minBudget;
		String defaultCurrency = this.repository.findDefaultCurrency();

		percentil1 = this.repository.firstPercentileProgressLogCompleteness(clientId);
		percentil2 = this.repository.secondPercentileProgressLogCompleteness(clientId);
		percentil3 = this.repository.thirdPercentileProgressLogCompleteness(clientId);
		percentil4 = this.repository.fourthPercentileProgressLogCompleteness(clientId);
		avgBudget = this.repository.avgAmountBudget(clientId, defaultCurrency);
		stddevBudget = this.repository.stddevAmountBudget(clientId, defaultCurrency);
		maxBudget = this.repository.maxAmountBudget(clientId, defaultCurrency);
		minBudget = this.repository.minAmountBudget(clientId, defaultCurrency);

		int[] percentilArray = new int[4];
		percentilArray[0] = percentil1;
		percentilArray[1] = percentil2;
		percentilArray[2] = percentil3;
		percentilArray[3] = percentil4;

		dashboard = new ClientDashboard();
		dashboard.setProgressLogsCounter(percentilArray);
		dashboard.setContractBudgetAverage(avgBudget);
		dashboard.setContractBudgetDeviation(stddevBudget);
		dashboard.setMaximumBudget(maxBudget);
		dashboard.setMinimumBudget(minBudget);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;

		Dataset dataset;
		int firstP = object.getProgressLogsCounter()[0];
		int secondP = object.getProgressLogsCounter()[1];
		int thirdP = object.getProgressLogsCounter()[2];
		int fourthP = object.getProgressLogsCounter()[3];

		dataset = super.unbind(object, //
			"contractBudgetAverage", "contractBudgetDeviation", //
			"minimumBudget", "maximumBudget");
		dataset.put("completenessFirstPercentile", firstP);
		dataset.put("completenessSecondPercentile", secondP);
		dataset.put("completenessThirdPercentile", thirdP);
		dataset.put("completenessFourthPercentile", fourthP);

		super.getResponse().addData(dataset);
	}

}
