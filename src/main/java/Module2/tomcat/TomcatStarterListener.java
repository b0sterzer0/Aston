package Module2.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class TomcatStarterListener implements ApplicationListener<ContextRefreshedEvent> {
    private final AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) return;
        if (!started.compareAndSet(false, true)) return;

        try {
            WebApplicationContext rootContext =
                    (WebApplicationContext) event.getApplicationContext();

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);
            tomcat.getConnector();

            Context tomcatContext =
                    tomcat.addContext("", new File(".").getAbsolutePath());

            AnnotationConfigWebApplicationContext mvcContext =
                    new AnnotationConfigWebApplicationContext();

            mvcContext.register(Module2.config.WebConfig.class);
            mvcContext.setParent(rootContext);

            DispatcherServlet dispatcherServlet =
                    new DispatcherServlet(mvcContext);

            Tomcat.addServlet(tomcatContext, "dispatcher", dispatcherServlet);
            tomcatContext.addServletMappingDecoded("/", "dispatcher");

            tomcat.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
