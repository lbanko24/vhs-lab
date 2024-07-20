package hr.truenorth.vhs_rental.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

/**
 * Object for storing and handling rental info
 * Contains id, user id, vhs id, rented date, due date and return date.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vhs_id")
    private VHS vhs;

    @Column(name = "date_rented")
    private Date dateRented;

    @Column(name = "date_due")
    private Date dateDue;

    @Column(name = "date_returned")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date dateReturned;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rental rental)) {
            return false;
        }

        return rental.getId().equals(id) &&
            rental.getUser().getId().equals(user.getId()) &&
            rental.getVhs().getId().equals(vhs.getId()) &&
            rental.getDateRented().toLocalDate().equals(dateRented.toLocalDate()) &&
            rental.getDateDue().toLocalDate().equals(dateDue.toLocalDate());
    }
}
