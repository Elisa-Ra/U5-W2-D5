package elisaraeli.U5_W2_D5.controllers;

import elisaraeli.U5_W2_D5.entities.Dipendente;
import elisaraeli.U5_W2_D5.exceptions.ValidationException;
import elisaraeli.U5_W2_D5.payloads.DipendenteDTO;
import elisaraeli.U5_W2_D5.payloads.LoginDTO;
import elisaraeli.U5_W2_D5.payloads.LoginResponseDTO;
import elisaraeli.U5_W2_D5.services.AuthService;
import elisaraeli.U5_W2_D5.services.DipendenteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final DipendenteService dipendenteService;

    public AuthController(AuthService authService, DipendenteService dipendenteService) {
        this.authService = authService;
        this.dipendenteService = dipendenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {

        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente createUser(@RequestBody @Validated DipendenteDTO payload, BindingResult validationResult) throws IOException {
        // @Validated serve per attivare la validazione, se non lo usiamo è come non farla

        if (validationResult.hasErrors()) {

//			String errors = validationResult.getFieldErrors().stream()
//					.map(fieldError -> fieldError.getDefaultMessage())
//					.collect(Collectors.joining(". "));
//
//			throw new ValidationException(errors);
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.dipendenteService.save(payload);
        }

    }
}