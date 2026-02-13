package elisaraeli.U5_W2_D5.repositories;

import elisaraeli.U5_W2_D5.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ViaggioRepository extends JpaRepository<Viaggio, UUID> {
    
}
