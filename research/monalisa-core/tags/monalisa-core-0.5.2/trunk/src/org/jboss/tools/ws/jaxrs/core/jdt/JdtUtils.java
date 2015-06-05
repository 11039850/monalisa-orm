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
 

import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT_ROOT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.CreateTypeHierarchyOperation;

/**
 * A JDT wrapper that provides utility methods to manipulate the Java Model.
 * 
 * @author xcoulon
 */
@SuppressWarnings("restriction")
public final class JdtUtils {

    /** Hidden constructor of the utility method. Prevents instantiation. */
    private JdtUtils() {
        super();
    }

    /**
     * Returns the compilation unit associated with the given resource.
     * 
     * @param resource
     *            the resource
     * @return the compilation unit or null if the resource is not a compilation
     *         unit.
     */
    public static ICompilationUnit getCompilationUnit(final IResource resource) {
        IJavaElement element = JavaCore.create(resource);
        if (element instanceof ICompilationUnit) {
            return (ICompilationUnit) element;
        }
        return null;
    }

    /**
     * Returns the compilation unit associated with the given resource.
     * 
     * @param resource
     *            the resource
     * @return the compilation unit or null if the resource is not a compilation
     *         unit.
     */
    public static ICompilationUnit getCompilationUnit(final IJavaElement element) {
        if (element instanceof IMember) {
            return ((IMember) element).getCompilationUnit();
        } else if (element instanceof IAnnotation 
                // ignore annotations on PackageDeclaration, such as in package-info.java
                && element.getParent() instanceof IMember) { 
            return ((IMember) (element.getParent())).getCompilationUnit();
        } else if (element instanceof ICompilationUnit) {
            return (ICompilationUnit) element;
        }
        return null;
    }

    /**
     * Checks whether the given type is abstract or not.
     * 
     * @param type
     *            the type to check
     * @return true if the type is abstract, false otherwise
     * @throws JavaModelException
     *             the underlying JavaModelException thrown by the manipulated
     *             JDT APIs
     */
    public static boolean isAbstractType(final IType type) throws JavaModelException {
        return Flags.isAbstract(type.getFlags());
    }

    /**
     * Returns the toplevel type of the given compilation unit.
     * 
     * @param compilationUnit
     *            the DOM CompilationUnit returned by the parse() method. This
     *            operation is expensive and should be performed only once for
     *            each type.
     * @return the top level type
     * @throws JavaModelException
     *             in case of exception
     */
    public static IType resolveTopLevelType(final ICompilationUnit compilationUnit) throws JavaModelException {

        if (compilationUnit != null && compilationUnit.exists() && compilationUnit.getTypes() != null
                && compilationUnit.getTypes().length > 0) {
            if (compilationUnit.getTypes()[0].getDeclaringType() != null) {
                return compilationUnit.getTypes()[0].getDeclaringType();
            }
            return compilationUnit.getTypes()[0];
        }
        return null;
    }

    /**
     * Checks if the given type is a top-level type in its own compilation unit.
     * 
     * @param type
     *            the given type
     * @return if the given type is a top-level type, false otherwise
     */
    public static boolean isTopLevelType(final IType type) {
        return (type.equals(type.getTypeRoot().findPrimaryType()));
    }

    /**
     * Returns true if the given Java Element is a ICompilationUnit element or
     * an IMember, and is in working copy state.
     * 
     * @param element
     * @return true if the enclosing compilation unit is a working copy, false
     *         otherwise
     */
    public static boolean isWorkingCopy(IJavaElement element) {
        ICompilationUnit compilationUnit = getCompilationUnit(element);
        if (compilationUnit != null) {
            return compilationUnit.isWorkingCopy();
        }
        return false;
    }

    public static IPackageFragmentRoot getPackageFragmentRoot(final IJavaElement element) {
        IJavaElement e = element;
        while (e.getElementType() != PACKAGE_FRAGMENT_ROOT) {
            e = e.getParent();
        }
        return (IPackageFragmentRoot) e;

    }

