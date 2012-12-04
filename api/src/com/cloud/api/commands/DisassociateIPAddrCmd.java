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
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.SuccessResponse;
import com.cloud.async.AsyncJob;
import com.cloud.event.EventTypes;
import com.cloud.exception.InsufficientAddressCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.network.IpAddress;
import com.cloud.user.Account;
import com.cloud.user.UserContext;

@Implementation(description="Disassociates an ip address from the account.", responseObject=SuccessResponse.class)
public class DisassociateIPAddrCmd extends BaseAsyncCmd {
    public static final Logger s_logger = Logger.getLogger(DisassociateIPAddrCmd.class.getName());

    private static final String s_name = "disassociateipaddressresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="user_ip_address")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, required=true, description="the id of the public ip address" +
            " to disassociate")
    private Long id;

    // unexposed parameter needed for events logging
    @IdentityMapper(entityTableName="account")
    @Parameter(name=ApiConstants.ACCOUNT_ID, type=CommandType.LONG, expose=false)
    private Long ownerId;
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getIpAddressId() {
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
    public void execute() throws InsufficientAddressCapacityException{
        UserContext.current().setEventDetails("Ip Id: " + getIpAddressId());
        boolean result = _networkService.releaseIpAddress(getIpAddressId());
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to disassociate ip address");
        }
    }

    @Override
    public String getEventType() {
        return EventTypes.EVENT_NET_IP_RELEASE;
    }

    @Override
    public String getEventDescription() {
        return  ("Disassociating ip address with id=" + id);
    }

    @Override
    public long getEntityOwnerId() {
        if (ownerId == null) {
            IpAddress ip = getIpAddress(id);
            if (ip == null) {
                throw new InvalidParameterValueException("Unable to find ip address by id=" + id);
            }
            ownerId = ip.getAccountId();
        }

        if (ownerId == null) {
            return Account.ACCOUNT_ID_SYSTEM;
        }
        return ownerId;
    }

    @Override
    public String getSyncObjType() {
        return BaseAsyncCmd.networkSyncObject;
    }

    @Override
    public Long getSyncObjId() {
        IpAddress ip = getIpAddress(id);
        return ip.getAssociatedWithNetworkId();
    }

    private IpAddress getIpAddress(long id) {
        IpAddress ip = _entityMgr.findById(IpAddress.class, id);

        if (ip == null) {
            throw new InvalidParameterValueException("Unable to find ip address by id=" + id);
        } else {
            return ip;
        }
    }

    @Override
    public AsyncJob.Type getInstanceType() {
        return AsyncJob.Type.IpAddress;
    }

    @Override
    public Long getInstanceId() {
        return getIpAddressId();
    }
}
