/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.spring.quartz;

import static eu.ecodex.utils.spring.quartz.configuration.ScheduledWithQuartzConfiguration.TRIGGER_AND_JOB_DEFINITION_LIST_BEAN_NAME;
import static eu.ecodex.utils.spring.quartz.testbeans.ScheduledBean.SCHEDULED_CRON_JOB_STRING;
import static eu.ecodex.utils.spring.quartz.testbeans.ScheduledBean.SCHEDULED_FIXED_RATE_JOB_STRING;
import static org.assertj.core.api.Assertions.assertThat;

import eu.domibus.connector.test.logging.MemoryAppender;
import eu.domibus.connector.test.logging.MemoryAppenderAssert;
import eu.ecodex.utils.spring.quartz.domain.TriggerAndJobDefinition;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"debug=true",
        "test.cron-string=* * * * * ?",
        "test.interval=1s"
})
@ActiveProfiles("test")
public class QuartzQuartzScheduledJobTest {

    static ConfigurableApplicationContext APP_CONTEXT;
    @Autowired(required = false)
    @Qualifier(TRIGGER_AND_JOB_DEFINITION_LIST_BEAN_NAME)
    private List<TriggerAndJobDefinition> triggerAndJobDefinitionList;

    @Test
    public void testRegisteredScheduled() {
//        List triggerAndJobDefinitionList = (List) APP_CONTEXT.getBean(TRIGGER_AND_JOB_DEFINITION_LIST_BEAN_NAME);
        assertThat(triggerAndJobDefinitionList).hasSize(2);
    }

    /*
     * Ensure that the beans are scheduled
     */
    @Test
    public void testJobsRunning() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(2).toMillis());
        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender())
                .containsLogLine(SCHEDULED_CRON_JOB_STRING);
        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender())
                .containsLogLine(SCHEDULED_FIXED_RATE_JOB_STRING);
    }

    @SpringBootApplication(scanBasePackages = "eu.ecodex.utils.spring.quartz")
    static class QuartzScheduledContext {

    }

}
