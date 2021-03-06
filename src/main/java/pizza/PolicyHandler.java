package pizza;

import pizza.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_RequestDelivery(@Payload Paid paid){

        if(paid.isMe()){
            Delivery delivery = new Delivery();
            delivery.setOrderId(paid.getOrderId());

            if(paid.getPaymentStatus().equals("Paid")) {
                delivery.setDeliveryStatus("Delivered");
            } else {
                delivery.setDeliveryStatus(paid.getPaymentStatus());
            }

            deliveryRepository.save(delivery);

            System.out.println("##### listener RequestDelivery : " + paid.toJson());
        }
    }

}
