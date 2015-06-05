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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * @author Xavier Coulon Utility class with a few handy methods on ASTNodes
 */
public class DOMUtils {

    /**
     * private constructor for this utility class
     */
    private DOMUtils() {
    }

    /**
     * Returns true if the given ASTNode's position in the source code matches with the given position parameter, false
     * otherwise.
     * 
     * @param node
     * @param position
     * @return
     */
    static boolean nodeMatches(ASTNode node, int position) {
        final int endPosition = node.getStartPosition() + node.getLength();
        final int startPosition = node.getStartPosition();
        return startPosition <= position && position <= endPosition;
    }

    /**
     * Returns the ASTNode associated with the given java Element expectedType and found at the given location. This method will
     * perform faster as the initial parentNode is precisely defined (eg : TypeDeclaration or MethodDeclaration instead of
     * CompilationUnit)
     * 
     * @param compilationUnit
     * @param elementType
     * @param position
     * @return
     * @see {@link IJavaElement}
     */
    //FIXME: this should be part of the visitor.
    static ASTNode getASTNodeByTypeAndLocation(final ASTNode parentNode, final int expectedType, final int location) {
        switch (parentNode.getNodeType()) {
        case ASTNode.COMPILATION_UNIT:
            @SuppressWarnings("unchecked")
            final List<ASTNode> types = ((CompilationUnit) parentNode).types();
            for (ASTNode type : types) {
                if (nodeMatches(type, location)) {
                    if (expectedType == IJavaElement.TYPE) {
                        return type;
                    }
                    // could also be ANNOTATION_TYPE_DECLARATION, which doesn't need to trigger recursive call to this
                    // method.
                    if (type.getNodeType() == CompilationUnit.TYPE_DECLARATION) {
                        return getASTNodeByTypeAndLocation(type, expectedType, location);
                    }
                }
            }
            break;
        case ASTNode.TYPE_DECLARATION:
            final FieldDeclaration[] fieldDeclarations = ((TypeDeclaration) parentNode).getFields();
            for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
                if (nodeMatches(fieldDeclaration, location)) {
                    if (expectedType == IJavaElement.FIELD) {
                        return fieldDeclaration;
                    }
                }
            }
            final MethodDeclaration[] methodDeclarations = ((TypeDeclaration) parentNode).getMethods();
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                if (nodeMatches(methodDeclaration, location)) {
                    if (expectedType == IJavaElement.METHOD) {
                        return methodDeclaration;
                    }
                    return getASTNodeByTypeAndLocation(methodDeclaration, expectedType, location);

                }
            }
            return null;
        case ASTNode.METHOD_DECLARATION:
            @SuppressWarnings("unchecked")
            final List<ASTNode> parameters = ((MethodDeclaration) parentNode).parameters();
            for (ASTNode parameter : parameters) {
                if (nodeMatches(parameter, location)) {
                    if (expectedType == IJavaElement.LOCAL_VARIABLE) {
                        return parameter;
                    }
                }
            }
        }

        return null;
    }
    
    

}