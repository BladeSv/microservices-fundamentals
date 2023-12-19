# microservices-fundamentals-learn

# Task2

## What to do

In this module it is needed to adjust services created in the first module with adding cross-servers calls.

## Sub-task 1: Asynchronous communication

1. Add asynchronous communication via messaging broker between **Resource Service** and **Resource Processor**.
2. On resource uploading, **Resource Service** should send information about uploaded resource to the **Resource
   Processor**, which contains “resourceId”.

[Rabbit MQ](https://hub.docker.com/_/rabbitmq), [ActiveMQ](https://hub.docker.com/r/rmohr/activemq) or any other broker
usage is possible.

## Sub-task 2: Events handling

1. When the **Resource Processor** has an event of receiving message, it uses a synchronous call to get the resource
   data (binary) from the **Resource Service**, parses the metadata, and uses the synchronous call to save the metadata
   of the song in the **Song. Service**.
2. Need to implement some way of queue listening/subscription. For
   example, [Rabbit Spring Streams](https://docs.spring.io/spring-cloud-stream-binder-rabbit/docs/current/reference/html/spring-cloud-stream-binder-rabbit.html)
   .

## Sub-task 3: Retry mechanism

While implementing communications between services it’s necessary to think about implementation of **Retry Mechanism**,
e.g: [Retry Pattern](https://docs.microsoft.com/en-us/azure/architecture/patterns/retry). Implementation can be based on
the [Spring Retry Template](https://docs.spring.io/spring-batch/docs/current/reference/html/retry.html) or annotations
for both synchronous and asynchronous communication.
![img.png](assets/img.png)
**Note**

For this module you could use any of the messaging brokers for asynchronous communication (it’s better to discuss with
expert).

# Task 1

## What to do

In this module you will need to create base structure of microservices system. During this task you need to implement
the next three services:

- Resource service
- Song service
- Resource processor

## Sub-task 1: Resource service

For a Resource service, it is recommended to implement a service with CRUD operations for processing mp3 files. This
service will be used to store data. You should also use cloud storage or its emulation (
e.g. [S3 emulator](https://github.com/localstack/localstack)) to store the source file. Resource tracking (with resource
location in the cloud storage) should be carried out in the underlying database of the service.

**Service definition could be next:**

<table dir="auto"><tbody><tr><td><b>POST /resources</b></td><td colspan="6"><b>Upload new resource</b></td></tr><tr><td rowspan="2"><b>Request</b></td><td><i>Parameter</i></td><td><i>Description</i></td><td><i>Restriction</i></td><td><i>Body example</i></td><td><i>Description</i></td><td><i>Restriction</i></td></tr><tr><td></td><td></td><td></td><td>Audio data binary</td><td>Content type – audio/mpeg</td><td>MP3 audio data</td></tr><tr><td rowspan="2"><b>Response</b></td><td><i>Body</i></td><td><i>Description</i></td><td colspan="4"><i>Code</i></td></tr><tr><td><p>{</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"id":1123</p><p>}</p></td><td>Integer id — ID of the created resource</td><td colspan="4"><p>200 – OK</p><p>400 – Validation failed or request body is invalid MP3</p><p>500 – An internal server error has occurred</p></td></tr><tr><td><b>GET /resources/{id}</b></td><td colspan="6"><b>Get the binary audio data of a resource(s)</b></td></tr><tr><td rowspan="3"><b>Request</b></td><td><i>Parameter</i></td><td><i>Description</i></td><td><i>Restriction</i></td><td><i>Body example</i></td><td><i>Description</i></td><td><i>Restriction</i></td></tr><tr><td>Integer id</td><td>The ID of the resource to get</td><td>ID of an existing resource</td><td></td><td></td><td></td></tr><tr><td>HTTP Header Range</td><td>Range of resources to get</td><td>Optional, all if empty</td><td></td><td></td><td></td></tr><tr><td rowspan="2"><b>Response</b></td><td><i>Body</i></td><td><i>Description</i></td><td colspan="4"><i>Code</i></td></tr><tr><td>Audio bytes</td><td></td><td colspan="4"><p>200 – OK</p><p>206 – Partial content (if a range is requested)</p><p>404 – The resource with the specified id does not exist</p><p>500 – An internal server error has occurred</p></td></tr><tr><td><b>DELETE /resources?id=1,2</b></td><td colspan="6"><b>Delete a resource(s). If there is no resource for id, do nothing</b></td></tr><tr><td rowspan="2"><b>Request</b></td><td><i>Parameter</i></td><td><i>Description</i></td><td><i>Restriction</i></td><td><i>Body example</i></td><td><i>Description</i></td><td><i>Restriction</i></td></tr><tr><td>String id</td><td>CSV (Comma Separated Values) of resource IDs to remove</td><td>Valid CSV length &lt; 200 characters</td><td></td><td></td><td></td></tr><tr><td rowspan="2"><b>Response</b></td><td><i>Body</i></td><td><i>Description</i></td><td colspan="4"><i>Code</i></td></tr><tr><td><p>{</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"ids": [1,2]</p><p>}</p></td><td>Integer [] ids – ids of deleted resources</td><td colspan="4"><p>200 – OK</p><p>500 – An internal server error has occurred</p></td></tr></tbody></table>

## Sub-task 2: Song service

For the Song service, it is recommended to implement a simple CRUD service to manage the song record (metadata). The
service should provide the ability to manage some metadata about the songs (artist, album, etc.). Make sure the service
is still available over HTTP.

**Service definition could be next:**

<table dir="auto"><tbody><tr><td><b>POST /songs</b></td><td colspan="6"><b>Create a new song metadata record in database</b></td></tr><tr><td rowspan="2"><b>Request</b></td><td><i>Parameter</i></td><td><i>Description</i></td><td colspan="2"><i>Body example</i></td><td><i>Description</i></td><td><i>Restriction</i></td></tr><tr><td></td><td></td><td colspan="2"><p>{</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"name": "We are the champions",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"artist": "Queen",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"album": "News of the world",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"length": "2:59",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"resourceId": "123",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"year": ""1977</p><p>}</p></td><td>Song metadata record, referencing to resource id (mp3 file itself)</td><td>MP3 audio data</td></tr><tr><td rowspan="2"><b>Response</b></td><td><i>Body</i></td><td><i>Description</i></td><td colspan="4"><i>Code</i></td></tr><tr><td><p>{</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"id":1123</p><p>}</p></td><td>Integer id – ID of the created song metadata</td><td colspan="4"><p>200 – OK</p><p>400 – Song metadata missing validation error</p><p>500 – An internal server error has occurred</p></td></tr><tr><td><b>GET /songs/{id}</b></td><td colspan="6"><b>Get song metadata</b></td></tr><tr><td rowspan="2"><b>Request</b></td><td><i>Parameter</i></td><td><i>Description</i></td><td><i>Restriction</i></td><td><i>Body example</i></td><td><i>Description</i></td><td><i>Restriction</i></td></tr><tr><td>Integer id</td><td>Song metadata ID to get</td><td>ID of an existing song metadata</td><td></td><td></td><td></td></tr><tr><td rowspan="2"><b>Response</b></td><td colspan="2"><i>Body</i></td><td colspan="2"><i>Description</i></td><td colspan="2"><i>Code</i></td></tr><tr><td colspan="2"><p>{</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"name": "We are the champions",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"artist": "Queen",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"album": "News of the world",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"length": "2:59",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"resourceId": "123",</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"year": ""1977</p><p>}</p></td><td colspan="2"></td><td colspan="2"><p>200 – OK</p><p>404 – The song metadata with the specified id does not exist</p><p>500 – An internal server error has occurred</p></td></tr><tr><td><b>DELETE /songs?id=1,2</b></td><td colspan="6"><b>Delete a song(s) metadata. If there is no song metadata for id, do nothing</b></td></tr><tr><td rowspan="2"><b>Request</b></td><td><i>Parameter</i></td><td><i>Description</i></td><td><i>Restriction</i></td><td><i>Body example</i></td><td><i>Description</i></td><td><i>Restriction</i></td></tr><tr><td>String id</td><td>CSV of song metadata IDs to remove</td><td>Valid CSV length &lt; 200 characters</td><td></td><td></td><td></td></tr><tr><td rowspan="2"><b>Response</b></td><td colspan="2"><i>Body</i></td><td colspan="2"><i>Description</i></td><td colspan="2"><i>Code</i></td></tr><tr><td colspan="2"><p>{</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"ids": [1,2]</p><p>}</p></td><td colspan="2">Integer [] ids - IDs of deleted resources</td><td colspan="2"><p>200 – OK</p><p>500 – An internal server error has occurred</p></td></tr></tbody></table>

## Sub-task 3: Resource processor

This service will be used to process the source MP3 data in the future and will not have a web interface. At this point,
this should be a basic Spring Boot application capable of extracting MP3 metadata for further storage using the Song
service API.

Implement initial version of each service:

- Basic structure (Spring Boot)

**Note**

As a database, it is best to use Docker database/storage containers (
e.g. [postgres image](hhttps://hub.docker.com/_/postgres)) in the implementation.
