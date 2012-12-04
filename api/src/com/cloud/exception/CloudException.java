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
package com.cloud.exception;

import com.cloud.utils.IdentityProxy;
import java.util.ArrayList;
import com.cloud.utils.exception.CSExceptionErrorCode;
import com.cloud.utils.AnnotationHelper;

/**
 * by the API response serializer. Any exceptions that are thrown by
 * class, which extends RuntimeException instead of Exception like this
 * class does.
 */

public class CloudException extends Exception {

	// This holds a list of uuids and their names. Add uuid:fieldname pairs
	protected ArrayList<IdentityProxy> idList = new ArrayList<IdentityProxy>();

	protected Integer csErrorCode;

	public CloudException(String message) {
		super(message);
		setCSErrorCode(CSExceptionErrorCode.getCSErrCode(this.getClass().getName()));
	}

    public CloudException(String message, Throwable cause) {
        super(message, cause);
        setCSErrorCode(CSExceptionErrorCode.getCSErrCode(this.getClass().getName()));
    }

    public void addProxyObject(Object voObj, Long id, String idFieldName) {
    	// Get the VO object's table name.
    	String tablename = AnnotationHelper.getTableName(voObj);
    	if (tablename != null) {
    		addProxyObject(tablename, id, idFieldName);
    	}
    	return;
    }

	public CloudException() {
		super();
		setCSErrorCode(CSExceptionErrorCode.getCSErrCode(this.getClass().getName()));
	}

	public void addProxyObject(String tableName, Long id, String idFieldName) {
		idList.add(new IdentityProxy(tableName, id, idFieldName));
		return;
	}

	public ArrayList<IdentityProxy> getIdProxyList() {
		return idList;
	}

	public void setCSErrorCode(int cserrcode) {
		this.csErrorCode = cserrcode;
	}

	public int getCSErrorCode() {
		return this.csErrorCode;
	}
}
