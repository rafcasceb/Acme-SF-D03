
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.entities.projects.Project;
import acme.roles.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.id = :id")
	Contract findOneContractById(int id);

	@Query("select c from Contract c where c.client.id = :clientId")
	Collection<Contract> findManyContractsByClientId(int clientId);

	@Query("select c from Client c where c.id = :id")
	Client findOneClientById(int id);

	@Query("select c from Contract c where c.code = :code")
	Contract findContractByCode(String code);

	@Query("select c from Contract c where c.code = :code and c.id != :contractId")
	Contract findContractByCodeDifferentId(String code, int contractId);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select p from Project p where p.published = false")
	Collection<Project> findAllUnpublishedProjects();

	@Query("select p from ProgressLog p where p.contract.id = :contractId ")
	Collection<ProgressLog> findManyProgressLogsByContractId(int contractId);

	@Query("select c.acceptedCurrencies from Configuration c")
	String findAcceptedCurrencies();

	@Query("select sum(c.budget.amount) from Contract c where c.project.id = :projectId and c.id != :id")
	Double findAmountContractsFromSameProjectExceptThis(int projectId, int id);

	@Query("select c from Configuration c")
	Configuration findConfiguration();

}
