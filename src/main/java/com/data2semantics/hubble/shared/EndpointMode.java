package com.data2semantics.hubble.shared;

import java.io.Serializable;

public class EndpointMode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static String DEFAULT_ENDPOINT = "default";
	public static String LOCAL_REPLICA = "replica";
	public static String PROXY = "proxy";
}
