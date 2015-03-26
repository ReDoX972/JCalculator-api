package com.blueteam.test;

import java.io.*;
import java.net.*;

import javax.ws.rs.*;

import org.junit.*;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;


@Path("/jersey_test_location")
public class CalcServiceTest {

    private static final String LOCALHOST = "http://localhost:9998/";

    private static HttpServer server;

    @GET
    @Produces("text/plain")
    public String testSystem(){
        return "Hello. This is a test";
    }

    @BeforeClass
    public static void setUp() throws Exception{
        System.out.println("Creating server");
        server = HttpServerFactory.create(LOCALHOST);

        System.out.println("Starting server");
        server.start();

        System.out.println("HTTP server started");
        System.out.println("Running tests...");

        testResourceAtUrl(new URL(LOCALHOST + "jersey_test_location"));
    }

    private static String testResourceAtUrl(URL url) throws Exception {

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == 400)
            	return connection.getResponseMessage();
            
            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String firstLineOfText = reader.readLine();//you can also read the whole thing and then test
            System.out.println("Read: " + firstLineOfText);

            System.out.println("System was initialized correctly. About to run actual tests...");

            connection.disconnect();

            return firstLineOfText;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new Exception("could not establish connection to " + url.toExternalForm());
    }

    @Test
    public void testEvaluateExpression() throws Exception {
        String result;
        
        // 4*4-1
        result = testResourceAtUrl(new URL(LOCALHOST + "math?expression=4*4-1"));
        Assert.assertEquals("{\"result\":15}", result);
        
        // 4/4-1
        result = testResourceAtUrl(new URL(LOCALHOST + "math?expression=4%2F4-1"));
        Assert.assertEquals("{\"result\":0}", result);
        
        // syntax error
        result = testResourceAtUrl(new URL(LOCALHOST + "math?expression=---"));
        Assert.assertEquals("Bad Request", result);
    }

    /**
    * Destroy the server
    */
    @AfterClass
    public static void tearDown() throws IOException{
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }

}