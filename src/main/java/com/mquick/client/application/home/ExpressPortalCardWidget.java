package com.mquick.client.application.home;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ExpressPortalCardWidget extends HTML {

	public ExpressPortalCardWidget(String html) {
		super(html);
		this.setWidth("30px");
		this.setHeight("40px");
		this.setVisible(true);
		this.getElement().getStyle().setBackgroundColor("red");
	}

}
