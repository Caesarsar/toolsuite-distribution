/******************************************************************************************
 * Copyright (c) 2008 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.ide.metadata.core;

import java.util.Set;

import org.springframework.ide.eclipse.beans.core.metadata.model.AbstractAnnotationMetadata;
import org.springframework.ide.eclipse.beans.core.metadata.model.IBeanMetadata;
import org.springframework.ide.eclipse.beans.core.metadata.model.IMethodMetadata;
import org.springframework.ide.eclipse.beans.core.model.IBean;
import org.springframework.ide.eclipse.core.java.annotation.AnnotationMemberValuePair;
import org.springframework.ide.eclipse.core.model.IModelSourceLocation;

/**
 * {@link IBeanMetadata} for the RequestMapping annotation.
 * @author Christian Dupuis
 * @author Leo Dos Santos
 * @since 1.0.0
 */
public class RequestMappingAnnotationMetadata extends AbstractAnnotationMetadata {

	private static final long serialVersionUID = 6978657032146327628L;
	
	private String classHandle = null;
	
	public RequestMappingAnnotationMetadata(IBean bean, String handle, Object value,
			IModelSourceLocation location, Set<IMethodMetadata> methodMetaData, String classHandle) {
		super(bean, handle, value, location, methodMetaData);
		this.classHandle = classHandle;
	}
	
	@Override
	public String getClassHandle() {
		return classHandle;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String getValueAsText() {
		if (getValue() instanceof Set) {
			StringBuilder buf = new StringBuilder();
			for (AnnotationMemberValuePair pair : (Set<AnnotationMemberValuePair>) getValue()) {
				if (pair.getName() != null) {
					buf.append(pair.getName());
					buf.append(" = "); //$NON-NLS-1$
				}
				buf.append(pair.getValue().toString());
				buf.append(", "); //$NON-NLS-1$
			}

			if (buf.length() > 0) {
				return buf.substring(0, buf.length() - 2) + " -> "; //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}
	
}
