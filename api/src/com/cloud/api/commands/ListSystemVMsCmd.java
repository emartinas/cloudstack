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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.SystemVmResponse;
import com.cloud.async.AsyncJob;
import com.cloud.utils.Pair;
import com.cloud.vm.VirtualMachine;

@Implementation(description="List system virtual machines.", responseObject=SystemVmResponse.class)
public class ListSystemVMsCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListSystemVMsCmd.class.getName());

    private static final String s_name = "listsystemvmsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="host")
    @Parameter(name=ApiConstants.HOST_ID, type=CommandType.LONG, description="the host ID of the system VM")
    private Long hostId;

    @IdentityMapper(entityTableName="vm_instance")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="the ID of the system VM")
    private Long id;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="the name of the system VM")
    private String systemVmName;

    @IdentityMapper(entityTableName="host_pod_ref")
    @Parameter(name=ApiConstants.POD_ID, type=CommandType.LONG, description="the Pod ID of the system VM")
    private Long podId;

    @Parameter(name=ApiConstants.STATE, type=CommandType.STRING, description="the state of the system VM")
    private String state;

    @Parameter(name=ApiConstants.SYSTEM_VM_TYPE, type=CommandType.STRING, description="the system VM type. Possible types are \"consoleproxy\" and \"secondarystoragevm\".")
    private String systemVmType;

    @IdentityMapper(entityTableName="data_center")
    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.LONG, description="the Zone ID of the system VM")
    private Long zoneId;

    @IdentityMapper(entityTableName="storage_pool")
    @Parameter(name=ApiConstants.STORAGE_ID, type=CommandType.LONG, description="the storage ID where vm's volumes belong to", since="3.0.1")
    private Long storageId;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getHostId() {
        return hostId;
    }

    public Long getId() {
        return id;
    }

    public String getSystemVmName() {
        return systemVmName;
    }

    public Long getPodId() {
        return podId;
    }

    public String getState() {
        return state;
    }

    public String getSystemVmType() {
        return systemVmType;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public Long getStorageId() {
        return storageId;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    public AsyncJob.Type getInstanceType() {
        return AsyncJob.Type.SystemVm;
    }

    @Override
    public void execute(){
        Pair<List<? extends VirtualMachine>, Integer> systemVMs = _mgr.searchForSystemVm(this);
        ListResponse<SystemVmResponse> response = new ListResponse<SystemVmResponse>();
        List<SystemVmResponse> vmResponses = new ArrayList<SystemVmResponse>();
        for (VirtualMachine systemVM : systemVMs.first()) {
            SystemVmResponse vmResponse = _responseGenerator.createSystemVmResponse(systemVM);
            vmResponse.setObjectName("systemvm");
            vmResponses.add(vmResponse);
        }

        response.setResponses(vmResponses, systemVMs.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
