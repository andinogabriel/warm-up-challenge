package alkemy.warmupchallenge;

import alkemy.warmupchallenge.dtos.CategoryDto;
import alkemy.warmupchallenge.models.responses.CategoryResponse;
import alkemy.warmupchallenge.security.AppProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class WarmupchallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarmupchallengeApplication.class, args);
	}

	//Creamos una sola instancia de esta clase para poder utilizarlas en todas partes
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}

	//Para tener una instancia ModelMapper global para no estar instanciando cada rato
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		return mapper;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.findAndRegisterModules();
	}

}
