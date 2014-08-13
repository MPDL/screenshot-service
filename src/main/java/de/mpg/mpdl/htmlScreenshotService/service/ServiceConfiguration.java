package de.mpg.mpdl.htmlScreenshotService.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
/**
 * 
 * @author saquet
 *
 */
public class ServiceConfiguration {
	public static final String SERVICE_NAME = "html-screenshot";
	private static final String PROPERTIES_FILENAME = "html-screenshots-service.properties";
	private Properties properties = new Properties();

	public ServiceConfiguration() {
		load();
	}

	public String getServiceUrl() {
		if (properties.containsKey("service.url"))
			return (String) properties.get("service.url");
		return "http://localhost:8080/" + SERVICE_NAME;
	}

	/**
	 * Return the home directory for PhantomJs (important for windows)
	 * 
	 * @return
	 */
	public String getPhantomJsBin() {
		if (properties.containsKey("phantomjs.bin"))
			return (String) properties.get("phantomjs.bin");
		return null;
	}
	
	/**
	 * Return ujrl of the magick service
	 * 
	 * @return
	 */
	public String getMagickSErviceUrl() {
		if (properties.containsKey("magick.service.url"))
			return (String) properties.get("magick.service.url");
		return null;
	}

	/**
	 * Load the properties
	 */
	private void load() {
		if (getPropertyFileLocation() != null) {
			try {
				properties.load(new FileInputStream(new File(
						getPropertyFileLocation())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Return the location of the property file according to the server
	 * 
	 * @return
	 */
	private String getPropertyFileLocation() {
		String loc = "";
		if (System.getProperty("jboss.server.config.dir") != null) {
			loc = System.getProperty("jboss.server.config.dir");
		} else if (System.getProperty("catalina.home") != null) {
			loc = System.getProperty("catalina.home") + "/conf";
		}
		return FilenameUtils.concat(loc, PROPERTIES_FILENAME);
	}
}
