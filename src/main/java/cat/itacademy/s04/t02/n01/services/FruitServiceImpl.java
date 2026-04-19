package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitDTO;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    public FruitServiceImpl(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Override
    public FruitDTO createFruit(FruitDTO fruitDTO) {
        Fruit fruit = new Fruit();
        fruit.setName(fruitDTO.getName());
        fruit.setWeightInKilos(fruitDTO.getWeightInKilos());
        Fruit saved = fruitRepository.save(fruit);
        return toDTO(saved);
    }

    @Override
    public FruitDTO getFruitById(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fruit not found with id: " + id));
        return toDTO(fruit);
    }

    @Override
    public List<FruitDTO> getAllFruits() {
        return fruitRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FruitDTO updateFruit(Long id, FruitDTO fruitDTO) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fruit not found with id: " + id));
        fruit.setName(fruitDTO.getName());
        fruit.setWeightInKilos(fruitDTO.getWeightInKilos());
        Fruit updated = fruitRepository.save(fruit);
        return toDTO(updated);
    }

    @Override
    public void deleteFruit(Long id) {
        if (!fruitRepository.existsById(id)) {
            throw new RuntimeException("Fruit not found with id: " + id);
        }
        fruitRepository.deleteById(id);
    }

    private FruitDTO toDTO(Fruit fruit) {
        FruitDTO dto = new FruitDTO();
        dto.setId(fruit.getId());
        dto.setName(fruit.getName());
        dto.setWeightInKilos(fruit.getWeightInKilos());
        return dto;
    }
}