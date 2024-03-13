package br.com.shop;

import br.com.shop.io.IO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import br.com.shop.domain.Order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;


public class ReadingReportServiceApplication {
    final Path SOURCE = new File("src/main/resources").toPath();

    public static void main(String[] args) throws IOException {
        ReadingReportServiceApplication readingReportServiceApplication = new ReadingReportServiceApplication();

        try (KafkaService<Order> kafkaService = new KafkaService(
                "user.reading.report",
                readingReportServiceApplication::parse,
                ReadingReportServiceApplication.class.getSimpleName(),
                Order.class,
                new HashMap<>())) {

            kafkaService.run();

        }
    }

    private void parse(ConsumerRecord<String, User> record) throws IOException {
        System.out.println("""
                Processing report for ------------> id: %s :: value: %s  :: offset: %s
                """.formatted(
                record.key(),
                record.value(),
                record.offset()
        ));

        User user = record.value();

        File target = new File(user.getReportPath());
        IO.copyTo(SOURCE,target);
        IO.append(target, "Created for "+ user.getUuid());
        System.out.println("File Created");
    }
}
