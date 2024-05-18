
package acme.features.any.progressLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;

@Service
public class AnyProgressLogListForContractService extends AbstractService<Any, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyProgressLogRepository repository;

	// AbstractService<Any, AuditRecord> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Contract contract;

		id = super.getRequest().getData("contractId", int.class);
		contract = this.repository.findOneContractById(id);

		status = contract != null && contract.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ProgressLog> objects;
		int contractId;

		contractId = super.getRequest().getData("contractId", int.class);
		objects = this.repository.findManyProgressLogsByContractId(contractId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "published");

		super.getResponse().addData(dataset);
	}

}
