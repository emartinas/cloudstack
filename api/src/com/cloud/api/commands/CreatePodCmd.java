// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.api.commands;

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.PodResponse;
import com.cloud.dc.Pod;
import com.cloud.user.Account;

@Implementation(description="Creates a new Pod.", responseObject=PodResponse.class)
public class CreatePodCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(CreatePodCmd.class.getName());

    private static final String s_name = "createpodresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, required=true, description="the name of the Pod")
    private String podName;

    @IdentityMapper(entityTableName="data_center")
    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.LONG, required=true, description="the Zone ID in which the Pod will be created   ")
    private Long zoneId;

    @Parameter(name=ApiConstants.START_IP, type=CommandType.STRING, required=true, description="the starting IP address for the Pod")
    private String startIp;

    @Parameter(name=ApiConstants.END_IP, type=CommandType.STRING, description="the ending IP address for the Pod")
    private String endIp;

    @Parameter(name=ApiConstants.NETMASK, type=CommandType.STRING, required=true, description="the netmask for the Pod")
    private String netmask;

    @Parameter(name=ApiConstants.GATEWAY, type=CommandType.STRING, required=true, description="the gateway for the Pod")
    private String gateway;

    @Parameter(name=ApiConstants.ALLOCATION_STATE, type=CommandType.STRING, description="Allocation state of this Pod for allocation of new resources")
    private String allocationState;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getNetmask() {
        return netmask;
    }

    public String getEndIp() {
        return endIp;
    }

    public String getGateway() {
        return gateway;
    }

    public String getPodName() {
        return podName;
    }

    public String getStartIp() {
        return startIp;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public String getAllocationState() {
        return allocationState;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }

    @Override
    public void execute(){
        Pod result = _configService.createPod(getZoneId(), getPodName(), getStartIp(), getEndIp(), getGateway(), getNetmask(), getAllocationState());
        if (result != null) {
            PodResponse response = _responseGenerator.createPodResponse(result, false);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to create pod");
        }
    }
}
