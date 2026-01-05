package gr.hua.dit.studyrooms.web.rest;

import gr.hua.dit.studyrooms.core.security.JwtService;
import gr.hua.dit.studyrooms.web.rest.model.ClientTokenRequest;
import gr.hua.dit.studyrooms.web.rest.model.ClientTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/token")
public class ClientAuthResource {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ClientAuthResource(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<ClientTokenResponse> getToken(@RequestBody ClientTokenRequest request) {
        // 1. Αυθεντικοποίηση με username/password (clientId/secret)
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.clientId(), request.clientSecret())
        );

        // 2. Δημιουργία Token
        // Χρησιμοποιούμε τη μέθοδο issue() αντί για generateToken()
        String token = jwtService.issue(
            authentication.getName(),
            authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(new ClientTokenResponse(token));
    }
}
