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
@Table(name = "lib_setting")
public class Setting {
// ------------------------------ FIELDS ------------------------------

    @Size(min = 10, max = 40)
    @Column(length = 40)
    private String apiKey;

    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "days_without_fine", nullable = false)
    private int daysWithoutFine;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "fine_per_day", nullable = false)
    private float finePerDay;

    @NotNull
    @Size(min = 1, max = 60)
    @Column(length = 60, nullable = false)
    private String password;

    @Id
    @Size(min = 4, max = 20)
    @Column(length = 20, nullable = false)
    private String username;
}
