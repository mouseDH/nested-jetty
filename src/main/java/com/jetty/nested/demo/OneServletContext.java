package com.jetty.nested.demo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ListenerHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

public class OneServletContext
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(System.getProperty("java.io.tmpdir"));
        server.setHandler(context);

        // Add dump servlet
        context.addServlet(
                context.addServlet(DumpServlet.class, "/dump/*"),
                "*.dump");
        context.addServlet(MinimalServlets.HelloServlet.class, "/hello/*");
        context.addServlet(DefaultServlet.class, "/");

        context.addFilter(TestFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(TestFilter.class,"/test", EnumSet.of(DispatcherType.REQUEST,DispatcherType.ASYNC));
        context.addFilter(TestFilter.class,"*.test", EnumSet.of(DispatcherType.REQUEST,DispatcherType.INCLUDE,DispatcherType.FORWARD));

//        context.getServletHandler().addListener(new ListenerHolder(InitListener.class));
//        context.getServletHandler().addListener(new ListenerHolder(RequestListener.class));

        server.start();
        server.dumpStdErr();
        server.join();
    }

    @SuppressWarnings("serial")
    public static class DumpServlet extends HttpServlet
    {
        @Override
        protected void doGet( HttpServletRequest request,
                              HttpServletResponse response ) throws ServletException,
                IOException
        {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from DumpServlet</h1>");
        }
    }

    public static class TestFilter implements Filter
    {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException
        {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
            chain.doFilter(request, response);
        }

        @Override
        public void destroy()
        {

        }
    }

    public static class InitListener implements ServletContextListener
    {
        @Override
        public void contextInitialized(ServletContextEvent sce)
        {
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce)
        {
        }
    }


    public static class RequestListener implements ServletRequestListener
    {
        @Override
        public void requestDestroyed(ServletRequestEvent sre)
        {

        }

        @Override
        public void requestInitialized(ServletRequestEvent sre)
        {

        }
    }
}
