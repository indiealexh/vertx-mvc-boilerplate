package com.github.indiealexh.vertx_mvc_boilerplate.models.authhandlers;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.web.RoutingContext;

public class LinkedinOAuthHandler extends OAuthHandler {

    private static final String provider = "linkedin";

    public LinkedinOAuthHandler(Vertx vertx, RoutingContext routingContext) {
        super(vertx, provider, routingContext);
    }

    @Override
    public void getAuthenticatedUserDetails(AccessToken accessToken) {
        JsonObject apiConfig = new JsonObject().put("access_token", accessToken.principal().getString("access_token"));
        this.getoAuth2Auth().api(HttpMethod.GET, "https://api.linkedin.com/v1/people/~?format=json", apiConfig, response -> {
            if (response.failed()) {
                System.out.println("OAuth Failed");
                this.routingContext.response().setStatusCode(500).end("Could not login");
            } else {
                JsonObject user = response.result();
                this.routingContext.response().end("Hello " + user.getString("firstName"));
            }
            // TODO: Do something about storing the data or retrieving a user.
        });
    }
}
