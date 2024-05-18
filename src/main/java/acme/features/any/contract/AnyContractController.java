
package acme.features.any.contract;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Any;
import acme.entities.contracts.Contract;

@Controller
public class AnyContractController extends AbstractController<Any, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyContractShowService	showService;

	@Autowired
	private AnyContractListService	listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("list", this.listService);
	}

}
