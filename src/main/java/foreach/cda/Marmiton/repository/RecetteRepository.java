package foreach.cda.Marmiton.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import foreach.cda.Marmiton.entity.Recette;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {
    
    List<Recette> findByUserId(Long userId);
    
    List<Recette> findByPartageTrue();
    
    List<Recette> findByNomPlatContainingIgnoreCase(String nomPlat);
    
    @Query("SELECT r FROM Recette r WHERE r.nombreCalories <= :maxCalories")
    List<Recette> findByMaxCalories(@Param("maxCalories") Integer maxCalories);
}
