
package acme.features.auditor.auditRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.audits.AuditRecord;
import acme.roles.Auditor;

@Controller
public class AuditorAuditRecordController extends AbstractController<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordListMineService			listMineService;

	@Autowired
	private AuditorAuditRecordListForCodeAuditsService	listForCodeAuditsService;

	@Autowired
	private AuditorAuditRecordShowService				showService;

	@Autowired
	private AuditorAuditRecordCreateService				createService;

	@Autowired
	private AuditorAuditRecordUpdateService				updateService;

	@Autowired
	private AuditorAuditRecordDeleteService				deleteService;

	@Autowired
	private AuditorAuditRecordPublishService			publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addCustomCommand("list-for-code-audits", "list", this.listForCodeAuditsService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
