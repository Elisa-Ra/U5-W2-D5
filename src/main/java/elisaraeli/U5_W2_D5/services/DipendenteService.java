package elisaraeli.U5_W2_D5.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import elisaraeli.U5_W2_D5.entities.Dipendente;
import elisaraeli.U5_W2_D5.exceptions.BadRequestException;
import elisaraeli.U5_W2_D5.exceptions.NotFoundException;
import elisaraeli.U5_W2_D5.payloads.DipendenteDTO;
import elisaraeli.U5_W2_D5.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DipendenteService {
    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    public Dipendente save(DipendenteDTO body) throws IOException {
        // controllo che l'email non sià già presente
        dipendenteRepository.findByEmail(body.email()).ifPresent(user -> {
            throw new BadRequestException("L'email: " + body.email() + " è già stata utilizzata");
        });
        // controllo che l'username non sia già presente
        dipendenteRepository.findByUsername(body.username()).ifPresent(user -> {
            throw new BadRequestException("L'username: " + body.username() + " è già stato utilizzato");
        });
        Dipendente newDipendente = new Dipendente();
        newDipendente.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        newDipendente.setNome(body.nome());
        newDipendente.setCognome(body.cognome());
        newDipendente.setEmail(body.email());
        newDipendente.setUsername(body.username());
        newDipendente.setPassword(body.password());
        return dipendenteRepository.save(newDipendente);
    }

    public Page<Dipendente> getDipendente(int page, int size, String sort) {
        if (size > 100) size = 100;
        if (size < 1) size = 1;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return dipendenteRepository.findAll(pageable);
    }

    // cerco un dipendente per id
    public Dipendente findById(UUID id) {
        return dipendenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // elimino un dipendente per id
    public void findByIdAndDelete(UUID id) {
        Dipendente found = this.findById(id);
        dipendenteRepository.delete(found);
    }

    // modifico un dipendente per id
    public Dipendente findByIdAndUpdate(UUID id, DipendenteDTO body) {

        Dipendente found = this.findById(id);
        // controllo che non siano già presenti le stesse email/username
        if (!found.getEmail().equals(body.email())) {
            dipendenteRepository.findByEmail(body.email()).ifPresent(u -> {
                throw new BadRequestException("L'email è già stata utilizzata.");
            });
        }
        if (!found.getUsername().equals(body.username())) {
            dipendenteRepository.findByUsername(body.username()).ifPresent(u -> {
                throw new BadRequestException("L'username è già stato utilizzato.");
            });
        }

        found.setEmail(body.email());
        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setUsername(body.username());

        return dipendenteRepository.save(found);
    }

    // carico l'avatar con cloudinary
    public Dipendente uploadAvatar(UUID id, MultipartFile file) throws IOException {
        Dipendente found = this.findById(id);
        String avatarURL = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("secure_url");
        found.setAvatar(avatarURL);
        return dipendenteRepository.save(found);
    }

    // Cerco un dipendente per email
    public Dipendente findByEmail(String email) {
        return this.dipendenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato!"));
    }
}
