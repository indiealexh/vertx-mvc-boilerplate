package com.github.indiealexh.vertx_mvc_boilerplate.models.responses;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Response {

    private final HttpServerResponse httpServerResponse;
    private JsonObject responseObject;
    private String message = null;
    private JsonArray errors = null;
    private JsonObject data = null;
    private int httpStatusCode = 200;

    public Response(RoutingContext routingContext) {
        this.httpServerResponse = routingContext.response();
        this.httpServerResponse
                .putHeader("Cache-Control", "no-store, no-cache")
                .putHeader("X-Content-Type-Options", "nosniff")
                .putHeader("Strict-Transport-Security", "max-age=" + 15768000)
                .putHeader("X-Download-Options", "noopen")
                .putHeader("X-XSS-Protection", "1; mode=block")
                .putHeader("X-FRAME-OPTIONS", "DENY");
        this.responseObject = new JsonObject();
    }

    public Response setStatus(int httpStatus) {
        this.httpStatusCode = httpStatus;
        return this;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response addData(String dataType, JsonArray dataArray) {
        this.data.put(dataType, dataArray);
        return this;
    }

    public Response addError(JsonObject errorObject) {
        this.errors.add(errorObject);
        return this;
    }

    public Response finish() {
        this.httpServerResponse.putHeader("Content-Type", "application/json");
        this.httpServerResponse.setStatusCode(this.httpStatusCode);
        this.responseObject.put("status", ((this.httpStatusCode >= 200 && this.httpStatusCode < 400)?"success":"failure"));
        if (this.message != null) this.responseObject.put("message", this.message);
        if (this.data != null) this.responseObject.put("data", this.data);
        if (this.errors != null) this.responseObject.put("errors", this.errors);
        String responseString = responseObject.toString();
        this.httpServerResponse.end(responseString);
        return null;
    }

}
