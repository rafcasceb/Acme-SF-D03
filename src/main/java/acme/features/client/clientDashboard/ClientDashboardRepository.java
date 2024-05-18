
package acme.features.client.clientDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.completeness <= 25.00")
	int firstPercentileProgressLogCompleteness(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.completeness > 25.00 and pl.completeness <= 50.00")
	int secondPercentileProgressLogCompleteness(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.completeness > 50.00 and pl.completeness <= 75.00")
	int thirdPercentileProgressLogCompleteness(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.completeness > 75.00 and pl.completeness <= 100.00")
	int fourthPercentileProgressLogCompleteness(int clientId);

	@Query("select avg(c.budget.amount) from Contract c where c.client.id = :clientId and c.budget.currency = :currency")
	Double avgAmountBudget(int clientId, String currency);

	@Query("select stddev(c.budget.amount) from Contract c where c.client.id = :clientId and c.budget.currency = :currency")
	Double stddevAmountBudget(int clientId, String currency);

	@Query("select max(c.budget.amount) from Contract c where c.client.id = :clientId and c.budget.currency = :currency")
	Double maxAmountBudget(int clientId, String currency);

	@Query("select min(c.budget.amount) from Contract c where c.client.id = :clientId and c.budget.currency = :currency")
	Double minAmountBudget(int clientId, String currency);

	@Query("select c.defaultCurrency from Configuration c")
	String findDefaultCurrency();

}
