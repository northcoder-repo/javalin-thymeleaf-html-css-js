package org.northcoder.javalinthymeleaf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

public class ThymeleafConfig {

    public static TemplateEngine templateEngine(
            ITemplateResolver templateResolver) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.addTemplateResolver(templateResolver);
        return templateEngine;
    }

    public static ITemplateResolver templateResolver(
            TemplateMode templateMode, String prefix, String suffix) {
        ClassLoaderTemplateResolver templateResolver
                = new ClassLoaderTemplateResolver(Thread
                        .currentThread().getContextClassLoader());
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setPrefix(prefix);
        templateResolver.setSuffix(suffix);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    public static ResourceResponse renderTemplate(String templateName, Context ctx,
            TemplateMode templateMode) {
        String prefix;
        String suffix;
        switch (templateMode) {
            case CSS -> {
                prefix = "/public/css/";
                suffix = ".css";
            }
            case JAVASCRIPT -> {
                prefix = "/public/js/";
                suffix = ".js";
            }
            default -> {
                prefix = "";
                suffix = "";
            }
        }
        ITemplateResolver resolver = templateResolver(templateMode, prefix, suffix);
        TemplateEngine engine = templateEngine(resolver);
        String renderedTemplate = null;
        try {
            renderedTemplate = engine.process(templateName, ctx);
        } catch (Exception ex) {
            if (ex.getCause().getClass().equals(java.io.FileNotFoundException.class)) {
                return notFound();
            } else {
                // exception already thrown
            }
        }
        if (renderedTemplate != null) {
            return ok(renderedTemplate);
        } else {
            return serverError();
        }

    }

    private static ResourceResponse ok(String renderedTemplate) {
        return new ResourceResponse(200, new ByteArrayInputStream(
                renderedTemplate.getBytes(StandardCharsets.UTF_8)));
    }

    private static ResourceResponse notFound() {
        return new ResourceResponse(404, new ByteArrayInputStream(
                ResourceResponse.FOUROHFOUR.getBytes(StandardCharsets.UTF_8)));
    }

    private static ResourceResponse serverError() {
        return new ResourceResponse(500, new ByteArrayInputStream(
                ResourceResponse.FIVEHUNDRED.getBytes(StandardCharsets.UTF_8)));
    }

}
