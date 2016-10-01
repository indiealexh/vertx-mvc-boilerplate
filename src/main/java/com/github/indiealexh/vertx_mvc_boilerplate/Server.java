package com.github.indiealexh.vertx_mvc_boilerplate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

    public static void main(String [] args) {
        try {
            new Server().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        Vertx vertx = Vertx.vertx();
        HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
        ServerRouter serverRouter = new ServerRouter(vertx);
        Router router = serverRouter.getRouter();
        vertx.createHttpServer(options).requestHandler(router::accept).listen(8080);
    }

}
