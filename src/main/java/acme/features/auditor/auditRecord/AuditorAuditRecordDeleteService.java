
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordDeleteService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService<Auditor, AuditRecord> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Auditor auditor;
		AuditRecord auditRecord;

		id = super.getRequest().getData("id", int.class);
		auditRecord = this.repository.findOneAuditRecordById(id);

		auditor = auditRecord == null ? null : auditRecord.getAudit().getAuditor();
		status = auditRecord != null && !auditRecord.isPublished() && super.getRequest().getPrincipal().hasRole(auditor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		int codeAuditId;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("audit", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);

		object.setAudit(codeAudit);
		super.bind(object, "code", "link", "mark", "initialMoment", "finalMoment");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("audit"))
			super.state(!object.getAudit().isPublished(), "audit", "validation.auditrecord.published.audit-is-published");

		if (!super.getBuffer().getErrors().hasErrors("published"))
			super.state(!object.isPublished(), "published", "validation.auditrecord.published");

	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		int auditorId;
		auditorId = super.getRequest().getPrincipal().getActiveRoleId();

		SelectChoices choices;
		SelectChoices codeAudits;
		Dataset dataset;

		Collection<CodeAudit> allCodeAudits = this.repository.findAllMyCodeAudits(auditorId);
		codeAudits = SelectChoices.from(allCodeAudits, "code", object.getAudit());
		choices = SelectChoices.from(Mark.class, object.getMark());

		dataset = super.unbind(object, "code", "published", "link", "mark", "initialMoment", "finalMoment");
		dataset.put("audit", codeAudits.getSelected().getKey());
		dataset.put("codeaudits", codeAudits);
		dataset.put("marks", choices);

		super.getResponse().addData(dataset);
	}

}
