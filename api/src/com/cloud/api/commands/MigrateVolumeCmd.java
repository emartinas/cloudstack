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

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.VolumeResponse;
import com.cloud.event.EventTypes;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.storage.Volume;
import com.cloud.user.Account;


@Implementation(description="Migrate volume", responseObject=VolumeResponse.class, since="3.0.0")
public class MigrateVolumeCmd extends BaseAsyncCmd {
    private static final String s_name = "migratevolumeresponse";

     /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="volumes")
    @Parameter(name=ApiConstants.VOLUME_ID, type=CommandType.LONG, required=true, description="the ID of the volume")
    private Long volumeId;

    @IdentityMapper(entityTableName="storage_pool")
    @Parameter(name=ApiConstants.STORAGE_ID, type=CommandType.LONG, required=true, description="destination storage pool ID to migrate the volume to")
    private Long storageId;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getVolumeId() {
        return volumeId;
    }

    public Long getStoragePoolId() {
        return storageId;
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
          Volume volume = _entityMgr.findById(Volume.class, getVolumeId());
          if (volume != null) {
              return volume.getAccountId();
          }

          return Account.ACCOUNT_ID_SYSTEM; // no account info given, parent this command to SYSTEM so ERROR events are tracked
    }

    @Override
    public String getEventType() {
        return EventTypes.EVENT_VOLUME_MIGRATE;
    }

    @Override
    public String getEventDescription() {
        return  "Attempting to migrate volume Id: " + getVolumeId() + " to storage pool Id: "+ getStoragePoolId();
    }


    @Override
    public void execute(){
        Volume result;
        try {
            result = _storageService.migrateVolume(getVolumeId(), getStoragePoolId());
             if (result != null) {
                 VolumeResponse response = _responseGenerator.createVolumeResponse(result);
                 response.setResponseName(getCommandName());
                 this.setResponseObject(response);
             }
        } catch (ConcurrentOperationException e) {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to migrate volume: ");
        }

    }

}
