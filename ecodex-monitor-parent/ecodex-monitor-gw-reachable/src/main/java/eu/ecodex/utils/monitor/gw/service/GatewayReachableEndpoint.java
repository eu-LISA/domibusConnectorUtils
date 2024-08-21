/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.gw.service;


import eu.ecodex.utils.monitor.gw.domain.AccessPoint;
import eu.ecodex.utils.monitor.gw.dto.AccessPointStatusDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

@Endpoint(id = "gateways")
public class GatewayReachableEndpoint {

    @Autowired
    ConfiguredGatewaysService configuredGatewaysService;

    @Autowired
    GatewaysCheckerService checkerService;

    @ReadOperation
    List<AccessPointStatusDTO> accessPointStatusList() {
        ArrayList<AccessPointStatusDTO> apStatus = new ArrayList<>();
        return configuredGatewaysService
                .getConfiguredGatewaysWithSelf()
                .stream()
                .map(ap -> checkerService.getGatewayStatus(ap))
                .collect(Collectors.toList());
    }


    @ReadOperation
    public AccessPointStatusDTO getStoreEntryInfo(@Selector String endpointName) {
        AccessPointStatusDTO dto = new AccessPointStatusDTO();
        AccessPoint byName = configuredGatewaysService.getByName(endpointName);
        if (byName == null) {
            return dto;
        }
        return checkerService.getGatewayStatus(byName);
    }


}
