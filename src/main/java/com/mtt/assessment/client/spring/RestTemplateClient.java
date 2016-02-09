package com.mtt.assessment.client.spring;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.client.RestTemplate;

import com.mtt.assessment.client.RestClient;

public class RestTemplateClient implements RestClient{

	private final ConcurrentHashMap<String, Object> params;
	
	private final String url;
	
	private RestTemplateClient(Builder builder) {
		params = builder.params;
		url = builder.url;
	}
 
	@Override
	public <RES> RES performGet(Class<RES> clazz) {
        return (RES) new RestTemplate().getForObject(url, clazz, params);	
	}
	
	public static class Builder {
		
		private final ConcurrentHashMap<String, Object> params = new ConcurrentHashMap<>();
		
		private final String url;
		
		public Builder(String url) {
			this.url = url;
		}
		
		public Builder withParam(String paramName, Object value) {
			params.put(paramName, value);
			return this;
		}
		
		public RestTemplateClient build() {
			return new RestTemplateClient(this);
		}
	}

}
