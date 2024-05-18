/*
 * AuthenticatedConsumerRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.entities.sponsorships.Invoice;
import acme.entities.sponsorships.Sponsorship;

@Repository
public interface SponsorInvoiceRepository extends AbstractRepository {

	@Query("select i from Invoice i where i.sponsorship.id = :id")
	Collection<Invoice> findInvoiceBySponsorshipId(int id);

	@Query("select i from Invoice i where i.sponsorship.sponsor.id = :id")
	Collection<Invoice> findAllInvoicesBySponsorId(int id);

	@Query("select i from Invoice i where i.id = :id")
	Invoice findOneInvoiceById(int id);

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("select s from Sponsorship s where s.published = false AND s.sponsor.id = :id")
	Collection<Sponsorship> findSponsorUnpublishedSponsorship(int id);

	@Query("select s from Sponsorship s where s.sponsor.id = :id")
	Collection<Sponsorship> findSponsorshipBySponsorId(int id);

	@Query("select i from Invoice i where i.code = :code")
	Invoice findInvoiceByCode(String code);

	@Query("select i from Invoice i where i.sponsorship.id = :id")
	Collection<Invoice> findAllInvoicesBySponsorshipId(int id);

	@Query("SELECT config FROM Configuration config")
	Configuration findConfiguration();

}