    /**
     * Returns the closest Java Element that surrounds the given location in the
     * given compilationUnit. This method can return SimpleAnnotation, which the
     * default JDT ICompilationUnit implementation does not support.
     * 
     * @param sourceRange
     * @param location
     * @return
     * @throws JavaModelException
     */
    public static IJavaElement getElementAt(ICompilationUnit compilationUnit, int location) throws JavaModelException {
        final IJavaElement element = compilationUnit.getElementAt(location);
        if (element instanceof IAnnotatable) {
            for (IAnnotation annotation : ((IAnnotatable) element).getAnnotations()) {
                final int length = annotation.getSourceRange().getLength();
                final int offset = annotation.getSourceRange().getOffset();
                if (offset <= location && location < (offset + length)) {
                    return annotation;
                }
            }
        }
        return element;
    }
    
    /**
     * Returns the closest Java Element of the expected type that surrounds the given location in the
     * given compilationUnit. This method can return SimpleAnnotation, which the
     * default JDT ICompilationUnit implementation does not support.
     * 
     * @param sourceRange
     * @param location
     * @param type
     * @return
     * @throws JavaModelException
     */
    public static IJavaElement getElementAt(ICompilationUnit compilationUnit, int location, int type) throws JavaModelException {
        IJavaElement element = getElementAt(compilationUnit, location);
        while (element != null && element.exists()) {
            if (element.getElementType() == type) {
                return element;
            }
            element = element.getParent();
        }
        return null;
    }

    /**
     * Parse the DOM of the given member, and resolve bindings. If the given
     * member is not a type, then its declaring type is used by the parser.
     * 
     * @param member
     *            the type to parse
     * @param progressMonitor
     *            the progress monitor
     * @return compilationUnit the DOM CompilationUnit returned by the parse()
     *         method. This operation is expensive and should be performed only
     *         once for each type. Returns null if the given member was null.
     * @throws JavaModelException
     *             in case of exception underneath...
     */
    public static CompilationUnit parse(final ICompilationUnit compilationUnit, final IProgressMonitor progressMonitor)
            throws JavaModelException {
        if (compilationUnit == null || !compilationUnit.exists()) {
            return null;
        }

        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(compilationUnit);
        parser.setResolveBindings(true);
        parser.setEnvironment(null, null, null, true);
        parser.setBindingsRecovery(true);
        final CompilationUnit ast = (CompilationUnit) parser.createAST(progressMonitor);
        return ast;
    }

    /**
     * Parse the DOM of the given member, and resolve bindings. If the given
     * member is not a type, then its declaring type is used by the parser.
     * 
     * @param member
     *            the type to parse
     * @param progressMonitor
     *            the progress monitor
     * @return compilationUnit the DOM CompilationUnit returned by the parse()
     *         method. This operation is expensive and should be performed only
     *         once for each type. Returns null if the given member was null.
     * @throws JavaModelException
     *             in case of exception underneath...
     */
    public static CompilationUnit parse(final IMember member, final IProgressMonitor progressMonitor)
            throws JavaModelException {
        if (member == null) {
            return null;
        }
        return parse(member.getCompilationUnit(), progressMonitor);
    }

    /**
     * Resolves the annotation given its type.
     * 
     * @param type
     * @param ast
     * @param annotationClass
     * @return
     * @throws JavaModelException
     */
    public static Annotation resolveAnnotation(IMember member, CompilationUnit ast, String annotationName)
            throws JavaModelException {
        if (member.isBinary()) {
            IAnnotatable javaElement = (IAnnotatable) member;
            final IAnnotation javaAnnotation = javaElement.getAnnotation(annotationName);
            if (javaAnnotation != null && javaAnnotation.exists()) {
                return new Annotation(javaAnnotation, javaAnnotation.getElementName(), resolveAnnotationElements(javaAnnotation));
            }
            return null;
        }
        // when the compilation is being created, the AST may not be available
        if (ast == null) {
            return null;
        }
        // TODO : do we really need to resolve the annotation binding ?
        JavaAnnotationsVisitor visitor = new JavaAnnotationsVisitor(member, annotationName);
        ast.accept(visitor);
        return visitor.getResolvedAnnotation();
    }

    /**
     * Resolves the annotation given its type.
     * 
     * @param type
     * @param ast
     * @param annotationClass
     * @return
     * @throws JavaModelException
     */
    public static Map<String, Annotation> resolveAnnotations(IMember member, CompilationUnit ast,
            String... annotationNames) throws JavaModelException {
        return resolveAnnotations(member, ast, Arrays.asList(annotationNames));

    }

