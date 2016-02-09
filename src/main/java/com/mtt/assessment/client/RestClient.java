package com.mtt.assessment.client;

public interface RestClient {

	<RES> RES performGet(Class<RES> clazz);
}
