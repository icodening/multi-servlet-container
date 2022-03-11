package cn.icodening.example.servlet.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.stream.Collectors;

/**
 * @author icodening
 * @date 2022.03.11
 */
@Import(ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class)
@EnableConfigurationProperties(ServerProperties.class)
public class MultipleServletConfiguration {

    public static final String SERVLET_PREFIX = "server.servlet";

    @Bean
    public ServletWebServerFactoryCustomizer servletWebServerFactoryCustomizer(ServerProperties serverProperties) {
        return new ServletWebServerFactoryCustomizer(serverProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = SERVLET_PREFIX, name = "type", havingValue = "tomcat", matchIfMissing = true)
    public ServletWebServerFactory tomcatServletWebServerFactory(
            ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
            ObjectProvider<TomcatContextCustomizer> contextCustomizers,
            ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.getTomcatConnectorCustomizers()
                .addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()));
        factory.getTomcatContextCustomizers()
                .addAll(contextCustomizers.orderedStream().collect(Collectors.toList()));
        factory.getTomcatProtocolHandlerCustomizers()
                .addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()));
        return factory;
    }

    @Bean
    @ConditionalOnProperty(prefix = SERVLET_PREFIX, name = "type", havingValue = "undertow")
    public ServletWebServerFactory undertowServletWebServerFactory(
            ObjectProvider<UndertowDeploymentInfoCustomizer> deploymentInfoCustomizers,
            ObjectProvider<UndertowBuilderCustomizer> builderCustomizers) {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.getDeploymentInfoCustomizers()
                .addAll(deploymentInfoCustomizers.orderedStream().collect(Collectors.toList()));
        factory.getBuilderCustomizers().addAll(builderCustomizers.orderedStream().collect(Collectors.toList()));
        return factory;
    }

    @Bean
    @ConditionalOnProperty(prefix = SERVLET_PREFIX, name = "type", havingValue = "jetty")
    public JettyServletWebServerFactory jettyServletWebServerFactory(
            ObjectProvider<JettyServerCustomizer> serverCustomizers) {
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
        factory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));
        return factory;
    }
}
