
package acme.features.sponsor.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Invoice;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceDeleteService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int invoiceId;
		Invoice invoice;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(invoiceId);
		status = !invoice.isPublished() && invoice != null && super.getRequest().getPrincipal().hasRole(invoice.getSponsorship().getSponsor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneInvoiceById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "link", "registrationTime", "dueDate", "quantity", "tax");
	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(object.isPublished() == false, "code", "sponsor.invoice.form.error.published");
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "link", "registrationTime", "dueDate", "quantity", "tax", "published");
		super.getResponse().addData(dataset);
	}

}
