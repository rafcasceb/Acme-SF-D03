
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.entities.configuration.Configuration;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Repository
public interface AuditorCodeAuditRepository extends AbstractRepository {

	@Query("select c from CodeAudit c where c.auditor.id = :auditorId")
	Collection<CodeAudit> findManyCodeAuditsByAuditorId(int auditorId);

	@Query("select c from CodeAudit c where c.id = :auditId")
	CodeAudit findOneCodeAuditById(int auditId);

	@Query("select a from Auditor a where a.id = :auditorId")
	Auditor findOneAuditorById(int auditorId);

	@Query("select c from CodeAudit c where c.code = :code")
	CodeAudit findCodeAuditByCode(String code);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select c from CodeAudit c where c.code = :code and c.id != :auditId")
	CodeAudit findCodeAuditByCodeDifferentId(String code, int auditId);

	@Query("select a from AuditRecord a where a.audit.id = :auditId")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int auditId);

	@Query("select a.mark from AuditRecord a where a.audit.id = :auditId")
	Collection<Mark> findMarksByAuditId(int auditId);

	@Query("select c from Configuration c")
	Configuration findConfiguration();

}
