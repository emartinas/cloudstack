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
import com.cloud.api.response.UserVmResponse;
import com.cloud.uservm.UserVm;

@Implementation(description="List all virtual machine instances that are assigned to a load balancer rule.", responseObject=UserVmResponse.class)
public class ListLoadBalancerRuleInstancesCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger (ListLoadBalancerRuleInstancesCmd.class.getName());

    private static final String s_name = "listloadbalancerruleinstancesresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.APPLIED, type=CommandType.BOOLEAN, description="true if listing all virtual machines currently applied to the load balancer rule; default is true")
    private Boolean applied;

    @IdentityMapper(entityTableName="firewall_rules")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, required=true, description="the ID of the load balancer rule")
    private Long id;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Boolean isApplied() {
        return applied;
    }

    public Long getId() {
        return id;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public void execute(){
        List<? extends UserVm> result = _lbService.listLoadBalancerInstances(this);
        ListResponse<UserVmResponse> response = new ListResponse<UserVmResponse>();
        List<UserVmResponse> vmResponses = new ArrayList<UserVmResponse>();
        if (result != null) {
            vmResponses = _responseGenerator.createUserVmResponse("loadbalancerruleinstance", result.toArray(new UserVm[result.size()]));
        }
        response.setResponses(vmResponses);
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
