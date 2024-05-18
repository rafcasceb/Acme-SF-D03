
package acme.features.any.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.configuration.Configuration;

@Repository
public interface AnyClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select c from Claim c")
	Collection<Claim> findAllClaims();

	@Query("select c from Claim c where c.code = :code")
	Claim findClaimByCode(String code);

	@Query("select c from Configuration c")
	Configuration findConfiguration();

}
