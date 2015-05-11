package com.statscollector.application.dao;

import com.statscollector.application.config.AbstractWebConfig;

public abstract class AbstractWebDao {

	private static final String PORT_SEPERATOR = ":";

	protected abstract AbstractWebConfig getConfig();
	
	protected String getTargetHost() {
		if(null == getConfig().getHostPort()){
			return getConfig().getHost();
		}else{
			return getConfig().getHost()+PORT_SEPERATOR+getConfig().getHostPort();
		}
	}
}
