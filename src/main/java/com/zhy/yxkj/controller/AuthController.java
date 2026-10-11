package com.zhy.yxkj.controller;

import com.zhy.yxkj.security.Accounts;
import com.zhy.yxkj.security.Sessions;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Accounts accounts;
    private final Sessions sessions;
    private final ConcurrentHashMap<String, long[]> attempts = new ConcurrentHashMap<>();
    public AuthController(Accounts accounts, Sessions sessions) { this.accounts = accounts; this.sessions = sessions; }
    public record Login(String username, String password) {}
    @PostMapping("/login")
    public synchronized Object login(@RequestBody Login login, HttpServletRequest request) {
        long now = System.currentTimeMillis();
        attempts.entrySet().removeIf(entry -> now - entry.getValue()[0] > 60000);
        if (attempts.size() >= 10000) throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        long[] count = attempts.computeIfAbsent(request.getRemoteAddr(), key -> new long[] { now, 0 });
        if (++count[1] > 10) throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        var user = accounts.authenticate(login.username(), login.password());
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        return Map.of("token", sessions.create(user), "id", user.id(), "role", user.role(), "expiresIn", 28800);
    }
    @PostMapping("/logout") public Object logout(HttpServletRequest request) {
        sessions.remove(request.getHeader("Authorization")); return Map.of("success", true);
    }
}
