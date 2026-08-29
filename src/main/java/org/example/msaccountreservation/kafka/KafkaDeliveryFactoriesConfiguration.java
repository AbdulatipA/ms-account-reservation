package org.example.msaccountreservation.kafka;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.example.msaccountreservation.events.ClientChangedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaDeliveryFactoriesConfiguration {
    private final KafkaProperties kafkaProperties;
    private final EntityManagerFactory entityManagerFactory;

    @Value("${KAFKA_TRANSACTIONAL_ID_CONFIG}")
    private  String transactionalId;

    // At-least-once
    @Bean
    public ProducerFactory<String, ClientChangedEvent> atLeastOnceProducerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(props);
    }
    @Bean
    public KafkaTemplate<String, ClientChangedEvent> atLeastOnceKafkaTemplate() {
        return new KafkaTemplate<>(atLeastOnceProducerFactory());
    }


    // exactly-once (настройки для продюсера)
    @Bean
    public ProducerFactory<String, ClientChangedEvent> exactlyOnceProducerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 35000);
        return new DefaultKafkaProducerFactory<>(props);
    }
    @Bean
    public KafkaTemplate<String, ClientChangedEvent> exactlyOnceKafkaTemplate() {
        return new KafkaTemplate<>(exactlyOnceProducerFactory());
    }

    // exactly-once (настройки для консюмера)
    @Bean
    public ConsumerFactory<String, ClientChangedEvent> exactlyOnceConsumerFactory() {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 35000);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    // транзакция для кафки
    @Bean(name = "kafkaTransactionManager")
    public KafkaTransactionManager<String, ClientChangedEvent> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(exactlyOnceProducerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientChangedEvent> exactlyOnceKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientChangedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(exactlyOnceConsumerFactory());
        factory.getContainerProperties().setKafkaAwareTransactionManager(kafkaTransactionManager());
        return factory;
    }

    // транзакция для JPA
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
