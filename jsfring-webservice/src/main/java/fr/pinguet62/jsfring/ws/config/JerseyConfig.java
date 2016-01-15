package fr.pinguet62.jsfring.ws.config;

import static java.util.stream.Collectors.joining;
import static org.glassfish.jersey.servlet.ServletProperties.JAXRS_APPLICATION_CLASS;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

@Configuration
@ApplicationPath("/")
public class JerseyConfig {

    /** Register for Jersey. */
    public static class PackageResourceConfig extends ResourceConfig {

        private static final Logger LOGGER = LoggerFactory.getLogger(PackageResourceConfig.class);

        @Autowired
        private BeanFactory beanFactory;

        /**
         * Define the package to scan, who contains JAX-RS annotated classes.
         * <p>
         * Use {@link PostConstruct} because {@link #beanFactory} is injected
         * after {@link PackageResourceConfig#PackageResourceConfig() default
         * constructor}.
         */
        @PostConstruct
        private void init() {
            List<String> packages = AutoConfigurationPackages.get(beanFactory);
            LOGGER.debug("Registrer packages: {}", packages.stream().collect(joining(", ", "[", "]")));
            registerScannedPackages(packages); // packages(packages.stream().toArray(String[]::new));
        }

        /**
         * Fix for Jersey package scanning.
         * <p>
         * Use Spring to scan packages, finding {@link Path} and setting
         * directly these classes to Jersey.
         *
         * @see <a href="https://java.net/jira/browse/JERSEY-2175">Jersey issue
         *      JERSEY-2175</a>
         */
        private void registerScannedPackages(Collection<String> packages) {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            // TODO Scan all @nnotations of JAX-RS
            scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class)); // JAX-RS
            scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
            for (String basePackage : packages)
                for (BeanDefinition bd : scanner.findCandidateComponents(basePackage))
                    register(ClassUtils.resolveClassName(bd.getBeanClassName(), getClass().getClassLoader()));
        }

    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/*");
        registration.addInitParameter(JAXRS_APPLICATION_CLASS, PackageResourceConfig.class.getName());
        return registration;
    }

}