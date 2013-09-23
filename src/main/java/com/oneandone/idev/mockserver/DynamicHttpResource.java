package com.oneandone.idev.mockserver;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.threadpool.GrizzlyExecutorService;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class DynamicHttpResource  extends HttpHandler {
    private ExecutorService es = GrizzlyExecutorService.createInstance(ThreadPoolConfig
            .defaultConfig().copy().setCorePoolSize(10).setMaxPoolSize(100));

    @Override
    public void service(Request request, Response response) throws Exception {
        System.out.println("HTTPResource called with path = " + request.getRequestURI() );
        response.suspend();
        es.submit(new ServiceHandler(request, response));
    }

    private class ServiceHandler implements Runnable {
        private Request request;
        private Response response;

        public ServiceHandler(Request request, Response response) {
            this.request = request;
            this.response = response;
        }

        @Override
        public void run() {
            String content = "Next random numer = " + Math.random()*100;

            response.setContentLength(content.length());
            try {
                response.getWriter(true).write(content);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    response.sendError(400);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                response.resume();
            }
        }
    }
}
