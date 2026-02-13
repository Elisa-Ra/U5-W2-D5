package elisaraeli.U5_W2_D5.controllers;

import elisaraeli.U5_W2_D5.entities.Dipendente;
import elisaraeli.U5_W2_D5.exceptions.BadRequestException;
import elisaraeli.U5_W2_D5.payloads.DipendenteDTO;
import elisaraeli.U5_W2_D5.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {
    @Autowired
    DipendenteService dipendenteService;

    // 1. - POST http://localhost:3001/dipendenti (+ req.body)
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public Dipendente saveDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validation) throws Exception {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return dipendenteService.save(body);

    }

    // 2. - GET http://localhost:3001/dipendenti
    @GetMapping("")
    public Page<Dipendente> getDipendente(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return dipendenteService.getDipendente(page, size, sortBy);
    }

    // 3. - GET http://localhost:3001/dipendenti/{id}
    @GetMapping("/{dipendenteId}")
    public Dipendente findById(@PathVariable UUID dipendenteId) {
        return dipendenteService.findById(dipendenteId);
    }

    // 4. - PUT http://localhost:3001/dipendenti/{id} (+ req.body)
    @PutMapping("/{dipendenteId}")
    public Dipendente findAndUpdate(@PathVariable UUID dipendenteId, @RequestBody @Validated DipendenteDTO body, BindingResult validation
    ) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return dipendenteService.findByIdAndUpdate(dipendenteId, body);
    }

    // 5. - DELETE http://localhost:3001/dipendenti/{id}
    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204 NO CONTENT
    public void findAndDelete(@PathVariable UUID dipendenteId) {
        dipendenteService.findByIdAndDelete(dipendenteId);
    }

    @PatchMapping("/{dipendenteId}/avatar")
    public Dipendente uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable UUID dipendenteId) {
        try {
            return dipendenteService.uploadAvatar(dipendenteId, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
