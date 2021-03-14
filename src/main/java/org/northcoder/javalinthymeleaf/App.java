package org.northcoder.javalinthymeleaf;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.get;
import io.javalin.http.Handler;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.context.Context;

public class App {

    //
    // A non-Spring version of this:
    // https://github.com/jmiguelsamper/thymeleaf3-template-modes-example
    //
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
            JavalinRenderer.register(JavalinThymeleaf.INSTANCE);
            JavalinThymeleaf.configure(ThymeleafConfig.templateEngine());
        }).start(7001);

        app.routes(() -> {
            get("/test", TEST);
            get("/js/:jsFile", JS);
            get("/css/:cssFile", CSS);
        });
    }

    private static final Handler TEST = (ctx) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("hello", "Hello, World.");
        ctx.render("test.html", model);
    };

    private static final Handler JS = (ctx) -> {
        String jsFileName = ctx.pathParam("jsFile");
        Context thymeleafCtx = new Context();
        thymeleafCtx.setVariable("jsTest", "This string is from a JS file");
        ctx.contentType("application/javascript");
        ctx.result(ThymeleafConfig.getJsTemplate(jsFileName, thymeleafCtx));
    };

    private static final Handler CSS = (ctx) -> {
        String cssFileName = ctx.pathParam("cssFile");
        Context thymeleafCtx = new Context();
        thymeleafCtx.setVariable("backgroundColor", "goldenrod");
        ctx.contentType("text/css");
        ctx.result(ThymeleafConfig.getCssTemplate(cssFileName, thymeleafCtx));
    };

}
