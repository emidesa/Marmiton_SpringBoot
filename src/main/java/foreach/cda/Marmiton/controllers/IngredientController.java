package foreach.cda.Marmiton.controllers;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import foreach.cda.Marmiton.DTO.CreateIngredientDto;
import foreach.cda.Marmiton.DTO.IngredientDto;
import foreach.cda.Marmiton.DTO.UpdateIngredientDto;
import foreach.cda.Marmiton.entity.Ingredient;
import foreach.cda.Marmiton.services.IngredientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "*")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(
            @Valid @RequestBody CreateIngredientDto createIngredientDto) {
        IngredientDto createdIngredient = ingredientService.createIngredient(createIngredientDto);
        return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        List<IngredientDto> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable Long id) {
        IngredientDto ingredient = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ingredient);
    }

    // READ BY TYPE
    @GetMapping("/type/{type}")
    public ResponseEntity<List<IngredientDto>> getIngredientsByType(@PathVariable Ingredient.Type type) {
        List<IngredientDto> ingredients = ingredientService.getIngredientsByType(type);
        return ResponseEntity.ok(ingredients);
    }

    // SEARCH BY NAME
    @GetMapping("/search")
    public ResponseEntity<List<IngredientDto>> searchIngredientsByName(@RequestParam String libelle) {
        List<IngredientDto> ingredients = ingredientService.searchIngredientsByName(libelle);
        return ResponseEntity.ok(ingredients);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIngredientDto updateIngredientDto) {
        IngredientDto updatedIngredient = ingredientService.updateIngredient(id, updateIngredientDto);
        return ResponseEntity.ok(updatedIngredient);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
