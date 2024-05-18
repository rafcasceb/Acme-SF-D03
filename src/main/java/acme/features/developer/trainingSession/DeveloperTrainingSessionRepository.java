
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingmodule.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select tm from TrainingModule tm where tm.id = :tmId")
	TrainingModule findOneTrainingModuleById(int tmId);

	@Query("select ts from TrainingSession ts where ts.code = :tsCode")
	TrainingSession findOneTrainingSessionByCode(String tsCode);

	@Query("select ts.trainingModule from TrainingSession ts where ts.id = :tsId")
	TrainingModule findOneTrainingModuleByTrainingSessionId(int tsId);

	@Query("select ts from TrainingSession ts where ts.id = :tsId")
	TrainingSession findOneTrainingSessionById(int tsId);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :tmId")
	Collection<TrainingSession> findManyTrainingSessionsByModuleId(int tmId);

	@Query("select c from Configuration c")
	Configuration findConfiguration();
}
