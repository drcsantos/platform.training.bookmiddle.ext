package com.ebsco.training.bookmiddle.repository;

import java.io.Serializable;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TrackableRepository<T extends Serializable> implements Repository<T> {

    public enum TrackOperation {
        Insert, Update, Delete
    };

    protected abstract String getAWSQueueUrl();

    protected abstract T insertData(T value);

    protected abstract T updateData(T value);

    protected abstract T deleteData(T value);

    protected void TrackObject(T value, TrackOperation operation) {

        String serialized;
        try {
            serialized = new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            serialized = e.getMessage();
        }
        String message = String.format("{\"object\": %s, \"operation\": \"%s\", \"queue\": \"%s\"}", serialized,
            operation.toString(), this.getAWSQueueUrl());

        /*
         * The ProfileCredentialsProvider returns your [default] credential
         * profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Can't load the credentials from the credential profiles file. "
                + "Please make sure that your credentials file is at the correct "
                + "location (~/.aws/credentials), and is a in valid format.", e);
        }

        @SuppressWarnings("deprecation") AmazonSQSClient sqs = new AmazonSQSClient(credentials);
        // Send a message
        String queueUrl = getAWSQueueUrl();
        System.out.println(String.format("Sending message %s", message));
        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, message);
        // You must provide a non-empty MessageGroupId when sending messages to
        // a FIFO queue
        sendMessageRequest.setMessageGroupId("messageGroup1");
        // Uncomment the following to provide the MessageDeduplicationId
        // sendMessageRequest.setMessageDeduplicationId("1");
        SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
        String sequenceNumber = sendMessageResult.getSequenceNumber();
        String messageId = sendMessageResult.getMessageId();
        System.out
            .println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber + "\n");
    }

    @Override
    public abstract T findById(String id);

    @Override
    public abstract List<T> find();

    @Override
    public T insert(T value) {

        T result = insertData(value);
        TrackObject(result, TrackOperation.Insert);

        return result;
    }

    @Override
    public T update(T value) {

        T result = updateData(value);
        TrackObject(result, TrackOperation.Update);

        return result;
    }

    @Override
    public T delete(T value) {

        T result = deleteData(value);
        TrackObject(result, TrackOperation.Delete);

        return result;
    }
}