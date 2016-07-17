package com.fasterxml.jackson.databind.ext;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.ser.Serializers;
import java.io.Serializable;

public class OptionalHandlerFactory implements Serializable {
    private static final String CLASS_NAME_DOM_DOCUMENT = "org.w3c.dom.Node";
    private static final String CLASS_NAME_DOM_NODE = "org.w3c.dom.Node";
    private static final String DESERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
    private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer";
    private static final String DESERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer";
    private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
    private static final String SERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
    private static final String SERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMSerializer";
    public static final OptionalHandlerFactory instance;
    private static final long serialVersionUID = 1;

    static {
        instance = new OptionalHandlerFactory();
    }

    protected OptionalHandlerFactory() {
    }

    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
        Class<?> rawType = type.getRawClass();
        String className = rawType.getName();
        if (doesImplement(rawType, CLASS_NAME_DOM_NODE)) {
            return (JsonSerializer) instantiate(SERIALIZER_FOR_DOM_NODE);
        }
        if (!className.startsWith(PACKAGE_PREFIX_JAVAX_XML) && !hasSupertypeStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) {
            return null;
        }
        Object ob = instantiate(SERIALIZERS_FOR_JAVAX_XML);
        if (ob != null) {
            return ((Serializers) ob).findSerializer(config, type, beanDesc);
        }
        return null;
    }

    public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        Class<?> rawType = type.getRawClass();
        if (rawType.getName().startsWith(PACKAGE_PREFIX_JAVAX_XML) || hasSupertypeStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) {
            Object ob = instantiate(DESERIALIZERS_FOR_JAVAX_XML);
            if (ob == null) {
                return null;
            }
            return ((Deserializers) ob).findBeanDeserializer(type, config, beanDesc);
        } else if (doesImplement(rawType, CLASS_NAME_DOM_NODE)) {
            return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_DOCUMENT);
        } else {
            if (doesImplement(rawType, CLASS_NAME_DOM_NODE)) {
                return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_NODE);
            }
            return null;
        }
    }

    private Object instantiate(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (LinkageError e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    private boolean doesImplement(Class<?> actualType, String classNameToImplement) {
        Class<?> type = actualType;
        while (type != null) {
            if (type.getName().equals(classNameToImplement) || hasInterface(type, classNameToImplement)) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    private boolean hasInterface(Class<?> type, String interfaceToImplement) {
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> iface : interfaces) {
            if (iface.getName().equals(interfaceToImplement)) {
                return true;
            }
        }
        for (Class<?> iface2 : interfaces) {
            if (hasInterface(iface2, interfaceToImplement)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSupertypeStartingWith(Class<?> rawType, String prefix) {
        for (Class<?> supertype = rawType.getSuperclass(); supertype != null; supertype = supertype.getSuperclass()) {
            if (supertype.getName().startsWith(prefix)) {
                return true;
            }
        }
        for (Class<?> cls = rawType; cls != null; cls = cls.getSuperclass()) {
            if (hasInterfaceStartingWith(cls, prefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInterfaceStartingWith(Class<?> type, String prefix) {
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> iface : interfaces) {
            if (iface.getName().startsWith(prefix)) {
                return true;
            }
        }
        for (Class<?> iface2 : interfaces) {
            if (hasInterfaceStartingWith(iface2, prefix)) {
                return true;
            }
        }
        return false;
    }
}
