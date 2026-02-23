package framework.context;

public interface Context {
    <T> T getBean(Class<T> beanClass);
    Object getBean(String name);
}