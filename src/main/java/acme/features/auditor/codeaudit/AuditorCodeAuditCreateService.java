
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audits.AuditType;
import acme.entities.audits.CodeAudit;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService<Auditor, CodeAudit> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit object;
		Auditor auditor;

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new CodeAudit();
		object.setPublished(false);
		object.setAuditor(auditor);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		object.setProject(project);
		super.bind(object, "code", "execution", "type", "correctiveActions", "link");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit isCodeUnique;
			isCodeUnique = this.repository.findCodeAuditByCode(object.getCode());
			super.state(isCodeUnique == null, "code", "validation.codeaudit.code.duplicate");
		}
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		SelectChoices choices;
		SelectChoices projects;
		Dataset dataset;

		Collection<Project> allProjects = this.repository.findAllProjects();
		projects = SelectChoices.from(allProjects, "code", object.getProject());
		choices = SelectChoices.from(AuditType.class, object.getType());

		dataset = super.unbind(object, "code", "published", "execution", "type", "correctiveActions", "link");
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);
		dataset.put("auditTypes", choices);

		super.getResponse().addData(dataset);
	}

}
