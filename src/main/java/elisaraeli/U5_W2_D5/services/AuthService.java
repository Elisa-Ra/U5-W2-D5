package elisaraeli.U5_W2_D5.services;

import elisaraeli.U5_W2_D5.entities.Dipendente;
import elisaraeli.U5_W2_D5.exceptions.UnauthorizedException;
import elisaraeli.U5_W2_D5.payloads.LoginDTO;
import elisaraeli.U5_W2_D5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DipendenteService dipendenteService;
    private final JWTTools jwtTools;

    @Autowired
    public AuthService(DipendenteService dipendenteService, JWTTools jwtTools) {

        this.dipendenteService = dipendenteService;
        this.jwtTools = jwtTools;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        // 1. Controllo credenziali
        // 1.1 Controllo se esiste un utente con quell'email
        Dipendente found = this.dipendenteService.findByEmail(body.email());

        // 1.2 Se esiste controllo se la sua password (quella nel DB) è uguale a quella nel body
        // TODO: migliorare gestione password
        if (found.getPassword().equals(body.password())) {
            // 2. Se credenziali OK
            // 2.1 Genero token
            String accessToken = jwtTools.generateToken(found);

            // 2.2 Ritorno token
            return accessToken;

        } else {
            // 3. Se credenziali non ok --> 401 Unauthorized
            throw new UnauthorizedException("Credenziali errate!");
        }


    }
}
