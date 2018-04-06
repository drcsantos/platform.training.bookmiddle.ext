# Book Middle Service

Training project demonstrating creation of a REST Spring Boot microservice using Mongodb to persist data and AWS SQS to track the data changes.

## Introduction to MongoDB
MongoDB is an open-source document database that provides high performance, high availability, and automatic scaling. MongoDB obviates the need for an Object Relational Mapping (ORM) to facilitate development.

### Documents

A record in MongoDB is a document, which is a data structure composed of field and value pairs. MongoDB documents are similar to JSON objects. The values of fields may include other documents, arrays, and arrays of documents.

```json
{
   "_id" : ObjectId("54c955492b7c8eb21818bd09"),
   "address" : {
      "street" : "2 Avenue",
      "zipcode" : "10075",
      "building" : "1480",
      "coord" : [ -73.9557413, 40.7720266 ]
   },
   "borough" : "Manhattan",
   "cuisine" : "Italian",
   "grades" : [
      {
         "date" : ISODate("2014-10-01T00:00:00Z"),
         "grade" : "A",
         "score" : 11
      },
      {
         "date" : ISODate("2014-01-16T00:00:00Z"),
         "grade" : "B",
         "score" : 17
      }
   ],
   "name" : "Vella",
   "restaurant_id" : "41704620"
}
```

### Collections

MongoDB stores documents in collections. Collections are analogous to tables in relational databases. Unlike a table, however, a collection does not require its documents to have the same schema.

In MongoDB, documents stored in a collection must have a unique **_id** field that acts as a **primary key**.

### Install MongoDB

