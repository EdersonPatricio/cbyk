package com.cbyk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	@Bean
    OpenAPI openAPI() {		
		return new OpenAPI()
			.info( new Info()
				.title( "SPRING REST API" )
				.description( "Documentação da API REST" )
				.version( "1.0" )
				.termsOfService( "Termo de uso: Open Source" )
				.license( 
					new License()
					.name( "Apache 2.0" )
					.url( "http://localhost" ) 
				)
			).externalDocs(
				new ExternalDocumentation()
					.description( "Ederson Patrício" )
					.url( "http://localhost" ) 
				)
				.components( new Components()
					.addSecuritySchemes( "basicScheme", new SecurityScheme()
					.type( SecurityScheme.Type.HTTP ).scheme( "basic" ) ) )
				.addSecurityItem( new SecurityRequirement().addList( "basicScheme" ) );
	}
}
