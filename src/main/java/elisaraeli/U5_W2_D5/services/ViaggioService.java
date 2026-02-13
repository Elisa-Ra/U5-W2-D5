package elisaraeli.U5_W2_D5.services;

import elisaraeli.U5_W2_D5.entities.StatoViaggio;
import elisaraeli.U5_W2_D5.entities.Viaggio;
import elisaraeli.U5_W2_D5.exceptions.NotFoundException;
import elisaraeli.U5_W2_D5.payloads.ViaggioDTO;
import elisaraeli.U5_W2_D5.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    public Viaggio save(ViaggioDTO body) throws IOException {

        Viaggio newViaggio = new Viaggio();
        newViaggio.setDestinazione(body.destinazione());
        newViaggio.setData(body.data());
        newViaggio.setStato(body.stato());
        return viaggioRepository.save(newViaggio);
    }

    public Page<Viaggio> getViaggio(int page, int size, String sort) {
        if (size > 100) size = 100;
        if (size < 1) size = 1;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return viaggioRepository.findAll(pageable);
    }

    // cerco un viaggio per id
    public Viaggio findById(UUID id) {
        return viaggioRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // elimino un viaggio per id
    public void findByIdAndDelete(UUID id) {
        Viaggio found = this.findById(id);
        viaggioRepository.delete(found);
    }

    // modifico un viaggio per id
    public Viaggio findByIdAndUpdate(UUID id, ViaggioDTO body) {

        Viaggio found = this.findById(id);
        found.setDestinazione(body.destinazione());
        found.setData(body.data());
        found.setStato(body.stato());
        return viaggioRepository.save(found);
    }

    // modifico il booleano che controlla lo stato del viaggio
    // l'endpoint sarà PATCH /viaggi/{id}/stato?completato=true
    public Viaggio updateStato(UUID id, StatoViaggio stato) {
        Viaggio found = this.findById(id);
        found.setStato(stato);
        return viaggioRepository.save(found);
    }


}
