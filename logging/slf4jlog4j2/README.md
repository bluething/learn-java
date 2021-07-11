### Configuration

To use Log4j 2 over SLF4J we need add the following libraries:  
* log4j-api  
* log4j-core  
* log4j-slf4j18-impl  
  
log4j-slf4j18-impl is a Log4j 2 SLF4J Binding that allow us to use SLF4J API and use Log4j 2 as the implementation.

We need to configure Logger and Appender. Logger is an entry point to logging library. Appender define where we want to send the log. Add log4j2.xml  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration xmlns="http://logging.apache.org/log4j/2.0/config">
    <Appenders>
        
        <Console name="stdout" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %p %m%n"/>
        </Console>
        
    </Appenders>
    <Loggers>
        
        <Logger name="io.github.bluething.java.logging.slf4jlog4j2" level="INFO" additivity="false">
            <AppenderRef ref="stdout"/>
        </Logger>

        <Root level="OFF">
            <AppenderRef ref="stdout"/>
        </Root>
        
    </Loggers>
</Configuration>
```  
We set additivity to false. Additivity mean log message will be pass to parent logger, we set false, so we don't sent the log to root appender. The level is INFO, so any logs with FATAL, ERROR, WARN and INFO will appear in console.

### Log level

Log4j have 8 log levels

Standard level | Integer value
---------------|---------------   
OFF | 0  
FATAL | 100
ERROR | 200  
WARN | 300  
INFO | 400
DEBUG | 500  
TRACE | 600  
ALL | Integer.MAX_VALUE

Each level has integer value, this value can be use when we need to create custom log level.

### Lazy evaluation

If we have some expression to print out, we can use lazy logging. With SLF4J 2 we can utilize Java 8 Lambda, but this version still experimental state (26-12-2020). For now, we can code like this  
```java
        int counter = 1;
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("The counter {}", ++counter);
        }
```

References:  
* [Log4j 2 SLF4J Binding](https://logging.apache.org/log4j/2.x/log4j-slf4j-impl/)
* [Log4j 2 Log Levels](https://logging.apache.org/log4j/2.x/manual/customloglevels.html)

