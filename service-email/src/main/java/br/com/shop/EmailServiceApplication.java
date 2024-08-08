package br.com.shop;

import br.com.shop.domain.Email;
import br.com.shop.domain.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class EmailServiceApplication implements ConsumerService<String> {

    public static void main(String[] args) throws IOException {
      new ServiceProvider().run(EmailServiceApplication::new);
    }

    public String getTopic(){
        return "ecommerce.send.email";
    }

    @Override
    public String getConsumerGroup() {
        return EmailServiceApplication.class.getSimpleName();
    }

    public void parse(ConsumerRecord<String, Message<Email>> consumerRecord){
        System.out.println("Processando novo email------------>");
        System.out.println(consumerRecord.key());
        System.out.println(consumerRecord.value().getPayload());
        System.out.println(consumerRecord.offset());
    }
}
