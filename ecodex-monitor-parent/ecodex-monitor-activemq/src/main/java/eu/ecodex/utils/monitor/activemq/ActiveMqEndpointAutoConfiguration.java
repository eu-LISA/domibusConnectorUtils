/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.activemq;

import eu.ecodex.utils.monitor.activemq.config.ActiveMqEndpointConfigurationProperties;
import eu.ecodex.utils.monitor.activemq.config.ActiveMqHealthChecksConfigurationProperties;
import eu.ecodex.utils.monitor.activemq.config.ActiveMqMetricConfigurationProperties;
import eu.ecodex.utils.monitor.activemq.service.ActiveMqHealthService;
import eu.ecodex.utils.monitor.activemq.service.ActiveMqMetricService;
import eu.ecodex.utils.monitor.activemq.service.ActiveMqQueuesMonitorEndpoint;
import eu.ecodex.utils.monitor.activemq.service.BrokerFacadeFactory;
import eu.ecodex.utils.monitor.activemq.service.DestinationService;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.Optional;
import org.apache.activemq.web.BrokerFacade;
import org.apache.activemq.web.SingletonBrokerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@EnableConfigurationProperties(ActiveMqEndpointConfigurationProperties.class)
@ConditionalOnProperty(prefix = ActiveMqEndpointConfigurationProperties.ACTIVEMQ_MONITOR_PREFIX, name = "enabled", havingValue = "true")
@ComponentScan(basePackageClasses = ActiveMqEndpointAutoConfiguration.class)
public class ActiveMqEndpointAutoConfiguration {

    @Autowired
    ActiveMqEndpointConfigurationProperties configurationProperties;

    @Bean
    ActiveMqQueuesMonitorEndpoint monitorEndpoint() {
        return new ActiveMqQueuesMonitorEndpoint();
    }

    @Bean
    DestinationService destinationService() {
        return new DestinationService();
    }

    @Bean
    BrokerFacadeFactory brokerFacadeFactory() {
        return new BrokerFacadeFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    BrokerFacade localBrokerFacade() {
        SingletonBrokerFacade brokerFacade = new SingletonBrokerFacade();
        return brokerFacade;
    }

    @Configuration
    @ConditionalOnProperty(prefix = ActiveMqMetricConfigurationProperties.PREFIX, name = "enabled", havingValue = "true")
    @EnableConfigurationProperties(ActiveMqMetricConfigurationProperties.class)
    public static class MetricConfiguration {
        @Bean
        @Lazy(false)
        ActiveMqMetricService activeMqMetricService() {
            return new ActiveMqMetricService();
        }
    }

//    @Bean
//    BrokerFacade brokerFacade() throws Exception {
//        return brokerFacadeFactory().getObject();
//    }

    @Configuration
    @ConditionalOnProperty(prefix = ActiveMqHealthChecksConfigurationProperties.PREFIX, name = "enabled", havingValue = "true")
    @EnableConfigurationProperties(ActiveMqHealthChecksConfigurationProperties.class)
    public static class HealthConfiguration {
        @Bean
        @Lazy(false)
        ActiveMqHealthService activeMqHealthService() {
            return new ActiveMqHealthService();
        }
    }

    public abstract static class ActiveMqEndpointConfigurationPropertiesCondition
            implements Condition {
        Optional<ActiveMqEndpointConfigurationProperties> getProps(ConditionContext context) {
            Bindable<ActiveMqEndpointConfigurationProperties> bindable =
                    Bindable.of(ActiveMqEndpointConfigurationProperties.class);
            Binder binder = Binder.get(context.getEnvironment());
            BindResult<ActiveMqEndpointConfigurationProperties> bindResult =
                    binder.bind(ActiveMqEndpointConfigurationProperties.ACTIVEMQ_MONITOR_PREFIX,
                            bindable, null);
            return Optional.ofNullable(bindResult.orElse(null));
        }
    }

    public static class JmxUrlNotEmptyCondition
            extends ActiveMqEndpointConfigurationPropertiesCondition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Optional<ActiveMqEndpointConfigurationProperties> bean = getProps(context);
            return bean.isPresent() && bean.get().getJmxUrl().size() > 0;
        }
    }

    public static class BrokerNameNotEmptyCondition
            extends ActiveMqEndpointConfigurationPropertiesCondition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Optional<ActiveMqEndpointConfigurationProperties> bean = getProps(context);
            return bean.isPresent() && StringUtils.isNotEmpty(bean.get().getBrokerName());
        }
    }

}
