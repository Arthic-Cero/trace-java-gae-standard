package com.example.appengine.helloworld;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;
import java.util.ArrayList;

import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;

public class ExampleContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Starting up!");
        try {
        	 ArrayList<String> scopes = new ArrayList<String>();
             scopes.add("https://www.googleapis.com/auth/trace.append");
    
        	final AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();
            final AppIdentityService.GetAccessTokenResult accessToken = appIdentity.getAccessToken(scopes);
        	
        	StackdriverTraceExporter.createAndRegister(
    			StackdriverTraceConfiguration.builder()
        			.setProjectId("jalc-rep-project")
        			.setCredentials(new GoogleCredentials(new AccessToken(accessToken.getAccessToken() , accessToken.getExpirationTime())))
        			.build());
        } catch(Exception e){
        	e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Shutting down!");
    }
}
