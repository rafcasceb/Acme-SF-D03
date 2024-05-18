
package acme.features.developer.developerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingmodule.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperDashboardRepository extends AbstractRepository {

	@Query("select count(tm) from TrainingModule tm where tm.updateMoment != null and tm.developer.id = :id")
	int totalTrainingModulesWithUpdateMoment(int id);

	@Query("select count(ts) from TrainingSession ts where ts.link is not null and ts.link not like '' and ts.trainingModule.developer.id = :id")
	int totalTrainingSessionsWithLink(int id);

	@Query("select avg(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	Double averageTrainingModulesTime(int id);

	@Query("select stddev(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	Double deviatonTrainingModulesTime(int id);

	@Query("select min(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	int minimumTrainingModulesTime(int id);

	@Query("select max(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	int maximumTrainingModulesTime(int id);

	@Query("select d from Developer d where d.id = :id")
	Developer findDeveloperById(int id);

	@Query("select tm from TrainingModule tm where tm.developer.id = :id")
	Collection<TrainingModule> findAllTrainingModulesByDeveloperId(int id);

	@Query("select ts from TrainingSession ts where ts.trainingModule.developer.id = :id")
	Collection<TrainingSession> findAllTrainingSessionsByDeveloperId(int id);

}
