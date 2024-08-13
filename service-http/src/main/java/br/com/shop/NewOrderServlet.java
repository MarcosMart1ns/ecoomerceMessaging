package br.com.shop;

import br.com.shop.domain.CorrelationId;
import br.com.shop.domain.Order;
import br.com.shop.producer.KafkaDispatcher;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet implements Servlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>()) {

            String email = req.getParameter("email");

            Order order = new Order(
                    UUID.randomUUID().toString(),
                    new BigDecimal(req.getParameter("amount")),
                    email
            );

            orderKafkaDispatcher.send("ecommerce.new.order", order.getEmail(), new CorrelationId(NewOrderServlet.class.getSimpleName()), order);

            resp.setStatus(200);
            resp.getWriter().println("Order %s created sucessfully for %s".formatted(order.getOrderId(), order.getEmail()));

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
