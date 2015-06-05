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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * A visitor for a single annotation on a java member (can be a method or a type).
 * 
 * @author xcoulon
 */
public class JavaAnnotationsVisitor extends ASTVisitor {

    /** the annotated member name. */
    private final String memberName;

    /** the annotated member type. */
    private final int memberType;

    /** the annotated member start position, to distinguish between overloaded methods. */
    private final int memberStartPosition;

    /** the annotated member end position, to distinguish between overloaded methods. */
    private final int memberEndPosition;

    /** the name of the annotation. */
    private final List<String> annotationNames = new ArrayList<String>();

    /** the bindings for the matching annotation. */
    private final List<Annotation> annotations = new ArrayList<Annotation>();

    /**
     * Full Constructor to resolve a single annotation from its fully qualified name.
     * 
     * @param name
     *            the member name
     * @param memberType
     *            the member type
     * @param name
     *            the annotation name
     * @throws JavaModelException
     */
    public JavaAnnotationsVisitor(final IMember member, final String annotationName) throws JavaModelException {
        super();
        this.memberName = member.getElementName();
        this.memberType = member.getElementType();
        this.memberStartPosition = member.getSourceRange().getOffset();
        this.memberEndPosition = member.getSourceRange().getOffset() + member.getSourceRange().getLength();
        this.annotationNames.add(annotationName);
    }

    /**
     * Full Constructor to resolve a multiple annotations from their fully qualified name.
     * 
     * @param name
     *            the member name
     * @param memberType
     *            the member type
     * @param name
     *            the annotation name
     * @throws JavaModelException
     */
    public JavaAnnotationsVisitor(final IMember member, final List<String> annotationNames) throws JavaModelException {
        super();
        this.memberName = member.getElementName();
        this.memberType = member.getElementType();
        this.memberStartPosition = member.getSourceRange().getOffset();
        this.memberEndPosition = member.getSourceRange().getOffset() + member.getSourceRange().getLength();
        this.annotationNames.addAll(annotationNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnnotationTypeDeclaration)
     */
    @Override
    public final boolean visit(final AnnotationTypeDeclaration node) {
        if (memberType == IJavaElement.TYPE && node.getName().getFullyQualifiedName().equals(memberName)
                && matchesLocation(node)) {
            visitExtendedModifiers((List<?>) node.getStructuralProperty(AnnotationTypeDeclaration.MODIFIERS2_PROPERTY));
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom. TypeDeclaration)
     */
    @Override
    public final boolean visit(final TypeDeclaration node) {
        if (memberType == IJavaElement.TYPE && node.getName().getFullyQualifiedName().equals(memberName)
                && matchesLocation(node)) {
            visitExtendedModifiers((List<?>) node.getStructuralProperty(TypeDeclaration.MODIFIERS2_PROPERTY));
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom. MethodDeclaration)
     */
    @Override
    public final boolean visit(final MethodDeclaration node) {
        if (memberType == IJavaElement.METHOD && node.getName().getFullyQualifiedName().equals(memberName)
                && matchesLocation(node)) {
            visitExtendedModifiers((List<?>) node.getStructuralProperty(MethodDeclaration.MODIFIERS2_PROPERTY));
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom. MethodDeclaration)
     */
    @Override
    public final boolean visit(final FieldDeclaration node) {
        if (memberType == IJavaElement.FIELD) {
            VariableDeclarationFragment fragment = (VariableDeclarationFragment) (node.fragments().get(0));
            if (fragment.getName().toString().equals(memberName) && matchesLocation(node)) {
                visitExtendedModifiers((List<?>) node.getStructuralProperty(FieldDeclaration.MODIFIERS2_PROPERTY));
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given node matches the expected member location by comparing start and end positions.
     * 
     * @param node
     * @return
     */
    private boolean matchesLocation(final ASTNode node) {
        return node.getStartPosition() >= this.memberStartPosition
                && (node.getStartPosition() + node.getLength()) <= this.memberEndPosition;
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
                IAnnotationBinding annotationBinding = ((org.eclipse.jdt.core.dom.Annotation) modifier)
                        .resolveAnnotationBinding();
                if (annotationBinding != null) {
                    final String qualifiedName = annotationBinding.getAnnotationType().getQualifiedName();
                    final String name = annotationBinding.getAnnotationType().getName();
                    if (annotationNames.contains(qualifiedName) || annotationNames.contains(name)) {
                        annotations.add(BindingUtils.toAnnotation(annotationBinding));
                    }
                }
            }
        }
    }

    /**
     * Returns the Annotation element matching the annotation name given in the visitor constructor. This method should
     * only be called when the constructor with a single annotation name was used.
     * 
     * @return the annotation found on the target java element
     * @throws JavaModelException
     *             in case of underlying exception
     */
    public final Annotation getResolvedAnnotation() throws JavaModelException {
        assert annotationNames.size() == 1;
        if (annotations.size() == 0) {
            return null;
        }
        return annotations.get(0);
    }

    /**
     * Returns the Annotation elements matching the annotations name given in the visitor constructor. The matching
     * annotations are indexed by their associated Java type's fully qualified names. This method should only be called
     * when the constructor with multiple annotation names was used.
     * 
     * @return the annotation found on the target java element
     * @throws JavaModelException
     *             in case of underlying exception
     */
    public final Map<String, Annotation> getResolvedAnnotations() throws JavaModelException {
        final Map<String, Annotation> resolvedJavaAnnotations = new HashMap<String, Annotation>();
        for (Annotation annotation : annotations) {
            resolvedJavaAnnotations.put(annotation.getFullyQualifiedName(), annotation);
        }
        return resolvedJavaAnnotations;
    }

    

}