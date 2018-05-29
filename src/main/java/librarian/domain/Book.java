package librarian.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lib_book")
public class Book {
// ------------------------------ FIELDS ------------------------------

    @NotNull
    @Column
    private String author;

    @Column(name = "is_available")
    private boolean available;

    @Id
    @Column(name = "book_id")
    private String bookId;

    @Column(name = "issue_time")
    private LocalDateTime issueTime;

    @NotNull
    @Column
    private String publisher;

    @Column(name = "renew_count")
    private Integer renewCount;

    @NotNull
    @Column
    private String title;
}
