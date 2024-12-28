package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.message.DispatchCompleted;
import org.example.message.DispatchPreparing;
import org.example.message.TrackingStatusUpdated;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingService {

    private static final String TRACKING_STATUS_TOPIC = "tracking.status";

    private final KafkaTemplate<String, Object> kafkaProducer;

    public void processDispatchPreparing(DispatchPreparing dispatchPreparing) throws ExecutionException, InterruptedException {
        log.info("Processing dispatchPreparing message: {}", dispatchPreparing);

        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .status(TrackingStatus.PREPARING)
                .orderId(dispatchPreparing.getOrderId())
                .build();
        kafkaProducer.send(TRACKING_STATUS_TOPIC, trackingStatusUpdated).get();

    }

    public void processDispatchCompleted(DispatchCompleted dispatchCompleted) throws ExecutionException, InterruptedException {
        log.info("Processing dispatchCompleted message: {}", dispatchCompleted);

        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .status(TrackingStatus.COMPLETED)
                .orderId(dispatchCompleted.getOrderId())
                .build();
        kafkaProducer.send(TRACKING_STATUS_TOPIC, trackingStatusUpdated).get();

    }
}
