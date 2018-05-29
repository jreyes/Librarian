package librarian.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "lib_issues")
public class Issue {
// ------------------------------ FIELDS ------------------------------

    @Id
    @Column(name = "book_id")
    private String bookId;

    @Column(name = "member_id", nullable = false)
    private String memberId;
}
