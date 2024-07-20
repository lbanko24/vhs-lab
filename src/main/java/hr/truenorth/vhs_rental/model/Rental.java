package hr.truenorth.vhs_rental.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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

    private Date dateReturned;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rental)) {
            return false;
        }

        Rental rental = (Rental) obj;
        boolean x = rental.getId().equals(id) &&
            rental.getUser().getId().equals(user.getId()) &&
            rental.getVhs().getId().equals(vhs.getId()) &&
            rental.getDateRented().toLocalDate().equals(dateRented.toLocalDate()) &&
            rental.getDateDue().toLocalDate().equals(dateDue.toLocalDate());
        return x;
    }
}
