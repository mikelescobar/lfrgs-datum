/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.workflow.web.custom.portlet.tab;

import com.liferay.portal.workflow.portlet.tab.WorkflowPortletTab;
import com.liferay.portal.workflow.web.custom.constants.CustomWebKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Adam Brandizzi
 */
@Component(
	immediate = true,
	property = "portal.workflow.tabs.name=" + CustomWebKeys.CUSTOM_TAB_MY_SUBMISSIONS,
	service = WorkflowPortletTab.class
)
public class MyWorkflowInstancePortletTab extends WorkflowInstancePortletTab {

	@Override
	public String getName() {
		return CustomWebKeys.CUSTOM_TAB_MY_SUBMISSIONS;
	}

}