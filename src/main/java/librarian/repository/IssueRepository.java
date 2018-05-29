package librarian.repository;

import librarian.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {
// -------------------------- OTHER METHODS --------------------------

    @Query("select count(distinct t.memberId) from Issue t")
    long countByMemberId();

    boolean existsByMemberId(String memberId);
}
