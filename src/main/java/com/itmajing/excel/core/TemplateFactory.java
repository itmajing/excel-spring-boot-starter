package com.itmajing.excel.core;


import com.itmajing.excel.exception.NoSuchTemplateException;
import com.itmajing.excel.exception.TemplateParseException;
import com.itmajing.excel.model.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class TemplateFactory {

    private static final Logger logger = LoggerFactory.getLogger(TemplateFactory.class);

    private static Map<String, Template> templates = new HashMap<>();

    private static final String HOME = "excel";

    private void init() {
        List<File> files = this.loadTemplate();
        if (files != null) {
            Map<String, Template> template = this.parseTemplate(files);
            templates.putAll(template);
        }
        logger.info("{} templates has been parse successful", templates.size());
    }

    public TemplateFactory(){
        this.init();
    }

    public static Template getTemplate(String name) {
        Template template = templates.get(name);
        if (template != null) {
            return template;
        }
        throw new NoSuchTemplateException(String.format("模板%s不存在", name));
    }

    private List<File> loadTemplate() {
        try {
            File templateDir = ResourceUtils.getFile(String.format("classpath:%s", HOME));
            File[] listFiles = templateDir.listFiles();
            if (listFiles == null) {
                logger.warn("the excel template directory is empty");
            } else {
                ArrayList<File> files = new ArrayList<>();
                for (File file : listFiles) {
                    String name = file.getName();
                    if (name.endsWith(".yml") || name.endsWith(".yaml")) {
                        files.add(file);
                    }
                }
                if (files.size() == 0) {
                    logger.warn("the excel template directory has none valid file");
                }
                return files;
            }
        } catch (FileNotFoundException e) {
            logger.warn("the excel template directory does not exist");
        }
        return null;
    }

    private Map<String, Template> parseTemplate(List<File> files) {
        Yaml yaml = new Yaml();
        Map<String, Template> templates = new HashMap<>(10);
        for (File file : files) {
            try(InputStream inputStream = new FileInputStream(file)) {
                String fileName = file.getName();
                logger.debug("parse excel template file: {}", fileName);
                Template template = yaml.loadAs(inputStream, Template.class);
                String key = fileName.substring(0,fileName.lastIndexOf("."));
                templates.put(key, template);
                logger.debug("parse excel template file success", fileName);
            } catch (Exception e) {
                throw new TemplateParseException("the excel template file parse error", e);
            }
        }
        return templates;
    }
}