For more information on installation, including supported platforms and installation tutorials for MongoDB Enterprise, see [Installation](https://docs.mongodb.com/manual/installation/ "Installation") in the MongoDB manual.

[Install MongoDB Community Edition on Linux](https://docs.mongodb.com/getting-started/shell/tutorial/install-on-linux/)

[Install MongoDB Community Edition on OS X](https://docs.mongodb.com/getting-started/shell/tutorial/install-mongodb-on-os-x/)

[Install MongoDB Community Edition on Windows](https://docs.mongodb.com/getting-started/shell/tutorial/install-mongodb-on-windows/)

### Components

- **mongod:** The database server.
- **mongos:** Sharding router.
- **mongo:** The database shell (uses interactive javascript).

### Utilities

- **mongodump:** Create a binary dump of the contents of a database.
- **mongorestore:** Restore data from the output created by mongodump.
- **mongoexport:** Export the contents of a collection to JSON or CSV.
- **mongoimport:** Import data from JSON, CSV or TSV.
- **mongofiles:** Put, get and delete files from GridFS.
- **mongostat:** Show the status of a running mongod/mongos.
- **bsondump:** Convert BSON files into human-readable formats.
- **mongoreplay:** Traffic capture and replay tool.
- **mongotop:** Track time spent reading and writing data.

### Running MongoDB

For command line options invoke:

```
$ ./mongod --help
```

To run a single server database:

```
$ sudo mkdir -p /data/db
$ ./mongod
$
$ # The mongo javascript shell connects to localhost and test database by default:
$ ./mongo
> help
```

### Drivers

Client drivers for most programming languages are available at https://docs.mongodb.com/manual/applications/drivers/. Use the shell
("mongo") for administrative tasks.

### Documentation

See (https://docs.mongodb.com/manual/)

### Cloud Hosted MongoDB

See https://www.mongodb.com/cloud/atlas


### Learn MongoDB

See (https://university.mongodb.com/)


## Amazon Simple Queue Service
Amazon Simple Queue Service (Amazon SQS) is a fully managed message queuing service that makes it easy to decouple and scale microservices, distributed systems, and serverless applications. Amazon SQS moves data between distributed application components and helps you decouple these components. For more information about see [SQS Documentation](https://aws.amazon.com/pt/documentation/sqs/)


## Bookmiddle Project
This project was based on [platform.training.bookmiddle](https://github.com/EBSCOIS/platform.training.bookmiddle) developed by [Michael Panson](https://github.com/mp-ebsco). The original project was changed to support data persistence in mongodb as well as a trace of each operation that modifies the data of each document to record those changes in a FIFO queue using AWS SQS.

## AWS Mock
### How to set-up Localstack
LocalStack provides an easy-to-use test/mocking framework for developing Cloud applications. When testing the application locally, instead of using the real AWS queues and topics, use localstack.
#### Install via docker
[Official repository](https://hub.docker.com/r/localstack/localstack/)

Create and run a container
```bash
docker run --name localstack-title -p 4575:4575 -p 4576:4576 -p 8080:8080 localstack/localstack:latest
```
Start a existing container
```bash
docker start localstack-title
```
#### Useful commands for localstack

##### SNS
Listing existing topics:
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4575 sns list-topics
```

Create a new topic:
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4575 sns create-topic --name topic-name
```

Subscribe to the topic
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4575 sns subscribe --topic-arn arn:aws:sns:us-east-1:123456789012:topic-name --protocol email --notification-endpoint emailAtDomainDotCom
```

List subscriptions
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4575 sns list-subscriptions
```

Publish to this topic
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4575 sns publish  --topic-arn arn:aws:sns:us-east-1:123456789012:topic-name --message 'Hello World!'
```

##### SQS
Create a Queue
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name queue_name
```

List existing queues
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4576 sqs list-queues
```

Send a message to this queue
```bash
bash-3.2$  aws --endpoint-url=http://localhost:4576 sqs send-message --queue-url http://localhost:4576/queue/queue_name --message-body 'Test Message!'
```

Receive the message from this queue
```bash
bash-3.2$  aws --endpoint-url=http://localhost:4576 sqs receive-message --queue-url http://localhost:4576/queue/queue_name
```

Delete this message
```bash
bash-3.2$ aws --endpoint-url=http://localhost:4576 sqs delete-message --queue-url http://localhost:4576/queue/queue_name --receipt-handle 'yugzzebhnnksfuvbjlibfnlejyqbulxqfegsnrgafjeabxaatxbmeiyfkfliedslohseosgjwkxhdzllpudhccjhorpkwbgjgyzeyzjwkfvqflathnvsmugeyevbqmfyqanuxetvdhcetseuwzrqpexogzggznndxmbqowtlalvqtffntdahhihel'
```

## Set up

### AWS Credentials
The application uses your **default** credential profile by reading from the credentials file located at (~/.aws/credentials).
For more information about see [Configuration and Credential Files](http://docs.aws.amazon.com/cli/latest/userguide/cli-config-files.html)

### Application Config
Look to _application.yml_.
```
mongoDB:
  database: bookDB
  host: 127.0.0.1
  port: 27017
  
sqs:
  queueUrl: https://sqs.us-east-2.amazonaws.com/404669482207/book-track.fifo
```
- **sqs.queueUrl:** Url to AWS SQS queue. Amazon SQS assigns each queue you create an identifier called a queue URL that includes the queue name and other Amazon SQS components. Whenever you want to perform an action on a queue, you provide its queue URL.. In your system, always store the entire queue URL exactly as Amazon SQS returns it to you when you create the queue (for example, http://sqs.us-east-2.amazonaws.com/123456789012/queue2). Don't build the queue URL from its separate components each time you need to specify the queue URL in a request because Amazon SQS can change the components that make up the queue URL. The profiles mentioned in AWS mock provides a queueURL from aws commands when using LocalStack, so queueUrl when using localStack must be the same as the one provided in AWS commands
- **mongoDB.database:** Type name used to persist the document.
- **mongoDB.host:** Host to access the MongoDB. Normally 127.0.0.1 when we are running it locally.
- **mongoDB.port:** Host to access the MongoDB. Normally 27017 when we are running it locally.

## Build

`$ gradle build`

## Run (local profile is optional)

`$ gradle [-Dspring.profiles.active=local] bootRun`

## Explore

Go to http://localhost:8080/books from a browser or Postman.
