package elisaraeli.U5_W2_D5.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "viaggio")
public class Viaggio {
    @Id
    @GeneratedValue
    private UUID id;
    private String destinazione;
    private LocalDate data;
    private boolean completato;
}
