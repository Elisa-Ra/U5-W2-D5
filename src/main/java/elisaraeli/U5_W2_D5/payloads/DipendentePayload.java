package elisaraeli.U5_W2_D5.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DipendentePayload {
    @NotEmpty(message = "Lo username è obbligatorio!")
    @Size(min = 3, max = 30, message = "Lo username deve essere compreso tra 3 e 30 caratteri.")
    private String username;
    @NotEmpty(message = "Il nome è obbligatorio")
    @Size(min = 3, max = 30, message = "Il nome deve essere compreso tra 3 e 30 caratteri.")
    private String nome;
    @NotEmpty(message = "Il cognome è obbligatorio")
    @Size(min = 3, max = 30, message = "Il cognome deve essere compreso tra 3 e 30 caratteri.")
    private String cognome;
    @NotEmpty(message = "L'email è obbligatoria")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "L'email inserita non è valida")
    private String email;
    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 4, message = "La password deve avere almeno 4 caratteri")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$", message = "La password deve contenere una maiuscola, una minuscola ecc ecc ...")
    private String password;

    public DipendentePayload(String nome, String cognome, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return nome;
    }

    public String getSurname() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
