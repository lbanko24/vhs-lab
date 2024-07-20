package hr.truenorth.vhs_rental.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VHSReturnDto {
    private Date returnDate;
    private float fee;
}
