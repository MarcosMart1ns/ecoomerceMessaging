package br.com.shop;

import br.com.shop.domain.CorrelationId;
import br.com.shop.domain.Email;
import br.com.shop.domain.Order;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
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
            try (KafkaDispatcher<Email> emailKafkaDispatcher = new KafkaDispatcher<>()) {

                String email = req.getParameter("email");
                String msg = "Obrigado pela compra!";

                Order order = new Order(
                        UUID.randomUUID().toString(),
                        new BigDecimal(req.getParameter("amount")),
                        email
                );

                orderKafkaDispatcher.send("ecommerce.new.order", order.getEmail(), new CorrelationId(NewOrderServlet.class.getSimpleName()), order);
                emailKafkaDispatcher.send("ecommerce.send.email", order.getEmail(), new CorrelationId(NewOrderServlet.class.getSimpleName()), new Email(
                        email, msg
                ));
                resp.setStatus(200);
                resp.getWriter().println("Order %s created sucessfully for %s".formatted(order.getOrderId(), order.getEmail()));

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
