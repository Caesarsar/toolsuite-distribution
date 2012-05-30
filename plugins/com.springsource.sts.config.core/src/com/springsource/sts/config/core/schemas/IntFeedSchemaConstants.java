/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.core.schemas;

/**
 * Integration Feed adapter schema derived from
 * <code>http://www.springframework.org/schema/integration/feed/spring-integration-feed-2.0.xsd</code>
 * @author Leo Dos Santos
 * @since STS 2.5.0
 * @version Spring Integration 2.0
 */
public class IntFeedSchemaConstants {

	// URI

	public static String URI = "http://www.springframework.org/schema/integration/feed"; //$NON-NLS-1$

	// Element tags

	public static String ELEM_INBOUND_CHANNEL_ADAPTER = "inbound-channel-adapter"; //$NON-NLS-1$

	// Attribute tags

	public static String ATTR_AUTO_STARTUP = "auto-startup"; //$NON-NLS-1$

	public static String ATTR_CHANNEL = "channel"; //$NON-NLS-1$

	public static String ATTR_FEED_FETCHER = "feed-fetcher"; //$NON-NLS-1$

	public static String ATTR_ID = "id"; //$NON-NLS-1$

	public static String ATTR_METADATA_STORE = "metadata-store"; //$NON-NLS-1$

	public static String ATTR_URL = "url"; //$NON-NLS-1$

}
