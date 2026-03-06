package foreach.cda.Marmiton.mappers;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import foreach.cda.Marmiton.dtos.CreateRecetteDto;
import foreach.cda.Marmiton.dtos.RecetteDto;
import foreach.cda.Marmiton.dtos.RecetteIngredientDto;
import foreach.cda.Marmiton.dtos.UpdateRecetteDto;
import foreach.cda.Marmiton.entity.Recette;
import foreach.cda.Marmiton.entity.RecetteIngredient;

@Component
public class RecetteMapper {

    public RecetteDto toDto(Recette recette) {
        if (recette == null) {
            return null;
        }
        
        RecetteDto dto = new RecetteDto();
        dto.setId(recette.getId());
        dto.setNomPlat(recette.getNomPlat());
        dto.setDureePreparation(recette.getDureePreparation());
        dto.setDureeCuisson(recette.getDureeCuisson());
        dto.setNombreCalories(recette.getNombreCalories());
        dto.setPartage(recette.getPartage());
        
        if (recette.getUser() != null) {
            dto.setUserId(recette.getUser().getId());
            dto.setUserNom(recette.getUser().getNom());
            dto.setUserPrenom(recette.getUser().getPrenom());
        }
        
        // Mapper les ingrédients
        List<RecetteIngredientDto> ingredientsDto = recette.getRecetteIngredients().stream()
                .map(this::toIngredientDto)
                .collect(Collectors.toList());
        dto.setIngredients(ingredientsDto);
        
        return dto;
    }

    public List<RecetteDto> toDtoList(List<Recette> recettes) {
        return recettes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RecetteIngredientDto toIngredientDto(RecetteIngredient recetteIngredient) {
        if (recetteIngredient == null) {
            return null;
        }
        
        RecetteIngredientDto dto = new RecetteIngredientDto();
        dto.setIngredientId(recetteIngredient.getIngredient().getId());
        dto.setIngredientLibelle(recetteIngredient.getIngredient().getLibelle());
        dto.setQuantite(recetteIngredient.getQuantite());
        
        return dto;
    }

    public Recette toEntity(CreateRecetteDto dto) {
        if (dto == null) {
            return null;
        }
        
        Recette recette = new Recette();
        recette.setNomPlat(dto.getNomPlat());
        recette.setDureePreparation(dto.getDureePreparation());
        recette.setDureeCuisson(dto.getDureeCuisson());
        recette.setNombreCalories(dto.getNombreCalories());
        recette.setPartage(dto.getPartage() != null ? dto.getPartage() : false);
        
        return recette;
    }

    public void updateEntityFromDto(UpdateRecetteDto dto, Recette recette) {
        if (dto == null || recette == null) {
            return;
        }
        
        if (dto.getNomPlat() != null) {
            recette.setNomPlat(dto.getNomPlat());
        }
        if (dto.getDureePreparation() != null) {
            recette.setDureePreparation(dto.getDureePreparation());
        }
        if (dto.getDureeCuisson() != null) {
            recette.setDureeCuisson(dto.getDureeCuisson());
        }
        if (dto.getNombreCalories() != null) {
            recette.setNombreCalories(dto.getNombreCalories());
        }
        if (dto.getPartage() != null) {
            recette.setPartage(dto.getPartage());
        }
    }
}
