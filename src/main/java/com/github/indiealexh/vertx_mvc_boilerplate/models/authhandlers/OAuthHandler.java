package com.github.indiealexh.vertx_mvc_boilerplate.models.authhandlers;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.web.RoutingContext;

public abstract class OAuthHandler {
    protected OAuth2ClientOptions providerClientOptions;
    protected JsonObject providerConfig;
    protected OAuth2Auth oAuth2Auth;
    protected String provider;
    protected Vertx vertx;
    protected RoutingContext routingContext;

    OAuthHandler(Vertx vertx, String provider, RoutingContext routingContext) {
        this.provider = provider;
        this.vertx = vertx;
        this.routingContext = routingContext;
        JsonObject clientOptions = this.vertx.getOrCreateContext().config().getJsonObject("authentication").getJsonObject(provider).getJsonObject("clientOptions");
        this.providerClientOptions = new OAuth2ClientOptions(clientOptions);
        this.providerConfig = this.vertx.getOrCreateContext().config().getJsonObject("authentication").getJsonObject(provider).getJsonObject("config");


    }

    public OAuth2ClientOptions getProviderClientOptions() {
        return this.providerClientOptions;
    }

    public OAuth2Auth getoAuth2Auth() {
        if (this.oAuth2Auth == null) {
            this.oAuth2Auth = OAuth2Auth.create(vertx, OAuth2FlowType.AUTH_CODE, getProviderClientOptions());
        }
        return this.oAuth2Auth;
    }

    public JsonObject getTokenConfig(String code) {
        return new JsonObject().put("code",code).put("redirect_uri", "http://localhost:8080/auth/" + this.provider + "/callback");
    }

    public String getAuthorizationUri() {
        JsonObject uriConfig = new JsonObject().put("redirect_uri", "http://localhost:8080/auth/" + this.provider + "/callback").put("scope", this.providerConfig.getValue("scope"));
        return this.getoAuth2Auth().authorizeURL(uriConfig);
    }

    public void loginHandler(String code) {
        this.getoAuth2Auth().getToken(this.getTokenConfig(code), accessTokenResponse -> {
            if (accessTokenResponse.failed()) {
                System.out.println("Failed to obtain token");
            } else {
                AccessToken accessToken = accessTokenResponse.result();
                this.getAuthenticatedUserDetails(accessToken);
            }
        });
    }

    public abstract void getAuthenticatedUserDetails(AccessToken accessToken);
}
