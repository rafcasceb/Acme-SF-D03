
package acme.features.any.progressLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;

@Repository
public interface AnyProgressLogRepository extends AbstractRepository {

	@Query("select p from ProgressLog p where p.contract.id = :contractId")
	Collection<ProgressLog> findManyProgressLogsByContractId(int contractId);

	@Query("select c from Contract c where c.id = :contractId")
	Contract findOneContractById(int contractId);

	@Query("select p from ProgressLog p where p.id = :plId")
	ProgressLog findOneProgressLogById(int plId);

	@Query("select c from Contract c")
	Collection<Contract> findAllContracts();

}
