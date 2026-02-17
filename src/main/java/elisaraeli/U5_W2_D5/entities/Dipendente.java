package elisaraeli.U5_W2_D5.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"password", "accountNonExpired", "accountNonLocked", "authorities", "credentialsNonExpired", "enabled"})
@Entity
// rendo unique sia l'email che lo username del dipendente
@Table(name = "dipendente", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
}
)
public class Dipendente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Dipendente(UUID id, String username, String nome, String cognome, String email, String password, String avatar, Role role) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.role = Role.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
