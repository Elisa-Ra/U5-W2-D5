package elisaraeli.U5_W2_D5.controllers;

import elisaraeli.U5_W2_D5.entities.Viaggio;
import elisaraeli.U5_W2_D5.exceptions.BadRequestException;
import elisaraeli.U5_W2_D5.payloads.ViaggioDTO;
import elisaraeli.U5_W2_D5.services.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {
    @Autowired
    ViaggioService viaggioService;

    // 1. - POST http://localhost:3001/viaggi (+ req.body)
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public Viaggio saveViaggio(@RequestBody @Validated ViaggioDTO body, BindingResult validation) throws Exception {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return viaggioService.save(body);

    }

    // 2. - GET http://localhost:3001/viaggi
    @GetMapping("")
    public Page<Viaggio> getViaggio(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return viaggioService.getViaggio(page, size, sortBy);
    }

    // 3. - GET http://localhost:3001/viaggi/{id}
    @GetMapping("/{viaggioId}")
    public Viaggio findById(@PathVariable UUID viaggioId) {
        return viaggioService.findById(viaggioId);
    }

    // 4. - PUT http://localhost:3001/viaggi/{id} (+ req.body)
    @PutMapping("/{viaggioId}")
    public Viaggio findAndUpdate(@PathVariable UUID viaggioId, @RequestBody @Validated ViaggioDTO body, BindingResult validation
    ) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return viaggioService.findByIdAndUpdate(viaggioId, body);
    }

    // 5. - DELETE http://localhost:3001/viaggi/{id}
    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204 NO CONTENT
    public void findAndDelete(@PathVariable UUID viaggioId) {
        viaggioService.findByIdAndDelete(viaggioId);
    }

    // 6. - PATCH http://localhost:3001/viaggi/{id}/stato?completato=true
    @PatchMapping("/{viaggioId}/stato")
    public Viaggio updateStato(
            @PathVariable UUID viaggioId,
            @RequestParam boolean completato
    ) {
        return viaggioService.updateStato(viaggioId, completato);
    }

}
