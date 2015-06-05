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


import java.util.List;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jboss.tools.ws.jaxrs.core.internal.utils.Logger;

/**
 * @author Xavier Coulon
 * 
 */
public class JavaAnnotationLocator extends ASTVisitor {

    private final int location;
    private final IMethod parentMethod;

    private Annotation locatedAnnotation;

    public JavaAnnotationLocator(final IJavaElement parentElement, final int location) {
        this.parentMethod = (parentElement.getElementType() == IJavaElement.METHOD) ? (IMethod) parentElement : null;
        this.location = location;
    }

    /**
     * @return the locatedJavaAnnotation
     */
    public Annotation getLocatedAnnotation() {
        return locatedAnnotation;
    }

    /**
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnnotationTypeDeclaration)
     */
    @Override
    public boolean visit(AnnotationTypeDeclaration node) {
        visitExtendedModifiers((List<?>) node.getStructuralProperty(AnnotationTypeDeclaration.MODIFIERS2_PROPERTY));
        // no need to visit furthermore
        return false;
    }

    /**
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.FieldDeclaration)
     */
    @Override
    public boolean visit(FieldDeclaration node) {
        visitExtendedModifiers((List<?>) node.getStructuralProperty(FieldDeclaration.MODIFIERS2_PROPERTY));
        // no need to visit furthermore
        return false;
    }

    /**
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeDeclaration)
     */
    @Override
    public boolean visit(TypeDeclaration node) {
        visitExtendedModifiers((List<?>) node.getStructuralProperty(TypeDeclaration.MODIFIERS2_PROPERTY));
        // no need to visit furthermore
        return false;
    }

    /**
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodDeclaration)
     */
    @Override
    public boolean visit(MethodDeclaration declaration) {
        visitExtendedModifiers((List<?>) declaration.getStructuralProperty(MethodDeclaration.MODIFIERS2_PROPERTY));
        return this.locatedAnnotation == null;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SingleVariableDeclaration)
     */
    @Override
    public boolean visit(SingleVariableDeclaration variableDeclaration) {
        // skip if parentMethod is undefined or if annotation is already located
        if (this.parentMethod == null || this.locatedAnnotation != null) {
            return false;
        }
        try {
            if (DOMUtils.nodeMatches(variableDeclaration, location)) {
                final IVariableBinding variableDeclarationBinding = variableDeclaration.resolveBinding();
                final IAnnotationBinding[] annotationBindings = variableDeclarationBinding.getAnnotations();
                // retrieve the parameter index in the parent method
                final ILocalVariable localVariable = getLocalVariable(variableDeclarationBinding);
                if (localVariable != null) {
                    final IAnnotation[] variableAnnotations = localVariable.getAnnotations();
                    for (int j = 0; j < annotationBindings.length; j++) {
                        final IAnnotation javaAnnotation = variableAnnotations[j];
                        if (RangeUtils.matches(javaAnnotation.getSourceRange(), location)) {
                            final IAnnotationBinding javaAnnotationBinding = annotationBindings[j];
                            this.locatedAnnotation = BindingUtils.toAnnotation(javaAnnotationBinding, javaAnnotation);
                            break;
                        }
                    }
                }
                // TODO : add support for thrown exceptions
            }
        } catch (JavaModelException e) {
            Logger.error("Failed to analyse compilation unit method '" + this.parentMethod.getElementName() + "'", e);
        }

        // no need to carry on from here
        return false;

    }

    /**
     * Returns the localVariable associated with the given variable declaration binding, or null if it could not be found
     * @param variableDeclarationBinding
     * @return
     * @throws JavaModelException
     */
    private ILocalVariable getLocalVariable(final IVariableBinding variableDeclarationBinding)
            throws JavaModelException {
        int i = -1;
        for (String paramName : parentMethod.getParameterNames()) {
            i++;
            if (paramName.equals(variableDeclarationBinding.getName())) {
                break;
            }
        }
        if(i>=0) {
        return this.parentMethod.getParameters()[i];
        }
        return null;
    }

    /**
     * Visits the modifiers.
     * 
     * @param modifiers
     *            the modifiers
     */
    private void visitExtendedModifiers(final List<?> modifiers) {
        for (Object modifier : modifiers) {
            if (modifier instanceof org.eclipse.jdt.core.dom.Annotation) {
                final org.eclipse.jdt.core.dom.Annotation annotation = (org.eclipse.jdt.core.dom.Annotation) modifier;
                if (DOMUtils.nodeMatches(annotation, location)) {
                    final IAnnotationBinding annotationBinding = annotation.resolveAnnotationBinding();
                    if (annotationBinding != null) {
                        final IAnnotation javaAnnotation = (IAnnotation) annotationBinding.getJavaElement();
                        this.locatedAnnotation = BindingUtils.toAnnotation(annotationBinding, javaAnnotation);
                    }
                }
            }
        }
    }

}