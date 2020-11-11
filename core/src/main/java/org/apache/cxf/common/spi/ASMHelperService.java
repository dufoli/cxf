/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.common.spi;

import org.apache.cxf.common.util.ASMHelper;

/**
 * SPI interface to implement the proxy defining logic.
 * It enables to switch from unsafe to classloader logic for instance for java >= 9.
 */
public interface ASMHelperService {
    /**
     * Returns the ASMHelper to use to define the given class.
     * @return the ASMHelper to use to define the class
     */
    ASMHelper getProxyASMHelper();
}
