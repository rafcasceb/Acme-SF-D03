
package acme.features.authenticated.auditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.roles.Auditor;

@Repository
public interface AuthenticatedAuditorRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select a from Auditor a where a.userAccount.id = :id")
	Auditor findOneAuditorByUserAccountId(int id);

	@Query("select a from Auditor a where a.professionalID = :code")
	Auditor findAuditorByCode(String code);

	@Query("select a from Auditor a where a.professionalID = :code and a.id != :auditorId")
	Auditor findAuditorByCodeDifferentId(String code, int auditorId);

	@Query("select c from Configuration c")
	Configuration findConfiguration();

}
