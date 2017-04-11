package fr.pinguet62.jsfring.ws.config;

import static fr.pinguet62.jsfring.ws.Config.BASE_URL;
import static fr.pinguet62.jsfring.ws.config.Oauth2Helper.HEADER_AUTHORIZATION;
import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaxrsClientConfig {

    @Inject
    private Oauth2Helper helper;

    /** Build an JAX-RS {@link Client} whith <b>base-URL</b> and <b>OAuth2</b> header token. */
    @Bean
    public WebTarget client() {
        return newClient().register(oauth2Filter()).target(BASE_URL);
    }

    private ClientRequestFilter oauth2Filter() {
        return requestContext -> requestContext.getHeaders().add(HEADER_AUTHORIZATION, helper.getAuthorization());
    }

}