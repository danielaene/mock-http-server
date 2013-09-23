package com.oneandone.idev.mockserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    static final Logger log = LoggerFactory.getLogger(RestServer.class);

    public static void main(String[] args) throws IOException {
        final RestServer server = new RestServer();
        server.start();
        log.debug("Application started");

        System.in.read();
    }

}
