package net.hajar.ioc_projects.framework.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import net.hajar.ioc_projects.framework.xml.BeanDefinition;

import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)

public class BeanConfig {
    @XmlElement(name = "bean")
    private List<BeanDefinition> beans;

    public List<BeanDefinition> getBeans() {
        return beans;
    }

    public void setBeans(List<BeanDefinition> beans) {
        this.beans = beans;
    }
}
