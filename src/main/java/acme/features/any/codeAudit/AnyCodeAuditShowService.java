
package acme.features.any.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.components.EnumMode;
import acme.entities.audits.AuditType;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.entities.projects.Project;

@Service
public class AnyCodeAuditShowService extends AbstractService<Any, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		CodeAudit codeAudit;

		masterId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);
		status = codeAudit != null && codeAudit.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		SelectChoices choices;
		SelectChoices projects;
		Dataset dataset;
		String modeMark;

		Collection<Mark> marks = this.repository.findMarksByAuditId(object.getId());
		modeMark = EnumMode.mode(marks);
		Collection<Project> allProjects = this.repository.findAllProjects();
		projects = SelectChoices.from(allProjects, "code", object.getProject());
		choices = SelectChoices.from(AuditType.class, object.getType());

		dataset = super.unbind(object, "code", "published", "execution", "type", "correctiveActions", "link");
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);
		dataset.put("auditTypes", choices);
		dataset.put("modeMark", modeMark);

		super.getResponse().addData(dataset);
	}
}
