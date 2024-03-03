## Redis Performance Playground
An application developed using Spring Boot, Redis and PostgreSQL to measure data transaction performance. 
This will cover the following data handling concepts.
- Perform CRUD operations without Redis cache (i.e. fetch data directly from Postgres DB)
- Perform data operation with Redis in place (centralized cache server for all app instances). 
- Using local In memory-cache strategy (local cache per app instance as opposed to a centralize Redis cache). The local cache of each instance will remain in-sync when data is updated by any instance.

### Running the app
**Step 1:** Go to `docker` directory and in the terminal run `docker-compose up` in order to bring up Postgres and Redis servers.

**Step 2:** Build and run the Spring boot Application via intelliJ.

### App data creation
The sample app data will be generated based on the schema `src/main/resources/schema.sql` and the data creation mechanism `src/main/java/blog/yrol/service/DataSetupService.java`.


### Calling the API endpoints
The following endpoints can be called for both v1 and v2 to GET and PUT products via Postman.

- `GET` v1: http://localhost:8080/product/v1/1
- `GET` v2: http://localhost:8080/product/v2/1

- `PUT` v1: http://localhost:8080/product/v1/1
- `PUT` v2: http://localhost:8080/product/v2/1

Payload for PUT
```
{
    "id": 1,
    "description": "product-1",
    "price": 15.0
}
```

![](https://i.imgur.com/0aAduFm.png)

![](https://i.imgur.com/PnILmQa.png)


### Accessing Redis data
UI interface Redis Commander: `http://localhost:8081/`. Username: `root`, Password `password`.
![](https://i.imgur.com/IxvSaxp.png)

Using the command line
**Step 1:** Accessing the Redis docker instance using the command:
```
docker exec -it redis bash
```

**Step 2:** Accessing the Redis CLI using the command:
```
redis-cli
```

**Step 3:** Accessing data

Getting all products
```
 hgetall product
```

Getting a specific product by key
```
hget product 955
```

### Accessing Postgres data
UI interface PG admin: `http://localhost:9000/`. Username: `admin@admin.com`, Password `admin`.
![](https://i.imgur.com/LxffsLh.png)

Registering a server using following information.
- Name: Redis Performance
- Hostname: postgres
- Port 5432
- Maintenance database: postgres
- Username: postgres
- Password: postgres

![](https://i.imgur.com/cGx9rNE.png)

![](https://i.imgur.com/ZW7SGqo.png)

![](https://i.imgur.com/dgCCZVq.png)


Additionally, Postgres data can also be accessed via its Docker container shell.


### Measuring performance
The performance of each data handling strategy is measured via [JMeter](https://jmeter.apache.org/download_jmeter.cgi).
The following actions can be performed while the app is running. The performance metrics will provide an overview of the throughput for both GET and PUT calls.
- v1 script: measure performance of the Postgres only implementation
- v2 script: measure performance of the Redis and Postgres implementation

**Running tests via JMeter UI**

**Step 1:** Download JMeter and from the above link and once unzipped go to the `/bin` folder and execute the following command.
```
./jmeter
```

**Step 2:** Load any of the following scripts located in `/JMeter` folder into JMeter.
- `product-service_v1.jmx`
- `product-service_v2.jmx`

![](https://i.imgur.com/VB3zmpk.png)

**Step 3:** Changing configs - both GET and PUT tests are set run for 5 mins (300 sec). This can be changes as required.

![](https://i.imgur.com/ACrdQ1C.png)

**Step 4:** Running test cases - click on the play button to run the test cases.
![](https://i.imgur.com/HLPq0D8.png)


**Running tests via JMeter commandline**

Download JMeter and from the above link and once unzipped go to the `/bin` folder and execute the following command.
```
./jmeter -n -t ~/<local_directory>/redis-performance/JMeter/product-service_v2.jmx -l ~/<local_directory>/Redis/redis-performance/JMeter/v2.jtl
```

The script location and result output location must be provided as shown.


### Test results
To access reports, click on the 'Aggregate Report' option in JMeter. Additionally, reports can also be loaded locally when generated via command-line tests

![](https://i.imgur.com/wbxiEQ5.png)

![](https://i.imgur.com/jXfSy76.png)

As shown, the reports will show:
- Maximum throughput per second (in this case for both GET & PUT)
- Median
- 90, 95 & 99 percentile






