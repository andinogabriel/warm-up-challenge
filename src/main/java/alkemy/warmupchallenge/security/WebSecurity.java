package alkemy.warmupchallenge.security;

import alkemy.warmupchallenge.services.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //De esta forma especificamos que la url de /users con el metodo POST va a ser publica pero las demas otra peticiones deben estar autenticadas
        http.cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/auth/sign_up", "/api/v1/auth/login").permitAll()
                .anyRequest()
                .authenticated()
                .and().addFilter(getAuthenticationFilter()) //Este es el filter que vamos a usar para autenticacion.
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Con esto cuando se establezca una comunicacion entre el cliente y el servidor, en nuestro servidor NO se va a crear una variable de sesion, ya que usamos JWT y un header para enviar el token para cada peticion.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }


    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());

        //Especificamos la url para el login
        filter.setFilterProcessesUrl("/api/v1/auth/login");
        return filter;
    }


}
