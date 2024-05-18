
package acme.features.authenticated.notices;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.Configuration;
import acme.entities.notices.Notice;

@Repository
public interface AuthenticatedNoticeRepository extends AbstractRepository {

	@Query("select n from Notice n")
	Collection<Notice> findAllNotices();

	@Query("select n from Notice n where n.id = :noticeId")
	Notice findOneNoticeById(int noticeId);

	@Query("select n from Notice n where n.instantiationMoment >= :deadline")
	Collection<Notice> findRecentNotices(Date deadline);

	@Query("select uc from UserAccount uc where uc.id =:id")
	UserAccount findOneUserAccountById(int id);

	@Query("select c from Configuration c")
	Configuration findConfiguration();

}
