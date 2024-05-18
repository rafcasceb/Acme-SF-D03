
package acme.features.any.progressLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Any;
import acme.entities.contracts.ProgressLog;

@Controller
public class AnyProgressLogController extends AbstractController<Any, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyProgressLogListForContractService	listForContractService;

	@Autowired
	private AnyProgressLogShowService				showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-for-contracts", "list", this.listForContractService);
		super.addBasicCommand("show", this.showService);
	}

}
