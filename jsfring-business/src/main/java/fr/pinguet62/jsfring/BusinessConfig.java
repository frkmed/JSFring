package fr.pinguet62.jsfring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application-business.properties")
public class BusinessConfig {}