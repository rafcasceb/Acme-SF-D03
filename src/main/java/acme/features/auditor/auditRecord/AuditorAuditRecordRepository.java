
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.roles.Auditor;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select a from AuditRecord a where a.audit.auditor.id = :auditorId")
	Collection<AuditRecord> findManyAuditRecordsByAuditorId(int auditorId);

	@Query("select a from AuditRecord a where a.audit.id = :auditId")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int auditId);

	@Query("select c from CodeAudit c where c.id = :auditId")
	CodeAudit findOneCodeAuditById(int auditId);

	@Query("select a from AuditRecord a where a.id = :auditId")
	AuditRecord findOneAuditRecordById(int auditId);

	@Query("select c from CodeAudit c where c.auditor.id = :auditorId")
	Collection<CodeAudit> findAllMyCodeAudits(int auditorId);

	@Query("select a from Auditor a where a.id = :auditorId")
	Auditor findOneAuditorById(int auditorId);

	@Query("select a from AuditRecord a where a.code = :code")
	AuditRecord findAuditRecordByCode(String code);

	@Query("select a from AuditRecord a where a.code = :code and a.id != :auditId")
	AuditRecord findAuditRecordByCodeDifferentId(String code, int auditId);

}
