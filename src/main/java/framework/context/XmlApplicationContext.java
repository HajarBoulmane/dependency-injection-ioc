package framework.context;

import framework.xml.BeanModel;
import framework.xml.BeansList;
import framework.xml.BeanModel.PropertyModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class XmlApplicationContext implements Context {
    private Map<String, Object> instances = new HashMap<>();

    public XmlApplicationContext(String xmlFile) {
        try {
            // 1. JAXB Unmarshalling
            File file = new File(getClass().getClassLoader().getResource(xmlFile).getFile());
            JAXBContext jaxbContext = JAXBContext.newInstance(BeansList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            BeansList beansList = (BeansList) unmarshaller.unmarshal(file);

            // 2. First Pass: Create all instances
            for (BeanModel bean : beansList.getBeans()) {
                Class<?> cls = Class.forName(bean.getClassName());
                Object instance = cls.getDeclaredConstructor().newInstance();
                instances.put(bean.getId(), instance);
            }

            // 3. Second Pass: Perform Injection (Setter Injection)
            for (BeanModel beanModel : beansList.getBeans()) {
                if (beanModel.getProperties() != null) {
                    Object instance = instances.get(beanModel.getId());

                    for (PropertyModel prop : beanModel.getProperties()) {
                        Object dependency = instances.get(prop.getRef());

                        // Convert "dao" -> "setDao"
                        String setterName = "set" + prop.getName().substring(0, 1).toUpperCase()
                                + prop.getName().substring(1);

                        // Find the setter method and invoke it
                        // We assume the setter takes the Interface as a parameter
                        Method setter = instance.getClass().getMethod(setterName,
                                dependency.getClass().getInterfaces()[0]);
                        setter.invoke(instance, dependency);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        return instances.values().stream()
                .filter(beanClass::isInstance)
                .map(beanClass::cast)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Object getBean(String name) {
        return instances.get(name);
    }
}