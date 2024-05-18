
package acme.features.any.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;

@Service
public class AnyContractShowService extends AbstractService<Any, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Contract contract;

		masterId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractById(masterId);
		status = contract != null && contract.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		SelectChoices projects;
		Dataset dataset;

		Collection<Project> unpublishedProjects = this.repository.findAllProjects();
		projects = SelectChoices.from(unpublishedProjects, "code", object.getProject());

		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget", "published");
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);

		super.getResponse().addData(dataset);
	}

}
