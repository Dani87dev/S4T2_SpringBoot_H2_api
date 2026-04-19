package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitDTO;
import cat.itacademy.s04.t02.n01.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping
    public ResponseEntity<FruitDTO> createFruit(@Valid @RequestBody FruitDTO fruitDTO) {
        FruitDTO created = fruitService.createFruit(fruitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitDTO> getFruitById(@PathVariable Long id) {
        return ResponseEntity.ok(fruitService.getFruitById(id));
    }

    @GetMapping
    public ResponseEntity<List<FruitDTO>> getAllFruits() {
        return ResponseEntity.ok(fruitService.getAllFruits());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitDTO> updateFruit(@PathVariable Long id, @Valid @RequestBody FruitDTO fruitDTO) {
        return ResponseEntity.ok(fruitService.updateFruit(id, fruitDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFruit(@PathVariable Long id) {
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }
}