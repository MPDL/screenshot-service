package de.mpg.mpdl.htmlScreenshotService.logic;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class HtmlScreenshotPhantomJS {
	private WebDriver driver;

	public HtmlScreenshotPhantomJS() {
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
