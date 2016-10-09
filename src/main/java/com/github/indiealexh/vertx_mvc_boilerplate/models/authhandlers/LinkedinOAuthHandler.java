package com.github.indiealexh.vertx_mvc_boilerplate.models.authhandlers;

import com.github.indiealexh.vertx_mvc_boilerplate.models.responses.Response;
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
                new Response(this.routingContext)
                        .setStatus(500)
                        .setMessage("Could not login")
                        .finish();
            } else {
                JsonObject user = response.result();
                new Response(this.routingContext)
                        .setMessage("Hello " + user.getString("firstName"))
                        .finish();
            }
            // TODO: Do something about storing the data or retrieving a user.
        });
    }
}
