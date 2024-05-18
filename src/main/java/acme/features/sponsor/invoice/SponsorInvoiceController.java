/*
 * AuthenticatedConsumerController.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.sponsor.invoice;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.sponsorships.Invoice;
import acme.roles.Sponsor;

@Controller
public class SponsorInvoiceController extends AbstractController<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceListService				listService;

	@Autowired
	private SponsorInvoiceListForSponsorshipService	listForSponsorshipService;

	@Autowired
	private SponsorInvoiceShowService				showService;

	@Autowired
	private SponsorInvoiceUpdateService				updateService;

	@Autowired
	private SponsorInvoiceDeleteService				deleteService;

	@Autowired
	private SponsorInvoiceCreateService				createService;

	@Autowired
	private SponsorInvoicePublishService			publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-for-sponsorship", "list", this.listForSponsorshipService);

	}

}
