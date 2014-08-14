html-screenshot-service
=======================

what is html-screenshot-service?
--------------------------------
html-screenshot-service is a service to create a screenshot of the html page by using selenium web driver.
The user can through the parameters to change the browser size and to tranform the scrennshot into the
image.

what needs to be install?
----------------------
html-screenshot-servie can be run in Firefox or in background by using PhantomJS.<br />
1. Install Firefox [download](https://www.mozilla.org/)<br />
2. Install Phantoms [download] (http://phantomjs.org/download.html) <br />
3. Install Java [download] (http://www.oracle.com/technetwork/java/javase/downloads/index.html)<br /> 
4. Install Maven [download] (http://maven.apache.org/download.cgi) <br />
5. Install Tomcat [download](http://maven.apache.org/download.cgi)<br />

how to install the service?
--------------------------
1. Clone the service:https://github.com/MPDL/html-screenshot-service
2. Compile the service: In service directory, run `mvn clean install`
3. Copy html-screenshot.war to Tomcat Webapp Directory
4. Start Tomcat
5. Run Services under `http://localhost:8080/html-screenshot`

how to call the service?
-----------------------
the user can send the parameters to the service by using methods `GET` and `POST` <br />
- **url** (Mandotory for `GET`): the url of the file to be transformed, e.g. `http://localhost:8080/html-screenshot/?url=`
- **useFirefox**: the browser Firefox be selected to call the service, e.g. `http://localhost:8080/html-screenshot/?url= & useFireFox=true`
- **browserWidth** and **browserHeight** to resize the browser width or height, e.g. `http://localhost:8080/html-screenshot/?url= & useFireFox=true & browserWidth= & browserHeight=`<br />

the html-screenshot-service use [magick service](https://github.com/MPDL/magick-service) to tranform the image by using some parameters, they are:
- **size**: As defined by imagemagick [resize](http://www.imagemagick.org/script/command-line-options.php#resize)
- **crop**:As defined by imagemagick [crop](http://www.imagemagick.org/script/command-line-options.php#crop)
- **format**: The format in which the file shhould be returned (for instance png, jpg, etc.)
- **priority** (size|crop): The method (size or crop) which is processed first (only relevant when resize and crop are both used)




