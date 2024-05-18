
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.audits.CodeAudit;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.entities.projects.ProjectUserStory;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.trainingmodule.TrainingModule;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Project project;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(masterId);
		manager = project == null ? null : project.getManager();
		status = project != null && !project.isPublished() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractDescription", "fatalErrorPresent", "estimatedCostInHours", "link");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		{
			Collection<CodeAudit> codeAudits;
			Collection<Contract> contracts;
			Collection<Sponsorship> sponsorships;
			Collection<TrainingModule> trainingModules;

			codeAudits = this.repository.findManyCodeAuditsByProjectId(object.getId());
			super.state(codeAudits.isEmpty(), "*", "manager.project.form.error.children-audits");

			contracts = this.repository.findManyContractsByProjectId(object.getId());
			super.state(contracts.isEmpty(), "*", "manager.project.form.error.children-contracts");

			sponsorships = this.repository.findManySponsorshipsByProjectId(object.getId());
			super.state(sponsorships.isEmpty(), "*", "manager.project.form.error.children-sponsorships");

			trainingModules = this.repository.findManyTrainingModulesByProjectId(object.getId());
			super.state(trainingModules.isEmpty(), "*", "manager.project.form.error.children-training-modules");
		}
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		Collection<ProjectUserStory> projectUserStoryTables;

		projectUserStoryTables = this.repository.findManyProjectUserStoryTablesByProjectId(object.getId());
		this.repository.deleteAll(projectUserStoryTables);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "published", "code", "title", "abstractDescription", "fatalErrorPresent", "estimatedCostInHours", "link");

		super.getResponse().addData(dataset);
	}

}
