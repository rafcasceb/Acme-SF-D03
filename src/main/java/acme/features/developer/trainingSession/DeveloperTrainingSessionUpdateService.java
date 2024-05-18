
package acme.features.developer.trainingSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.configuration.Configuration;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingmodule.TrainingSession;
import acme.roles.Developer;
import spam_detector.SpamDetector;

@Service
public class DeveloperTrainingSessionUpdateService extends AbstractService<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int sessionId;
		TrainingModule module;
		TrainingSession session;

		sessionId = super.getRequest().getData("id", int.class);
		module = this.repository.findOneTrainingModuleByTrainingSessionId(sessionId);
		session = this.repository.findOneTrainingSessionById(sessionId);
		status = module != null && !module.isPublished() && super.getRequest().getPrincipal().hasRole(module.getDeveloper()) && !session.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession session;
		int id;

		id = super.getRequest().getData("id", int.class);
		session = this.repository.findOneTrainingSessionById(id);

		super.getBuffer().addData(session);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startDate", "endDate", "location", "instructor", "email", "link");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;
		String dateString = "2201/01/01 00:00";
		Date futureMostDate = MomentHelper.parse(dateString, "yyyy/MM/dd HH:mm");
		Date startMaximumDate = MomentHelper.parse("2200/12/25 00:00", "yyyy/MM/dd HH:mm");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingSession existing;

			existing = this.repository.findOneTrainingSessionByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "developer.training-session.form.error.duplicated");
		}
		if (object.getStartDate() != null) {
			if (!super.getBuffer().getErrors().hasErrors("startDate"))
				super.state(MomentHelper.isBefore(object.getStartDate(), futureMostDate), "startDate", "developer.training-session.form.error.date-not-before-limit");

			if (!super.getBuffer().getErrors().hasErrors("startDate"))
				super.state(MomentHelper.isBefore(object.getStartDate(), startMaximumDate), "startDate", "developer.training-session.form.error.date-not-before-limit-week");

			if (!super.getBuffer().getErrors().hasErrors("startDate")) {
				TrainingModule module;
				int id;
				id = super.getRequest().getData("id", int.class);
				module = this.repository.findOneTrainingModuleByTrainingSessionId(id);
				super.state(MomentHelper.isAfter(object.getStartDate(), module.getCreationMoment()), "startDate", "developer.training-session.form.error.creation-moment-invalid");
			}
			if (object.getEndDate() != null) {

				if (!super.getBuffer().getErrors().hasErrors("endDate")) {
					Date minimumEnd;

					minimumEnd = MomentHelper.deltaFromMoment(object.getStartDate(), 7, ChronoUnit.DAYS);
					super.state(object.getStartDate() != null && MomentHelper.isAfter(object.getEndDate(), minimumEnd), "endDate", "developer.training-session.form.error.too-close");
				}
				if (!super.getBuffer().getErrors().hasErrors("endDate"))
					super.state(MomentHelper.isBefore(object.getEndDate(), futureMostDate), "endDate", "developer.training-session.form.error.date-not-before-limit");

			}

		}
		if (!super.getBuffer().getErrors().hasErrors("location")) {
			Configuration config = this.repository.findConfiguration();
			String spamTerms = config.getSpamTerms();
			Double spamThreshold = config.getSpamThreshold();
			SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);
			super.state(!spamHelper.isSpam(object.getLocation()), "location", "validation.training-module.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("instructor")) {
			Configuration config = this.repository.findConfiguration();
			String spamTerms = config.getSpamTerms();
			Double spamThreshold = config.getSpamThreshold();
			SpamDetector spamHelper = new SpamDetector(spamTerms, spamThreshold);
			super.state(!spamHelper.isSpam(object.getInstructor()), "instructor", "validation.training-module.form.error.spam");
		}

	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {

		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDate", "endDate", "location", "instructor", "email", "link", "published");
		dataset.put("masterId", object.getTrainingModule().getId());

		super.getResponse().addData(dataset);
	}

}
