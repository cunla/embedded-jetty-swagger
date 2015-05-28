package org.delirium.swagger.app;

import org.delirium.swagger.webservices.EntityBrowser;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Main Class for starting the Entity Browser
 */
public class App {
    private static final Logger LOG = Logger.getLogger(App.class);
    private static final String WS_PACKAGE = EntityBrowser.class.getPackage().getName();

    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        try {
            // Workaround for resources from JAR files
            Resource.setDefaultUseCaches(false);

            // Build the Swagger Bean.
            buildSwagger();

            // Holds handlers
            final HandlerList handlers = new HandlerList();

            // Handler for Swagger UI, static handler.
            handlers.addHandler(buildSwaggerUI());

            // Handler for Entity Browser and Swagger
            handlers.addHandler(buildContext());

            // Start server
            Server server = new Server(SERVER_PORT);
            server.setHandler(handlers);
            server.start();
            server.join();
        } catch (Exception e) {
            LOG.error("There was an error starting up the Entity Browser", e);
        }
    }

    private static void buildSwagger() {
        // This configures Swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setResourcePackage(WS_PACKAGE);
        beanConfig.setScan(true);
        beanConfig.setBasePath("/");
        beanConfig.setDescription(
                "Demonstrate Swagger with Jersey2 in an embedded Jetty instance, with no web.xml or Spring MVC.");
        beanConfig.setTitle("REST Services");
    }

    private static ContextHandler buildContext() {
        // com.wordnik.swagger.jaxrs.listing loads up Swagger
        ResourceConfig resourceConfig = new ResourceConfig().packages(WS_PACKAGE, "com.wordnik.swagger.jaxrs.listing");
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder entityBrowser = new ServletHolder(servletContainer);
        ServletContextHandler entityBrowserContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        entityBrowserContext.setContextPath("/");
        entityBrowserContext.addServlet(entityBrowser, "/*");

        return entityBrowserContext;
    }

    // This starts the Swagger UI at http://localhost:9999/docs
    private static ContextHandler buildSwaggerUI() throws Exception {
        final ResourceHandler swaggerUIResourceHandler = new ResourceHandler();
        swaggerUIResourceHandler.setResourceBase(
                App.class.getClassLoader().getResource("webapp").toURI().toString());
        final ContextHandler swaggerUIContext = new ContextHandler();
        swaggerUIContext.setContextPath("/docs/");
        swaggerUIContext.setHandler(swaggerUIResourceHandler);

        return swaggerUIContext;
    }
}
