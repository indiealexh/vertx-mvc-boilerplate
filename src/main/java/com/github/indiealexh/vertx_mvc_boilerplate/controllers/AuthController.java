package com.github.indiealexh.vertx_mvc_boilerplate.controllers;

import com.github.indiealexh.vertx_mvc_boilerplate.models.authhandlers.OAuthHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ahaslam on 04/10/16.
 */
public class AuthController extends Controller {
    public AuthController(Vertx vertx, AsyncSQLClient databaseClient) {
        super(vertx, databaseClient);
        this.controllerRouter.get("/:provider/login").handler(this::loginRedirect);
        this.controllerRouter.get("/:provider/callback").handler(this::loginCallback);
    }

    private void loginRedirect(RoutingContext rc) {
        String provider = rc.request().getParam("provider");
        String providerClass = "com.github.indiealexh.vertx_mvc_boilerplate.models.authhandlers." + provider.substring(0, 1).toUpperCase() + provider.substring(1) + "OAuthHandler";
        try {
            Class<OAuthHandler> _tempClass = (Class<OAuthHandler>) Class.forName(providerClass);
            Constructor<OAuthHandler> oAuthHandlerConstructor = _tempClass.getDeclaredConstructor(Vertx.class, RoutingContext.class);
            OAuthHandler oAuthHandler = oAuthHandlerConstructor.newInstance(vertx, rc);
            rc.response().putHeader("Location", oAuthHandler.getAuthorizationUri()).setStatusCode(302).end();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void loginCallback(RoutingContext rc) {
        String provider = rc.request().getParam("provider");
        String code = rc.request().getParam("code");
        String providerClass = "com.github.indiealexh.vertx_mvc_boilerplate.models.authhandlers." + provider.substring(0, 1).toUpperCase() + provider.substring(1) + "OAuthHandler";
        try {
            Class<OAuthHandler> _tempClass = (Class<OAuthHandler>) Class.forName(providerClass);
            Constructor<OAuthHandler> oAuthHandlerConstructor = _tempClass.getDeclaredConstructor(Vertx.class, RoutingContext.class);
            OAuthHandler oAuthHandler = oAuthHandlerConstructor.newInstance(vertx, rc);
            oAuthHandler.loginHandler(code);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
