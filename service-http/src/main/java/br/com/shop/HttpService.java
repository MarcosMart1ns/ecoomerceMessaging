package br.com.shop;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpService {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(new ServletHolder(new OrderServlet()),"/new");
        server.setHandler(context);


        server.start();
        server.join();
    }
}