### Using Mutual Authenitcation with restfulWS-3.x feauture under Liberty ###

---

Under OpenLiberty or Liberty the `jaxrs-2.x` [feauture](https://openliberty.io/docs/latest/reference/feature/jaxrs-2.0.html) allowed for the use of a [webTarget]( https://openliberty.io/docs/latest/reference/config/webTarget.html) to configure certain elements of the communications between REST Client and the server hosting the RESTFul Web Service.  However, when using the `restfulWS-3.x` the `webTarget` information is ignored.  This shows how to get around this issue in terms of mutual authentication.

---

The [RestfulWS](RestfulWS) contains a simple RESTful Web Service that returns a list of `Properties` defined on the system. To build and run:
```
mvn liberty:Dev
```
This should start the Liberty server on port 9444.  Using the url https://localhost:9444/RestfulWS/properties should give an error since either the browser is not passing a client certificate or the client certifcate is not known.  To test without the requirement of the client certifcate, then update the `server.xml` file and change the `ssl` property `clientAuthentication="true"` to `clientAuthentication="false"`. Testing should show the properties now.  Remember to change the property to `true` for testing with the RESTFul Client.

---

The [RestfulClient](RestfulClient) is a simple RESTful Client that will call the RESTful service and pass a client certificate. To build and run:
```
mvn liberty:Dev
```
This should start the Liberty server on port 9443.  Using the url https://localhost:9443/RestfulClient/rest should bring the list of `Properties` and format them.  The way this works is instead of using a `webTarget`, an `ssl` configuration must be added in the `server.xml` file for each endpoint that is different.  In this case a `clientSSLConfig` contains the necessary information for mutual authentication.  The program gets this information using a Liberty/WebSphere specific API, [JSSEHelper](https://openliberty.io/docs/modules/reference/24.0.0.11/com.ibm.websphere.appserver.api.ssl_1.7-javadoc/com/ibm/websphere/ssl/JSSEHelper.html).  Then the SSLContext is passed to the RESTful [ClientBuilder](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/websvcs/rest-client/rest-client.html). 
