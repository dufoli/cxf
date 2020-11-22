/**
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

import java.util.logging.Logger;

import org.apache.cxf.Bus;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.common.logging.LogUtils;

public class GeneratedClassClassLoader {
    private static final Logger LOG = LogUtils.getL7dLogger(ClassLoaderProxyService.class);
    protected final Bus bus;
    public GeneratedClassClassLoader(Bus bus) {
        this.bus = bus;
    }
    protected Class<?> loadClass(String className, Class<?> callingClass) {
        ClassLoader cl = bus.getExtension(ClassLoader.class);
        if (cl != null) {
            try {
                return cl.loadClass(className);
            } catch (ClassNotFoundException e) {
                //ignore and try with other class loader
            }
        }
        try {
            return ClassLoaderUtils.loadClass(className, callingClass);
        } catch (ClassNotFoundException e) {
            LOG.fine("Failed to load class :" + e.toString());
        }
        return null;
    }

}
