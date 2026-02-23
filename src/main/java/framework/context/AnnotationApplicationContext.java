package framework.context;

import framework.annotations.Autowired;
import framework.annotations.Component;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationApplicationContext implements Context {
    private Map<String, Object> instances = new HashMap<>();

    public AnnotationApplicationContext(String... packages) {
        try {
            for (String pkg : packages) {
                scanPackage(pkg);
            }
            performInjection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanPackage(String pkg) throws Exception {
        String path = pkg.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File directory = new File(classLoader.getResource(path).getFile());

        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = pkg + "." + file.getName().replace(".class", "");
                Class<?> cls = Class.forName(className);

                if (cls.isAnnotationPresent(Component.class)) {
                    Object instance = cls.getDeclaredConstructor().newInstance();
                    instances.put(cls.getName(), instance);
                    // Also store by interface type if possible
                    for (Class<?> iface : cls.getInterfaces()) {
                        instances.put(iface.getName(), instance);
                    }
                }
            }
        }
    }

    private void performInjection() throws Exception {
        for (Object instance : instances.values()) {
            // Look at every variable (field) inside the class (like 'private IDao dao')
            for (java.lang.reflect.Field field : instance.getClass().getDeclaredFields()) {

                // 1. Check if the field has YOUR @Autowired label
                if (field.isAnnotationPresent(framework.annotations.Autowired.class)) {

                    // 2. Find the dependency (the DaoImpl) in our instances map
                    // We search by the interface name: "dao.IDao"
                    Object dependency = instances.get(field.getType().getName());

                    if (dependency != null) {
                        // 3. THIS IS THE MAGIC: Allow access to the private field
                        field.setAccessible(true);

                        // 4. Inject it! (Put the Dao instance into the Metier instance)
                        field.set(instance, dependency);
                    }
                }
            }
        }
    }
    @Override
    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(instances.get(beanClass.getName()));
    }

    @Override
    public Object getBean(String name) {
        return instances.get(name);
    }
}