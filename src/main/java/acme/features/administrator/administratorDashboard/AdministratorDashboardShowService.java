
package acme.features.administrator.administratorDashboard;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.forms.AdministratorDashboard;

@Service
public class AdministratorDashboardShowService extends AbstractService<Administrator, AdministratorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int[] rolesCounter = new int[5];
		rolesCounter[0] = this.repository.totalNumberOfAuditors();
		rolesCounter[1] = this.repository.totalNumberOfClients();
		rolesCounter[2] = this.repository.totalNumberOfDevelopers();
		rolesCounter[3] = this.repository.totalNumberOfManagers();
		rolesCounter[4] = this.repository.totalNumberOfSponsors();

		int totalNumberOfNotice = this.repository.totalNumberNotices();
		int noticesWithEmailAndLink = this.repository.totalNumberNoticesWithBothEmailAndLink();
		Double ratioOfNotices = this.calculateAverage(noticesWithEmailAndLink, totalNumberOfNotice);

		int totalNumberOfObjectives = this.repository.totalNumberObjectives();
		int objectivesCrit = this.repository.totalNumberObjectivesCrit();
		int objectivesNonCrit = this.repository.totalNumberObjectivesNonCrit();
		Double ratioObjCrit = this.calculateAverage(objectivesCrit, totalNumberOfObjectives);
		Double ratioObjNonCrit = this.calculateAverage(objectivesNonCrit, totalNumberOfObjectives);

		Double avgImpact = this.repository.avgImpactRisk();
		Double avgProb = this.repository.avgProbRisk();
		Double avgValue = this.getValue(avgImpact, avgProb);

		Double stddevImpact = this.repository.stddevImpactRisk();
		Double stddevProb = this.repository.stddevProbRisk();
		Double stddevValue = this.getValue(stddevImpact, stddevProb);

		Double maxImpact = this.repository.maxImpactRisk();
		Double maxProb = this.repository.maxProbRisk();
		Double maxValue = this.getValue(maxImpact, maxProb);

		Double minImpact = this.repository.minImpactRisk();
		Double minProb = this.repository.minProbRisk();
		Double minValue = this.getValue(minImpact, minProb);

		Integer weeksOffset = -10;
		Date threshold = MomentHelper.deltaFromCurrentMoment(weeksOffset, ChronoUnit.WEEKS);
		double avgClaimsLastTenWeeks = this.repository.avgNumberOfClaimsPostedLastTenWeeks(threshold);
		double stddevClaimsLastTenWeeks = this.repository.deviationNumberOfClaimPostedLastTenWeeks(threshold);
		int maxClaimsTenWeeks = this.repository.maxNumberOfClaimsPostedLastTenWeeks();
		int minClaimsTenWeeks = this.repository.minNumberOfClaimsPostedLastTenWeeks();

		AdministratorDashboard dashboard = new AdministratorDashboard();
		dashboard.setPrincipalRolesCounter(rolesCounter);
		dashboard.setRatioOfNoticeWithLinkAndEmail(ratioOfNotices);
		dashboard.setRatioOfObjetiveCritical(ratioObjCrit);
		dashboard.setRatioOfObjetiveNonCritical(ratioObjNonCrit);
		dashboard.setAvgValueInRisks(avgValue);
		dashboard.setStanDevValueInRisks(stddevValue);
		dashboard.setMaximumValueInRisks(maxValue);
		dashboard.setMinimumValueInRisks(minValue);
		dashboard.setAvgNumberOfClaimsLastTenWeeks(avgClaimsLastTenWeeks);
		dashboard.setStanDevNumberOfClaimsLastTenWeeks(stddevClaimsLastTenWeeks);
		dashboard.setMaximumNumberOfClaimsLastTenWeeks(maxClaimsTenWeeks);
		dashboard.setMinimumNumberOfClaimsLastTenWeeks(minClaimsTenWeeks);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard object) {
		assert object != null;

		Dataset dataset;
		int numberAuditor = object.getPrincipalRolesCounter()[0];
		int numberClient = object.getPrincipalRolesCounter()[1];
		int numberDev = object.getPrincipalRolesCounter()[2];
		int numberMan = object.getPrincipalRolesCounter()[3];
		int numberSpon = object.getPrincipalRolesCounter()[4];

		dataset = super.unbind(object, //
			"ratioOfNoticeWithLinkAndEmail", "ratioOfObjetiveCritical", //
			"ratioOfObjetiveNonCritical", "avgValueInRisks", "stanDevValueInRisks", "maximumValueInRisks", "minimumValueInRisks", "avgNumberOfClaimsLastTenWeeks", "stanDevNumberOfClaimsLastTenWeeks", "maximumNumberOfClaimsLastTenWeeks",
			"minimumNumberOfClaimsLastTenWeeks");
		dataset.put("numberAuditor", numberAuditor);
		dataset.put("numberClient", numberClient);
		dataset.put("numberDev", numberDev);
		dataset.put("numberMan", numberMan);
		dataset.put("numberSpon", numberSpon);

		super.getResponse().addData(dataset);
	}

	private Double calculateAverage(final int sum, final int total) {
		return sum * 1. / total;
	}

	private Double getValue(final Double impact, final Double prob) {
		if (prob != null && impact != null)
			return impact * prob;
		else
			return null;
	}

}
