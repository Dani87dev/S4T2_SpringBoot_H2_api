package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitDTO;
import cat.itacademy.s04.t02.n01.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FruitServiceTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    void createFruit_shouldReturnCreatedFruit() {
        Fruit saved = new Fruit(1L, "Apple", 5);
        when(fruitRepository.save(any(Fruit.class))).thenReturn(saved);

        FruitDTO input = new FruitDTO(null, "Apple", 5);
        FruitDTO result = fruitService.createFruit(input);

        assertEquals("Apple", result.getName());
        assertEquals(5, result.getWeightInKilos());
        assertNotNull(result.getId());
    }

    @Test
    void getFruitById_shouldReturnFruit() {
        Fruit fruit = new Fruit(1L, "Apple", 5);
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        FruitDTO result = fruitService.getFruitById(1L);

        assertEquals("Apple", result.getName());
    }

    @Test
    void getFruitById_shouldThrowWhenNotFound() {
        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.getFruitById(99L));
    }

    @Test
    void getAllFruits_shouldReturnList() {
        List<Fruit> fruits = Arrays.asList(
                new Fruit(1L, "Apple", 5),
                new Fruit(2L, "Banana", 3)
        );
        when(fruitRepository.findAll()).thenReturn(fruits);

        List<FruitDTO> result = fruitService.getAllFruits();

        assertEquals(2, result.size());
    }

    @Test
    void updateFruit_shouldReturnUpdatedFruit() {
        Fruit existing = new Fruit(1L, "Apple", 5);
        Fruit updated = new Fruit(1L, "Banana", 3);
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updated);

        FruitDTO input = new FruitDTO(null, "Banana", 3);
        FruitDTO result = fruitService.updateFruit(1L, input);

        assertEquals("Banana", result.getName());
        assertEquals(3, result.getWeightInKilos());
    }

    @Test
    void deleteFruit_shouldDeleteWhenExists() {
        when(fruitRepository.existsById(1L)).thenReturn(true);

        fruitService.deleteFruit(1L);

        verify(fruitRepository).deleteById(1L);
    }

    @Test
    void deleteFruit_shouldThrowWhenNotFound() {
        when(fruitRepository.existsById(99L)).thenReturn(false);

        assertThrows(FruitNotFoundException.class, () -> fruitService.deleteFruit(99L));
    }
}