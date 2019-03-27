package com.restapi.daos;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
//import com.amazonaws.services.lambda.AWSLambda;
//import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
//import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
//import com.amazonaws.services.sns;

public class PasswordResetDAO 
{
	private static String lambdaFuncName = "";
	
	private static String topicArn = "arn:aws:sns:us-east-1:753810521416:MyTopic";
	
	public void sendEmailToUser (String email)
	{
//		Regions region = Regions.fromName("us-east-1");
//		AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard().withRegion(region);
//		AWSLambda client = builder.build();
//		
//		InvokeRequest req = new InvokeRequest().withFunctionName(lambdaFuncName).withPayload("{ email : "+ email +"}"); 
//		
//		client.invoke(req);
		
		AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
		
		
		final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
		final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
		final PublishResult publishResponse = snsClient.publish(publishRequest);
		
		System.out.println("MessageId: " + publishResponse.getMessageId());

	}

}
