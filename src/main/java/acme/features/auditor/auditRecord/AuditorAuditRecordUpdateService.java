
package acme.features.auditor.auditRecord;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {

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

		Date pastMostDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");

		if (!super.getBuffer().getErrors().hasErrors("audit"))
			super.state(!object.getAudit().isPublished(), "audit", "validation.auditrecord.published.audit-is-published");

		if (!super.getBuffer().getErrors().hasErrors("published"))
			super.state(!object.isPublished(), "published", "validation.auditrecord.published");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord isCodeUnique;
			isCodeUnique = this.repository.findAuditRecordByCodeDifferentId(object.getCode(), object.getId());
			super.state(isCodeUnique == null, "code", "validation.auditrecord.code.duplicate");
		}

		if (object.getInitialMoment() != null && object.getFinalMoment() != null) {

			if (!super.getBuffer().getErrors().hasErrors("initialMoment"))
				super.state(MomentHelper.isAfterOrEqual(object.getInitialMoment(), pastMostDate), "initialMoment", "validation.auditrecord.moment.minimum-date");

			if (!super.getBuffer().getErrors().hasErrors("finalMoment"))
				super.state(MomentHelper.isAfterOrEqual(object.getFinalMoment(), pastMostDate), "finalMoment", "validation.auditrecord.moment.minimum-date");

			if (!super.getBuffer().getErrors().hasErrors("initialMoment"))
				super.state(MomentHelper.isAfter(object.getFinalMoment(), object.getInitialMoment()), "initialMoment", "validation.auditrecord.moment.initial-after-final");

			if (!super.getBuffer().getErrors().hasErrors("finalMoment")) {
				Date minimumEnd;

				minimumEnd = MomentHelper.deltaFromMoment(object.getInitialMoment(), 1, ChronoUnit.HOURS);
				super.state(MomentHelper.isAfterOrEqual(object.getFinalMoment(), minimumEnd), "finalMoment", "validation.auditrecord.moment.minimum-one-hour");
			}
		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;
		this.repository.save(object);
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
