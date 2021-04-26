package com.liferay.custom.form.listener;

import com.liferay.custom.form.listener.service.CustomListenerService;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = ModelListener.class)
public class DDMFormInstanceRecordModelListener extends BaseModelListener<DDMFormInstanceRecord> {

	private static final Log _log = LogFactoryUtil.getLog(DDMFormInstanceRecordModelListener.class);

	@Reference
	private CustomListenerService customFormService;

	@Override
	public void onAfterCreate(DDMFormInstanceRecord model) throws ModelListenerException {
		_log.debug("After create form");

		TransactionCommitCallbackUtil.registerCallback(() -> {
			_log.debug("Registered callback");
			customFormService.serviceForm(model);
			return null;
		});
		_log.debug("After register callback");
	}

	@Override
	public void onAfterUpdate(DDMFormInstanceRecord model) throws ModelListenerException {
		_log.debug("After update form without servicing");
		super.onAfterUpdate(model);
	}

	@Override
	public void onAfterRemove(DDMFormInstanceRecord model) throws ModelListenerException {
		_log.debug("After remove form without servicing");
		super.onAfterRemove(model);
	}
	
	@Override
	public void onBeforeCreate(DDMFormInstanceRecord model) throws ModelListenerException {
		// _log.debug("Before create");
		super.onBeforeCreate(model);
	}
	
	@Override
	public void onBeforeUpdate(DDMFormInstanceRecord model) throws ModelListenerException {
		// _log.debug("Before update");
		super.onBeforeUpdate(model);
	}

}