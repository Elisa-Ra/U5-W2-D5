package elisaraeli.U5_W2_D5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "prenotazione")
public class Prenotazione {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDate data_prenotazione;
    private String note;
    @ManyToOne
    @JoinColumn(name = "id_dipendente")
    private Dipendente dipendente;
    @ManyToOne
    @JoinColumn(name = "id_viaggio")
    private Viaggio viaggio;
}