    /**
     * Resolves the annotation given its type.
     * 
     * @param type
     * @param ast
     * @param annotationClass
     * @return
     * @throws JavaModelException
     */
    public static Map<String, Annotation> resolveAnnotations(IMember member, CompilationUnit ast,
            List<String> annotationNames) throws JavaModelException {
        if (member.isBinary()) {
            IAnnotatable javaElement = (IAnnotatable) member;
            final Map<String, Annotation> annotations = new HashMap<String, Annotation>();
            for (String annotationName : annotationNames) {
                final IAnnotation javaAnnotation = javaElement.getAnnotation(annotationName);
                if (javaAnnotation.exists()) {
                    annotations.put(annotationName, new Annotation(javaAnnotation, javaAnnotation.getElementName(),
                            resolveAnnotationElements(javaAnnotation)));
                }
            }
            return annotations;
        }
        // TODO : do we really need to resolve the annotation binding ?
        JavaAnnotationsVisitor visitor = new JavaAnnotationsVisitor(member, annotationNames);
        ast.accept(visitor);
        return visitor.getResolvedAnnotations();
    }

    /**
     * Resolves the annotation given its type.
     * 
     * @param type
     * @param ast
     * @param annotationClass
     * @return
     * @throws JavaModelException
     */
    public static Annotation resolveAnnotation(final IAnnotation javaAnnotation, final CompilationUnit ast)
            throws JavaModelException {
        if (javaAnnotation.getParent() instanceof IMember) {
            return resolveAnnotation((IMember) javaAnnotation.getParent(), ast,
                    javaAnnotation.getElementName());
        }
        return null;
    }
    
    /**
     * Locates the annotation located at the given position in the compilation unit, with a hint on the search scope provided by the given eponym parameter. 
     * @param location
     * @param scope
     * @return the {@link IAnnotation} or null if the element at the given location is not an IJavaAnnotation
     * @throws JavaModelException 
     */
    public static Annotation resolveAnnotationAt(final int location, final ICompilationUnit compilationUnit) throws JavaModelException {
        final CompilationUnit ast = CompilationUnitsRepository.getInstance().getAST(compilationUnit);
        if (ast != null) {
            final IJavaElement element = compilationUnit.getElementAt(location);
            final ASTNode astChildNode = DOMUtils.getASTNodeByTypeAndLocation(ast, element.getElementType(), location);
            if (astChildNode != null) {
                final JavaAnnotationLocator annotationLocator = new JavaAnnotationLocator(element, location);
                astChildNode.accept(annotationLocator);
                return annotationLocator.getLocatedAnnotation();
            }
        }
        return null;
    }
    
    /**
     * Returns the source range for the MemberValuePair whose name is the given memberName, in the given annotation.
     * @param annotation
     * @param memberName
     * @return the sourceRange or null if it could not be evaluated.
     * @throws JavaModelException 
     */
    public static ISourceRange resolveMemberPairValueRange(final IAnnotation annotation, final String annotationQualifiedName, 
            final String memberName) throws JavaModelException {
        final IType ancestor = (IType) annotation.getAncestor(IJavaElement.TYPE);
        if(ancestor != null && ancestor.exists()) {
            final ICompilationUnit compilationUnit = ancestor.getCompilationUnit();
            final CompilationUnit ast = CompilationUnitsRepository.getInstance().getAST(compilationUnit);
            if (ast != null) {
                MemberValuePairLocationRetriever locationRetriever = new MemberValuePairLocationRetriever(annotation,
                        annotationQualifiedName, memberName);
                ast.accept(locationRetriever);
                return locationRetriever.getMemberValuePairSourceRange();
            }
        }
        return null;
    }


    private static Map<String, List<String>> resolveAnnotationElements(IAnnotation annotation)
            throws JavaModelException {
        final Map<String, List<String>> annotationElements = new HashMap<String, List<String>>();
        for (IMemberValuePair element : annotation.getMemberValuePairs()) {
            final List<String> values = new ArrayList<String>();
            if (element.getValue() instanceof Collection<?>) {
                for (Object v : (Collection<?>) element.getValue()) {
                    values.add(v.toString());
                }
            } else {
                values.add(element.getValue().toString());
            }
            annotationElements.put(element.getMemberName(), values);
        }
        return annotationElements;
    }

