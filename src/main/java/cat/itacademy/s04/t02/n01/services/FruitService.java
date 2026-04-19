package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitDTO;
import java.util.List;

public interface FruitService {

    FruitDTO createFruit(FruitDTO fruitDTO);
    FruitDTO getFruitById(Long id);
    List<FruitDTO> getAllFruits();
    FruitDTO updateFruit(Long id, FruitDTO fruitDTO);
    void deleteFruit(Long id);
}