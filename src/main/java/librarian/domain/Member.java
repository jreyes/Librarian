package librarian.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "lib_member")
public class Member {
// ------------------------------ FIELDS ------------------------------

    @Column
    private String email;

    @Id
    @Size(min = 4, max = 10)
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @NotNull
    @Size(min = 10, max = 60)
    @Column(length = 60, nullable = false)
    private String mobile;

    @NotNull
    @Size(min = 10, max = 60)
    @Column(length = 60, nullable = false)
    private String name;
}
