package foreach.cda.Marmiton.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import foreach.cda.Marmiton.dtos.CreateRecetteDto;
import foreach.cda.Marmiton.dtos.IngredientQuantiteDto;
import foreach.cda.Marmiton.dtos.RecetteDto;
import foreach.cda.Marmiton.dtos.UpdateRecetteDto;
import foreach.cda.Marmiton.entity.Ingredient;
import foreach.cda.Marmiton.entity.Recette;
import foreach.cda.Marmiton.entity.User;
import foreach.cda.Marmiton.exceptions.ForbiddenException;
import foreach.cda.Marmiton.exceptions.NotFoundException;
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
    private final CurrentUserService currentUserService;
    private final RecetteExportService recetteExportService;

    public RecetteService(RecetteRepository recetteRepository,
                         RecetteIngredientRepository recetteIngredientRepository,
                         UserRepository userRepository,
                         IngredientRepository ingredientRepository,
                         RecetteMapper recetteMapper,
                         CurrentUserService currentUserService,
                         RecetteExportService recetteExportService) {
        this.recetteRepository = recetteRepository;
        this.recetteIngredientRepository = recetteIngredientRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.recetteMapper = recetteMapper;
        this.currentUserService = currentUserService;
        this.recetteExportService = recetteExportService;
    }

    // CREATE
    public RecetteDto createRecette(CreateRecetteDto createRecetteDto) {
        User currentUser = currentUserService.requireCurrentUser();
        boolean isAdmin = currentUserService.isAdmin();

        User user;
        if (isAdmin) {
            if (createRecetteDto.getUserId() == null) {
                user = currentUser;
            } else {
                user = userRepository.findById(createRecetteDto.getUserId())
                        .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
            }
        } else {
            user = currentUser;
        }

        Recette recette = recetteMapper.toEntity(createRecetteDto);
        recette.setUser(user);
        
        // Sauvegarder la recette
        Recette savedRecette = recetteRepository.save(recette);

        // Ajouter les ingrédients
        if (createRecetteDto.getIngredients() != null) {
            for (IngredientQuantiteDto ingredientDto : createRecetteDto.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                        .orElseThrow(() -> new NotFoundException("Ingrédient non trouvé: " + ingredientDto.getIngredientId()));
                
                savedRecette.addIngredient(ingredient, ingredientDto.getQuantite());
            }
            savedRecette = recetteRepository.save(savedRecette);
        }

        return recetteMapper.toDto(savedRecette);
    }

    // READ ALL
    public List<RecetteDto> getAllRecettes() {
        List<Recette> recettes;
        if (currentUserService.isAdmin()) {
            recettes = recetteRepository.findAll();
        } else {
            User currentUser = currentUserService.requireCurrentUser();
            recettes = recetteRepository.findVisibleToUser(currentUser.getId());
        }
        return recetteMapper.toDtoList(recettes);
    }

    // READ ONE
    public RecetteDto getRecetteById(Long id) {
        Recette recette = recetteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recette non trouvée avec l'id: " + id));
        assertCanView(recette);
        return recetteMapper.toDto(recette);
    }

    // READ BY USER
    public List<RecetteDto> getRecettesByUserId(Long userId) {
        if (!currentUserService.isAdmin()) {
            User currentUser = currentUserService.requireCurrentUser();
            if (!currentUser.getId().equals(userId)) {
                throw new ForbiddenException("Accès interdit: recettes d'un autre utilisateur");
            }
        }
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
        List<Recette> recettes;
        if (currentUserService.isAdmin()) {
            recettes = recetteRepository.findByNomPlatContainingIgnoreCase(nomPlat);
        } else {
            User currentUser = currentUserService.requireCurrentUser();
            recettes = recetteRepository.searchVisibleByName(currentUser.getId(), nomPlat);
        }
        return recetteMapper.toDtoList(recettes);
    }

    // SEARCH BY MAX CALORIES
    public List<RecetteDto> getRecettesByMaxCalories(Integer maxCalories) {
        List<Recette> recettes;
        if (currentUserService.isAdmin()) {
            recettes = recetteRepository.findByMaxCalories(maxCalories);
        } else {
            User currentUser = currentUserService.requireCurrentUser();
            recettes = recetteRepository.findVisibleByMaxCalories(currentUser.getId(), maxCalories);
        }
        return recetteMapper.toDtoList(recettes);
    }

    // UPDATE
    public RecetteDto updateRecette(Long id, UpdateRecetteDto updateRecetteDto) {
        Recette recette = recetteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recette non trouvée avec l'id: " + id));
        assertOwnerOrAdmin(recette);

        recetteMapper.updateEntityFromDto(updateRecetteDto, recette);

        // Mettre à jour les ingrédients si fournis
        if (updateRecetteDto.getIngredients() != null) {
            // Supprimer les anciens ingrédients
            recetteIngredientRepository.deleteByIdRecetteId(id);
            recette.getRecetteIngredients().clear();

            // Ajouter les nouveaux ingrédients
            for (IngredientQuantiteDto ingredientDto : updateRecetteDto.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                        .orElseThrow(() -> new NotFoundException("Ingrédient non trouvé: " + ingredientDto.getIngredientId()));
                
                recette.addIngredient(ingredient, ingredientDto.getQuantite());
            }
        }

        Recette updatedRecette = recetteRepository.save(recette);
        return recetteMapper.toDto(updatedRecette);
    }

    // DELETE
    public void deleteRecette(Long id) {
        Recette recette = recetteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recette non trouvée avec l'id: " + id));
        assertOwnerOrAdmin(recette);
        recetteRepository.delete(recette);
    }

    // ADD TO FAVORITES
    public void addToFavorites(Long userId, Long recetteId) {
        if (!currentUserService.isAdmin()) {
            User currentUser = currentUserService.requireCurrentUser();
            if (!currentUser.getId().equals(userId)) {
                throw new ForbiddenException("Accès interdit: favoris d'un autre utilisateur");
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new NotFoundException("Recette non trouvée"));
        assertCanView(recette);

        user.getRecettesFavorites().add(recette);
        userRepository.save(user);
    }

    // REMOVE FROM FAVORITES
    public void removeFromFavorites(Long userId, Long recetteId) {
        if (!currentUserService.isAdmin()) {
            User currentUser = currentUserService.requireCurrentUser();
            if (!currentUser.getId().equals(userId)) {
                throw new ForbiddenException("Accès interdit: favoris d'un autre utilisateur");
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new NotFoundException("Recette non trouvée"));

        user.getRecettesFavorites().remove(recette);
        userRepository.save(user);
    }

    // GET USER FAVORITES
    public List<RecetteDto> getUserFavorites(Long userId) {
        if (!currentUserService.isAdmin()) {
            User currentUser = currentUserService.requireCurrentUser();
            if (!currentUser.getId().equals(userId)) {
                throw new ForbiddenException("Accès interdit: favoris d'un autre utilisateur");
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
        
        return recetteMapper.toDtoList(user.getRecettesFavorites().stream().toList());
    }

    public byte[] exportRecettePdf(Long recetteId) {
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new NotFoundException("Recette non trouvée avec l'id: " + recetteId));
        assertCanView(recette);
        return recetteExportService.toPdf(recette);
    }

    public byte[] exportRecetteXlsx(Long recetteId) {
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new NotFoundException("Recette non trouvée avec l'id: " + recetteId));
        assertCanView(recette);
        return recetteExportService.toXlsx(recette);
    }

    private void assertCanView(Recette recette) {
        if (Boolean.TRUE.equals(recette.getPartage())) {
            return;
        }
        if (currentUserService.isAdmin()) {
            return;
        }
        User currentUser = currentUserService.requireCurrentUser();
        if (recette.getUser() != null && recette.getUser().getId() != null
                && recette.getUser().getId().equals(currentUser.getId())) {
            return;
        }
        throw new ForbiddenException("Accès interdit: recette privée");
    }

    private void assertOwnerOrAdmin(Recette recette) {
        if (currentUserService.isAdmin()) {
            return;
        }
        User currentUser = currentUserService.requireCurrentUser();
        if (recette.getUser() != null && recette.getUser().getId() != null
                && recette.getUser().getId().equals(currentUser.getId())) {
            return;
        }
        throw new ForbiddenException("Accès interdit: vous n'êtes pas le propriétaire de la recette");
    }
}
