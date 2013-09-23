package com.oneandone.idev.mockserver;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class RestServer {

    public HttpServer httpServer;

    public void start() {
        String serverAddress = getServerAddress();
        httpServer = new HttpServer();
        try {
            URI uri = new URI(serverAddress);
            final NetworkListener listener = new NetworkListener("grizzly", uri.getHost(), 8080);
            httpServer.addListener(listener);
            httpServer.getServerConfiguration().addHttpHandler(new DynamicHttpResource(), "/");
            httpServer.start();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    private String getServerAddress() {
        Properties ret = new Properties();
        try {
            InputStream stream = new FileInputStream("src/main/config/application.properties");
            ret.load(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not load: application.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String serverAddress = ret.getProperty("server.address", "http://0.0.0.0");
        return serverAddress;
    }

}