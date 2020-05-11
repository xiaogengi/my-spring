package com.xg.my.webmvc;

import com.xg.my.annotation.XgController;
import com.xg.my.annotation.XgRequestMapping;
import com.xg.my.annotation.XgRequestParam;
import com.xg.my.context.XgApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: my-spring
 * @description: 控制器
 * @author: gzk
 * @create: 2020-05-08 17:11
 **/
public class XgDispatcherServlet extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";

    private List<XgHandlerMapping> handlerMappings = new ArrayList();

    private Map<XgHandlerMapping,XgHandlerAdapter>  handlerAdapters = new HashMap();

    private List<XgViewResolver> viewResolvers = new ArrayList();

    private XgApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new XgApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(XgApplicationContext context) {
        //初始化9大策略

        //文件上传解析 如果请求类型是multipart 将通过 MultipartResolver进行解析
        initMultipartResolver(context);

        //本地化解析
        initLocaleResolver(context);

        //主题解析
        initThemeResolver(context);

        //保存controller 中对应的RequestMapping对应的Method信息
        initHandlerMappings(context);

        //进行动态参数分配
        initHandlerAdapters(context);

        //如果执行中碰到异常，将交给HandlerExceptionResolver解析
        initHandlerExceptionResolver(context);

        //直接解析请求到视图名
        initRequestToViewNameTranslator(context);

        //动态模版解析
        initViewResolver(context);

        //flash 映射管理器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(XgApplicationContext context) {
    }

    private void initViewResolver(XgApplicationContext context) {
        //比如在页面敲一个 localhost:8080/demo.html
        //解决页面名字跟模版文件关联的问题
        String templateRoot = context.getConfig().getProperty("templateRoot");

        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);

        for (File template : templateRootDir.listFiles()) {
            this.viewResolvers.add(new XgViewResolver(templateRoot));
        }
    }

    private void initRequestToViewNameTranslator(XgApplicationContext context) {
    }

    private void initHandlerExceptionResolver(XgApplicationContext context) {
    }

    private void initHandlerAdapters(XgApplicationContext context) {
        for (XgHandlerMapping handlerMapping : this.handlerMappings) {
            //每个方法都有一个形参列表，那么这里就是保存形参列表
            this.handlerAdapters.put(handlerMapping, new XgHandlerAdapter());
        }
    }



    private void initHandlerMappings(XgApplicationContext context) {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        try {
            for (String beanDefinitionName : beanDefinitionNames) {
                    Object controller = context.getBean(beanDefinitionName);
                    Class<?> clazz = controller.getClass();

                    if(!clazz.isAnnotationPresent(XgController.class)){
                        continue;
                    }

                    String baseUrl = "";

                    if(clazz.isAnnotationPresent(XgRequestMapping.class)){
                        XgRequestMapping requestMapping = clazz.getAnnotation(XgRequestMapping.class);
                        baseUrl = requestMapping.value();
                    }

                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if(!method.isAnnotationPresent(XgRequestMapping.class)){continue;}

                        XgRequestMapping requestMapping = method.getAnnotation(XgRequestMapping.class);

                        String regex = ("/" + baseUrl + requestMapping.value().replaceAll("\\*", ".*").replaceAll("/+","/"));

                        Pattern pattern = Pattern.compile(regex.replaceAll("//","/"));

                        this.handlerMappings.add(new XgHandlerMapping(controller, method, pattern));

                        System.err.println("Mapping : " + regex +" , " + method);
                    }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private void initThemeResolver(XgApplicationContext context) {
    }

    private void initLocaleResolver(XgApplicationContext context) {
    }

    private void initMultipartResolver(XgApplicationContext context) {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        try {
            doDispatch(req, resp);
        } catch (Exception e){
            e.printStackTrace();
            resp.getWriter().write("<font size='25' color='blue'>500 Exception</font><br/>Details:<br/>" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]","") .replaceAll("\\s","\r\n") + "<font color='green'><i>Copyright@GupaoEDU</i></font>");
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        XgHandlerMapping handler = getHandler(req);
        if(handler == null){
            processDispatchResult(req, resp, new XgModelAndView("404"));
            return;
        }

        XgHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        XgModelAndView mv = handlerAdapter.handler(req, resp, handler);

        processDispatchResult(req, resp, mv);
    }

    private XgHandlerAdapter getHandlerAdapter(XgHandlerMapping handler) {
        if(this.handlerAdapters.isEmpty()){return null;}
        XgHandlerAdapter handlerAdapter = this.handlerAdapters.get(handler);
        if(handlerAdapter.supports(handler)){
            return handlerAdapter;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, XgModelAndView modelAndView) throws Exception {
        if(modelAndView == null){return;}

        if(this.viewResolvers.isEmpty()){return;}

        if(this.viewResolvers != null){
            for (XgViewResolver viewResolver : this.viewResolvers) {
                XgView view = viewResolver.resolveViewName(modelAndView.getViewName(), null);
                if(view != null){
                    view.render(modelAndView.getModel() ,req ,resp);
                    return;
                }
            }
        }
    }

    private XgHandlerMapping getHandler(HttpServletRequest req) {
        if(this.handlerMappings.isEmpty()){return null;}

        String url = req.getRequestURI();

        String contextPath = req.getContextPath();

        url = url.replace(contextPath, "").replaceAll("/+","/");

        for (XgHandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if(!matcher.matches()){continue;}
            return handlerMapping;
        }
        return null;
    }


}
