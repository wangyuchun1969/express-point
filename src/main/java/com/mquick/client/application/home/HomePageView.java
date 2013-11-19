/**
 * Copyright 2012 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mquick.client.application.home;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class HomePageView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePagePresenter.MyView {
    public interface Binder extends UiBinder<Widget, HomePageView> {
    }

    @UiHandler("beep")
    void onHomeClicked(ClickEvent event) {
    	getUiHandlers().Beep();
    }
    
    
    @Inject
    public HomePageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

	@UiField
    HTMLPanel dashboardstatus;

	@Override
	public void showDashboardStatus(String status) {
		dashboardstatus.getElement().setInnerHTML(status);
	}


	@Override
	public void showExpressPortal(String path) {
		GWT.log("Portal: " + path);
		flows.add(new ExpressPortalCardWidget(path));
	}

	@UiField
	FlowPanel flows;
}
