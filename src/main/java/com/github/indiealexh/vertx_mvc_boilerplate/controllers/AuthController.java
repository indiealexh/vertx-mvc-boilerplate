package com.github.indiealexh.vertx_mvc_boilerplate.controllers;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.web.RoutingContext;

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
        // TODO: Make Generic instead of google specific
        JsonObject config = this.vertx.getOrCreateContext().config();
        String provider = rc.request().getParam("provider");
        OAuth2ClientOptions auth = new OAuth2ClientOptions(config.getJsonObject("authentication").getJsonObject(provider));
        OAuth2Auth oauth2 = OAuth2Auth.create(this.vertx, OAuth2FlowType.AUTH_CODE, auth);
        String authorization_uri = oauth2.authorizeURL(new JsonObject()
                .put("redirect_uri", "http://localhost:8080/auth/" + provider + "/callback")
                .put("scope","profile email https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/plus.login"));
        System.out.println(authorization_uri);
        rc.response().putHeader("Location", authorization_uri).setStatusCode(302).end();
    }

    private void loginCallback(RoutingContext rc) {
        // TODO: Make Generic instead of google specific
        String provider = rc.request().getParam("provider");
        String code = rc.request().getParam("code");
        JsonObject config = this.vertx.getOrCreateContext().config();
        OAuth2ClientOptions auth = new OAuth2ClientOptions(config.getJsonObject("authentication").getJsonObject(provider));
        OAuth2Auth oauth2 = OAuth2Auth.create(vertx, OAuth2FlowType.AUTH_CODE, auth);
        JsonObject tokenConfig = new JsonObject().put("code",code).put("redirect_uri", "http://localhost:8080/auth/" + provider + "/callback");
        oauth2.getToken(tokenConfig, res -> {
            if (res.failed()) {
                System.err.println("Access Token Error: " + res.cause().getMessage());
                rc.response().end("I DON'T KNOW YOU!");
            } else {
                AccessToken token = res.result();

                oauth2.api(HttpMethod.GET, "https://www.googleapis.com/oauth2/v3/userinfo", new JsonObject().put("access_token", token.principal().getString("access_token")), res2 -> {
                    JsonObject user = res2.result();
                    rc.response().end("Hello "+user.getString("name"));
                });
            }
        });
    }
}
