package com.sentimeter.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentimeter.ApiGatewayResponse;
import com.sentimeter.Response;
import com.sentimeter.pojo.Product;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class CreateProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(CreateProductHandler.class.getName());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

            // create the Product object for post
            Product product = new Product();
            // product.setId(body.get("id").asText());
            product.setName(body.get("name").asText());
            if(null != body.get("price")) product.setPrice((float) body.get("price").asDouble());
            if(null != body.get("review")) product.setReview(body.get("review").asText());
            product.save(product);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(product)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            logger.info("Error in saving product: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in saving product: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}