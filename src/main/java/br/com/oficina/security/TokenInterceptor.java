package br.com.oficina.security;

import br.com.oficina.exception.ApiException;
import br.com.oficina.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Exige o cabeçalho "Authorization: Bearer TOKEN" em todas as rotas, menos no login. */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final String PREFIXO = "Bearer ";

    private final AuthService authService;

    public TokenInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String cabecalho = request.getHeader("Authorization");
        if (cabecalho == null || !cabecalho.startsWith(PREFIXO)) {
            throw ApiException.naoAutenticado();
        }
        if (!authService.tokenValido(cabecalho.substring(PREFIXO.length()).trim())) {
            throw ApiException.naoAutenticado();
        }
        return true;
    }
}
