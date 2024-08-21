/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.spring.quartz.domain;


import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.MDC;
import org.springframework.scheduling.quartz.QuartzJobBean;

//@DisallowConcurrentExecution
public class CronJob extends QuartzJobBean {

    public static final String MDC_ACTIVE_QUARTZ_JOB = "quartzJobName";

    private static final Logger LOGGER = LogManager.getLogger(QuartzJobBean.class);

    private final boolean toStopFlag = true;
    private Method method;
    private Object bean;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {


        MDC.put(MDC_ACTIVE_QUARTZ_JOB, convertJobToGroupName(jobExecutionContext.getJobDetail()));
        try {
            LOGGER.trace("Invoking method [{}] on bean [{}] to run cron job", method, bean);
            method.invoke(bean);
        } catch (Exception e) {

            throw new JobExecutionException(e);
        } finally {
            MDC.remove(MDC_ACTIVE_QUARTZ_JOB);
        }
    }

    private String convertJobToGroupName(JobDetail jobDetail) {
        return jobDetail.getKey().getGroup() + "_" + jobDetail.getKey().getName();
    }

}
