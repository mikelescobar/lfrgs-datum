import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import com.liferay.custom.form.service.CustomFormServiceUtil;

String className = (String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME);
long classPK = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));

returnValue = CustomFormServiceUtil.isSignedWorkflowAction(className, classPK, "approve", "incomplete");