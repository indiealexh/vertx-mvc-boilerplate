package com.github.indiealexh.vertx_mvc_boilerplate.controllers;

import io.vertx.core.Vertx;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.web.RoutingContext;

public class HelloController extends Controller {

    public HelloController(Vertx vertx, AsyncSQLClient databaseClient) {
        super(vertx, databaseClient);
        this.controllerRouter.route("/").handler(this::helloWorld);
        this.controllerRouter.route("/:name").handler(this::helloName);
    }

    private void helloWorld(RoutingContext rc) {
        rc.response().end("Hello world!");
    }

    private void helloName(RoutingContext rc) {
        String name = rc.request().getParam("name");
        rc.response().end(String.format("Hello %s", name));
    }

}
