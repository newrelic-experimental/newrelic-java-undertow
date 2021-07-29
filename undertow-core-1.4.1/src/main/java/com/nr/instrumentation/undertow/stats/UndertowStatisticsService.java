package com.nr.instrumentation.undertow.stats;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.newrelic.agent.service.AbstractService;
import com.newrelic.api.agent.NewRelic;

public class UndertowStatisticsService extends AbstractService {
	
	private ScheduledExecutorService executor = null;
	
	public UndertowStatisticsService() {
		super("UndertowStatisticsService");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	protected void doStart() throws Exception {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Starting UndertowStatisticsService");
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new UndertowStatsCollector(), 1L, 1L, TimeUnit.MINUTES);

	}

	@Override
	protected void doStop() throws Exception {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Stopping UndertowStatisticsService");
		executor.shutdown();
	}

}
