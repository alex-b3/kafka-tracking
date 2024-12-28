package org.example.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.message.DispatchCompleted;
import org.example.message.DispatchPreparing;
import org.example.service.TrackingService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(
        id = "dispatchTrackingConsumerClient",
        topics = "dispatch.tracking",
        groupId = "tracking.dispatch.tracking",
        containerFactory = "kafkaListenerContainerFactory"
)
public class TrackingHandler {

    private final TrackingService trackingService;


    @KafkaHandler
    public void listen(DispatchPreparing dispatchPreparing) {
        log.info("Received dispatchPreparing: {}", dispatchPreparing);
        try {
            trackingService.processDispatchPreparing(dispatchPreparing);
        } catch (Exception e) {
            log.error("Error processing dispatchPreparing: {}", dispatchPreparing, e);
        }
    }

    @KafkaHandler
    public void listen(DispatchCompleted dispatchCompleted) {
        log.info("Received dispatchCompleted: {}", dispatchCompleted);
        try {
            trackingService.processDispatchCompleted(dispatchCompleted);
        } catch (Exception e) {
            log.error("Error processing dispatchPreparing: {}", dispatchCompleted, e);
        }
    }
}
