# Camel Project for Workflow Modeling and Process Management 2015S
## Project Info

Groupnumber: 08  
Topic: HR-department

## Install Instructions

Install [Maven](http://maven.apache.org/download.cgi) on your system and check if it is correctly installed with:
```shell
mvn --version
```

### Optional MongoDB installation to test the hosted DB
Install [MongoDB](http://docs.mongodb.org/v2.6/) locally to try out some requests. You can connect over the shell via
```shell
mongo ds031802.mongolab.com:31802/hrdep -u <dbuser> -p <dbpassword>
```
You can also connect using a driver via the standard URI:
```
mongodb://<dbuser>:<dbpassword>@ds031802.mongolab.com:31802/hrdep
```

## Build and run with Maven

To build this project use
  ```shell
  mvn install
  ```

To run this project with Maven use
  ```shell
  mvn compile exec:java
  ```
## Examples
Good simple example: https://github.com/vrto/apache-camel-invoices  
Original Camel examples: https://github.com/apache/camel/tree/master/examples  

For more help see the Apache Camel documentation http://camel.apache.org/  
