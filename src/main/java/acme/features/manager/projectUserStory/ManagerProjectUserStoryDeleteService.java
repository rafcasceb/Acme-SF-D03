
package acme.features.manager.projectUserStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.projects.ProjectUserStory;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryDeleteService extends AbstractService<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		UserStory userStory;
		Manager manager;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		manager = userStory == null ? null : userStory.getManager();
		status = userStory != null && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProjectUserStory object;
		int userStoryId;
		UserStory userStory;

		object = new ProjectUserStory();
		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		object.setUserStory(userStory);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProjectUserStory object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("projectId", int.class);
		project = this.repository.findOneProjectById(projectId);
		object.setProject(project);
	}

	@Override
	public void validate(final ProjectUserStory object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("projectId"))
			super.state(object.getProject() != null, "projectId", "manager.project-user-story.form.error.no-project");

		if (!super.getBuffer().getErrors().hasErrors("projectId") && object.getProject() != null)
			super.state(!object.getProject().isPublished(), "projectId", "manager.project-user-story.form.error.published");
	}

	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;
		int projectId;
		int userStoryId;

		projectId = object.getProject().getId();
		userStoryId = object.getUserStory().getId();
		this.repository.deleteProjectUserStory(projectId, userStoryId);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		int userStoryId;
		UserStory userStory;
		Collection<Project> availableProjects;
		SelectChoices choices;
		Dataset dataset;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		availableProjects = this.repository.findManyUnpublishedProjectsLinkedToUserStory(userStoryId);
		choices = SelectChoices.from(availableProjects, "code", object.getProject());

		dataset = super.unbind(object, "project", "userStory");
		dataset.put("userStoryTitle", userStory.getTitle());
		dataset.put("projectCodes", choices);

		super.getResponse().addData(dataset);
	}

}
