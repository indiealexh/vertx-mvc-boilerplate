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
//        this.router.route().handler(routingContext -> {
//            routingContext.response()
//                    // do not allow proxies to cache the data
//                    .putHeader("Cache-Control", "no-store, no-cache")
//                    // prevents Internet Explorer from MIME - sniffing a
//                    // response away from the declared content-type
//                    .putHeader("X-Content-Type-Options", "nosniff")
//                    // Strict HTTPS (for about ~6Months)
//                    .putHeader("Strict-Transport-Security", "max-age=" + 15768000)
//                    // IE8+ do not allow opening of attachments in the context of this resource
//                    .putHeader("X-Download-Options", "noopen")
//                    // enable XSS for IE
//                    .putHeader("X-XSS-Protection", "1; mode=block")
//                    // deny frames
//                    .putHeader("X-FRAME-OPTIONS", "DENY");
//        });
        this.router.route().handler(cookieHandler);
        this.router.route().handler(sessionHandler);
    }

    private void buildRoutes() {
        this.router.mountSubRouter("/hello", new HelloController(this.vertx,this.databaseClient).getControllerRouter());
        this.router.mountSubRouter("/auth", new AuthController(this.vertx,this.databaseClient).getControllerRouter());
    }
}
