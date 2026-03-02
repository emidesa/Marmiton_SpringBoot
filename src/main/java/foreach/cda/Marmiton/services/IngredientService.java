package foreach.cda.Marmiton.services;



import java.util.List;

import org.springframework.stereotype.Service;

import foreach.cda.Marmiton.DTO.CreateIngredientDto;
import foreach.cda.Marmiton.DTO.IngredientDto;
import foreach.cda.Marmiton.DTO.UpdateIngredientDto;
import foreach.cda.Marmiton.entity.Ingredient;
import foreach.cda.Marmiton.mappers.IngredientMapper;
import foreach.cda.Marmiton.repository.IngredientRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository ingredientRepository, 
                            IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    // CREATE
    public IngredientDto createIngredient(CreateIngredientDto createIngredientDto) {
        Ingredient ingredient = ingredientMapper.toEntity(createIngredientDto);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(savedIngredient);
    }

    // READ ALL
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredientMapper.toDtoList(ingredients);
    }

    // READ ONE
    public IngredientDto getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé avec l'id: " + id));
        return ingredientMapper.toDto(ingredient);
    }

    // READ BY TYPE
    public List<IngredientDto> getIngredientsByType(Ingredient.Type type) {
        List<Ingredient> ingredients = ingredientRepository.findByType(type);
        return ingredientMapper.toDtoList(ingredients);
    }

    // SEARCH BY NAME
    public List<IngredientDto> searchIngredientsByName(String libelle) {
        List<Ingredient> ingredients = ingredientRepository.findByLibelleContainingIgnoreCase(libelle);
        return ingredientMapper.toDtoList(ingredients);
    }

    // UPDATE
    public IngredientDto updateIngredient(Long id, UpdateIngredientDto updateIngredientDto) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé avec l'id: " + id));
        
        ingredientMapper.updateEntityFromDto(updateIngredientDto, ingredient);
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(updatedIngredient);
    }

    // DELETE
    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingrédient non trouvé avec l'id: " + id);
        }
        ingredientRepository.deleteById(id);
    }
}
