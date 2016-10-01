package com.github.indiealexh.vertx_mvc_boilerplate.controllers;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

abstract class Controller {

    final Router controllerRouter;

    Controller(Vertx vertx) {
        this.controllerRouter = Router.router(vertx);
    }

    public Router getControllerRouter() {
        return this.controllerRouter;
    }
}
