package elisaraeli.U5_W2_D5.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // Classe di configurazione apposita di Spring Security
@EnableMethodSecurity // Annotazione OBBLIGATORIA per usare le varie @PreAuthorize sugli endpoint
public class SecurityConfig {
    @Autowired
    private JWTCheckerFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        // Bean per configurare le impostazioni di sicurezza di Spring Security

        // - Disabilitare comportamenti di default che non voglio
        // come l'autenticazione via form di default di Sprint Security (userò quello di React nel front-end)
        httpSecurity.formLogin(formLogin -> formLogin.disable());

        // - Togliere la protezione da attacchi CSRF di default (non serve nel caso dell'autenticazione tramite JWT)
        httpSecurity.csrf(csrf -> csrf.disable());

        // - Mettere l'autenticazione stateless invece di statefull.
        // L'autenticazione JWT è l'esatto opposto del lavorare tramite sessioni
        httpSecurity.sessionManagement(sessions ->
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // - Personalizzare il comportamento di funzionalità pre-esistenti
        // Disabilitiamo il meccanismo di default di Spring Security che ritorna 401 a ogni richiesta.
        // In modo da implementare un meccanismo di autenticazione personalizzato
        httpSecurity.authorizeHttpRequests(req ->
                req.requestMatchers("/**").permitAll());

        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://mywonderfulfe.com"));
        // Qua definisco una WHITELIST di uno o più indirizzi FRONTEND che voglio possano accedere a questo backend senza i problemi di CORS
        // Volendo ma rischioso si potrebbe anche mettere '*' però questo permetterebbe l'accesso a tutti (utile solo nel caso di API pubbliche)
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}