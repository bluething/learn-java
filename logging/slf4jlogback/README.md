### Configuration

To use SLF4J with Logback, we only need to include  
```text
dependencies {
    implementation 'ch.qos.logback:logback-classic:1.2.3'
}
```  
in your dependencies list. Logback is already using SLF4J. We can configure Logback either programmatically or with a configuration script.  
How Logback configure itself:  
1. Try to find logback-test.xml in the classpath.  
2. If not found, try to find logback.groovy in the classpath.  
3. If not found, try to find logback.xml in the classpath.  
4. If no such file found, using service-provider loading facility to resolve the implementation of com.qos.logback.classic.spi.Configurator interface by looking up the file META-INF\services\ch.qos.logback.classic.spi.Configurator in the class path.  
5. If all above failed, logback configures itself automatically using the BasicConfigurator which will cause logging output to be directed to the console (ConsoleAppender attached to the root logger). By default, the root logger is assigned the DEBUG level.

References:
* [Chapter 3: Logback configuration](http://logback.qos.ch/manual/configuration.html)