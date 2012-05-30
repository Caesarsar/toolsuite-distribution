/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.core.schemas;

/**
 * Integration HTTP Invoker adapter schema derived from
 * <code>http://www.springframework.org/schema/integration/httpinvoker/spring-integration-httpinvoker-2.0.xsd</code>
 * @author Leo Dos Santos
 * @since STS 2.5.0
 * @version Spring Integration 2.0
 */
public class IntHttpInvokerSchemaConstants {

	// URI

	public static String URI = "http://www.springframework.org/schema/integration/httpinvoker"; //$NON-NLS-1$

	// Element tags

	public static String ELEM_INBOUND_GATEWAY = "inbound-gateway"; //$NON-NLS-1$

	public static String ELEM_OUTBOUND_GATEWAY = "outbound-gateway"; //$NON-NLS-1$

	// Attribute tags

	public static String ATTR_AUTO_STARTUP = "auto-startup"; //$NON-NLS-1$

	public static String ATTR_EXPECTED_REPLY = "expected-reply"; //$NON-NLS-1$

	public static String ATTR_ID = "id"; //$NON-NLS-1$

	public static String ATTR_NAME = "name"; //$NON-NLS-1$

	public static String ATTR_REPLY_CHANNEL = "reply-channel"; //$NON-NLS-1$

	public static String ATTR_REPLY_TIMEOUT = "reply-timeout"; //$NON-NLS-1$

	public static String ATTR_REQUEST_CHANNEL = "request-channel"; //$NON-NLS-1$

	public static String ATTR_REQUEST_TIMEOUT = "request-timeout"; //$NON-NLS-1$

	public static String ATTR_URL = "url"; //$NON-NLS-1$

}
