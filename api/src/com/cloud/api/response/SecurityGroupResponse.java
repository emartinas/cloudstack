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
package com.cloud.api.response;

import java.util.List;

import org.apache.cloudstack.api.ApiConstants;
import com.cloud.serializer.Param;
import com.cloud.utils.IdentityProxy;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SecurityGroupResponse extends BaseResponse implements ControlledEntityResponse{
    @SerializedName(ApiConstants.ID) @Param(description="the ID of the security group")
    private IdentityProxy id = new IdentityProxy("security_group");

    @SerializedName(ApiConstants.NAME) @Param(description="the name of the security group")
    private String name;

    @SerializedName(ApiConstants.DESCRIPTION) @Param(description="the description of the security group")
    private String description;

    @SerializedName(ApiConstants.ACCOUNT) @Param(description="the account owning the security group")
    private String accountName;

    @SerializedName(ApiConstants.PROJECT_ID) @Param(description="the project id of the group")
    private IdentityProxy projectId = new IdentityProxy("projects");

    @SerializedName(ApiConstants.PROJECT) @Param(description="the project name of the group")
    private String projectName;

    @SerializedName(ApiConstants.DOMAIN_ID) @Param(description="the domain ID of the security group")
    private IdentityProxy domainId = new IdentityProxy("domain");

    @SerializedName(ApiConstants.DOMAIN) @Param(description="the domain name of the security group")
    private String domainName;

    @SerializedName("ingressrule")  @Param(description="the list of ingress rules associated with the security group", responseObject = SecurityGroupRuleResponse.class)
    private List<SecurityGroupRuleResponse> ingressRules;

    @SerializedName("egressrule")  @Param(description="the list of egress rules associated with the security group", responseObject = SecurityGroupRuleResponse.class)
    private List<SecurityGroupRuleResponse> egressRules;

    @SerializedName(ApiConstants.TAGS)  @Param(description="the list of resource tags associated with the rule", responseObject = ResourceTagResponse.class)
    private List<ResourceTagResponse> tags;

    public void setId(Long id) {
        this.id.setValue(id);
    }

    public Long getId() {
        return id.getValue();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setDomainId(Long domainId) {
        this.domainId.setValue(domainId);
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setSecurityGroupIngressRules(List<SecurityGroupRuleResponse> securityGroupRules) {
        this.ingressRules = securityGroupRules;
    }

    public void setSecurityGroupEgressRules(List<SecurityGroupRuleResponse> securityGroupRules) {
        this.egressRules = securityGroupRules;
    }

    @Override
    public Long getObjectId() {
        return getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecurityGroupResponse other = (SecurityGroupResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public void setProjectId(Long projectId) {
        this.projectId.setValue(projectId);
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setTags(List<ResourceTagResponse> tags) {
        this.tags = tags;
    }
}
