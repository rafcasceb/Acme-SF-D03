
package acme.features.authenticated.notices;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;
import acme.entities.notices.Notice;
import spam_detector.SpamDetector;

@Service
public class AuthenticatedNoticeCreateService extends AbstractService<Authenticated, Notice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedNoticeRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Notice object;
		Date moment;
		String username;
		String name;
		String author;
		int accountId;

		accountId = this.getRequest().getPrincipal().getAccountId();
		name = this.repository.findOneUserAccountById(accountId).getIdentity().getFullName();
		username = super.getRequest().getPrincipal().getUsername();
		author = username + "-" + name;

		moment = MomentHelper.getCurrentMoment();

		object = new Notice();
		object.setInstantiationMoment(moment);
		object.setAuthor(author);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Notice object) {
		assert object != null;

		super.bind(object, "title", "message", "email", "link");
	}

	@Override
	public void validate(final Notice object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

		if (!super.getBuffer().getErrors().hasErrors("title")) {
			Configuration config = this.repository.findConfiguration();
			String spamTerms = config.getSpamTerms();
			Double spamThreshold = config.getSpamThreshold();
			SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);
			super.state(!spamHelper.isSpam(object.getTitle()), "title", "validation.notice.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("message")) {
			Configuration config = this.repository.findConfiguration();
			String spamTerms = config.getSpamTerms();
			Double spamThreshold = config.getSpamThreshold();
			SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);
			super.state(!spamHelper.isSpam(object.getMessage()), "message", "validation.notice.form.error.spam");
		}

	}

	@Override
	public void perform(final Notice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "instantiationMoment", "message", "email", "link", "author");
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}

}