    /**
     * Return the first IType that matches the QualifiedName in the javaProject
     * (anyway, there shouldn't be more than one, unless there are duplicate
     * jars in the classpath, should it ?).
     * 
     * @param qName
     *            the fully qualified name of the searched type
     * @param javaProject
     *            the java project in which the type should be resolved
     * @param progressMonitor
     *            a progress monitor (or null)
     * @return the first IType found
     * @throws CoreException
     *             the underlying CoreException thrown by the manipulated JDT
     *             APIs
     */
    public static IType resolveType(final String qName, final IJavaProject javaProject,
            final IProgressMonitor progressMonitor) throws CoreException {
        if (qName == null) {
            return null;
        }
        IType findType = javaProject.findType(qName, progressMonitor);
        
        return findType;
    }

    /**
     * Returns the hierarchy for the given type, or null if it could not be 'computed'.
     * 
     * @param baseType
     *            the base type for the hierarchy
     * @param includeLibraries
     *            should the hierarchy include type from libraries
     * @param progressMonitor
     *            a progress monitor (or null)
     * @return the Type Hierarchy for the base type
     * @throws CoreException
     *             the underlying CoreException thrown by the manipulated JDT
     *             APIs
     */
    public static ITypeHierarchy resolveTypeHierarchy(final IType baseType, final IJavaElement scope, final boolean includeLibraries,
            final IProgressMonitor progressMonitor) throws CoreException {
        // create type hierarchy
        // FIXME : restrict operation scope to sources only, exclude application
        // libraries.
        int appLibs = 0;
        if (includeLibraries) {
            appLibs = IJavaSearchScope.APPLICATION_LIBRARIES;
        }
        IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(
                new IJavaElement[] { scope }, IJavaSearchScope.SOURCES | appLibs
                        | IJavaSearchScope.REFERENCED_PROJECTS);
        CreateTypeHierarchyOperation operation = new CreateTypeHierarchyOperation(baseType, null, searchScope, true);
        ITypeHierarchy hierarchy = operation.getResult();
        if (hierarchy != null && hierarchy.exists()) {
            hierarchy.refresh(progressMonitor);
            return hierarchy;
        }
         
        return null;
    }

