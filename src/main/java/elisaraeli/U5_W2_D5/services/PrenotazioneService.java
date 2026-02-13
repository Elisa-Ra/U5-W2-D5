package elisaraeli.U5_W2_D5.services;

import elisaraeli.U5_W2_D5.entities.Dipendente;
import elisaraeli.U5_W2_D5.entities.Prenotazione;
import elisaraeli.U5_W2_D5.entities.StatoViaggio;
import elisaraeli.U5_W2_D5.entities.Viaggio;
import elisaraeli.U5_W2_D5.exceptions.BadRequestException;
import elisaraeli.U5_W2_D5.exceptions.NotFoundException;
import elisaraeli.U5_W2_D5.payloads.PrenotazioneDTO;
import elisaraeli.U5_W2_D5.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private ViaggioService viaggioService;

    public Prenotazione save(PrenotazioneDTO body) {

        Dipendente dipendente = dipendenteService.findById(body.dipendenteId());
        Viaggio viaggio = viaggioService.findById(body.viaggioId());

        // durante la creazione lo stato del viaggio non può essere COMPLETATO
        if (viaggio.getStato() == StatoViaggio.COMPLETATO) {
            throw new BadRequestException("Non è possibile prenotare un viaggio già completato.");
        }

        // controllo se c'è già un dipendente che ha prenotato un viaggio in una determinata data
        boolean stessoViaggio = prenotazioneRepository
                .existsByDipendente_IdAndViaggio_Data(
                        dipendente.getId(),
                        viaggio.getData()
                );

        if (stessoViaggio) {
            throw new BadRequestException(
                    "Il dipendente ha già un viaggio nella seguente data: " + viaggio.getData()
            );
        }


        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData_prenotazione(body.data_prenotazione());
        prenotazione.setNote(body.note());
        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);

        return prenotazioneRepository.save(prenotazione);
    }

    // cerco una prenotazione per id
    public Prenotazione findById(UUID id) {
        return prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La prenotazione con l'id: " + id + "non è stata trovata."));
    }

    // lista delle prenotazioni di un determinato dipendente
    public List<Prenotazione> getByDipendente(UUID dipendenteId) {
        return prenotazioneRepository.findByDipendenteId(dipendenteId);
    }
}
