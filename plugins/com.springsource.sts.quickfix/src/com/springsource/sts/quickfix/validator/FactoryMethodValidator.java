/******************************************************************************************
 * Copyright (c) 2008 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.quickfix.validator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.ide.eclipse.beans.core.internal.model.BeansModelUtils;
import org.springframework.ide.eclipse.beans.core.internal.model.validation.rules.BeanFactoryRule;
import org.springframework.ide.eclipse.beans.core.model.IBeansConfig;
import org.springframework.ide.eclipse.core.internal.model.validation.ValidationRuleDefinition;
import org.springframework.ide.eclipse.core.model.IResourceModelElement;
import org.springframework.ide.eclipse.core.model.validation.ValidationProblemAttribute;

import com.springsource.sts.config.core.schemas.BeansSchemaConstants;
import com.springsource.sts.quickfix.BeansEditorValidator;
import com.springsource.sts.quickfix.processors.QuickfixProcessorFactory;
import com.springsource.sts.quickfix.validator.helper.BeanHelper;
import com.springsource.sts.quickfix.validator.helper.BeansValidationContextHelper;

/**
 * Validates factory method attribute of a bean configuration.
 * @author Terry Denney
 * @author Leo Dos Santos
 * @author Christian Dupuis
 * @since 2.0
 */
public class FactoryMethodValidator extends BeanValidator {

	@Override
	public boolean validateAttributeWithConfig(IBeansConfig config, IResourceModelElement contextElement, IFile file,
			AttrImpl attribute, IDOMNode parent, IReporter reporter, boolean reportError,
			BeansEditorValidator validator, String text) {
		IProject project = file.getProject();

		ValidationRuleDefinition ruleDefinition = getValidationRule(project, BeanFactoryRule.class);
		BeanFactoryRule rule = (BeanFactoryRule) (ruleDefinition != null ? ruleDefinition.getRule() : null);
		BeanHelper parentBean = new BeanHelper(parent, file, project);
		parentBean.getBeanDefinition().setFactoryMethodName(text);

		AttrImpl factoryBeanAttr = (AttrImpl) parent.getAttributes().getNamedItem(
				BeansSchemaConstants.ATTR_FACTORY_BEAN);
		if (factoryBeanAttr != null) {
			parentBean.getBeanDefinition().setFactoryBeanName(factoryBeanAttr.getNodeValue());
		}

		BeansValidationContextHelper context = new BeansValidationContextHelper(attribute, parent, contextElement,
				project, reporter, validator, QuickfixProcessorFactory.FACTORY_METHOD, false, reportError, config);

		ITextRegion valueRegion = attribute.getValueRegion();

		AbstractBeanDefinition mergedBd = (AbstractBeanDefinition) BeansModelUtils.getMergedBeanDefinition(parentBean,
				context.getContextElement());

		// For non-factory beans validate it's init-method and destroy-method
		String mergedClassName = mergedBd.getBeanClassName();
		if (valueRegion != null && mergedClassName != null) {
			// add rename refactoring option
			validator.createAndAddEmptyMessage(valueRegion, parent, "", reporter,
					QuickfixProcessorFactory.RENAME_METHOD, null, new ValidationProblemAttribute("CLASS",
							mergedClassName));
		}

		if (rule != null) {
			context.setCurrentRuleDefinition(ruleDefinition);
			rule.validate(parentBean, context, null);

			if (context.getErrorFound()) {
				return true;
			}
		}

		return false;
	}
}
