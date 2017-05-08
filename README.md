# Connection Cassandra and PostgreSQL using Spring MVC4
Totem Power Internship Assignment

> You may use this on Ubuntu environment.

## Step 1 : Environment Setting

1. Run Cassandra Database.
- Type "sudo cassandra" on Ubuntu bash shell.
- You may use the following table structures.

```SQL
CREATE TABLE totem.deviceInfo (
    totemDevice text,
    timeStamp timestamp,
    datePartition text,
    date text,
    amp double,
    volt double,
    PRIMARY KEY (datePartition, timeStamp)
);

CREATE TABLE totem.tempInfo (
    totemDevice text,
    timeStamp timestamp,
    datePartition text,
    date text,
    amp double,
    volt double,
    PRIMARY KEY (datePartition, timeStamp)
);
```

2. Run PostgreSQL Database.
- You may login psql using ID as wonje
- You should make a table structure before run this program.
- You may use postgres as DB and "wonje" as ID.
- You may use the following table structure.
- It will collect average data set from input data in every 5 minutes.

```SQL
CREATE TABLE deviceInfo (
    equip_id serial PRIMARY KEY,
    totemDevice varchar (50) NOT NULL,
    timeStamp timestamptz NOT NULL,
    watts float (10) NOT NULL
);
```

3. Data Set
- Put the data set file "grid_stats.csv" into the adequate path.
- The path is "\TotemSpringMVC4\src\test\resources"
- You must put the data set before running this project. I did not upload this.

```
micrototem1,0-grid,2016-03-01,1474881547339,8.22222,9.0848

The format is "TotemDevice, [IGNORE VALUE], Date, Timestamp (UTC), Amp, Volt"
```

## Step 2 : Run TotemSpringMVC4.

1. You may use Intellij IDE to run this project.

2. Run Apache Tomcat 8, using this project on Intellij IDE.

3. Run "TotemDataUploadAgent.java" agent.
- This is located in "com.wonje.springmvc" package in Test Java folder.
- It will insert all of data set into Cassandra.

4. Then, Cassandra collects the data set.
- deviceInfo Table : All of data set
- tempInfo Table : data to be stored into PostgreSQL

5. Data transfer from Cassandra to PostgreSQL in every 5 minutes.
- All data in tempInfo table transfer to PostgreSQL.
- The tempInfo table delete all of data itself.

6. After that, Refresh the webpage. Then, you will see updated Highchart.

## Step 3 : Run index.jsp

>You may see index.jsp page.
- Datepicker : Choose 'StartDate' and 'EndDate' to confine displayed data on Highcharts for Cassandra.
- Highcharts for Cassandra : display all of data set for the chosen date.
- Highcharts for PostgreSQL : display all of average data set stored in every 5 minutes.

