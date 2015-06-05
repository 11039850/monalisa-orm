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

import org.eclipse.jdt.core.ISourceRange;

/**
 * @author Xavier Coulon
 * 
 */
public class RangeUtils {

    /**
     * Private constructor for this utility class
     */
    public RangeUtils() {
    }

    /**
     * Returns true if the source range matches the given position, false otherwise
     * @param sourceRange
     * @param position
     * @return
     */
    public static boolean matches(final ISourceRange range, final int position) {
        return range.getOffset() <= position && position <= (range.getOffset() + range.getLength());
    }
    
}
