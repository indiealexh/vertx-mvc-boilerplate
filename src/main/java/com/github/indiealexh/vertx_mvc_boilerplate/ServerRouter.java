package com.github.indiealexh.vertx_mvc_boilerplate;

import com.github.indiealexh.vertx_mvc_boilerplate.controllers.AuthController;
import com.github.indiealexh.vertx_mvc_boilerplate.controllers.HelloController;
import io.vertx.core.Vertx;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

class ServerRouter {

    private Router router;
    private Vertx vertx;
    private AsyncSQLClient databaseClient;

    private static final int KB = 1024;
    private static final int MB = 1024 * KB;

    ServerRouter(Vertx vertx, AsyncSQLClient databaseClient) {
        this.vertx = vertx;
        this.databaseClient = databaseClient;
        this.router = Router.router(this.vertx);
        this.addMiddleware();
        this.buildRoutes();
    }

    Router getRouter() {
        return this.router;
    }

    private void addMiddleware() {
        CookieHandler cookieHandler = CookieHandler.create();
        SessionStore sessionStore = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler
                .create(sessionStore)
                .setCookieHttpOnlyFlag(true)
                .setCookieSecureFlag(true);
        this.router.route().handler(BodyHandler.create().setBodyLimit(50 * MB));
        this.router.route().handler(cookieHandler);
        this.router.route().handler(sessionHandler);
    }

    private void buildRoutes() {
        this.router.mountSubRouter("/hello", new HelloController(this.vertx,this.databaseClient).getControllerRouter());
        this.router.mountSubRouter("/auth", new AuthController(this.vertx,this.databaseClient).getControllerRouter());
    }
}
