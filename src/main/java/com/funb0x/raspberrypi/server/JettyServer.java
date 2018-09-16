package com.funb0x.raspberrypi.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class JettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);
    public static void main(String[] args) throws Exception {

        Server server = new Server(8123);

        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(com.funb0x.raspberrypi.web.WebSocketHandler.class);
            }
        };
        ContextHandler socketContext = new ContextHandler();
        socketContext.setContextPath("/webcam/socket");
        socketContext.setHandler(wsHandler);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        String  baseStr  = "/webapp";  //... contains: helloWorld.html, login.html, etc. and folder: other/xxx.html
        URL baseUrl  = JettyServer.class.getResource(baseStr);
        String  basePath = baseUrl.toExternalForm();
        resourceHandler.setResourceBase(basePath);

        ContextHandler staticContextHandler = new ContextHandler();
        staticContextHandler.setContextPath("/webcam");
        staticContextHandler.setHandler(resourceHandler);

        //ServletContextHandler servletContextHandler = new ServletContextHandler(server,"/webcam", false, false);
        //servletContextHandler.addServlet(DelegatingHandlingServlet.class, "/img");
        //servletContextHandler.addServlet(VideoServlet.class, "/img");
        //servletContextHandler.addServlet(ImageServlet.class, "/singleimage");

        HandlerList handlers = new HandlerList();
        handlers.addHandler(staticContextHandler);
        //handlers.addHandler(servletContextHandler);
        handlers.addHandler(socketContext);

        server.setHandler(handlers);
        server.start();
        server.join();
    }

}
