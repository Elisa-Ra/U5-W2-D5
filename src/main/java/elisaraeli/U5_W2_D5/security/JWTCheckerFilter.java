package elisaraeli.U5_W2_D5.security;

import elisaraeli.U5_W2_D5.entities.Dipendente;
import elisaraeli.U5_W2_D5.exceptions.UnauthorizedException;
import elisaraeli.U5_W2_D5.services.DipendenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
// Per poter essere inserito nella filter chain deve essere un component e deve estendere OncePerRequestFilter
public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final DipendenteService dipendenteService;

    @Autowired
    public JWTCheckerFilter(JWTTools jwtTools, DipendenteService dipendenteService) {
        this.jwtTools = jwtTools;
        this.dipendenteService = dipendenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Questo è il metodo che viene eseguito a ogni richiesta
        // Sarà questo metodo quindi che dovrà fare il controllo dei token

        // PIANO DI BATTAGLIA

        // 1. Verifichiamo se la richiesta contiene l'header Authorization e che in caso sia nel formato "Bearer oi1j3oj21o3j213jo12j3"
        // Se l'header non c'è oppure se è malformato --> lanciamo eccezione
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'Authorization header nel formato corretto");

        // 2. Estraiamo il token dall'header
        //  authHeader -> "Bearer oi1j3oj21o3j213jo12j3"
        String accessToken = authHeader.replace("Bearer ", "");

        // 3. Verifichiamo se il token è valido (controllare la firma e verificare data di scadenza)
        jwtTools.verifyToken(accessToken);
        // ---------------- AUTORIZZAZIONE -------------
        // 1. Cerchiamo l'utente nel DB tramite l'id (che è nel token)
        // 1.1 Leggiamo l'id dal token
        UUID dipendenteId = jwtTools.extractIdFromToken(accessToken);

        // 1.2 Find by ID
        Dipendente authenticatedDipendente = this.dipendenteService.findById(dipendenteId);

        // 2. Associamo l'utente al Security Context
        // è uno step fondamentale che serve per associare l'utente che sta effettuando la richiesta (il proprietario del token) alla richiesta
        // stessa per tutta la sua durata, cioè fino a che essa non ottiene una risposta
        // Così facendo chiunque arriverà dopo questo filtro, altri filtri, il controller, un endpoint... potrà risalire a chi sia l'utente che
        // ha effettuato la richiesta
        // Questo è molto utile per ad esempio controllare i ruoli dell'utente prima di arrivare ad uno specifico endpoint. Oppure ci può servire
        // per effettuare determinati controlli all'interno degli endpoint stessi per verificare che chi stia facendo una certa operazione di
        // lettura/modifica/cancellazione sia l'effettivo proprietario della risorsa, oppure per, in fase di creazione di una risorsa, associare
        // l'effettivo proprietario a tale risorsa.

        // Primo parametro è lo user, secondo null, terzo le authorities
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedDipendente, null, authenticatedDipendente.getAuthorities());
        // Imposto l'utente nel contesto della richiesta.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Se tutto è OK --> andiamo avanti, trasmettiamo la richiesta al prossimo (può essere o un altro elemento della catena oppure il controller)
        filterChain.doFilter(request, response);

        // 5. Se c'è qualche problema con il token -> eccezione
    }

    // Tramite l'Override del metodo sottostante posso specificare quando il filtro non debba essere chiamato in causa
    // Ad esempio posso dirgli di non filtrare tutte le richieste dirette al controller "/auth"
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
        // return request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/auth/register");
    }
}