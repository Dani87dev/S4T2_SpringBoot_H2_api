package cat.itacademy.s04.t02.n01.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FruitDTO {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Positive(message = "Weight must be a positive number")
    private int weightInKilos;
}