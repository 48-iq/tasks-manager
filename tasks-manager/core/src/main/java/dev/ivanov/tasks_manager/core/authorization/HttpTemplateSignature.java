package dev.ivanov.tasks_manager.core.authorization;

import org.springframework.util.AntPathMatcher;

import java.util.List;

public class HttpTemplateSignature {
    private final List<String> methods;
    private final String template;

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public HttpTemplateSignature(String template, String... methods) {
        this.template = template;
        this.methods = List.of(methods);
    }

    public boolean matches(String path, String method) {
        return antPathMatcher.match(template, path) &&  (methods.isEmpty() ||
                methods.stream().anyMatch(m -> m.equals(method)));
    }
}
