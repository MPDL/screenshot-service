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
 * f��r wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur F��rderung der Wissenschaft e.V.
 * All rights reserved. Use is subject to license terms.
 */
package de.mpg.mpdl.service.rest.screenshot.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 * TODO Description
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class HtmlScreenshotServlet extends HttpServlet {
	private static final long serialVersionUID = -3073642728935619196L;
	private HtmlScreenshotService screenshotService;

	private File file;
	// private File resizeImageFile;
	private int browserWidth;
	private int browserHeight;
	private boolean useFireFox;
	private ImageTransformer imageTransformer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		screenshotService = new HtmlScreenshotService();
		imageTransformer = new ImageTransformer();

	}

	private String readBrowserParam(HttpServletRequest req, String param) {
		if (req.getParameter(param) != null) {
			return req.getParameter(param);
		} else {
			return "";
		}

	}

	/**
	 * Read a parameter from the request
	 * 
	 * @param req
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String readParam(HttpServletRequest req, String name)
			throws UnsupportedEncodingException {
		String value = req.getParameter(name);
		if ("crop".equals(name) && value != null) {
			String notEncodedvalue = repareCropParam(value);
			if (!notEncodedvalue.equals(value))
				return notEncodedvalue;
		}
		return value == null ? "" : URLDecoder.decode(value, "UTF-8");
	}

	/**
	 * When the crop parameter is not encoded, the + are interpreted as a white
	 * space in the url
	 * 
	 * @param crop
	 * @return
	 */
	private String repareCropParam(String crop) {
		return crop.trim().replace(" ", "+");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String url = req.getParameter("url");

			if (url != null) {
				browserWidth = (readBrowserParam(req, "browserWidth") != "") ? Integer
						.parseInt(req.getParameter("browserWidth")) : 1920;
				browserHeight = (readBrowserParam(req, "browserHeight") != "") ? Integer
						.parseInt(req.getParameter("browserHeight")) : 1080;
				useFireFox = (readBrowserParam(req, "useFireFox") != "") ? Boolean
						.parseBoolean(req.getParameter("useFireFox")) : false;

				file = screenshotService.takeScreenshot(url, browserWidth,
						browserHeight, useFireFox);

				if (transformScreenshot(req)) {
					imageTransformer.transform(file, resp.getOutputStream(),
							readParam(req, "format"), readParam(req, "size"),
							readParam(req, "crop"), readParam(req, "priority"),
							readParam(req, "params1"),
							readParam(req, "params2"));
				} else
					IOUtils.copy(new FileInputStream(file),
							resp.getOutputStream());
			}
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if (ServletFileUpload.isMultipartContent(req)) {
				file = screenshotService.takeScreenshot(getUploadedFiles(req));
			} else {
				File html = File.createTempFile("htmlScreenshot", ".html");
				if (req.getParameter("html") != null) {
					IOUtils.write(req.getParameter("html").getBytes(),
							new FileOutputStream(html));
				} else {
					IOUtils.copy(req.getInputStream(), new FileOutputStream(
							html));
				}
				System.out.println(html.getAbsolutePath());
				// TO Do After the copy the of the files
				browserWidth = (readBrowserParam(req, "browserWidth") != "") ? Integer
						.parseInt(req.getParameter("browserWidth"))
						: HtmlScreenshotService.DEFAULT_WIDTH;
				browserHeight = (readBrowserParam(req, "browserHeight") != "") ? Integer
						.parseInt(req.getParameter("browserHeight"))
						: HtmlScreenshotService.DEFAULT_HEIGHT;
				useFireFox = (readBrowserParam(req, "useFireFox") != "") ? Boolean
						.parseBoolean(req.getParameter("useFireFox")) : false;

				file = screenshotService.takeScreenshot(html, browserWidth,
						browserHeight, useFireFox);
				if (transformScreenshot(req)) {
					imageTransformer.transform(file, resp.getOutputStream(),
							readParam(req, "format"), readParam(req, "size"),
							readParam(req, "crop"), readParam(req, "priority"),
							readParam(req, "params1"),
							readParam(req, "params2"));
				} else
					IOUtils.copy(new FileInputStream(file),
							resp.getOutputStream());
			}
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Read Multipart files from {@link HttpServletRequest}
	 * 
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws FileUploadException
	 */
	private List<FileItem> getUploadedFiles(HttpServletRequest req)
			throws IOException, FileUploadException {
		File repository = File.createTempFile("servlet", null).getParentFile();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(repository);
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		return fileUpload.parseRequest(req);
	}

	/**
	 * 
	 * True if the screenshot must be transformed
	 * 
	 * @param req
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private boolean transformScreenshot(HttpServletRequest req)
			throws UnsupportedEncodingException {
		return !readParam(req, "format").equals("")
				|| !readParam(req, "size").equals("")
				|| !readParam(req, "crop").equals("")
				|| !readParam(req, "priority").equals("")
				|| !readParam(req, "params1").equals("")
				|| !readParam(req, "params2").equals("");
	}

}
