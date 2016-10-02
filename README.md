# vertx-mvc-boilerplate

A project boilerplate for creating MVC style web applications in java using [Vertx Core and Web](http://vertx.io/)

# Installation and Running

At the moment I use the Vertx Launcher as part of debugging, which can be obtained as part of the [Vert.x Dist](http://vertx.io/download/)

Copy and ammend the `src/main/conf/config.json.example` to `src/main/conf/config.json`

In IntelliJ:

- Create a new application launcher.
- Set its main class to be `io.vertx.core.Launcher`
- The program arguments to be `run com.github.indiealexh.vertx_mvc_boilerplate.ServerVerticle --redeploy=**/*.class --launcher-class=io.vertx.core.Launcher -conf src/main/conf/config.json`
- Then save and run the launcher

Now when you "Make" your project it will redeploy it.