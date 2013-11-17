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

package com.mquick.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.mquick.server.dispatch.ListWebAppsServerHandler;
import com.mquick.server.websocket.DeployWebsocket;
import com.mquick.shared.dispatch.ListWebAppsAction;

public class ServerModule extends HandlerModule {
    @Override
    protected void configureHandlers() {
    	
    	bindHandler(ListWebAppsAction.class, ListWebAppsServerHandler.class);
    	
    	try {
			new DeployWebsocket();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
}
