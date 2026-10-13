package br.com.oficina.service;

import br.com.oficina.dto.LoginRequest;
import br.com.oficina.dto.TokenResponse;
import br.com.oficina.exception.ApiException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Usuário de demonstração (e-mail -> senha)
    private final Map<String, String> usuarios = Map.of("usuario@email.com", "123456");
    private final Set<String> tokensValidos = ConcurrentHashMap.newKeySet();

    public TokenResponse login(LoginRequest request) {
        if (request == null || request.email() == null || request.senha() == null) {
            throw ApiException.credenciaisInvalidas();
        }
        String senha = usuarios.get(request.email().trim().toLowerCase());
        if (senha == null || !senha.equals(request.senha())) {
            throw ApiException.credenciaisInvalidas();
        }
        String token = UUID.randomUUID().toString();
        tokensValidos.add(token);
        return new TokenResponse(token);
    }

    public boolean tokenValido(String token) {
        return token != null && tokensValidos.contains(token);
    }
}
