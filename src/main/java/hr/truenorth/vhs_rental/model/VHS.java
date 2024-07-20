package hr.truenorth.vhs_rental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Object for storing and handling VHS info
 * It can be rented and returned. Contains id and title.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VHS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{title.not_blank}")
    private String title;
}
