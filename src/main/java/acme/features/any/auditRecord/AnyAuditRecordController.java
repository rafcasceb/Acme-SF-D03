
package acme.features.any.auditRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Any;
import acme.entities.audits.AuditRecord;

@Controller
public class AnyAuditRecordController extends AbstractController<Any, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyAuditRecordListForCodeAuditsService	listForCodeAuditsService;

	@Autowired
	private AnyAuditRecordShowService				showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-for-code-audits", "list", this.listForCodeAuditsService);
		super.addBasicCommand("show", this.showService);
	}

}
