package com.github.indiealexh.vertx_mvc_boilerplate.controllers;

import io.vertx.core.Vertx;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.web.Router;

abstract class Controller {

    final Router controllerRouter;
    final AsyncSQLClient databaseClient;

    Controller(Vertx vertx, AsyncSQLClient databaseClient) {
        this.databaseClient = databaseClient;
        this.controllerRouter = Router.router(vertx);
    }

    public Router getControllerRouter() {
        return this.controllerRouter;
    }
}
