html-screenshot-service
=======================

what is html-screenshot-service?
--------------------------------
html-screenshot-service is a service to create a screenshot of the html page by using selenium driver.
The user can through the parameters to change the browser size and to tranform the scrennshot into the
image that they needed.

what needs to install?
----------------------
html-screenshot-servie can be run in Firefox or in background by using PhantomJS.<br />
[Download Firefox](https://www.mozilla.org/)<br />
[Download PhantomJS] (http://phantomjs.org/download.html) <br />

how to run the service?
-----------------------
### GET
the user can send the parameter to the service by using method GET <br />
GET: localhost:8080/html-screenshot/?url=
If choose Firefox to run the service
GET: localhost:8080/html-screenshot/?url= & useFireFox=true
If resize the browser width or height
GET: localhost:8080/html-screenshot/?url= & useFireFox=true & width= & height= 
### POST
the user can also send html file by using method POST <br />
POST: file///the path of html file



