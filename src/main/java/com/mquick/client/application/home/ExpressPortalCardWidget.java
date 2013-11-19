package com.mquick.client.application.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExpressPortalCardWidget extends Composite {
	private final CardsResources cardresources = GWT.create(CardsResources.class);

	public interface CardsResources extends ClientBundle {
    	@Source("cards.css")
    	CardsStyle css();
    	
    	interface CardsStyle extends CssResource {
    		public String UsersCards();
    		public String UsersCardsItem();
    	}
	
	}

	VerticalPanel vPanel = new VerticalPanel();
	private final AbsolutePanel cardPanel = new AbsolutePanel();
	
	public ExpressPortalCardWidget(String html) {
		cardresources.css().ensureInjected();
		cardPanel.setStyleName(cardresources.css().UsersCards());
		initWidget(cardPanel);
		cardPanel.add(vPanel);
		vPanel.setStyleName(cardresources.css().UsersCardsItem());
		vPanel.add(new HTMLPanel(html));
	}

}
