
package acme.features.administrator.risk;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.entities.risks.Risk;

@Repository
public interface AdministratorRiskRepository extends AbstractRepository {

	@Query("select r from Risk r")
	Collection<Risk> findAllRisks();

	@Query("select r from Risk r where r.id = :riskId")
	Risk findOneRiskById(int riskId);

	@Query("select r from Risk r where r.reference = :reference")
	Risk findRiskByReference(String reference);

	@Query("select c from Configuration c")
	Configuration findConfiguration();
}
