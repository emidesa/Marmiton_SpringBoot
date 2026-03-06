package foreach.cda.Marmiton.mappers;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import foreach.cda.Marmiton.dtos.CreateIngredientDto;
import foreach.cda.Marmiton.dtos.IngredientDto;
import foreach.cda.Marmiton.dtos.UpdateIngredientDto;
import foreach.cda.Marmiton.entity.Ingredient;

@Component
public class IngredientMapper {

    public IngredientDto toDto(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setLibelle(ingredient.getLibelle());
        dto.setType(ingredient.getType());
        dto.setNombreCalories(ingredient.getNombreCalories());
        
        return dto;
    }

    public List<IngredientDto> toDtoList(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Ingredient toEntity(CreateIngredientDto dto) {
        if (dto == null) {
            return null;
        }
        
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle(dto.getLibelle());
        ingredient.setType(dto.getType());
        ingredient.setNombreCalories(dto.getNombreCalories());
        
        return ingredient;
    }

    public void updateEntityFromDto(UpdateIngredientDto dto, Ingredient ingredient) {
        if (dto == null || ingredient == null) {
            return;
        }
        
        if (dto.getLibelle() != null) {
            ingredient.setLibelle(dto.getLibelle());
        }
        if (dto.getType() != null) {
            ingredient.setType(dto.getType());
        }
        if (dto.getNombreCalories() != null) {
            ingredient.setNombreCalories(dto.getNombreCalories());
        }
    }
}
