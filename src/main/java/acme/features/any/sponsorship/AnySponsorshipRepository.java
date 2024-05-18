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

package acme.features.any.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;

@Repository
public interface AnySponsorshipRepository extends AbstractRepository {

	@Query("select s from Sponsorship s")
	Collection<Sponsorship> findAllSponsorships();

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select s from Sponsorship s where s.published = true")
	Collection<Sponsorship> findAllPublishedSponsorships();

}
