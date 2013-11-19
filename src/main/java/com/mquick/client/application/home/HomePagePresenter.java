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
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.mquick.client.application.ApplicationPresenter;
import com.mquick.client.place.NameTokens;
import com.mquick.shared.dispatch.ListWebAppsAction;
import com.mquick.shared.dispatch.ListWebAppsResult;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, 
	HomePagePresenter.MyProxy> implements HomeUiHandlers{
    public interface MyView extends View,HasUiHandlers<HomeUiHandlers> {
    	void showDashboardStatus(String status);
    }

    private final DispatchAsync dispatcher;
    
    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    @Inject
    public HomePagePresenter(EventBus eventBus,MyView view, MyProxy proxy, DispatchAsync dispatcher) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
        this.dispatcher = dispatcher;
        
        getView().setUiHandlers(this);
        
        getEventBus().addHandler(DashboardEvent.type, new DashboardEvent.DashboardEventHandler() {
			
			@Override
			public void onMessage(String message) {
				
			}
			
			@Override
			public void onDie() {
				getView().showDashboardStatus("Offline");
			}
			
			@Override
			public void onAlive() {
				getView().showDashboardStatus("Online");
			}
		});
    }

	@Override
	public void Beep() {
		
		loadExpressPortals();
		
/*		dispatcher.execute(new ListWebAppsAction(), new AsyncCallback<ListWebAppsResult>(){

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Oops, List web apps failed");
			}

			@Override
			public void onSuccess(ListWebAppsResult result) {
				GWT.log("Yes, it success");
			}});
*/	}

	void loadExpressPortals() {
		String url = "/list/";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

		try {
		  builder.sendRequest(null, new RequestCallback() {
		    public void onError(Request request, Throwable exception) {
		        GWT.log("list Express Portals Error.");
		    }

			@Override
		    public void onResponseReceived(Request request, Response response) {
		      if (200 == response.getStatusCode()) {
			        GWT.log("list Express Portals.");
		          GWT.log(response.getText());
		      } else {
		        GWT.log("list Express Portals Failed.");
		      }
		    }

		  });
		  
		} catch (RequestException e) {
		  // Couldn't connect to server
		}		
	}
	
}
