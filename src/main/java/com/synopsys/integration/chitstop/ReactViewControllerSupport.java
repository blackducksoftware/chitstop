package com.synopsys.integration.chitstop;

import java.util.List;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

public class ReactViewControllerSupport {
    private final ViewControllerRegistry registry;

    public ReactViewControllerSupport(ViewControllerRegistry registry) {
        this.registry = registry;
    }

    public void add(List<String> viewPatterns) {
        for (String pattern : viewPatterns) {
            registry.addViewController(pattern).setViewName("forward:/index.html");
        }
    }

}
