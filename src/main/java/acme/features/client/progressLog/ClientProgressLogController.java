
package acme.features.client.progressLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Controller
public class ClientProgressLogController extends AbstractController<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogListForContractsService	listForContractsService;

	@Autowired
	private ClientProgressLogShowService				showService;

	@Autowired
	private ClientProgressLogDeleteService				deleteService;

	@Autowired
	private ClientProgressLogListMineService			listMineService;

	@Autowired
	private ClientProgressLogCreateService				createService;

	@Autowired
	private ClientProgressLogPublishService				publishService;

	@Autowired
	private ClientProgressLogUpdateService				updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-for-contracts", "list", this.listForContractsService);
		super.addCustomCommand("list-mine", "list", this.listMineService);

	}

}
