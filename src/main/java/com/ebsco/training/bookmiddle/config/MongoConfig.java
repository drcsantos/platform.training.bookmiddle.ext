package com.ebsco.training.bookmiddle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
    
    @Value("${mongoDB.database}")
    private String database;
    
    @Value("${mongoDB.host}")
    private String host;
    
    @Value("${mongoDB.port}")
    private int port;
    
    @Override
    protected String getDatabaseName() {
        return this.database;
    }
  
    public Mongo mongo() throws Exception {
        return new MongoClient(this.host, this.port);
    }
  
    @Override
    protected String getMappingBasePackage() {
        return "com.ebsco.training";
    }
}

