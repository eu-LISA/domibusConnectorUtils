/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.spring.quartz.testbeans;

import eu.ecodex.utils.spring.quartz.annotation.QuartzScheduled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class ScheduledBean {

    public static final String SCHEDULED_CRON_JOB_STRING = "scheduledCronJob";
    public static final String SCHEDULED_FIXED_RATE_JOB_STRING = "scheduledFixedRateJob";
    private static final Logger LOGGER = LogManager.getLogger(ScheduledBean.class);

    //    @Scheduled(cron = "* * * * * ?")
    @QuartzScheduled(cron = ScheduledBeanConfigurationProperties.class)
    public void scheduledCronJob() {
        long now = System.currentTimeMillis() / 1000;
        LOGGER.info("schedule tasks using cron jobs [{}] - {}", SCHEDULED_CRON_JOB_STRING, now);
    }

    //    @Scheduled(fixedDelay = 1000)
    @QuartzScheduled(fixedRate = ScheduledBeanConfigurationProperties.class)
    public void scheduledFixedRateJob() {
        long now = System.currentTimeMillis() / 1000;
        LOGGER.info("schedule tasks using cron jobs [{}] - {}", SCHEDULED_FIXED_RATE_JOB_STRING,
                now);
    }

}
