package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.service.TemplateHelper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TemplateHelperImpl implements TemplateHelper {
    @Override
    public String compile(String name,Object context) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader("/templates");
        Handlebars handlebars = new Handlebars(loader);
        return handlebars.compile(name).apply(context);
    }
}
