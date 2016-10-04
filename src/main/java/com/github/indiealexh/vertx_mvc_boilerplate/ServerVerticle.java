package com.github.indiealexh.vertx_mvc_boilerplate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.web.Router;

public class ServerVerticle extends AbstractVerticle {

    private AsyncSQLClient mySQLClient;

    @Override
    public void start() throws Exception {
        JsonObject config = vertx.getOrCreateContext().config();
        HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
        ServerRouter serverRouter = new ServerRouter(vertx, mySQLClient);

        // Get the default database config and create and test the connection
        JsonObject mySQLClientConfig = config.getJsonObject("database").getJsonObject("default");
        this.mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);

        // TODO - Remove this code
        this.mySQLClient.getConnection(res -> {
            if (res.succeeded()) {
                System.out.println("Success");
            } else {
                System.out.println("Failure");
            }
        });

        Router router = serverRouter.getRouter();
        vertx.createHttpServer(options).requestHandler(router::accept).listen(8080);

    }

    @Override
    public void stop() throws Exception {
        // We wanna make sure to close the connection when we're done with it.
        this.mySQLClient.close();
    }

}
