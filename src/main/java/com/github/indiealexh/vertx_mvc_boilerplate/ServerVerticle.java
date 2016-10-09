package com.github.indiealexh.vertx_mvc_boilerplate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.web.Router;

public class ServerVerticle extends AbstractVerticle {

    private AsyncSQLClient mySQLClient;

    @Override
    public void start() throws Exception {
        JsonObject config = vertx.getOrCreateContext().config();
        HttpServerOptions options = new HttpServerOptions();
        options.setLogActivity(true);
        if (config.getJsonObject("ssl").getBoolean("enabled")) {
            System.out.println(System.getProperty("user.dir"));
            System.out.println("Enabling ssl");
            options.setPemKeyCertOptions(new PemKeyCertOptions()
                    .setCertPath(System.getProperty("user.dir")+config.getJsonObject("ssl").getString("certpath"))
                    .setKeyPath(System.getProperty("user.dir")+config.getJsonObject("ssl").getString("keypath"))
            );
            options.setSsl(true);
        }

        ServerRouter serverRouter = new ServerRouter(vertx, mySQLClient);

        // Get the default database config and create and test the connection
        JsonObject mySQLClientConfig = config.getJsonObject("database").getJsonObject("default");
        this.mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);

        Router router = serverRouter.getRouter();
        vertx.createHttpServer(options).requestHandler(router::accept).listen(8080);

    }

    @Override
    public void stop() throws Exception {
        // We wanna make sure to close the connection when we're done with it.
        this.mySQLClient.close();
    }

}
