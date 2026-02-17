package elisaraeli.U5_W2_D5.security;

import elisaraeli.U5_W2_D5.entities.Dipendente;
import elisaraeli.U5_W2_D5.entities.Role;
import elisaraeli.U5_W2_D5.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminRunner implements CommandLineRunner {

    private final DipendenteRepository dipendenteRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.nome}")
    private String adminNome;

    @Value("${admin.cognome}")
    private String adminCognome;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    public AdminRunner(DipendenteRepository dipendenteRepository, PasswordEncoder passwordEncoder) {
        this.dipendenteRepository = dipendenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // Controllo per non ricreare lo stesso admin ogni volta
        try {
            if (dipendenteRepository.findByEmail(adminEmail).isEmpty()) {

                Dipendente admin = new Dipendente();
                admin.setUsername(adminUsername);
                admin.setNome(adminNome);
                admin.setCognome(adminCognome);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setAvatar("https://ui-avatars.com/api/?name=" + adminNome + "+" + adminCognome);
                admin.setRole(Role.ADMIN);

                dipendenteRepository.save(admin);

                System.out.println("L'admin è stato creato!");
            } else {
                System.out.println("L'admin con questa mail è già presente.");
            }

        } catch (Exception ex) {
            System.out.println("Ops! C'è stato un errore." + ex.getMessage());
        }

    }
}

