/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.ws.jaxrs.core.jdt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class JavaMethodSignature {

    /** Underlying java method. */
    private final IMethod javaMethod;
    /** Java method return type.*/
    private final IType returnedType;
    /** Method parameters, indexed by their own name.*/
    private final Map<String, JavaMethodParameter> methodParameters;

    public JavaMethodSignature(IMethod javaMethod, IType returnedType, List<JavaMethodParameter> methodParameters) {
        this.javaMethod = javaMethod;
        this.returnedType = returnedType;
        this.methodParameters = new HashMap<String, JavaMethodParameter>(methodParameters.size()*2);
        for (JavaMethodParameter javaMethodParameter : methodParameters) {
            this.methodParameters.put(javaMethodParameter.getName(), javaMethodParameter);
        }
    }

    /** @return the method */
    public IMethod getJavaMethod() {
        return javaMethod;
    }

    /**
     * @return the java method return type.
     */
    public IType getReturnedType() {
        return returnedType;
    }

    /**
     * The java method parameters.
     * @return
     */
    public Map<String, JavaMethodParameter> getMethodParameters() {
        return methodParameters;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder("JavaMethodSignature ");
        if (returnedType != null) {
            stb.append(returnedType.getElementName()).append(" ");
        } else {
            stb.append("void ");
        }
        stb.append(javaMethod.getElementName()).append("(");
        for (Iterator<JavaMethodParameter> paramIterator = methodParameters.values().iterator(); paramIterator.hasNext();) {
            JavaMethodParameter methodParam = (JavaMethodParameter) paramIterator.next();
            for (Entry<String, Annotation> entry : methodParam.getAnnotations().entrySet()) {
                stb.append(entry.getValue()).append(" ");
            }
            stb.append(methodParam.getTypeName());
            if (paramIterator.hasNext()) {
                stb.append(", ");
            }
        }
        stb.append(")");
        return stb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((javaMethod == null) ? 0 : javaMethod.hashCode());
        result = prime * result + ((methodParameters == null) ? 0 : methodParameters.hashCode());
        result = prime * result + ((returnedType == null) ? 0 : returnedType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JavaMethodSignature other = (JavaMethodSignature) obj;
        if (javaMethod == null) {
            if (other.javaMethod != null) {
                return false;
            }
        } else if (!javaMethod.equals(other.javaMethod)) {
            return false;
        }
        if (methodParameters == null) {
            if (other.methodParameters != null) {
                return false;
            }
        } else if (!methodParameters.equals(other.methodParameters)) {
            return false;
        }

        if (returnedType == null) {
            if (other.returnedType != null)
                return false;
        } else if (!returnedType.equals(other.returnedType))
            return false;
        return true;
    }

}