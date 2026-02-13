package elisaraeli.U5_W2_D5.payloads;

import elisaraeli.U5_W2_D5.entities.StatoViaggio;
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
        @NotNull(message = "Lo stato del viaggio è obbligatorio!")
        StatoViaggio stato

) {

}
