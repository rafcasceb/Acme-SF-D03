
package acme.features.auditor.codeAudit;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.components.EnumMode;
import acme.entities.audits.AuditType;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.entities.configuration.Configuration;
import acme.entities.projects.Project;
import acme.roles.Auditor;
import spam_detector.SpamDetector;

@Service
public class AuditorCodeAuditUpdateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService<Auditor, CodeAudit> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Auditor auditor;
		CodeAudit codeAudit;

		id = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(id);

		auditor = codeAudit == null ? null : codeAudit.getAuditor();
		status = codeAudit != null && !codeAudit.isPublished() && super.getRequest().getPrincipal().hasRole(auditor);

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

		Date pastMostDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");

		if (object.getExecution() != null && !super.getBuffer().getErrors().hasErrors("execution"))
			super.state(MomentHelper.isAfterOrEqual(object.getExecution(), pastMostDate), "execution", "validation.auditrecord.moment.minimum-date");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit isCodeUnique;
			isCodeUnique = this.repository.findCodeAuditByCodeDifferentId(object.getCode(), object.getId());
			super.state(isCodeUnique == null, "code", "validation.codeaudit.code.duplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("correctiveActions")) {
			Configuration config = this.repository.findConfiguration();
			String spamTerms = config.getSpamTerms();
			Double spamThreshold = config.getSpamThreshold();
			SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);
			super.state(!spamHelper.isSpam(object.getCorrectiveActions()), "correctiveActions", "validation.codeaudit.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("published"))
			super.state(!object.isPublished(), "published", "validation.codeaudit.published");
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
