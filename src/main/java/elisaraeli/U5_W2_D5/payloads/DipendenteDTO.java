package elisaraeli.U5_W2_D5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(
        @NotEmpty(message = "Lo username è obbligatorio!")
        @Size(min = 3, max = 30, message = "Lo username deve essere compreso tra 3 e 30 caratteri.")
        String username,
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il nome deve essere compreso tra 3 e 30 caratteri.")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il cognome deve essere compreso tra 3 e 30 caratteri.")
        String cognome,
        @NotEmpty(message = "L'email è obbligatoria")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "L'email inserita non è valida")
        String email
) {

}
