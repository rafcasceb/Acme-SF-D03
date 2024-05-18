
package acme.features.administrator.objective;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;

@Repository
public interface AdministratorObjectiveRepository extends AbstractRepository {

	@Query("select c from Configuration c")
	Configuration findConfiguration();

}
