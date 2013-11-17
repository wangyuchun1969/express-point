package com.mquick.server.dispatch;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import com.mquick.shared.dispatch.ListWebAppsAction;
import com.mquick.shared.dispatch.ListWebAppsResult;

public class ListWebAppsServerHandler implements ActionHandler<ListWebAppsAction, ListWebAppsResult>{

	@Override
	public ListWebAppsResult execute(ListWebAppsAction action,
			ExecutionContext executioncontext) throws ActionException {
		System.out.println("Do execute");
		// TODO: List All webapps.
		return new ListWebAppsResult();
	}

	@Override
	public Class<ListWebAppsAction> getActionType() {
		return ListWebAppsAction.class;
	}

	@Override
	public void undo(ListWebAppsAction arg0, ListWebAppsResult arg1,
			ExecutionContext arg2) throws ActionException {
		// Not undoable
	}

}
