/*
 *
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License"). You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or http://www.escidoc.de/license.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */
/*
 * Copyright 2006-2007 Fachinformationszentrum Karlsruhe Gesellschaft
 * für wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Förderung der Wissenschaft e.V.
 * All rights reserved. Use is subject to license terms.
 */
package de.mpg.mpdl.service.rest.screenshot.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

import de.mpg.mpdl.service.rest.screenshot.logic.HtmlScreenshot;

/**
 * Service implementation to take screenshot
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class HtmlScreenshotService {
	private HtmlScreenshot htmlScreenshot;
	public static final int DEFAULT_WIDTH = 1920;
	public static final int DEFAULT_HEIGHT = 1020;

	/**
     * 
     */
	public HtmlScreenshotService() {
		this.htmlScreenshot = new HtmlScreenshot();
	}

	/**
	 * Take a Screenshot of an url
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @param useFireFox
	 * @return
	 * @throws MalformedURLException
	 */
	public File takeScreenshot(String url, int width, int height,
			boolean useFireFox) throws MalformedURLException {
		return htmlScreenshot.takeScreenshot(url, width, height, useFireFox);
	}

	/**
	 * Take a screenshot of a file
	 * 
	 * @param f
	 * @param width
	 * @param height
	 * @param useFireFox
	 * @return
	 * @throws MalformedURLException
	 */
	public File takeScreenshot(File f, int width, int height, boolean useFireFox)
			throws MalformedURLException {
		String path = "file:///" + f.getAbsolutePath();
		return htmlScreenshot.takeScreenshot(path, width, height, useFireFox);
	}

	/**
	 * Take a Screenshot for a multipart request
	 * 
	 * @param items
	 * @return
	 * @throws IOException
	 */
	public File takeScreenshot(List<FileItem> items) throws IOException {
		FileItem item = getUploadedFileItem(items);
		File f = File.createTempFile("screenshot", null);
		IOUtils.copy(item.getInputStream(), new FileOutputStream(f));
		return takeScreenshot(f, getBrowserWidth(items),
				getBrowserHeight(items), getUseFirefox(items));
	}

	/**
	 * Read the param browserWidth in the request
	 * 
	 * @param items
	 * @return
	 */
	private int getBrowserWidth(List<FileItem> items) {
		String str = getFieldValue(items, "browserWidth");
		return str != null ? Integer.parseInt(str) : DEFAULT_WIDTH;
	}

	/**
	 * Read the param browserHeight in the request
	 * 
	 * @param items
	 * @return
	 */
	private int getBrowserHeight(List<FileItem> items) {
		String str = getFieldValue(items, "browserHeight");
		return str != null ? Integer.parseInt(str) : DEFAULT_HEIGHT;
	}

	/**
	 * Read the param useFireFox in the request
	 * 
	 * @param items
	 * @return
	 */
	private boolean getUseFirefox(List<FileItem> items) {
		String str = getFieldValue(items, "useFireFox");
		return str != null ? Boolean.parseBoolean(str) : false;
	}

	/**
	 * Return the file which is uploaded as a {@link FileItem}
	 * 
	 * @param items
	 * @return
	 */
	private FileItem getUploadedFileItem(List<FileItem> items) {
		for (FileItem item : items) {
			if (!item.isFormField()) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Return the Field (param) with the given name as a {@link FileItem}
	 * 
	 * @param items
	 * @param name
	 * @return
	 */
	private String getFieldValue(List<FileItem> items, String name) {
		for (FileItem item : items) {
			if (item.isFormField() && name.equals(item.getFieldName())) {
				return item.getString();
			}
		}
		return null;
	}

}
