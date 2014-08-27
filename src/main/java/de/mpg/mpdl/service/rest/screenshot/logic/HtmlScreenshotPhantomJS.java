package de.mpg.mpdl.service.rest.screenshot.logic;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.service.DriverService;

public class HtmlScreenshotPhantomJS {
	private WebDriver driver;

	public HtmlScreenshotPhantomJS() {
		System.out.println(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY);
		driver = new PhantomJSDriver();
	}

	/**
	 * Return a screenshot of the html page at the passed {@link URL}
	 * 
	 * @param url
	 * @return
	 */

	public File takeScreenshot(String url) {
		driver.get(url);
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}

}
