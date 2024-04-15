
package acme.features.any.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;

@Repository
public interface AnyAuditRecordRepository extends AbstractRepository {

	@Query("select a from AuditRecord a where a.audit.id = :auditId")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int auditId);

	@Query("select c from CodeAudit c where c.id = :auditId")
	CodeAudit findOneCodeAuditById(int auditId);

	@Query("select a from AuditRecord a where a.id = :auditId")
	AuditRecord findOneAuditRecordById(int auditId);

	@Query("select c from CodeAudit c")
	Collection<CodeAudit> findAllCodeAudits();
}
