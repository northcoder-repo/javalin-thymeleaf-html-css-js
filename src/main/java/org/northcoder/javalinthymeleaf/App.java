package org.northcoder.javalinthymeleaf;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.get;
import io.javalin.http.Handler;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;

public class App {

    //
    // A non-Spring version of this:
    // https://github.com/jmiguelsamper/thymeleaf3-template-modes-example
    //
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
            JavalinRenderer.register(JavalinThymeleaf.INSTANCE);
            JavalinThymeleaf.configure(ThymeleafConfig.templateEngine(
                    ThymeleafConfig.templateResolver(
                            TemplateMode.HTML, "/thymeleaf/", ".html"))
            );
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
        ResourceResponse resourceResponse = renderedFileAsStream(jsFileName,
                thymeleafCtx, TemplateMode.JAVASCRIPT);
        sendResult(resourceResponse, "application/javascript", ctx);
    };

    private static final Handler CSS = (ctx) -> {
        String cssFileName = ctx.pathParam("cssFile");
        Context thymeleafCtx = new Context();
        thymeleafCtx.setVariable("backgroundColor", "goldenrod");
        ResourceResponse resourceResponse = renderedFileAsStream(cssFileName,
                thymeleafCtx, TemplateMode.CSS);
        sendResult(resourceResponse, "text/css", ctx);
    };
    
    private static void sendResult(ResourceResponse resourceResponse, 
            String contentType, io.javalin.http.Context ctx) {
        ctx.contentType(contentType);
        ctx.status(resourceResponse.getHttpStatus());
        ctx.result(resourceResponse.getResponse());
    }

    private static ResourceResponse renderedFileAsStream(String cssFileName,
            Context thymeleafCtx, TemplateMode templateMode) {
        return ThymeleafConfig.renderTemplate(cssFileName, thymeleafCtx,
                templateMode);
    }

}
