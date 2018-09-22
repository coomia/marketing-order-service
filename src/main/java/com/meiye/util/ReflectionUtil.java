package com.meiye.util;

import com.meiye.annotation.DateFormat;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lance Chen
 */
public class ReflectionUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private ReflectionUtil() {
    }

    /**
     * Get the non-static fields by class, including all supper class fields.
     *
     * @param clazz The given class
     * @return Return the non-static field
     */
    public static Field[] getNonStaticFields(Class clazz) {
        List<Field> fields = new ArrayList<Field>();
        Class currentClass = clazz;
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
            	if (field.getName().equals("serialVersionUID") || field.getName().equals("$jacocoData")){
            		continue;
            	}
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        Field[] fieldArray = new Field[fields.size()];
        return fields.toArray(fieldArray);
    }



    /**
     * Get the non-static methods by class, including all supper class methods.
     *
     * @param clazz The given class
     * @return Return the non-static methods
     */
    public static Method[] getNonStaticMethods(Class clazz) {
        List<Method> methods = new ArrayList<Method>();
        Class currentClass = clazz;
        while (currentClass != null) {
            for (Method method : currentClass.getDeclaredMethods()) {
            	if(method.getName().equals("$jacocoInit")){
            		continue;
            	}
                if (!Modifier.isStatic(method.getModifiers())) {
                    methods.add(method);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        Method[] methodArray = new Method[methods.size()];
        return methods.toArray(methodArray);
    }

    /**
     * Get field by field name, including supper class fields.
     *
     * @param clazz The class
     * @param fieldName field name
     * @return Return the field, if not found the field, return NULL
     */
    public static Field getField(Class clazz, String fieldName) {
        Class currentClass = clazz;
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
            	if (field.getName().equals("serialVersionUID") || field.getName().equals("$jacocoData")){
            		continue;
            	}
                if (fieldName.equals(field.getName())) {
                    return field;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }

    /**
     * Get method by method name, args.
     *
     * @param clazz The given class
     * @param methodName The method name
     * @param args The args class
     * @return Return the method
     */
    public static Method getMethod(Class clazz, String methodName, Class... args) throws NoSuchMethodException {
        if (clazz == null) {
            throw new NoSuchMethodException("Could not find method [" + methodName);
        }
        try {
            Method method = clazz.getMethod(methodName, args);
            return method;
        } catch (NoSuchMethodException ex) {
            return getMethod(clazz.getSuperclass(), methodName);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get method by method name, args.
     *
     * @param clazz The given class
     * @param methodName The method name
     * @return Return the method
     */
    public static Method getMethodByName(Class clazz, String methodName) throws NoSuchMethodException {
        try {
            Method[] methods = getNonStaticMethods(clazz);
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
            throw new NoSuchMethodException("Method not found for [" + methodName + "]");
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Invoke getter method by field name.
     *
     * @param obj The object
     * @param fieldName The field name
     * @return Return the value of field
     */
    public static Object invokeGetter(Object obj, String fieldName) {
        try {
            Class clazz = obj.getClass();
            Method method = getMethod(clazz, createGetterMethod(fieldName));
            return method.invoke(obj);
        } catch (NoSuchMethodException ex) {
            try {
                boolean value = invokeBooleaner(obj, fieldName);
                return Boolean.valueOf(value);
            } catch (NoSuchMethodException ex1) {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Invoke boolean method by field name.
     *
     * @param obj The object
     * @param fieldName The field name
     * @return Return the boolean value of field name
     */
    public static boolean invokeBooleaner(Object obj, String fieldName) throws NoSuchMethodException {
        try {
            Class clazz = obj.getClass();
            Method method = getMethod(clazz, createBooleanerMethod(fieldName));
            Object object = method.invoke(obj);
            return (Boolean) object;
        } catch (NoSuchMethodException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Invoke set method for field name.
     *
     * @param obj The object
     * @param fieldName The field name
     * @param fieldValue The field value
     */
    public static void invokeSetter(Object obj, String fieldName, Object fieldValue) {
        if (fieldValue == null) {
            return;
        }
        try {
            Class clazz = obj.getClass();
            Field field = getField(clazz, fieldName);
            if (field == null) {
                return;
            }
//            if (field.getType() != fieldValue.getClass()) {
//                return;
//            }
            Method method = getMethod(clazz, createSetterMethod(fieldName), field.getType());
            method.invoke(obj, fieldValue);
        } catch (Exception ex) {
        }
    }

    public static void invokeStringSetter(Object obj, String fieldName, String[] fieldValue) {
        if (fieldValue == null || fieldValue.length == 0) {
            return;
        }
        try {
            Class clazz = obj.getClass();
            Field field = getField(clazz, fieldName);
            if (field == null) {
                return;
            }
            Method method = getMethod(clazz, createSetterMethod(fieldName), field.getType());
            DateFormat dateFormat = null;
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(DateFormat.class)) {
                    dateFormat = field.getAnnotation(DateFormat.class);
                    break;
                }
            }
            String datePattern = DEFAULT_DATE_FORMAT;
            if (dateFormat != null) {
                datePattern = dateFormat.value();
            }
            if (field.getType().isArray()) {
                Object objArray = Array.newInstance(field.getType().getComponentType(), fieldValue.length);
                ReflectionUtil.invokeSetter(obj, fieldName, objArray);
                for (int i = 0; i < fieldValue.length; i++) {
                    Array.set(objArray, i, convertStringToObject(field.getType().getComponentType(), fieldValue[i], datePattern));
                }
            } else {
                Object value = convertStringToObject(field.getType(), fieldValue[0], datePattern);
                if (value == null) {
                    return;
                }
                method.invoke(obj, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Object convertStringToObject(Class fieldType, String fieldValue, String datePattern) {
        Object value = null;
        try{
            if (fieldType == boolean.class || fieldType == Boolean.class) {
                value = Boolean.valueOf(fieldValue);
            } else if (fieldType == int.class || fieldType == Integer.class) {
                value = Integer.valueOf(fieldValue);
            } else if (fieldType == float.class || fieldType == Float.class) {
                value = Float.valueOf(fieldValue);
            } else if (fieldType == double.class || fieldType == Double.class) {
                value = Double.valueOf(fieldValue);
            } else if (fieldType == long.class || fieldType == Long.class) {
                value = Long.valueOf(fieldValue);
            } else if (fieldType == byte.class || fieldType == Byte.class) {
                value = Byte.valueOf(fieldValue);
            } else if (fieldType == short.class || fieldType == Short.class) {
                value = Short.valueOf(fieldValue);
            } else if (fieldType == String.class) {
                if (fieldValue != null) {
                    value = fieldValue.trim();
                } else {
                    value = fieldValue;
                }
            } else if (fieldType == Date.class) {
                if (!StringUtils.isEmpty(fieldValue)) {
                    value = FormatUtil.parseDate(datePattern, fieldValue);
                }
            } else if (fieldType == BigDecimal.class) {
                value = new BigDecimal(fieldValue);
            }
        }catch (Exception ex){
        }
        return value;
    }

    /**
     * Create boolean method name for field. Formating by isX.
     *
     * @param fieldName The field name
     * @return Return the boolean method name of field
     */
    public static String createBooleanerMethod(String fieldName) {
        return new StringBuilder().append("is").append(firstToUpper(fieldName)).toString();
    }

    /**
     * Create get method name for field. Formating by getX.
     *
     * @param fieldName The field name
     * @return Return the get method name of field
     */
    public static String createGetterMethod(String fieldName) {
        return new StringBuilder().append("get").append(firstToUpper(fieldName)).toString();
    }

    /**
     * Create set method name for field. Formating by setX.
     *
     * @param fieldName The field name
     * @return Return the set method name of field
     */
    public static String createSetterMethod(String fieldName) {
        return new StringBuilder().append("set").append(firstToUpper(fieldName)).toString();
    }

    /**
     * Convert the first Char to upper.
     *
     * @param str The given string
     * @return Return the string
     */
    private static String firstToUpper(String str) {
        return new StringBuilder().append(str.substring(0, 1).toUpperCase()).append(str.substring(1)).toString();
    }

    /**
     * New a instance by class.
     *
     * @param clazz The given class
     * @return Return the instance
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
