package elisaraeli.U5_W2_D5.payloads;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ViaggioDTO(
        @NotEmpty(message = "La destinazione è obbligatoria!")
        @Size(min = 3, max = 30, message = "La destinazione deve essere compresa tra 3 e 30 caratteri.")
        String destinazione,
        @NotNull(message = "La data del viaggio è obbligatoria!")
        LocalDate data,
        // per il booleano "completato", durante la creazione del viaggio voglio che sia false
        // perché il viaggio non è ancora stato completato
        @AssertFalse(message = "Il campo deve essere falso")
        boolean completato
) {

}
