package org.northcoder.javalinthymeleaf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

public class ThymeleafConfig {

    public static TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    private static TemplateEngine jsTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(jsTemplateResolver());
        return templateEngine;
    }

    private static TemplateEngine cssTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(cssTemplateResolver());
        return templateEngine;
    }

    private static ITemplateResolver htmlTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver
                = new ClassLoaderTemplateResolver(Thread
                        .currentThread().getContextClassLoader());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/thymeleaf/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    private static ITemplateResolver jsTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver
                = new ClassLoaderTemplateResolver(Thread
                        .currentThread().getContextClassLoader());
        templateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        templateResolver.setPrefix("/public/js/");
        templateResolver.setSuffix(".js");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
    
    private static ITemplateResolver cssTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver
                = new ClassLoaderTemplateResolver(Thread
                        .currentThread().getContextClassLoader());
        templateResolver.setTemplateMode(TemplateMode.CSS);
        templateResolver.setPrefix("/public/css/");
        templateResolver.setSuffix(".css");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
    
    public static InputStream getJsTemplate(String templateName, Context ctx) {
        return new ByteArrayInputStream(jsTemplateEngine()
                .process(templateName, ctx)
                .getBytes(StandardCharsets.UTF_8));
    }

    public static InputStream getCssTemplate(String templateName, Context ctx) {
        return new ByteArrayInputStream(cssTemplateEngine()
                .process(templateName, ctx)
                .getBytes(StandardCharsets.UTF_8));
    }

}
