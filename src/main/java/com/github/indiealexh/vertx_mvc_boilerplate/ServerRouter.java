package com.github.indiealexh.vertx_mvc_boilerplate;

import com.github.indiealexh.vertx_mvc_boilerplate.controllers.HelloController;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

class ServerRouter {

    private Router router;
    private Vertx vertx;

    ServerRouter(Vertx vertx) {
        this.vertx = vertx;
        this.router = Router.router(this.vertx);
        this.buildRoutes();
    }

    Router getRouter() {
        return this.router;
    }

    private void buildRoutes() {
        this.router.mountSubRouter("/hello", new HelloController(this.vertx).getControllerRouter());
    }
}
