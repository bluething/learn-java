package io.github.bluething.java.heapdump.simulateclassloadermemoryleak;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

public class ApplicationRunner {
    public static void main(String[] args) throws IOException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        final String pluginName = "io.github.bluething.java.heapdump.simulateclassloadermemoryleak.ExamplePlugin";

        while (true) {
            System.out.println("Press any key to reload the plugins");
            System.in.read();

            final URL[] urls = { new File("heapdump/simulateclassloadermemoryleak/build/classes/java/main").toURI().toURL()};
            final URLClassLoader classLoader = new URLClassLoader(urls, null);
            final Class<?> aClass = classLoader.loadClass(pluginName);
            final Object plugin = aClass.newInstance();
            aClass.getMethod("initialize").invoke(plugin);
        }
    }
}
