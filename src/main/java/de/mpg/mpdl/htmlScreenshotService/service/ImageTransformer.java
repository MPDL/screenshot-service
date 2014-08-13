package de.mpg.mpdl.htmlScreenshotService.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;

/**
 * Use the Magick Service to transform an image
 * 
 * @author saquet
 *
 */
public class ImageTransformer {

	private String magickServiceUrl;
	private static final String DEFAULT_FORMAT = "png";
	private static final String DEFAULT_SIZE = "";
	private static final String DEFAULT_CROP = "0";
	private static final String DEFAULT_PRIORITY = "";
	private static final String DEFAULT_NAME = "screenshot.png";

	public ImageTransformer() {
		ServiceConfiguration config = new ServiceConfiguration();
		magickServiceUrl = config.getMagickSErviceUrl();
	}

	/**
	 * TRansform an image (in) into another image (out) according to the
	 * parameters (see explanation below). This method use the magick service
	 * 
	 * @param in
	 * @param out
	 * @param name
	 *            - The name of the image to be transformed
	 * @param format
	 *            - The format of the transformed image
	 * @param size
	 *            - The size of the image (widthxheight for instance 100x50)
	 * @param crop
	 *            - the rule to crop the input image (widthxheight+X+Y for
	 *            instance 100x50+10+5)
	 * @param priority
	 *            - (if crop and size both defined), defines what to do first:
	 *            crop of resize
	 * @param params1
	 *            - (see magick service docmentation)
	 * @param params2
	 *            - (see magick service docmentation)
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void transform(File f, OutputStream out, String format, String size,
			String crop, String priority, String params1, String params2)
			throws MalformedURLException, IOException {
		// Initialize the connection to the magick service according to the
		// parameters
		URLConnection magickConn = URI
				.create(magickServiceUrl
						+ generateMagickParameters("screenshot.png", format,
								size, crop, priority, params1, params2))
				.toURL().openConnection();
		// Allow to write data in the connection (for POST request)
		magickConn.setDoOutput(true);
		// Send the request to the magick service
		IOUtils.copy(new FileInputStream(f), magickConn.getOutputStream());
		// Copy the response of the magick service to the output
		IOUtils.copy(magickConn.getInputStream(), out);
	}

	/**
	 * Generate the url parameters for the query to the magick service
	 * 
	 * @param name
	 * @param format
	 * @param size
	 * @param crop
	 * @param priority
	 * @param params1
	 * @param params2
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String generateMagickParameters(String name, String format,
			String size, String crop, String priority, String params1,
			String params2) throws UnsupportedEncodingException {
		return "?format=" + getNonNullValue(format, DEFAULT_FORMAT) + "&size="
				+ getNonNullValue(size, DEFAULT_SIZE) + "&crop="
				+ getNonNullValue(crop, DEFAULT_CROP) + "&priority="
				+ getNonNullValue(priority, DEFAULT_PRIORITY) + "&name="
				+ getNonNullValue(name, DEFAULT_NAME) + "&params1="
				+ URLEncoder.encode(params1, "UTF-8") + "&params2="
				+ URLEncoder.encode(params2, "UTF-8");
	}

	/**
	 * If the passed value is null, return the default value
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getNonNullValue(String value, String defaultValue)
			throws UnsupportedEncodingException {
		return value != null ? URLEncoder.encode(value, "UTF-8") : URLEncoder
				.encode(defaultValue, "UTF-8");
	}
}
