package br.com.shop;

;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GenerateAllReportsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        try (KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>()) {

            batchDispatcher.send("send.message.to.all.users", "user.reading.report","user.reading.report");

            System.out.println("Sent generated reports to all users");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Report requests generated");

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}