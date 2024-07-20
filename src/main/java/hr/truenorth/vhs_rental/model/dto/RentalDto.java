package hr.truenorth.vhs_rental.model.dto;

import hr.truenorth.vhs_rental.validation.DateDueAfterDateRented;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DateDueAfterDateRented
public class RentalDto {
    @NotNull(message = "{userID.not_null}")
    private Long userId;

    @NotNull(message = "{vhsId.not_null}")
    private Long vhsId;

    private Date dateRented;

    private Date dateDue;
}
