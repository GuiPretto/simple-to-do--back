# Simple To-Do API

Back-End application built in Java, using SpringBoot and PostgreSQL.

## Installation

### Eclipse
1. Install Eclipse IDE for Enterprise Java Developers.
2. Install [Lombok](https://projectlombok.org/).
3. Download and extract this repository.
4. In Eclipse, File -> Import -> Maven -> Existing Maven Projects.
5. Select the repository extracted folder, then click Finish.

Obs.: If the project shows some errors, you probably don't have JUnit 5. To fix this right-click the project and go: Properties -> Java Build Path -> Libraries. Select 'Classpath' and choose Add Library -> JUnit -> JUnit library version: JUnit 5 -> Finish. Finally, right-click the project again and go: Maven -> Update Project -> Ok.

### PostgreSQL
1. Execute [PostgreSQL 13.0](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads) installer. For password, type 'postgres', the same as the user, and for port, use the default '5432'.
2. After installation ends, start pgAdmin4 and search for 'postgres' default database. Then right-click and choose 'Query Tool'.
3. Copy and paste the first line of the [schema](https://github.com/GuiPretto/simple-to-do--back/blob/main/schema.sql) file and execute the query.
4. Then, right-click the newly created database 'simpletodo' and choose once again 'Query Tool'.
5. Finally, copy and paste the rest of the [schema](https://github.com/GuiPretto/simple-to-do--back/blob/main/schema.sql) file and execute the query.

Finally, run the main application file as 'Java Application' on Eclipse.

## About this project

This project was mainly made using the Java framework SpringBoot, including Mockito and JUnit 5 for the tests. 


## Front-End

The Front-End project can be found here:

[https://github.com/GuiPretto/simple-to-do--front](https://github.com/GuiPretto/simple-to-do--front)

