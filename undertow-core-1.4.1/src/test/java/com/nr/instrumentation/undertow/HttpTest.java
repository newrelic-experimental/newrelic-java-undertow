package com.nr.instrumentation.undertow;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.newrelic.agent.introspec.InstrumentationTestConfig;
import com.newrelic.agent.introspec.InstrumentationTestRunner;
import com.newrelic.agent.introspec.Introspector;

import io.undertow.Undertow;
import io.undertow.attribute.RequestPathAttribute;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.RoutingHandler;

@RunWith(InstrumentationTestRunner.class)
@InstrumentationTestConfig(includePrefixes="io.undertow")
public class HttpTest {
	
	
	private HelloHandler handler = new HelloHandler();
	private Undertow server = null;
	private static HttpTest instance = null;
//	private static final String GETHTTP = "WebTransaction/Undertow/hello - GET";
//	private static final String HANDLEREQUEST = "Custom/Undertow/HttpHandler/RoutingHandler/handleRequest";
//	private static final String HELLOHANDLER = "Custom/Undertow/HttpHandler/HelloHandler/handleRequest";
	
	@BeforeClass
	public static void beforeClass() {
		instance = new HttpTest();
		instance.init();
	}
	
	public HttpTest() {
		
	}
	
	public void init() {
		Predicate regexPredicate = Predicates.regex(RequestPathAttribute.INSTANCE, RegExHandler.regex);
		Predicate suffixPredicate = Predicates.suffix(DepositHandler.SUFFIX);
		
		Predicate suffixPredicate2 = Predicates.suffix(InquiryHandler.SUFFIX1);
		Predicate suffixPredicate3 = Predicates.suffix(InquiryHandler.SUFFIX2);
		
		Predicate orPredicate = Predicates.or(suffixPredicate2,suffixPredicate3);

		RoutingHandler routing = new RoutingHandler()
				.get("/hello", handler)
				.get("/get/*", regexPredicate, new RegExHandler())
				.post("/account/*", suffixPredicate, new DepositHandler())
				.post("/account/*", orPredicate, new InquiryHandler());
		
		server = Undertow.builder().addHttpListener(8000, "localhost",routing).build();
		server.start();
	}
	

	public void stop() {
		server.stop();
	}
	
	@Test
	public void httpTest() {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			// Test Hello
			HttpGet get = new HttpGet("http://localhost:8000/hello?name=NewRelic");
			CloseableHttpResponse response = client.execute(get);
			outputResponse(response);
			response.close();
			
			HttpGet regExGet = new HttpGet("http://localhost:8000/get/Test1");
			CloseableHttpResponse response2 = client.execute(regExGet);
			outputResponse(response2);
			response2.close();
			
//			HttpPost deposit = new HttpPost("http://localhost:8000/account/deposit/12345/deposit");
//			String json = "{\"name\": \"Doug\",\"amount\": 1000.00 }";
//			StringEntity entity = new StringEntity(json);
//			deposit.setEntity(entity);
//			deposit.setHeader("Accept", "application/json");
//			deposit.setHeader("Content-Type", "application/json");
//			
//			CloseableHttpResponse response3 = client.execute(deposit);
//			outputResponse(response3);
//			response3.close();
			
			client.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Introspector introspector = InstrumentationTestRunner.getIntrospector();
		int finishedTransactions = introspector.getFinishedTransactionCount(10000L);
		System.out.println("Number of Transactions: "+finishedTransactions);
		//assertTrue(finishedTransactions==1);
		
		Collection<String> transactionNames = introspector.getTransactionNames();
//		assertTrue(transactionNames.contains(GETHTTP));
//		Map<String, TracedMetricData> metrics = introspector.getMetricsForTransaction(GETHTTP);
//		Set<String> metricNames = metrics.keySet();
//		assertTrue(metricNames.contains(HANDLEREQUEST));
//		assertTrue(metricNames.contains(HELLOHANDLER));
		
		for(String txnName : transactionNames) {
			System.out.println("Transaction: "+txnName);
		}
	}
	
	@AfterClass
	public static void afterClass() {
		instance.stop();
	}
	
	private void outputResponse(CloseableHttpResponse response) throws ParseException, IOException {
		System.out.println("Response code: "+response.getStatusLine().getStatusCode());
		outputEntity(response.getEntity());
		
	}
	
	private void outputEntity(HttpEntity entity) throws ParseException, IOException {
		System.out.println("Response: "+EntityUtils.toString(entity));
	}
}
