package elisaraeli.U5_W2_D5.controllers;

import elisaraeli.U5_W2_D5.entities.Prenotazione;
import elisaraeli.U5_W2_D5.exceptions.BadRequestException;
import elisaraeli.U5_W2_D5.payloads.PrenotazioneDTO;
import elisaraeli.U5_W2_D5.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    // 1. POST - http://localhost:3001/prenotazioni (+ req.body)
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(
            @RequestBody @Validated PrenotazioneDTO body,
            BindingResult validation
    ) throws Exception {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return prenotazioneService.save(body);
    }

    // 2. GET - http://localhost:3001/prenotazioni/{id}
    @GetMapping("/{prenotazioneId}")
    public Prenotazione getById(@PathVariable UUID prenotazioneId) {
        return prenotazioneService.findById(prenotazioneId);
    }

    // 3. GET -  http://localhost:3001/prenotazioni/dipendente/{dipendenteId}
    // per avere le prenotazioni di un determinato dipendente
    @GetMapping("/dipendente/{dipendenteId}")
    public List<Prenotazione> getByDipendente(@PathVariable UUID dipendenteId) {
        return prenotazioneService.getByDipendente(dipendenteId);
    }
}

