/******************************************************************************************
 * Copyright (c) 2010 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.quickfix;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author Terry Denney
 * @since 2.6
 */
public class QuickfixImages {

	private static final URL baseURL = Activator.getDefault().getBundle().getEntry("/icons/");

	private final static String OBJ = "obj16/";

	public static final ImageDescriptor ANNOTATION = create(OBJ, "annotation_obj.gif");

	public static final ImageDescriptor LOCAL_VARIABLE = create(OBJ, "localvariable_obj.gif");

	public static final ImageDescriptor REMOVE_CORRECTION = create(OBJ, "remove_correction.gif");

	public static final ImageDescriptor CLASS = create(OBJ, "class_obj.gif");

	private static ImageRegistry imageRegistry;

	public static ImageDescriptor create(String prefix, String name) {
		try {
			return ImageDescriptor.createFromURL(makeIconFileURL(prefix, name));
		}
		catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	/**
	 * Lazily initializes image map.
	 */
	public static Image getImage(ImageDescriptor imageDescriptor) {
		ImageRegistry imageRegistry = getImageRegistry();

		Image image = imageRegistry.get("" + imageDescriptor.hashCode());
		if (image == null) {
			image = imageDescriptor.createImage();
			imageRegistry.put("" + imageDescriptor.hashCode(), image);
		}
		return image;
	}

	private static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}

		return imageRegistry;
	}

	private static URL makeIconFileURL(String prefix, String name) throws MalformedURLException {
		if (baseURL == null) {
			throw new MalformedURLException();
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("full");
		buffer.append('/');
		buffer.append(prefix);
		buffer.append('/');
		buffer.append(name);
		return new URL(baseURL, buffer.toString());
	}

}
