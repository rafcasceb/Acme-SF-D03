
package acme.features.sponsor.sponsorDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(i) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id AND i.tax <= 0.21")
	int countInvoicesWithTaxLessThanOrEqual21(int id);

	@Query("SELECT COUNT(s) FROM Sponsorship s WHERE s.sponsor.id = :id AND s.link IS NOT NULL")
	int countSponsorshipsWithLink(int id);

	@Query("SELECT AVG(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id AND s.amount.currency = :currency")
	Double averageAmountSponsorships(int id, String currency);

	@Query("SELECT STDDEV(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id AND s.amount.currency = :currency")
	Double stdevAmountSponsorships(int id, String currency);

	@Query("SELECT MIN(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id AND s.amount.currency = :currency")
	Double minimumAmountSponsorships(int id, String currency);

	@Query("SELECT MAX(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :id AND s.amount.currency = :currency")
	Double maximumAmountSponsorships(int id, String currency);

	@Query("SELECT AVG(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id AND i.quantity.currency = :currency")
	Double averageQuantityInvoices(int id, String currency);

	@Query("SELECT STDDEV(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id AND i.quantity.currency = :currency")
	Double stdevQuantityInvoices(int id, String currency);

	@Query("SELECT MIN(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id AND i.quantity.currency = :currency")
	Double minimumQuantityInvoices(int id, String currency);

	@Query("SELECT MAX(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :id AND i.quantity.currency = :currency")
	Double maximumQuantityInvoices(int id, String currency);

	@Query("SELECT config FROM Configuration config")
	Configuration findConfiguration();

}
