/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.ws.jaxrs.core.jdt;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

/**
 * @author Xavier Coulon
 *
 */
public class BindingUtils {

    /**
     * Private constructor for this utility class. 
     */
    private BindingUtils() {
    }
    
    /**
     * Creates a instance of {@link Annotation} from the given annotation binding.
     * @param annotationBinding
     * @return
     */
    public static Annotation toAnnotation(final IAnnotationBinding annotationBinding) {
        return toAnnotation(annotationBinding, (IAnnotation) annotationBinding.getJavaElement());
    }
    
    /**
     * Creates a instance of {@link Annotation} from the given annotation binding, specifically using the given javaAnnotation instead of the one that could be retrieved from the binding.
     * @param annotationBinding
     * @param javaAnnotation
     * @return
     */
    public static Annotation toAnnotation(IAnnotationBinding annotationBinding, IAnnotation javaAnnotation) {
        final String annotationName = annotationBinding.getAnnotationType().getQualifiedName();
        final Map<String, List<String>> annotationElements = BindingUtils.resolveAnnotationElements(annotationBinding);
        return new Annotation(javaAnnotation, annotationName, annotationElements);
    }

    
    public static Map<String, List<String>> resolveAnnotationElements(IAnnotationBinding annotationBinding) {
        final Map<String, List<String>> annotationElements = new HashMap<String, List<String>>();
        try {
            for (IMemberValuePairBinding binding : annotationBinding.getAllMemberValuePairs()) {
                final List<String> values = new ArrayList<String>();
                if(binding.getValue() != null) {
                    if (binding.getValue() instanceof Object[]) {
                    for (Object v : (Object[]) binding.getValue()) {
                        values.add(toString(v));
                    }
                } else {
                    values.add(toString(binding.getValue()));
                }
                }
                annotationElements.put(binding.getName(), values);
            }
            // if the code is not valid, the underlying DefaultValuePairBinding
            // may throw a NPE:
            // at
            // org.eclipse.jdt.core.dom.DefaultValuePairBinding.<init>(DefaultValuePairBinding.java:31)
            // at
            // org.eclipse.jdt.core.dom.AnnotationBinding.getAllMemberValuePairs(AnnotationBinding.java:98)
        } catch (Throwable e) {
            // silently ignore
        }
        return annotationElements;
    }

    /**
     * Converts the given value into String. The actual types that are supported are:
     * java.lang.Class - the ITypeBinding for the class object
     * java.lang.String - the string value itself
     * enum type - the IVariableBinding for the enum constant
     * annotation type - an IAnnotationBinding
     * for other types, the <code>java.lang.Object{@link #toString()}</code> method is used.
     * @param value
     * @return litteral value
     */
    public static String toString(Object value) {
        if(value instanceof ITypeBinding) {
            return ((ITypeBinding)value).getQualifiedName();
        } else if(value instanceof IVariableBinding) {
            return ((IVariableBinding)value).getName();
        } else if(value instanceof IAnnotationBinding) {
            return ((IAnnotationBinding)value).getName();
        } 
        return value.toString();
    }

}
