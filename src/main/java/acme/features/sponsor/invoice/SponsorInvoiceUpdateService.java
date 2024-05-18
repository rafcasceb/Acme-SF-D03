
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.sponsorships.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceUpdateService extends AbstractService<Sponsor, Invoice> {

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

		Date moment;
		moment = MomentHelper.getCurrentMoment();
		object.setRegistrationTime(moment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "link", "dueDate", "quantity", "tax", "sponsorship");
	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;

		String dateString = "2201/01/01 00:00";
		Date futureMostDate = MomentHelper.parse(dateString, "yyyy/MM/dd HH:mm");
		String acceptedCurrencies = this.repository.findConfiguration().getAcceptedCurrencies();
		List<String> acceptedCurrencyList = Arrays.asList(acceptedCurrencies.split("\\s*,\\s*"));

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoice invoiceSameCode;
			invoiceSameCode = this.repository.findInvoiceByCode(object.getCode());
			int id = invoiceSameCode.getId();
			super.state(id == object.getId() || invoiceSameCode == null, "code", "sponsor.invoice.form.error.duplicate");
		}

		if (object.getDueDate() != null) {

			if (!super.getBuffer().getErrors().hasErrors("dueDate"))
				super.state(MomentHelper.isAfter(object.getDueDate(), object.getRegistrationTime()), "dueDate", "sponsor.invoice.form.error.dueDate");

			if (!super.getBuffer().getErrors().hasErrors("dueDate"))
				super.state(MomentHelper.isBefore(object.getDueDate(), futureMostDate), "dueDate", "sponsor.invoice.form.error.dateOutOfBounds");

			if (!super.getBuffer().getErrors().hasErrors("dueDate"))
				super.state(MomentHelper.isLongEnough(object.getRegistrationTime(), object.getDueDate(), 1, ChronoUnit.MONTHS), "dueDate", "sponsor.invoice.form.error.period");
		}

		if (!super.getBuffer().getErrors().hasErrors("sponsorship"))
			super.state(object.getSponsorship() != null && object.getSponsorship().isPublished() == false, "sponsorship", "sponsor.invoice.form.error.sponsorship");

		if (!super.getBuffer().getErrors().hasErrors("published"))
			super.state(object.isPublished() == false, "sponsorship", "sponsor.invoice.form.error.published");

		if (!super.getBuffer().getErrors().hasErrors("quanitity"))
			super.state(object.getSponsorship() != null && object.getQuantity().getAmount() <= 1000000.00 && object.getQuantity().getAmount() >= 0.00, "quantity", "sponsor.invoice.form.error.amountOutOfBounds");

		if (!super.getBuffer().getErrors().hasErrors("quanitity"))
			super.state(object.getQuantity() != null && acceptedCurrencyList.contains(object.getQuantity().getCurrency()), "quantity", "sponsor.invoice.form.error.currencyNotSupported");

		if (!super.getBuffer().getErrors().hasErrors("quantity"))
			super.state(object.getSponsorship() != null && object.getQuantity().getCurrency().equals(object.getSponsorship().getAmount().getCurrency()), "quantity", "sponsor.invoice.form.error.currency");

	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;
		SelectChoices sponsorships;
		int sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		Collection<Sponsorship> sponsorSponsorships = this.repository.findSponsorshipBySponsorId(sponsorId);
		sponsorships = SelectChoices.from(sponsorSponsorships, "code", object.getSponsorship());

		dataset = super.unbind(object, "code", "link", "registrationTime", "dueDate", "quantity", "tax", "published");
		Sponsorship selectedSponsorship = this.repository.findOneSponsorshipById(Integer.valueOf(sponsorships.getSelected().getKey()));
		dataset.put("sponsorship", sponsorships.getSelected().getKey());

		sponsorSponsorships = this.repository.findSponsorUnpublishedSponsorship(sponsorId);
		if (!sponsorSponsorships.contains(selectedSponsorship) && selectedSponsorship != null)
			sponsorSponsorships.add(selectedSponsorship);

		sponsorships = SelectChoices.from(sponsorSponsorships, "code", object.getSponsorship());
		dataset.put("sponsorships", sponsorships);

		super.getResponse().addData(dataset);
	}

}
