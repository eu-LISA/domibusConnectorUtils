/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.gw.service;

import eu.domibus.ext.domain.PModeArchiveInfoDTO;
import eu.ecodex.configuration.pmode.Configuration;
import eu.ecodex.utils.monitor.gw.config.GatewayRestInterfaceConfiguration;
import eu.ecodex.utils.monitor.gw.domain.AccessPoint;
import eu.ecodex.utils.monitor.gw.domain.AccessPointsConfiguration;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class PModeDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PModeDownloader.class);

    private final GatewayRestInterfaceConfiguration gatewayRestInterfaceConfiguration;

    private final RestTemplate restTemplate;
    private final ConfigurationWrapper wrappedConfiguration = new ConfigurationWrapper();

    public PModeDownloader(
            @Autowired GatewayRestInterfaceConfiguration gatewayRestInterfaceConfiguration) {
        this.gatewayRestInterfaceConfiguration = gatewayRestInterfaceConfiguration;
        this.restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(
                        new DefaultUriBuilderFactory(gatewayRestInterfaceConfiguration.getUrl()))
                .basicAuthentication(gatewayRestInterfaceConfiguration.getUsername(),
                        gatewayRestInterfaceConfiguration.getPassword())
                .build();
    }


    public Configuration downloadPModes() {
        return downloadNewPModes(-1).config;
    }

    public ConfigurationWrapper downloadNewPModes(int id) {
        String lineSeperator = "\n\n####################";

        ResponseEntity<PModeArchiveInfoDTO> currentPMode =
                restTemplate.getForEntity("/ext/pmode/current", PModeArchiveInfoDTO.class);

        int pmodeId = -1;
        LOGGER.debug("Retrieved json [{}]", currentPMode);

        pmodeId = currentPMode.getBody().getId();

        if (pmodeId > id) {
            ResponseEntity<String> forEntity = restTemplate
                    .getForEntity("/ext/pmode/" + pmodeId, String.class);

            String xmlString = forEntity.getBody();

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Downloaded pmode:" + lineSeperator + xmlString + lineSeperator);
            }

            JAXBContext jaxbContext;
            try {
                jaxbContext = JAXBContext.newInstance(Configuration.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                Configuration configuration =
                        (Configuration) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));
                this.wrappedConfiguration.config = configuration;
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Downloaded pmode: " + lineSeperator + this.wrappedConfiguration +
                            lineSeperator);
                }

            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return this.wrappedConfiguration;
    }

    public AccessPointsConfiguration updateAccessPointsConfig(
            @NotNull AccessPointsConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException("Config is not allowed to be null!");
        }
        ConfigurationWrapper wrappedConfiguration = this.downloadNewPModes(config.getId());
        Configuration conf = wrappedConfiguration.config;
        String selfParty = conf.getParty();

        Map<String, AccessPoint> aps = conf
                .getBusinessProcesses()
                .getParties().getParty().stream()
                .map(this::mapMpc)
                .collect(Collectors.toMap(AccessPoint::getName, Function.identity()));

        AccessPoint self = aps.remove(selfParty);
        config.setSelf(self);
        config.setRemoteAccessPoints(aps.values());
        config.setId(wrappedConfiguration.id);
        return config;
    }

    private AccessPoint mapMpc(Configuration.BusinessProcesses.Parties.Party party) {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setName(party.getName());
        accessPoint.setEndpoint(party.getEndpoint());
        return accessPoint;
    }

    private static class ConfigurationWrapper {
        Configuration config;
        int id = -1;
    }

}



