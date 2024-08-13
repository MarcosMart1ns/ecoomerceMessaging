package br.com.shop;

import br.com.shop.consumer.ConsumerService;
import br.com.shop.consumer.ServiceRunner;
import br.com.shop.domain.Message;
import br.com.shop.domain.User;
import br.com.shop.io.IO;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


public class ReadingReportServiceApplication implements ConsumerService<User> {
    final Path SOURCE = new File("src/main/resources").toPath();

    public static void main(String[] args) throws IOException {
        new ServiceRunner<>(ReadingReportServiceApplication::new).start(5);
    }

    public void parse(ConsumerRecord<String, Message<User>> record) {
        System.out.println("""
                Processing report for ------------> id: %s :: value: %s  :: offset: %s
                """.formatted(
                record.key(),
                record.value(),
                record.offset()
        ));

        User user = record.value().getPayload();

        File target = new File(user.getReportPath());
        try {
            IO.copyTo(SOURCE,target);
            IO.append(target, "Created for "+ user.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("File Created");
    }

    @Override
    public String getTopic() {
        return "ecommerce.user.reading.report";
    }

    @Override
    public String getConsumerGroup() {
        return ReadingReportServiceApplication.class.getSimpleName();
    }
}
