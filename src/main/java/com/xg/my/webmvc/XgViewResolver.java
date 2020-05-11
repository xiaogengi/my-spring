package com.xg.my.webmvc;

import java.io.File;
import java.util.Locale;
import java.util.Timer;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-09 12:33
 **/
public class XgViewResolver {

    private static final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;
    private String viewName;

    public XgViewResolver(String templateRoot){
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }


    public XgView resolveViewName(String viewName, Locale locale) throws Exception {
        this.viewName =viewName;
        if(null == viewName || "".endsWith(viewName.trim())){return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new XgView(templateFile);
    }

    public File getTemplateRootDir() {
        return templateRootDir;
    }

    public void setTemplateRootDir(File templateRootDir) {
        this.templateRootDir = templateRootDir;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
