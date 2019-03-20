package com.restapi.metrics;

import org.springframework.stereotype.Service;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Service
public class StatMetric {
	private static final StatsDClient statsd = new NonBlockingStatsDClient("csye6225.webapp.restapi", "localhost", 8125);
	
	public void increementStat(String endpoint) {
		statsd.increment(endpoint);
	}
	
	
	
}
