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
package com.cloud.resource;

import java.util.List;

import com.cloud.api.commands.AddClusterCmd;
import com.cloud.api.commands.AddHostCmd;
import com.cloud.api.commands.AddSecondaryStorageCmd;
import com.cloud.api.commands.AddSwiftCmd;
import com.cloud.api.commands.CancelMaintenanceCmd;
import com.cloud.api.commands.DeleteClusterCmd;
import com.cloud.api.commands.ListSwiftsCmd;
import com.cloud.api.commands.PrepareForMaintenanceCmd;
import com.cloud.api.commands.ReconnectHostCmd;
import com.cloud.api.commands.UpdateHostCmd;
import com.cloud.api.commands.UpdateHostPasswordCmd;
import com.cloud.exception.DiscoveryException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceInUseException;
import com.cloud.host.Host;
import com.cloud.hypervisor.Hypervisor.HypervisorType;
import com.cloud.org.Cluster;
import com.cloud.storage.Swift;
import com.cloud.utils.fsm.NoTransitionException;

public interface ResourceService {
    /**
     * Updates a host
     *
     * @param cmd
     *            - the command specifying hostId
     * @return hostObject
     * @throws NoTransitionException
     */
    Host updateHost(UpdateHostCmd cmd) throws NoTransitionException;

    Host cancelMaintenance(CancelMaintenanceCmd cmd);

    Host reconnectHost(ReconnectHostCmd cmd);

    /**
     * We will automatically create a cloud.com cluster to attach to the external cluster and return a hyper host to perform
     * host related operation within the cluster
     *
     * @param cmd
     * @return
     * @throws IllegalArgumentException
     * @throws DiscoveryException
     */
    List<? extends Cluster> discoverCluster(AddClusterCmd cmd) throws IllegalArgumentException, DiscoveryException, ResourceInUseException;

    boolean deleteCluster(DeleteClusterCmd cmd);

    Cluster updateCluster(Cluster cluster, String clusterType, String hypervisor, String allocationState, String managedstate);

    List<? extends Host> discoverHosts(AddHostCmd cmd) throws IllegalArgumentException, DiscoveryException, InvalidParameterValueException;

    List<? extends Host> discoverHosts(AddSecondaryStorageCmd cmd) throws IllegalArgumentException, DiscoveryException, InvalidParameterValueException;

    Host maintain(PrepareForMaintenanceCmd cmd);

    /**
     * Deletes a host
     *
     * @param hostId
     *            TODO
     * @param isForced
     *            TODO
     *
     * @param true if deleted, false otherwise
     */
    boolean deleteHost(long hostId, boolean isForced, boolean isForceDeleteStorage);

    boolean updateHostPassword(UpdateHostPasswordCmd upasscmd);

    Host getHost(long hostId);

    Cluster getCluster(Long clusterId);

    Swift discoverSwift(AddSwiftCmd addSwiftCmd) throws DiscoveryException;

    List<HypervisorType> getSupportedHypervisorTypes(long zoneId, boolean forVirtualRouter, Long podId);

    List<? extends Swift> listSwifts(ListSwiftsCmd cmd);
}
