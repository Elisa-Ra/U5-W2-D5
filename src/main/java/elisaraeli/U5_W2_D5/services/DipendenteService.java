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
        dipendenteRepository.findByEmail(body.email()).ifPresent(user -> {
            throw new BadRequestException("L'email: " + body.email() + " è già stata utilizzata");
        });
        dipendenteRepository.findByUsername(body.username()).ifPresent(user -> {
            throw new BadRequestException("L'username: " + body.username() + " è già stato utilizzato");
        });
        Dipendente newDipendente = new Dipendente();
        newDipendente.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        newDipendente.setNome(body.nome());
        newDipendente.setCognome(body.cognome());
        newDipendente.setEmail(body.email());
        newDipendente.setUsername(body.username());
        return dipendenteRepository.save(newDipendente);
    }

    public Page<Dipendente> getDipendente(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return dipendenteRepository.findAll(pageable);
    }

    public Dipendente findById(UUID id) {
        return dipendenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Dipendente found = this.findById(id);
        dipendenteRepository.delete(found);
    }

    public Dipendente findByIdAndUpdate(UUID id, Dipendente body) {

        Dipendente found = this.findById(id);
        found.setEmail(body.getEmail());
        found.setNome(body.getNome());
        found.setCognome(body.getCognome());
        found.setUsername(body.getUsername());
        found.setAvatar(body.getAvatar());
        return dipendenteRepository.save(found);
    }

    public Dipendente uploadAvatar(UUID id, MultipartFile file) throws IOException {
        Dipendente found = this.findById(id);
        String avatarURL = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(avatarURL);
        return dipendenteRepository.save(found);
    }
}
