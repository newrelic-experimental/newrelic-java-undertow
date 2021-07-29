package io.undertow.server.session;

import com.newrelic.api.agent.weaver.Weave;
import com.nr.instrumentation.undertow.stats.UndertowStatsCollector;

@Weave
public abstract class InMemorySessionManager {

	public abstract String getDeploymentName();
	
	public abstract SessionManagerStatistics getStatistics();
	
	public InMemorySessionManager(SessionIdGenerator sessionIdGenerator, String deploymentName, int maxSessions, boolean expireOldestUnusedSessionOnMax, boolean statisticsEnabled) {
		UndertowStatsCollector.addSessionManager(this);
	}
}
