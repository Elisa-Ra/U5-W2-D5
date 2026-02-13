package elisaraeli.U5_W2_D5.repositories;

import elisaraeli.U5_W2_D5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByDipendenteId(UUID dipendenteId);

    boolean existsByDipendente_IdAndViaggio_Data(UUID dipendenteId, LocalDate dataViaggio);
}
