package sa.tabadul.useraccessmanagement;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import sa.tabadul.useraccessmanagement.common.configs.RestClientWithDisabledSSLValidation;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "UserManagement API Documentation"))
public class UserManagementApplication {


	public static void main(String[] args) {
		
		
		RestClientWithDisabledSSLValidation.disableSSLValidation();
		
		SpringApplication.run(UserManagementApplication.class, args);

	}


	
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	
	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
	
	
	@Bean
	public CommonsRequestLoggingFilter logFilter() {

		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(10000000);
		filter.setIncludeHeaders(true);
		filter.setIncludeClientInfo(true);
		return filter;
	}

}
