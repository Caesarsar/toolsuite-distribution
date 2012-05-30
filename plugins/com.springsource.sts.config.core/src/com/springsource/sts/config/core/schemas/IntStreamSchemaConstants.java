/******************************************************************************************
 * Copyright (c) 2010 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.core.schemas;

/**
 * Integration Stream adapter schema derived from
 * <code>http://www.springframework.org/schema/integration/stream/spring-integration-stream-2.0.xsd</code>
 * @author Leo Dos Santos
 * @since STS 2.5.0
 * @version Spring Integration 2.0
 */
public class IntStreamSchemaConstants {

	// URI

	public static String URI = "http://www.springframework.org/schema/integration/stream"; //$NON-NLS-1$

	// Element tags

	public static String ELEM_STDERR_CHANNEL_ADAPTER = "stderr-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_STDIN_CHANNEL_ADAPTER = "stdin-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_STDOUT_CHANNEL_ADAPTER = "stdout-channel-adapter"; //$NON-NLS-1$

	// Attribute tags

	public static String ATTR_APPEND_NEWLINE = "append-newline"; //$NON-NLS-1$

	public static String ATTR_AUTO_STARTUP = "auto-startup"; //$NON-NLS-1$

	public static String ATTR_CHANNEL = "channel"; //$NON-NLS-1$

	public static String ATTR_CHARSET = "charset"; //$NON-NLS-1$

	public static String ATTR_ID = "id"; //$NON-NLS-1$

	public static String ATTR_ORDER = "order"; //$NON-NLS-1$

}
