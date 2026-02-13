package elisaraeli.U5_W2_D5.payloads;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "La data di prenotazione è obbligatoria")
        LocalDate data_prenotazione,
        // per le note metto Nullable perché possono non esserci note
        @Size(min = 3, max = 255, message = "Le note devono essere comprese tra 3 e 255 caratteri.")
        @Nullable
        String note,
        @NotNull(message = "L'ID del dipendente è obbligatorio.")
        UUID dipendenteId,
        @NotNull(message = "L'ID del viaggio è obbligatorio.")
        UUID viaggioId
) {
}
