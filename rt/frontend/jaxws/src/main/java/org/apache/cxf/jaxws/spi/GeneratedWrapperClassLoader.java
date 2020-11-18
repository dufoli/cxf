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
package org.apache.cxf.jaxws.spi;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.cxf.Bus;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.common.util.PackageUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.jaxws.WrapperClassGenerator;
import org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean;
import org.apache.cxf.service.model.InterfaceInfo;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.wsdl.service.factory.ReflectionServiceFactoryBean;

public class GeneratedWrapperClassLoader implements WrapperClassCreator {
    private Set<Class<?>> wrapperBeans = new LinkedHashSet<>();
    private InterfaceInfo interfaceInfo;
    private JaxWsServiceFactoryBean factory;

    public GeneratedWrapperClassLoader() {
    }

    @Override
    public Set<Class<?>> generate(Bus bus, JaxWsServiceFactoryBean fact, InterfaceInfo ii, boolean q) {
        factory = fact;
        this.interfaceInfo = ii;
        for (OperationInfo opInfo : interfaceInfo.getOperations()) {
            if (opInfo.isUnwrappedCapable()) {
                Method method = (Method)opInfo.getProperty(ReflectionServiceFactoryBean.METHOD);
                if (method == null) {
                    continue;
                }
                MessagePartInfo inf = opInfo.getInput().getFirstMessagePart();
                if (inf.getTypeClass() == null) {
                    MessageInfo messageInfo = opInfo.getUnwrappedOperation().getInput();
                    createWrapperClass(inf,
                            messageInfo,
                            opInfo,
                            method,
                            true);
                }
                MessageInfo messageInfo = opInfo.getUnwrappedOperation().getOutput();
                if (messageInfo != null) {
                    inf = opInfo.getOutput().getFirstMessagePart();
                    if (inf.getTypeClass() == null) {
                        createWrapperClass(inf,
                                messageInfo,
                                opInfo,
                                method,
                                false);
                    }
                }
            }
        }
        return wrapperBeans;
    }

    private void createWrapperClass(MessagePartInfo wrapperPart,
                                    MessageInfo messageInfo,
                                    OperationInfo op,
                                    Method method,
                                    boolean isRequest) {
        boolean anonymous = factory.getAnonymousWrapperTypes();

        String pkg = getPackageName(method) + ".jaxws_asm" + (anonymous ? "_an" : "");
        String className = pkg + "."
                + StringUtils.capitalize(op.getName().getLocalPart());
        if (!isRequest) {
            className = className + "Response";
        }

        Class<?> clz = null;
        try {
            clz = ClassLoaderUtils.loadClass(className, GeneratedWrapperClassLoader.class);
        } catch (ClassNotFoundException e) {
        }
        wrapperPart.setTypeClass(clz);
        wrapperBeans.add(clz);
    }
    private String getPackageName(Method method) {
        String pkg = PackageUtils.getPackageName(method.getDeclaringClass());
        return pkg.length() == 0 ? WrapperClassGenerator.DEFAULT_PACKAGE_NAME : pkg;
    }
}
