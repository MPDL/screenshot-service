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
package de.mpg.mpdl.htmlScreenshotService.logic;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Make a Screenshot of an HTML Page
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class HtmlScreenshot {
	private WebDriver driver;

	/**
	 * Initialize the {@link HtmlScreenshot} with Firefox
	 */
	public HtmlScreenshot() {
		driver = new FirefoxDriver();

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

	public File takeScreenshot(URL url, int width, int height) {
		driver.get(url.toString());
		driver.manage().window().setSize(new Dimension(width, height));

		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}

	public File takeScreenshot(URL url, Boolean fullSize) {
		driver.get(url.toString());

		if (fullSize == true) {
			driver.manage().window().maximize();
		}

		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}
}
