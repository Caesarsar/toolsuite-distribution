/******************************************************************************************
 * Copyright (c) 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.core.schemas;

/**
 * Integration Gemfire adapter schema derived from
 * <code>http://www.springframework.org/schema/integration/gemfire/spring-integration-gemfire-2.1.xsd</code>
 * @author Leo Dos Santos
 * @since STS 2.9.0
 * @version Spring Integration 2.1
 */
public class IntGemfireSchemaConstants {

	// URI

	public static String URI = "http://www.springframework.org/schema/integration/gemfire"; //$NON-NLS-1$

	// Element tags

	public static String ELEM_CACHE_ENTRIES = "cache-entries"; //$NON-NLS-1$

	public static String ELEM_CQ_INBOUND_CHANNEL_ADAPTER = "cq-inbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_INBOUND_CHANNEL_ADAPTER = "inbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_OUTBOUND_CHANNEL_ADAPTER = "outbound-channel-adapter"; //$NON-NLS-1$

	// Attribute tags

	public static String ATTR_CACHE_EVENTS = "cache-events"; //$NON-NLS-1$

	public static String ATTR_CHANNEL = "channel"; //$NON-NLS-1$

	public static String ATTR_CQ_LISTENER_CONTAINER = "cq-listener-container"; //$NON-NLS-1$

	public static String ATTR_DURABLE = "durable"; //$NON-NLS-1$

	public static String ATTR_ERROR_CHANNEL = "error-channel"; //$NON-NLS-1$

	public static String ATTR_EXPRESSION = "expression"; //$NON-NLS-1$

	public static String ATTR_ID = "id"; //$NON-NLS-1$

	public static String ATTR_ORDER = "order"; //$NON-NLS-1$

	public static String ATTR_QUERY = "query"; //$NON-NLS-1$

	public static String ATTR_QUERY_EVENTS = "query-events"; //$NON-NLS-1$

	public static String ATTR_QUERY_NAME = "query-name"; //$NON-NLS-1$

	public static String ATTR_REGION = "region"; //$NON-NLS-1$

}
