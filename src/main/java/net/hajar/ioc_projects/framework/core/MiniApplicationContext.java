package net.hajar.ioc_projects.framework.core;

import net.hajar.ioc_projects.framework.annotations.Autowired;
import net.hajar.ioc_projects.framework.annotations.Component;
import net.hajar.ioc_projects.framework.annotations.Qualifier;
import net.hajar.ioc_projects.framework.xml.BeanConfig;
import net.hajar.ioc_projects.framework.xml.BeanDefinition;
import net.hajar.ioc_projects.framework.xml.PropertyDefinition;
import net.hajar.ioc_projects.framework.xml.XmlConfigLoader;

import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;

public class MiniApplicationContext {

    // All beans stored here: "d" -> DaoImpl object
    private Map<String, Object> context = new HashMap<>();

    // ─────────────────────────────────────────────────
    // VERSION 1: XML
    // ─────────────────────────────────────────────────
    public MiniApplicationContext(String xmlFile) throws Exception {
        XmlConfigLoader loader = new XmlConfigLoader();
        BeanConfig config = loader.load(xmlFile);

        // STEP 1: Create all objects
        for (BeanDefinition bean : config.getBeans()) {
            Class<?> cls = Class.forName(bean.getClassName());
            Object instance = createInstance(cls, bean);
            context.put(bean.getId(), instance);
        }

        // STEP 2: Inject dependencies
        for (BeanDefinition bean : config.getBeans()) {
            if (bean.getProperties() != null) {
                Object instance = context.get(bean.getId());
                for (PropertyDefinition prop : bean.getProperties()) {
                    injectDependency(instance, prop);
                }
            }
        }
    }

    // ─────────────────────────────────────────────────
    // VERSION 2: ANNOTATIONS — scan a package
    // ─────────────────────────────────────────────────
    public MiniApplicationContext(Class<?> basePackageClass) throws Exception {
        String packageName = basePackageClass.getPackageName();
        List<Class<?>> classes = scanPackage(packageName);

        // STEP 1: Find all @Component classes and create instances
        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(Component.class)) {
                Component comp = cls.getAnnotation(Component.class);
                String beanId = comp.value().isEmpty()
                        ? cls.getSimpleName().substring(0,1).toLowerCase()
                          + cls.getSimpleName().substring(1)
                        : comp.value();
                Object instance = createInstanceFromAnnotation(cls);
                context.put(beanId, instance);
            }
        }

        // STEP 2: Inject into fields marked @Autowired
        for (Object instance : context.values()) {
            injectFieldDependencies(instance);
        }
    }

    // ─────────────────────────────────────────────────
    // OBJECT CREATION — XML version
    // Supports: constructor injection OR no-arg
    // ─────────────────────────────────────────────────
    private Object createInstance(Class<?> cls, BeanDefinition bean)
            throws Exception {

        // Try constructor injection first
        // (if bean has <constructor-arg ref="..."/>)
        if (bean.getProperties() != null) {
            for (Constructor<?> constructor : cls.getConstructors()) {
                if (constructor.getParameterCount() == bean.getProperties().size()) {
                    // build args array from context
                    Object[] args = new Object[constructor.getParameterCount()];
                    List<PropertyDefinition> props = bean.getProperties();
                    for (int i = 0; i < props.size(); i++) {
                        args[i] = context.get(props.get(i).getRef());
                    }
                    try {
                        return constructor.newInstance(args);
                    } catch (Exception ignored) {}
                }
            }
        }

        // Fallback: no-arg constructor
        return cls.getDeclaredConstructor().newInstance();
    }

    // ─────────────────────────────────────────────────
    // SETTER INJECTION — XML version
    // <property name="dao" ref="d"/> → calls setDao(daoObject)
    // ─────────────────────────────────────────────────
    private void injectDependency(Object instance, PropertyDefinition prop)
            throws Exception {

        Object dependency = context.get(prop.getRef());
        if (dependency == null) return;

        // Build setter name: "dao" → "setDao"
        String setterName = "set"
                + prop.getName().substring(0, 1).toUpperCase()
                + prop.getName().substring(1);

        // Find the setter
        for (Method m : instance.getClass().getMethods()) {
            if (m.getName().equals(setterName) && m.getParameterCount() == 1) {
                m.invoke(instance, dependency);
                return;
            }
        }

        // If no setter found → try FIELD injection directly
        try {
            Field field = instance.getClass().getDeclaredField(prop.getName());
            field.setAccessible(true); // bypass private
            field.set(instance, dependency);
        } catch (NoSuchFieldException ignored) {}
    }

    // ─────────────────────────────────────────────────
    // OBJECT CREATION — Annotation version
    // Supports constructor injection via @Autowired
    // ─────────────────────────────────────────────────
    private Object createInstanceFromAnnotation(Class<?> cls) throws Exception {

        // Look for a constructor with @Autowired or single constructor with params
        for (Constructor<?> constructor : cls.getConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)
                    || constructor.getParameterCount() > 0) {

                Parameter[] params = constructor.getParameters();
                Object[] args = new Object[params.length];

                for (int i = 0; i < params.length; i++) {
                    if (params[i].isAnnotationPresent(Qualifier.class)) {
                        // @Qualifier("d") → get bean named "d"
                        String beanId = params[i]
                                .getAnnotation(Qualifier.class).value();
                        args[i] = context.get(beanId);
                    } else {
                        // No qualifier → find by type
                        args[i] = getBeanByType(params[i].getType());
                    }
                }

                return constructor.newInstance(args);
            }
        }

        // No special constructor → use no-arg
        return cls.getDeclaredConstructor().newInstance();
    }

    // ─────────────────────────────────────────────────
    // FIELD INJECTION — Annotation version
    // @Autowired private IDao dao; → inject directly
    // ─────────────────────────────────────────────────
    private void injectFieldDependencies(Object instance) throws Exception {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Object dependency;

                if (field.isAnnotationPresent(Qualifier.class)) {
                    // @Qualifier("d") → get by name
                    String beanId = field.getAnnotation(Qualifier.class).value();
                    dependency = context.get(beanId);
                } else {
                    // No qualifier → find by type
                    dependency = getBeanByType(field.getType());
                }

                field.setAccessible(true); // bypass private
                field.set(instance, dependency);
            }
        }
    }

    // ─────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────

    // Find a bean by its type (interface or class)
    private Object getBeanByType(Class<?> type) {
        for (Object bean : context.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return bean;
            }
        }
        return null;
    }

    // Get bean by ID
    public Object getBean(String id) {
        return context.get(id);
    }

    // Get bean by type
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        for (Object bean : context.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return (T) bean;
            }
        }
        throw new RuntimeException("No bean found for: " + type.getName());
    }

    // Scan all classes in a package
    private List<Class<?>> scanPackage(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread()
                .getContextClassLoader().getResource(path);
        if (resource == null) return classes;

        File directory = new File(resource.toURI());
        scanDirectory(directory, packageName, classes);
        return classes;
    }

    private void scanDirectory(File dir, String packageName,
                               List<Class<?>> classes) throws Exception {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(file,
                        packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "."
                        + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
    }
}