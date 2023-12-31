package br.com.internet.bank.token.security;

import br.com.internet.bank.token.dto.User;
import jakarta.servlet.ServletContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {
    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    @Resource
    private ServletContext context;



    private final Map<String, User> users = new ConcurrentHashMap<>();

    public InMemoryUserDetailsService() {
        users.put(USER,new User(USER, "{noop}"+USER, List.of(USER_ROLE)));
        users.put(ADMIN, new User(ADMIN, "{noop}"+ADMIN, List.of(ADMIN_ROLE)));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String passwordId= (String) context.getAttribute("password");
        System.out.println(passwordId);

        return Optional.ofNullable(users.get(username))
                .map(this::getUser)
                .orElseThrow(()-> new RuntimeException(String.format( "user = %s not present ", username)));
    }
    private UserDetails getUser(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.username())
                .password(user.password())
                .roles(user.roles().toArray(new String[0]))
                .build();
    }
}
