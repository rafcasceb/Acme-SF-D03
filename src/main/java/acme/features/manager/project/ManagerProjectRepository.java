
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.CodeAudit;
import acme.entities.configuration.Configuration;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.entities.projects.ProjectUserStory;
import acme.entities.projects.UserStory;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.trainingmodule.TrainingModule;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findManyProjectsByManagerId(int managerId);

	@Query("select p from Project p where p.code = :code")
	Project findProjectByCode(String code);

	@Query("select m from Manager m where m.id = :id")
	Manager findOneManagerById(int id);

	@Query("select p_us.userStory from ProjectUserStory p_us where p_us.project.id = :projectId")
	Collection<UserStory> findManyUserStoriesByProjectId(int projectId);

	@Query("select p_us from ProjectUserStory p_us where p_us.project.id = :projectId")
	Collection<ProjectUserStory> findManyProjectUserStoryTablesByProjectId(int projectId);

	@Query("select ca from CodeAudit ca where ca.project.id = :projectId")
	Collection<CodeAudit> findManyCodeAuditsByProjectId(int projectId);

	@Query("select c from Contract c where c.project.id = :projectId")
	Collection<Contract> findManyContractsByProjectId(int projectId);

	@Query("select s from Sponsorship s where s.project.id = :projectId")
	Collection<Sponsorship> findManySponsorshipsByProjectId(int projectId);

	@Query("select tm from TrainingModule tm where tm.project.id = :projectId")
	Collection<TrainingModule> findManyTrainingModulesByProjectId(int projectId);

	@Query("select c from Configuration c")
	Configuration findConfiguration();
}
