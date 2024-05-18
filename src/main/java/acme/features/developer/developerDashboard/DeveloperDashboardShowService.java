
package acme.features.developer.developerDashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingmodule.TrainingSession;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	@Autowired
	private DeveloperDashboardRepository dashboardRepository;


	@Override
	public void authorise() {
		boolean status;

		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getActiveRoleId();
		Developer developer = this.dashboardRepository.findDeveloperById(id);
		status = developer != null && principal.hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Principal principal = super.getRequest().getPrincipal();
		int id = principal.getActiveRoleId();
		DeveloperDashboard developerDashboard = new DeveloperDashboard();
		Collection<TrainingModule> modules = this.dashboardRepository.findAllTrainingModulesByDeveloperId(id);
		Collection<TrainingSession> sessions = this.dashboardRepository.findAllTrainingSessionsByDeveloperId(id);

		if (!modules.isEmpty()) {
			developerDashboard.setTotalNumberTrainingModulesWithUpdateMoment(this.dashboardRepository.totalTrainingModulesWithUpdateMoment(id));
			developerDashboard.setAverageTimeTrainingModules(this.dashboardRepository.averageTrainingModulesTime(id));
			developerDashboard.setDeviationTimeTrainingModules(this.dashboardRepository.deviatonTrainingModulesTime(id));
			developerDashboard.setMinimumTimeTrainingModules(this.dashboardRepository.minimumTrainingModulesTime(id));
			developerDashboard.setMaximumTimeTrainingModules(this.dashboardRepository.maximumTrainingModulesTime(id));
		}

		if (!sessions.isEmpty())
			developerDashboard.setTotalNumberTrainingSessionsWithLink(this.dashboardRepository.totalTrainingSessionsWithLink(id));

		super.getBuffer().addData(developerDashboard);

	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "totalNumberTrainingModulesWithUpdateMoment", "totalNumberTrainingSessionsWithLink", "averageTimeTrainingModules", "deviationTimeTrainingModules", "minimumTimeTrainingModules", "maximumTimeTrainingModules");

		super.getResponse().addData(dataset);
	}
}
