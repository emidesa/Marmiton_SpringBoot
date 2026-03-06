package foreach.cda.Marmiton.controllers;


import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import foreach.cda.Marmiton.dtos.CreateRecetteDto;
import foreach.cda.Marmiton.dtos.RecetteDto;
import foreach.cda.Marmiton.dtos.UpdateRecetteDto;
import foreach.cda.Marmiton.services.RecetteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recettes")
@CrossOrigin(origins = "*")
public class RecetteController {

    private final RecetteService recetteService;

    public RecetteController(RecetteService recetteService) {
        this.recetteService = recetteService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<RecetteDto> createRecette(@Valid @RequestBody CreateRecetteDto createRecetteDto) {
        RecetteDto createdRecette = recetteService.createRecette(createRecetteDto);
        return new ResponseEntity<>(createdRecette, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<RecetteDto>> getAllRecettes() {
        List<RecetteDto> recettes = recetteService.getAllRecettes();
        return ResponseEntity.ok(recettes);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<RecetteDto> getRecetteById(@PathVariable Long id) {
        RecetteDto recette = recetteService.getRecetteById(id);
        return ResponseEntity.ok(recette);
    }

    // READ BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecetteDto>> getRecettesByUserId(@PathVariable Long userId) {
        List<RecetteDto> recettes = recetteService.getRecettesByUserId(userId);
        return ResponseEntity.ok(recettes);
    }

    // READ PUBLIC RECETTES
    @GetMapping("/publiques")
    public ResponseEntity<List<RecetteDto>> getPublicRecettes() {
        List<RecetteDto> recettes = recetteService.getPublicRecettes();
        return ResponseEntity.ok(recettes);
    }

    // SEARCH BY NAME
    @GetMapping("/search")
    public ResponseEntity<List<RecetteDto>> searchRecettesByName(@RequestParam String nomPlat) {
        List<RecetteDto> recettes = recetteService.searchRecettesByName(nomPlat);
        return ResponseEntity.ok(recettes);
    }

    // SEARCH BY MAX CALORIES
    @GetMapping("/calories")
    public ResponseEntity<List<RecetteDto>> getRecettesByMaxCalories(@RequestParam Integer maxCalories) {
        List<RecetteDto> recettes = recetteService.getRecettesByMaxCalories(maxCalories);
        return ResponseEntity.ok(recettes);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RecetteDto> updateRecette(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRecetteDto updateRecetteDto) {
        RecetteDto updatedRecette = recetteService.updateRecette(id, updateRecetteDto);
        return ResponseEntity.ok(updatedRecette);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecette(@PathVariable Long id) {
        recetteService.deleteRecette(id);
        return ResponseEntity.noContent().build();
    }

    // FAVORITES
    @PostMapping("/{recetteId}/favorites/{userId}")
    public ResponseEntity<Void> addToFavorites(
            @PathVariable Long userId,
            @PathVariable Long recetteId) {
        recetteService.addToFavorites(userId, recetteId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recetteId}/favorites/{userId}")
    public ResponseEntity<Void> removeFromFavorites(
            @PathVariable Long userId,
            @PathVariable Long recetteId) {
        recetteService.removeFromFavorites(userId, recetteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<RecetteDto>> getUserFavorites(@PathVariable Long userId) {
        List<RecetteDto> favorites = recetteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    // EXPORT
    @GetMapping(value = "/{id}/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportRecettePdf(@PathVariable Long id) {
        byte[] pdf = recetteService.exportRecettePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recette-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/{id}/export/xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportRecetteXlsx(@PathVariable Long id) {
        byte[] xlsx = recetteService.exportRecetteXlsx(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recette-" + id + ".xlsx\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(xlsx);
    }
}
