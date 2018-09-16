package com.funb0x.webcam.server;

import com.funb0x.webcam.web.DelegatingHandlingServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;


public class JettyServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8123);


        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"src/main/webapp/index.html"});
        resourceHandler.setResourceBase("src/main/resources/");

        ContextHandler staticContextHandler = new ContextHandler();
        staticContextHandler.setContextPath("/webcam");
        staticContextHandler.setHandler(resourceHandler);

        ServletContextHandler servletContextHandler = new ServletContextHandler(server,"/webcam", false, false);
        servletContextHandler.addServlet(DelegatingHandlingServlet.class, "/img");
        //servletContextHandler.addServlet(VideoServlet.class, "/img");
        //servletContextHandler.addServlet(ImageServlet.class, "/singleimage");

        HandlerList handlers = new HandlerList();
        handlers.addHandler(staticContextHandler);
        handlers.addHandler(servletContextHandler);

        server.setHandler(handlers);
        server.start();
        server.join();
    }

}
