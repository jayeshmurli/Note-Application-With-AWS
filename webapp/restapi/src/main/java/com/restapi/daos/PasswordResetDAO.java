package com.restapi.daos;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;

public class PasswordResetDAO 
{
	private static String lambdaFuncName = "";
	
	public void sendEmailToUser (String email)
	{
		Regions region = Regions.fromName("us-east-1");
		AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard().withRegion(region);
		AWSLambda client = builder.build();
		
		InvokeRequest req = new InvokeRequest().withFunctionName(lambdaFuncName).withPayload("{ email : "+ email +"}"); 
		
		client.invoke(req);
	}

}
