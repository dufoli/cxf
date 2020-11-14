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
package org.apache.cxf.jaxb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.cxf.Bus;
import org.apache.cxf.databinding.WrapperHelper;

public class WrapperHelperProxyService implements WrapperHelperCreator {
    WrapperHelperCreator srv;
    public WrapperHelperProxyService(Bus bus) {
        this(new WrapperHelperCompiler(bus));
    }
    public WrapperHelperProxyService(WrapperHelperCreator srv) {
        super();
        this.srv = srv;
    }

    @Override
    public WrapperHelper compile(Bus bus, Class<?> wrapperType, Method[] setMethods, Method[] getMethods,
                                 Method[] jaxbMethods, Field[] fields, Object objectFactory) {
        return srv.compile(bus, wrapperType, setMethods, getMethods, jaxbMethods, fields, objectFactory);
    }

    public class LoadFirst extends WrapperHelperProxyService {
        public LoadFirst(ClassLoader cl) {
            //TODO not sure here if I get class loader like that ???
            // or I need to inject another class loader from outside
            super(new WrapperHelperClassLoader(cl));
        }
    }
    public class GenerateJustInTime extends WrapperHelperProxyService {
        public GenerateJustInTime(Bus bus) {
            super(new WrapperHelperCompiler(bus));
        }
    }
}
