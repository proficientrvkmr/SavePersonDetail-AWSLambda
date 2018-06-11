package com.practice.alphaawslambda;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.practice.alphaawslambda.domain.PersonRequest;
import com.practice.alphaawslambda.domain.PersonResponse;

public class SavePersonHandler implements RequestHandler<PersonRequest, PersonResponse> {

	private String DYNAMODB_TABLE_NAME = "Person";

	public PersonResponse handleRequest(PersonRequest personRequest, Context context) {
		final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
		try {
			Map<String, AttributeValue> personItem = new HashMap<>();
			personItem.put("id", new AttributeValue(personRequest.getId()));
			personItem.put("firstName", new AttributeValue(personRequest.getFirstName()));
			personItem.put("lastName", new AttributeValue(personRequest.getLastName()));
			personItem.put("email", new AttributeValue(personRequest.getEmail()));
			ddb.putItem(DYNAMODB_TABLE_NAME, personItem);
		} catch (ResourceNotFoundException e) {
			context.getLogger().log(String.format("Error: The table \"%s\" can't be found.\n", DYNAMODB_TABLE_NAME));
		} catch (AmazonServiceException e) {
			context.getLogger().log(e.getMessage());
		}

		PersonResponse personResponse = new PersonResponse();
		personResponse.setMessage("Your records has been saved successfully!!!");
		return personResponse;
	}

}
