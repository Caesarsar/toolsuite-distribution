/******************************************************************************************
 * Copyright (c) 2010 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.core.schemas;

/**
 * Integration Twitter adapter schema derived from
 * <code>http://www.springframework.org/schema/integration/twitter/spring-integration-twitter-2.0.xsd</code>
 * @author Leo Dos Santos
 * @since STS 2.5.0
 * @version Spring Integration 2.0
 */
public class IntTwitterSchemaConstants {

	// URI

	public static String URI = "http://www.springframework.org/schema/integration/twitter"; //$NON-NLS-1$

	// Element tags

	public static String ELEM_DM_INBOUND_CHANNEL_ADAPTER = "dm-inbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_DM_OUTBOUND_CHANNEL_ADAPTER = "dm-outbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_INBOUND_CHANNEL_ADAPTER = "inbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_MENTIONS_INBOUND_CHANNEL_ADAPTER = "mentions-inbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_OUTBOUND_CHANNEL_ADAPTER = "outbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_SEARCH_INBOUND_CHANNEL_ADAPTER = "search-inbound-channel-adapter"; //$NON-NLS-1$

	// Attribute tags

	public static String ATTR_AUTO_STARTUP = "auto-startup"; //$NON-NLS-1$

	public static String ATTR_CHANNEL = "channel"; //$NON-NLS-1$

	public static String ATTR_ID = "id"; //$NON-NLS-1$

	public static String ATTR_QUERY = "query"; //$NON-NLS-1$

	public static String ATTR_ORDER = "order"; //$NON-NLS-1$

	public static String ATTR_TWITTER_TEMPLATE = "twitter-template"; //$NON-NLS-1$

}
