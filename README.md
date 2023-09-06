1) create docker compose file with redis and beanstalkd
2) create app, that expose endpoints for pushing message to redis or beanstalk. Also set up listeners, that will pull print messages from queues and print them
3) use JMeter to write simpe test (test attached). This test will push message to redis and beanstalk through app endpoints.

## Redis set up
- Redis RDB: 'save 60 1000' - save every 60 seconds or 10000 requests.
- Redis AOF: 'appendonly yes' - append all to the file - slowest approach.
## Beanstalk set up
- default setup

## Endpoints:
- POST http://localhost:8080/api/redis
- POST http://localhost:8080/api/beanstalkd
- Body example: {"key": "one", "name": "Maxim"}

## Test set up:
- Number of threads (users) : 700
- Ramp-up period: 3 seconds
- Loop count (how many times each thread will repeat request): 1000

## Result:

| Queue name | Time (sec) |
|------------|------------|
| Redis RDB  | 2.29       |
| Redis AOF  | 12.5       |
| Beanstalkd | 10.5       |


## Analysis
Redis in RDB mode works the fastest.

## PS
I suspect my Beanstalkd is slowens that it should be. Maybe because of the library I use, or the way I measure it.