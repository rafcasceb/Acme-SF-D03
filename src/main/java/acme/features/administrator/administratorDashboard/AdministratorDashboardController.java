
package acme.features.administrator.administratorDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.forms.AdministratorDashboard;

@Controller
public class AdministratorDashboardController extends AbstractController<Administrator, AdministratorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