    /**
     * Resolves the Type Argument for the given parameterizedType against the
     * given matchGenericType that is part of the parameterizedTypeHierarchy.
     * Binding information is obtained from the Java model. This means that the
     * compilation unit must be located relative to the Java model. This happens
     * automatically when the source code comes from either
     * setSource(ICompilationUnit) or setSource(IClassFile). When source is
     * supplied by setSource(char[]), the location must be established
     * explicitly by calling setProject(IJavaProject) and setUnitName(String).
     * Note that the compiler options that affect doc comment checking may also
     * affect whether any bindings are resolved for nodes within doc comments.
     * 
     * Note : the binding resolution on IClassFile requires the
     * 'org.eclipse.jdt.launching' bundle, but Eclipse PDE detects it as an
     * unused dependency.
     * 
     * @param parameterizedType
     *            the parameterized type
     * @param compilationUnit
     *            the DOM CompilationUnit returned by the parse() method. This
     *            operation is expensive and should be performed only once for
     *            each type.
     * @param matchGenericType
     *            the super type
     * @param parameterizedTypeHierarchy
     *            the parameterized type hierarchy
     * @param progressMonitor
     *            a progress monitor (or null)
     * @return a list of fully qualified type names
     * @throws CoreException
     *             the underlying CoreException thrown by the manipulated JDT
     *             APIs
     */
    @SuppressWarnings("unchecked")
    public static List<IType> resolveTypeArguments(final IType parameterizedType,
            final CompilationUnit compilationUnit, final IType matchGenericType,
            final ITypeHierarchy parameterizedTypeHierarchy, final IProgressMonitor progressMonitor)
            throws CoreException {
        if (compilationUnit == null) {
            
            return null;
        }
        // find path to the matchGenericType (class or interface)
        // ITypeHierarchy parameterizedTypeHierarchy =
        // getTypeHierarchy(parameterizedType, false, progressMonitor);
        List<IType> pathToParameterizedType = new ArrayList<IType>(Arrays.asList(parameterizedTypeHierarchy
                .getAllSubtypes(matchGenericType)));
        // skip the last values as they are the parameterized type and its
        // optionally sub types
        int index = pathToParameterizedType.indexOf(parameterizedType);
        // the generic type does not belong to the parameterized type's
        // hierarchy
        if (index < 0) {
            return null;
        }
        pathToParameterizedType = pathToParameterizedType.subList(0, index);
        // add match/target generic type, as by default it is not included
        // in
        // the result
        pathToParameterizedType.add(0, matchGenericType);
        // reverse the path, for easier comprehension of the code below
        Collections.reverse(pathToParameterizedType);
        List<IType> arguments = null;
        for (TypeDeclaration typeDeclaration : (List<TypeDeclaration>) compilationUnit.types()) {
            // ohoh, everything is resolved with bindings :-)
            ITypeBinding typeBinding = typeDeclaration.resolveBinding();
            if (typeBinding.getJavaElement().equals(parameterizedType)) {
                // locate the matchGenericType declaration...
                for (int i = 0; i < pathToParameterizedType.size(); i++) {
                    IType superType = pathToParameterizedType.get(i);
                    // lookup in the type's interfaces
                    if (superType.isInterface()) {
                        for (ITypeBinding superInterfaceBinding : typeBinding.getInterfaces()) {
                            String superInterfaceErasureQName = superInterfaceBinding.getErasure().getQualifiedName();
                            if (superInterfaceErasureQName.equals(superType.getFullyQualifiedName())) {
                                typeBinding = superInterfaceBinding;
                                break;
                            }
                        }
                    } else {
                        // lookup in type's super class
                        typeBinding = typeBinding.getSuperclass();
                    }
                }
                // ... then resolve the type parameters using its bindings
                // resolve in the parameterized type's interfaces
                ITypeBinding[] typeArgBindings = typeBinding.getTypeArguments();
                arguments = new ArrayList<IType>(typeArgBindings.length);
                for (ITypeBinding typeArgBinding : typeArgBindings) {
                     
                    IJavaElement javaElement = typeArgBinding.getJavaElement();
                    if (javaElement.getElementType() == IJavaElement.TYPE && javaElement.exists()) {
                        arguments.add((IType) javaElement);
                    }
                }
                // FIXME : path for a sample result with the help of
                // bindings
                // superClassBinding.getSuperclass().getInterfaces()[0].getInterfaces()[0].getTypeArguments()[0].qualifiedName;
            }
        }

        return arguments;
    }

    public static List<JavaMethodSignature> resolveMethodSignatures(IType type, CompilationUnit ast) {
        JavaMethodSignaturesVisitor methodsVisitor = new JavaMethodSignaturesVisitor(type.getCompilationUnit());
        ast.accept(methodsVisitor);
        return methodsVisitor.getMethodSignatures();
    }

    /**
     * Returns the method signature for the given method with the given AST.
     * @param method the java method 
     * @param ast the associated Compilation Unit AST
     * @return the JavaMethodSignature or null if the given AST is null.
     */
    public static JavaMethodSignature resolveMethodSignature(IMethod method, CompilationUnit ast) {
        if(ast == null) {
            return null;
        }
        JavaMethodSignaturesVisitor methodsVisitor = new JavaMethodSignaturesVisitor(method);
        ast.accept(methodsVisitor);
        return methodsVisitor.getMethodSignature();
    }

    /**
     * Return true if the given superType parameter is actually a super type of
     * the given subType parameter, ie, the superType belongs to the supertypes
     * in the subtype's hierarchy.
     * 
     * @param superType
     *            the suspected super type
     * @param subType
     *            the suspected sub type
     * @return true or false
     * @throws CoreException
     */
    public static boolean isTypeOrSuperType(IType superType, IType subType) throws CoreException {
        if (subType == null || superType == null) {
            return false;
        }
        if (superType.getHandleIdentifier().equals(subType.getHandleIdentifier())) {
            return true;
        }
        final ITypeHierarchy hierarchy = JdtUtils.resolveTypeHierarchy(subType, subType.getJavaProject(), true, new NullProgressMonitor());
        final List<IType> allSuperclasses = Arrays.asList(hierarchy.getAllSuperclasses(subType));
        for (IType type : allSuperclasses) {
            if (type.getHandleIdentifier().equals(superType.getHandleIdentifier())) {
                return true;
            }
        }
        return false;

    }


}