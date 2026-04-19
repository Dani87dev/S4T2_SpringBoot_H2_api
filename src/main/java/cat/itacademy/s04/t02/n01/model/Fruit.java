package cat.itacademy.s04.t02.n01.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fruits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int weightInKilos;
}