package com.github.indiealexh.vertx_mvc_boilerplate.controllers;

import com.github.indiealexh.vertx_mvc_boilerplate.models.responses.Response;
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
        System.out.println((String) rc.session().get("name"));
        rc.session().put("name","world");
        new Response(rc)
                .setMessage("Hello world!")
                .finish();
    }

    private void helloName(RoutingContext rc) {
        String name = rc.request().getParam("name");
        new Response(rc)
                .setMessage(String.format("Hello %s!", name))
                .finish();
    }

}
