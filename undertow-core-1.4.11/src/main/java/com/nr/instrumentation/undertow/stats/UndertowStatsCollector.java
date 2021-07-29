package com.nr.instrumentation.undertow.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.newrelic.agent.service.Service;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.Logger;
import com.newrelic.api.agent.NewRelic;

import io.undertow.client.ClientStatistics;
import io.undertow.server.ConnectorStatistics;
import io.undertow.server.HttpHandler;
import io.undertow.server.OpenListener;
import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionManagerStatistics;

public class UndertowStatsCollector implements Runnable {
	
	static private UndertowStatsCollector instance = null;

	static private Map<String, ClientStatistics> clientStats = new HashMap<String, ClientStatistics>();
	
	static private Map<String, ConnectorStatistics> connectorStats = new HashMap<String, ConnectorStatistics>();
	
	static private List<OpenListener> openListeners = new ArrayList<OpenListener>();
	
	static private List<InMemorySessionManager> sessionManagers = new ArrayList<InMemorySessionManager>();
	
	private static boolean initialized = false;
	
	static {
		if(!initialized) {
			Logger logger = NewRelic.getAgent().getLogger();
			logger.log(Level.FINE, "Adding UndertowStatisticsService" );
			ServiceFactory.addService(new UndertowStatisticsService());
			
			Service service = ServiceFactory.getService("UndertowStatisticsService");
			if(service != null) {
				logger.log(Level.FINE, "Found UndertowStatisticsService");
				if (!service.isStartedOrStarting()) {
					logger.log(Level.FINE, "Attempting to start UndertowStatisticsService");
					try {
						service.start();
					} catch (Exception e) {
						logger.log(Level.FINE, e, "Threw exception while trying to start UndertowStatisticsService");
					} 
				} else {
					logger.log(Level.FINE, "UndertowStatisticsService has started or is starting");
				}
			} else {
				logger.log(Level.FINE, "Failed to find UndertowStatisticsService");
			}
			initialized = true;
		}
		
	}
	
	public UndertowStatsCollector getInstance() {
		if(instance == null) {
			instance = new UndertowStatsCollector();
		}
		return instance;
	}

	@Override
	public void run() {
		
		Set<String> keys = clientStats.keySet();
		for(String key : keys) {
			ClientStatistics stats = clientStats.get(key);
			if(stats != null) {
				NewRelic.recordMetric("Undertow-Statistics/ClientStatistics/"+key+"/Read", stats.getRead());
				NewRelic.recordMetric("Undertow-Statistics/ClientStatistics/"+key+"/Requests", stats.getRequests());
				NewRelic.recordMetric("Undertow-Statistics/ClientStatistics/"+key+"/Written", stats.getWritten());
			}
		}
		
		keys = connectorStats.keySet();
		for(String key : keys) {
			ConnectorStatistics stats = connectorStats.get(key);
			if(stats != null) {
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ActiveConnections", stats.getActiveConnections());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ActiveRequests", stats.getActiveRequests());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/BytesReceived", stats.getBytesReceived());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/BytesSent", stats.getBytesSent());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/MaxActiveConnections", stats.getMaxActiveConnections());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/MaxActiveRequests", stats.getMaxActiveRequests());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ErrorCount", stats.getErrorCount());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/MaxProcessingTime", stats.getMaxProcessingTime());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ProcessingTime", stats.getProcessingTime());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/RequestCount", stats.getRequestCount());
			}
		}
		
		for(OpenListener listener : openListeners) {
			String type = getType(listener.getClass().getSimpleName());
			ConnectorStatistics stats = listener.getConnectorStatistics();
			if(stats != null) {
				HttpHandler rootHandler = listener.getRootHandler();
				String handlerType = "Unknown";
				if(rootHandler != null) {
					handlerType = rootHandler.getClass().getSimpleName();
				}
				String key = type+"-"+handlerType;
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ActiveConnections", stats.getActiveConnections());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ActiveRequests", stats.getActiveRequests());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/BytesReceived", stats.getBytesReceived());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/BytesSent", stats.getBytesSent());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/MaxActiveConnections", stats.getMaxActiveConnections());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/MaxActiveRequests", stats.getMaxActiveRequests());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ErrorCount", stats.getErrorCount());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/MaxProcessingTime", stats.getMaxProcessingTime());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/ProcessingTime", stats.getProcessingTime());
				NewRelic.recordMetric("Undertow-Statistics/ConnectorStatistics/"+key+"/RequestCount", stats.getRequestCount());
				
			}
		}
		
		for(InMemorySessionManager manager : sessionManagers) {
			String deployment = manager.getDeploymentName();
			SessionManagerStatistics stats = manager.getStatistics();
			
			NewRelic.recordMetric("Undertow-Statistics/SessionStatistics/"+deployment+"/ActiveSessionCount", stats.getActiveSessionCount());
			NewRelic.recordMetric("Undertow-Statistics/SessionStatistics/"+deployment+"/AverageSessionAliveTime", stats.getAverageSessionAliveTime());
			NewRelic.recordMetric("Undertow-Statistics/SessionStatistics/"+deployment+"/CreatedSessionCount", stats.getCreatedSessionCount());
			NewRelic.recordMetric("Undertow-Statistics/SessionStatistics/"+deployment+"/ExpiredSessionCount", stats.getExpiredSessionCount());
			NewRelic.recordMetric("Undertow-Statistics/SessionStatistics/"+deployment+"/MaxActiveSessionCount", stats.getMaxActiveSessions());
			NewRelic.recordMetric("Undertow-Statistics/SessionStatistics/"+deployment+"/MaxAverageSessionAliveTime", stats.getMaxSessionAliveTime());
			NewRelic.recordMetric("Undertow-Statistics/SessionStatistics/"+deployment+"/RejectedSessions", stats.getRejectedSessions());
		}

	}
	
	private static String getType(String classname) {
		int index = classname.indexOf("OpenListener");
		if(index > 0) {
			return classname.substring(0, index);
		}
		return classname;
	}

	public static void addClientStatistics(String name, ClientStatistics stats) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "UndertowStatsCollector: adding ClientStatistics with name: {0}", name);
		clientStats.put(name, stats);
	}
	
	public static void addConnectorStatistics(String name, ConnectorStatistics stats) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "UndertowStatsCollector: adding ConnectorStatistics with name: {0}", name);
		connectorStats.put(name, stats);
	}
	 
	public static void addOpenListener(OpenListener listener) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "UndertowStatsCollector: adding OpenListener with type: {0}", listener.getClass().getSimpleName());
		openListeners.add(listener);
	}
	
	public static void addSessionManager(InMemorySessionManager manager) {
		sessionManagers.add(manager);
	}
}
