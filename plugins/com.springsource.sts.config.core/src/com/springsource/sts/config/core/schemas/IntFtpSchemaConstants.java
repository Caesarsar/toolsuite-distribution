/******************************************************************************************
 * Copyright (c) 2010 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.core.schemas;

/**
 * Integration FTP adapter schema derived from
 * <code>http://www.springframework.org/schema/integration/ftp/spring-integration-ftp-2.0.xsd</code>
 * @author Leo Dos Santos
 * @since STS 2.5.0
 * @version Spring Integration 2.0
 */
public class IntFtpSchemaConstants {

	// URI

	public static String URI = "http://www.springframework.org/schema/integration/ftp"; //$NON-NLS-1$

	// Element tags

	public static String ELEM_INBOUND_CHANNEL_ADAPTER = "inbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_OUTBOUND_CHANNEL_ADAPTER = "outbound-channel-adapter"; //$NON-NLS-1$

	public static String ELEM_OUTBOUND_GATEWAY = "outbound-gateway"; //$NON-NLS-1$

	// Attribute tags

	public static String ATTR_AUTO_CREATE_DIRECTORIES = "auto-create-directories"; //$NON-NLS-1$

	public static String ATTR_AUTO_CREATE_LOCAL_DIRECTORY = "auto-create-local-directory"; //$NON-NLS-1$

	public static String ATTR_AUTO_DELETE_REMOTE_FILES_ON_SYNC = "auto-delete-remote-files-on-sync"; //$NON-NLS-1$

	public static String ATTR_CHANNEL = "channel"; //$NON-NLS-1$

	public static String ATTR_CLIENT_MODE = "client-mode"; //$NON-NLS-1$

	public static String ATTR_COMMAND = "command"; //$NON-NLS-1$

	public static String ATTR_COMMAND_OPTIONS = "command-options"; //$NON-NLS-1$

	public static String ATTR_EXPRESSION = "expression"; //$NON-NLS-1$

	public static String ATTR_FILE_TYPE = "file-type"; //$NON-NLS-1$

	public static String ATTR_FILENAME_PATTERN = "filename-pattern"; //$NON-NLS-1$

	public static String ATTR_FILENAME_REGEX = "filename-regex"; //$NON-NLS-1$

	public static String ATTR_FILTER = "filter"; //$NON-NLS-1$

	public static String ATTR_HOST = "host"; //$NON-NLS-1$

	public static String ATTR_ID = "id"; //$NON-NLS-1$

	public static String ATTR_LOCAL_DIRECTORY = "local-directory"; //$NON-NLS-1$

	public static String ATTR_LOCAL_WORKING_DIRECTORY = "local-working-directory"; //$NON-NLS-1$

	public static String ATTR_ORDER = "order"; //$NON-NLS-1$

	public static String ATTR_PASSWORD = "password"; //$NON-NLS-1$

	public static String ATTR_PORT = "port"; //$NON-NLS-1$

	public static String ATTR_REMOTE_DIRECTORY = "remote-directory"; //$NON-NLS-1$

	public static String ATTR_REPLY_CHANNEL = "reply-channel"; //$NON-NLS-1$

	public static String ATTR_REQUEST_CHANNEL = "request-channel"; //$NON-NLS-1$

	public static String ATTR_USERNAME = "username"; //$NON-NLS-1$

}
