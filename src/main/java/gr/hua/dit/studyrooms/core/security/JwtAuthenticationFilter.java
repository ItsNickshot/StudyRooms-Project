package gr.hua.dit.studyrooms.core.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Προσθήκη του Service για φόρτωση χρήστη

    // Ενημέρωση του Constructor
    public JwtAuthenticationFilter(final JwtService jwtService,
                                   final UserDetailsService userDetailsService) {
        if (jwtService == null) throw new NullPointerException();
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    private void writeError(final HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"invalid_token\"}");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String path = request.getServletPath();
        if (path.equals("/api/auth/token")) return true;
        return !path.startsWith("/api");
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(7);
        try {
            final Claims claims = this.jwtService.parse(token);
            final String subject = claims.getSubject();

            // Έτσι το Principal θα είναι τύπου ApplicationUserDetails και όχι απλό User
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject);

            final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception ex) {
            LOGGER.warn("JwtAuthenticationFilter failed", ex);
            this.writeError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
