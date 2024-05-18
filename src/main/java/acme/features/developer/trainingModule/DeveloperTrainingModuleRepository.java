
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.entities.projects.Project;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingmodule.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select t from TrainingModule t where t.id = :id")
	TrainingModule findOneTrainingModuleById(int id);

	@Query("select t from TrainingModule t where t.developer.id = :developerId")
	Collection<TrainingModule> findManyTrainingModulesByDeveloperId(int developerId);

	@Query("select d from Developer d where d.id = :id")
	Developer findOneDeveloperById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select count(t) from TrainingModule t where t.updateMoment is not null")
	Integer computeNumberTrainingModuleWithUpdateMoment();

	@Query("select count(t) from TrainingModule t where t.link is not null")
	Integer computeNumberTrainingModuleWithLink();

	@Query("select p from Project p")
	Collection<Project> findManyProjects();

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :id")
	Collection<TrainingSession> findManyTrainingSessionByModuleId(int id);

	@Query("select t from TrainingModule t where t.code = :code")
	TrainingModule findOneTrainingModuleByCode(String code);

	@Query("select c from Configuration c")
	Configuration findConfiguration();

}
