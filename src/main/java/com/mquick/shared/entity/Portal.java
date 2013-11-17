package com.mquick.shared.entity;

import java.net.URL;

public class Portal {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public URL getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(URL portalUrl) {
		this.portalUrl = portalUrl;
	}
	private int id;
	private URL	portalUrl;
}
