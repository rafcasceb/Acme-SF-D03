
package acme.features.any.codeAudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.entities.projects.Project;

@Repository
public interface AnyCodeAuditRepository extends AbstractRepository {

	@Query("select c from CodeAudit c where c.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("select c from CodeAudit c where c.published = true")
	Collection<CodeAudit> findAllPublishedCodeAudits();

	@Query("select a.mark from AuditRecord a where a.audit.id = :auditId")
	Collection<Mark> findMarksByAuditId(int auditId);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

}
