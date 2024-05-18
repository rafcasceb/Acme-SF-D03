
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.features.sponsor.invoice.SponsorInvoiceRepository;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository	repository;

	@Autowired
	private SponsorInvoiceRepository		invoiceRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
		status = !sponsorship.isPublished() && sponsorship != null && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		super.bind(object, "code", "moment", "startDate", "endDate", "amount", "email", "link", "published");
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(object.isPublished() == false, "code", "sponsor.sponsorship.form.error.published");

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.repository.countPublishedInvoicesBySponsorshipId(object.getId()) == 0, "code", "sponsor.sponsorship.form.error.deleteWithPublishedInvoices");
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;
		Collection<Invoice> invoices = this.repository.findAllInvoicesBySponsorshipId(object.getId());
		for (Invoice i : invoices)
			this.invoiceRepository.delete(i);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "moment", "startDate", "endDate", "amount", "type", "email", "link", "published", "project");
		super.getResponse().addData(dataset);
	}

}
