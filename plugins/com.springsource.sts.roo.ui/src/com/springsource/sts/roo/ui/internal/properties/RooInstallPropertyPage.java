/******************************************************************************************
 * Copyright (c) 2009 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.roo.ui.internal.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.springframework.ide.eclipse.core.SpringCorePreferences;
import org.springframework.ide.eclipse.ui.dialogs.ProjectAndPreferencePage;

import com.springsource.sts.roo.core.RooCoreActivator;

/**
 * @author Christian Dupuis
 * @since 2.2.0
 */
public class RooInstallPropertyPage extends ProjectAndPreferencePage {

	public static final String PREF_ID = "com.springsource.sts.roo.ui.preferencePage"; //$NON-NLS-1$

	public static final String PROP_ID = "com.springsource.sts.roo.ui.projectPropertyPage"; //$NON-NLS-1$

	private Combo rooInstallCombo;

	public RooInstallPropertyPage() {
		noDefaultAndApplyButton();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean performOk() {

		if (useProjectSettings()) {
			SpringCorePreferences.getProjectPreferences(getProject(), RooCoreActivator.PLUGIN_ID).putBoolean(
					RooCoreActivator.PROJECT_PROPERTY_ID, false);
			SpringCorePreferences.getProjectPreferences(getProject(), RooCoreActivator.PLUGIN_ID).putString(
					RooCoreActivator.ROO_INSTALL_PROPERTY, rooInstallCombo.getText());
		}
		else {
			SpringCorePreferences.getProjectPreferences(getProject(), RooCoreActivator.PLUGIN_ID).putBoolean(
					RooCoreActivator.PROJECT_PROPERTY_ID, true);
		}

		RooCoreActivator.getDefault().savePluginPreferences();

		// always say it is ok
		return super.performOk();
	}

	@Override
	protected Control createPreferenceContent(Composite composite) {
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);

		Label notes = new Label(composite, SWT.WRAP);
		notes
				.setText("If no project specific Roo installation is selected, the workspace default installation will be used. ");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		notes.setLayoutData(gd);

		// Label spacer = new Label(composite, SWT.NONE);
		// spacer.setLayoutData(gd);

		Label options = new Label(composite, SWT.WRAP);
		options.setText("Roo Installation: ");
		options.setLayoutData(new GridData(GridData.BEGINNING));

		rooInstallCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		rooInstallCombo.setItems(RooCoreActivator.getDefault().getInstallManager().getAllInstallNames());
		rooInstallCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		String installName = SpringCorePreferences.getProjectPreferences(getProject(), RooCoreActivator.PLUGIN_ID)
				.getString(RooCoreActivator.ROO_INSTALL_PROPERTY, null);
		String[] names = rooInstallCombo.getItems();
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(installName)) {
				rooInstallCombo.select(i);
				break;
			}
		}

		Dialog.applyDialogFont(composite);

		return composite;
	}

	@Override
	protected String getPreferencePageID() {
		return PREF_ID;
	}

	@Override
	protected String getPropertyPageID() {
		return PROP_ID;
	}

	@Override
	protected boolean hasProjectSpecificOptions(IProject project) {
		return !SpringCorePreferences.getProjectPreferences(project, RooCoreActivator.PLUGIN_ID).getBoolean(
				RooCoreActivator.PROJECT_PROPERTY_ID, false);
	}
}
