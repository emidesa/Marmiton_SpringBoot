package foreach.cda.Marmiton.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import foreach.cda.Marmiton.DTO.CreateRecetteDto;
import foreach.cda.Marmiton.DTO.IngredientQuantiteDto;
import foreach.cda.Marmiton.DTO.RecetteDto;
import foreach.cda.Marmiton.DTO.UpdateRecetteDto;
import foreach.cda.Marmiton.entity.Ingredient;
import foreach.cda.Marmiton.entity.Recette;
import foreach.cda.Marmiton.entity.User;
import foreach.cda.Marmiton.mappers.RecetteMapper;
import foreach.cda.Marmiton.repository.IngredientRepository;
import foreach.cda.Marmiton.repository.RecetteIngredientRepository;
import foreach.cda.Marmiton.repository.RecetteRepository;
import foreach.cda.Marmiton.repository.UserRepository;

@Service
@Transactional
public class RecetteService {

    private final RecetteRepository recetteRepository;
    private final RecetteIngredientRepository recetteIngredientRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final RecetteMapper recetteMapper;

    public RecetteService(RecetteRepository recetteRepository,
                         RecetteIngredientRepository recetteIngredientRepository,
                         UserRepository userRepository,
                         IngredientRepository ingredientRepository,
                         RecetteMapper recetteMapper) {
        this.recetteRepository = recetteRepository;
        this.recetteIngredientRepository = recetteIngredientRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.recetteMapper = recetteMapper;
    }

    // CREATE
    public RecetteDto createRecette(CreateRecetteDto createRecetteDto) {
        // Vérifier que l'utilisateur existe
        User user = userRepository.findById(createRecetteDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Recette recette = recetteMapper.toEntity(createRecetteDto);
        recette.setUser(user);
        
        // Sauvegarder la recette
        Recette savedRecette = recetteRepository.save(recette);

        // Ajouter les ingrédients
        if (createRecetteDto.getIngredients() != null) {
            for (IngredientQuantiteDto ingredientDto : createRecetteDto.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                        .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé: " + ingredientDto.getIngredientId()));
                
                savedRecette.addIngredient(ingredient, ingredientDto.getQuantite());
            }
            savedRecette = recetteRepository.save(savedRecette);
        }

        return recetteMapper.toDto(savedRecette);
    }

    // READ ALL
    public List<RecetteDto> getAllRecettes() {
        List<Recette> recettes = recetteRepository.findAll();
        return recetteMapper.toDtoList(recettes);
    }

    // READ ONE
    public RecetteDto getRecetteById(Long id) {
        Recette recette = recetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée avec l'id: " + id));
        return recetteMapper.toDto(recette);
    }

    // READ BY USER
    public List<RecetteDto> getRecettesByUserId(Long userId) {
        List<Recette> recettes = recetteRepository.findByUserId(userId);
        return recetteMapper.toDtoList(recettes);
    }

    // READ PUBLIC RECETTES
    public List<RecetteDto> getPublicRecettes() {
        List<Recette> recettes = recetteRepository.findByPartageTrue();
        return recetteMapper.toDtoList(recettes);
    }

    // SEARCH BY NAME
    public List<RecetteDto> searchRecettesByName(String nomPlat) {
        List<Recette> recettes = recetteRepository.findByNomPlatContainingIgnoreCase(nomPlat);
        return recetteMapper.toDtoList(recettes);
    }

    // SEARCH BY MAX CALORIES
    public List<RecetteDto> getRecettesByMaxCalories(Integer maxCalories) {
        List<Recette> recettes = recetteRepository.findByMaxCalories(maxCalories);
        return recetteMapper.toDtoList(recettes);
    }

    // UPDATE
    public RecetteDto updateRecette(Long id, UpdateRecetteDto updateRecetteDto) {
        Recette recette = recetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée avec l'id: " + id));

        recetteMapper.updateEntityFromDto(updateRecetteDto, recette);

        // Mettre à jour les ingrédients si fournis
        if (updateRecetteDto.getIngredients() != null) {
            // Supprimer les anciens ingrédients
            recetteIngredientRepository.deleteByRecetteId(id);
            recette.getRecetteIngredients().clear();

            // Ajouter les nouveaux ingrédients
            for (IngredientQuantiteDto ingredientDto : updateRecetteDto.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                        .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé: " + ingredientDto.getIngredientId()));
                
                recette.addIngredient(ingredient, ingredientDto.getQuantite());
            }
        }

        Recette updatedRecette = recetteRepository.save(recette);
        return recetteMapper.toDto(updatedRecette);
    }

    // DELETE
    public void deleteRecette(Long id) {
        if (!recetteRepository.existsById(id)) {
            throw new RuntimeException("Recette non trouvée avec l'id: " + id);
        }
        recetteRepository.deleteById(id);
    }

    // ADD TO FAVORITES
    public void addToFavorites(Long userId, Long recetteId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));

        user.getRecettesFavorites().add(recette);
        userRepository.save(user);
    }

    // REMOVE FROM FAVORITES
    public void removeFromFavorites(Long userId, Long recetteId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));

        user.getRecettesFavorites().remove(recette);
        userRepository.save(user);
    }

    // GET USER FAVORITES
    public List<RecetteDto> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return recetteMapper.toDtoList(user.getRecettesFavorites().stream().toList());
    }
}
