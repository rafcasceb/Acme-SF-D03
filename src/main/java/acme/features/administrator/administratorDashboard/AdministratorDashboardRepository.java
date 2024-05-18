
package acme.features.administrator.administratorDashboard;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.MomentHelper;
import acme.client.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select count(c) from Client c")
	int totalNumberOfClients();

	@Query("select count(a) from Auditor a")
	int totalNumberOfAuditors();

	@Query("select count(d) from Developer d")
	int totalNumberOfDevelopers();

	@Query("select count(m) from Manager m")
	int totalNumberOfManagers();

	@Query("select count(c) from Sponsor c")
	int totalNumberOfSponsors();

	@Query("select count(n) from Notice n")
	int totalNumberNotices();

	@Query("select count(n) from Notice n where n.email != null and n.link != null")
	int totalNumberNoticesWithBothEmailAndLink();

	@Query("select count(o) from Objective o")
	int totalNumberObjectives();

	@Query("select count(o) from Objective o where o.critical = false")
	int totalNumberObjectivesNonCrit();

	@Query("select count(o) from Objective o where o.critical = true")
	int totalNumberObjectivesCrit();

	@Query("select avg(r.impact) from Risk r")
	Double avgImpactRisk();

	@Query("select avg(r.probability) from Risk r")
	Double avgProbRisk();

	@Query("select stddev(r.impact) from Risk r")
	Double stddevImpactRisk();

	@Query("select stddev(r.probability) from Risk r")
	Double stddevProbRisk();

	@Query("select min(r.impact) from Risk r")
	Double minImpactRisk();

	@Query("select min(r.probability) from Risk r")
	Double minProbRisk();

	@Query("select max(r.impact) from Risk r")
	Double maxImpactRisk();

	@Query("select max(r.probability) from Risk r")
	Double maxProbRisk();

	@Query("select count(c) from Claim c where c.moment > :threshold")
	int numberOfClaimPostedAfterThreshold(Date threshold);

	@Query("select count(c) / 10.0 from Claim c where c.moment > :threshold")
	double avgNumberOfClaimsPostedLastTenWeeks(Date threshold);

	@Query("select count(c) from Claim c where c.moment > :farThreshold and c.moment <= :closeThreshold")
	int numberOfClaimPostedBetweenThresholds(Date farThreshold, Date closeThreshold);

	default double deviationNumberOfClaimPostedLastTenWeeks(final Date threshold) {
		double averageOfClaims = this.avgNumberOfClaimsPostedLastTenWeeks(threshold);
		double totalSum = 0;
		Date farThreshold = MomentHelper.getCurrentMoment();
		Date closeThreshold;

		for (int w = 1; w <= 10; w++) {
			Integer weeksOffset = -1 * w;
			closeThreshold = farThreshold;
			farThreshold = MomentHelper.deltaFromCurrentMoment(weeksOffset, ChronoUnit.WEEKS);
			int numberOfClaimsOfWeek = this.numberOfClaimPostedBetweenThresholds(farThreshold, closeThreshold);
			totalSum += Math.pow(numberOfClaimsOfWeek - averageOfClaims, 2);
		}

		return Math.sqrt(totalSum / (10.0 - 1.0));
	}

	default int maxNumberOfClaimsPostedLastTenWeeks() {
		int maxNumberOfClaims = 0;
		Date farThreshold = MomentHelper.getCurrentMoment();
		Date closeThreshold;

		for (int w = 1; w <= 10; w++) {
			Integer weeksOffset = -1 * w;
			closeThreshold = farThreshold;
			farThreshold = MomentHelper.deltaFromCurrentMoment(weeksOffset, ChronoUnit.WEEKS);
			int numberOfClaimsOfWeek = this.numberOfClaimPostedBetweenThresholds(farThreshold, closeThreshold);
			if (numberOfClaimsOfWeek > maxNumberOfClaims)
				maxNumberOfClaims = numberOfClaimsOfWeek;
		}

		return maxNumberOfClaims;
	}

	default int minNumberOfClaimsPostedLastTenWeeks() {
		Integer minNumberOfClaims = null;
		Date farThreshold = MomentHelper.getCurrentMoment();
		Date closeThreshold;

		for (int w = 1; w <= 10; w++) {
			Integer weeksOffset = -1 * w;
			closeThreshold = farThreshold;
			farThreshold = MomentHelper.deltaFromCurrentMoment(weeksOffset, ChronoUnit.WEEKS);
			int numberOfClaimsOfWeek = this.numberOfClaimPostedBetweenThresholds(farThreshold, closeThreshold);
			if (numberOfClaimsOfWeek == 0)
				return 0;
			else if (minNumberOfClaims == null || numberOfClaimsOfWeek < minNumberOfClaims)
				minNumberOfClaims = numberOfClaimsOfWeek;
		}

		return minNumberOfClaims;
	}

}
