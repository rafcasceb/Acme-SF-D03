
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;
import acme.entities.projects.Project;
import acme.roles.Manager;
import spam_detector.SpamDetector;

@Service
public class ManagerProjectUpdateService extends AbstractService<Manager, Project> {

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

		Configuration config = this.repository.findConfiguration();
		String spamTerms = config.getSpamTerms();
		Double spamThreshold = config.getSpamThreshold();
		SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project projectSameCode;
			Boolean isCodeUsedByAnotherProject;

			projectSameCode = this.repository.findProjectByCode(object.getCode());
			isCodeUsedByAnotherProject = projectSameCode != null && projectSameCode.getId() != object.getId();
			super.state(!isCodeUsedByAnotherProject, "code", "manager.project.form.error.duplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!spamHelper.isSpam(object.getTitle()), "title", "manager.project.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("abstractDescription"))
			super.state(!spamHelper.isSpam(object.getAbstractDescription()), "abstractDescription", "manager.project.form.error.spam");

	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "published", "code", "title", "abstractDescription", "fatalErrorPresent", "estimatedCostInHours", "link");

		super.getResponse().addData(dataset);
	}

}
