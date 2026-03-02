package foreach.cda.Marmiton.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import foreach.cda.Marmiton.entity.RecetteIngredient;
import foreach.cda.Marmiton.entity.RecetteIngredientId;

@Repository
public interface RecetteIngredientRepository extends JpaRepository<RecetteIngredient, RecetteIngredientId> {
    List<RecetteIngredient> findByRecetteId(Long recetteId);
    void deleteByRecetteId(Long recetteId);
}