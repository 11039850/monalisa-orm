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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.ws.jaxrs.core.internal.utils.CollectionUtils;

/**
 * Annotation wrapper for IAnnotation on types, fields, methods and method parameters as well. Annotation wrappers
 * should follow the same lifecycle as their underlying java elements, which means that in the particular case of the
 * ILocalVariable wrapper (java method parameter), the Annotation maybe destroy/re-created as the ILocalVariable is
 * re-created, too.
 * 
 * @author Xavier Coulon
 * 
 */
public class Annotation {

    private final IAnnotation javaAnnotation;

    private final String javaAnnotationName;

    private final Map<String, List<String>> javaAnnotationElements;

    /**
     * Full constructor
     * 
     * @param annotation
     * @param annotationName
     * @param annotationElements
     * @param sourceRange
     * @throws JavaModelException 
     */
    public Annotation(final IAnnotation annotation, final String annotationName,
            final Map<String, List<String>> annotationElements) {
        this.javaAnnotation = annotation;
        this.javaAnnotationName = annotationName;
        this.javaAnnotationElements = new HashMap<String, List<String>>(annotationElements);
    }

    /**
     * Full constructor with a single unnamed 'value'
     * 
     * @param annotation
     * @param annotationName
     * @param annotationValue
     * @param sourceRange
     * @throws JavaModelException 
     */
    public Annotation(final IAnnotation annotation, final String annotationName, final String annotationValue)  {
        this(annotation, annotationName, CollectionUtils.toMap("value", annotationValue));
    }

    /**
     * Update this Annotation from the given other annotation.
     * @param otherAnnotation
     * @return true if some updates in the annotation elements (member pair values) were performed, false otherwise.
     */
    public boolean update(final Annotation otherAnnotation) {
        assert otherAnnotation != null;
        if (this.javaAnnotationElements.equals(otherAnnotation.getJavaAnnotationElements())) {
            return false;
        }
        this.javaAnnotationElements.clear();
        this.javaAnnotationElements.putAll(otherAnnotation.getJavaAnnotationElements());
        return true;
    }
    
    public IAnnotation getJavaAnnotation() {
        return javaAnnotation;
    }

    public IJavaElement getJavaParent() {
        return javaAnnotation.getParent();
    }

    public String getFullyQualifiedName() {
        return javaAnnotationName;
    }

    public Map<String, List<String>> getJavaAnnotationElements() {
        return javaAnnotationElements;
    }

    /** @return the value */
    public List<String> getValues(final String elementName) {
        return javaAnnotationElements.get(elementName);
    }

    /** @return the default value */
    public String getValue() {
        final List<String> values = javaAnnotationElements.get("value");
        if (values != null) {
            assert !(values.size() > 1);
            if (values.size() == 1) {
                return values.get(0);
            }
        }
        return null;
    }

    /** @return the value */
    public String getValue(final String elementName) {
        final List<String> values = javaAnnotationElements.get(elementName);
        if (values != null) {
            assert !(values.size() > 1);
            if (values.size() == 1) {
                return values.get(0);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Annotation [" + javaAnnotationName + " " + javaAnnotationElements + "]";
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((javaAnnotation == null) ? 0 : javaAnnotation.getHandleIdentifier().hashCode());
        result = prime * result + ((javaAnnotationElements == null) ? 0 : javaAnnotationElements.hashCode());
        result = prime * result + ((javaAnnotationName == null) ? 0 : javaAnnotationName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        Annotation other = (Annotation) obj;
        if (javaAnnotationElements == null) {
            if (other.javaAnnotationElements != null) {
                return false;
            }
        } else if (!javaAnnotationElements.equals(other.javaAnnotationElements)) {
            return false;
        }
        if (javaAnnotationName == null) {
            if (other.javaAnnotationName != null) {
                return false;
            }
        } else if (!javaAnnotationName.equals(other.javaAnnotationName)) {
            return false;
        }
        return true;
    }


}