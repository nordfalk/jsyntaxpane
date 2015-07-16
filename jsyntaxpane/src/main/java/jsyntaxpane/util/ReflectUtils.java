/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *       at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsyntaxpane.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reflection Utility methods
 * @author Ayman Al-Sairafi
 */
public class ReflectUtils {

    /**
     * Adds all methods (from Class.getMethodCalls) to the list
     * @param aClass
     * @param list
     * @return number of methods added
     */
    public static int addMethods(Class aClass, List<Member> list) {
        Method[] methods = aClass.getMethods();
        for (Method m : methods) {
            list.add(m);
        }
        return methods.length;
    }

    /**
     * Adds all static methods (from Class.getMethodCalls) to the list
     * @param aClass
     * @param list
     * @return number of methods added
     */
    public static int addStaticMethods(Class aClass, List<Member> list) {
        Method[] methods = aClass.getMethods();
        for (Method m : methods) {
            if (Modifier.isStatic(m.getModifiers())) {
                list.add(m);
            }
        }
        return methods.length;
    }

    /**
     * Adds all static Fields (from Class.getFields) to the list
     * @param aClass
     * @param list
     * @return number of fields added
     */
    public static int addStaticFields(Class aClass, List<Member> list) {
        Field[] fields = aClass.getFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())) {
                list.add(f);
            }
        }
        return fields.length;
    }

    /**
     * Adds all Fields (from Class.getFields) to the list
     * @param aClass
     * @param list
     * @return number of fields added
     */
    public static int addFields(Class aClass, List<Member> list) {
        Field[] fields = aClass.getFields();
        for (Field f : fields) {
            list.add(f);
        }
        return fields.length;
    }

    /**
     * Adds all Constructor (from Class.getConstructorCalls) to the list
     * @param aClass
     * @param list
     * @return number of constructors added
     */
    public static int addConstrcutors(Class aClass, List<Member> list) {
        Constructor[] constructors = aClass.getConstructors();
        for (Constructor c : constructors) {
            list.add(c);
        }
        return constructors.length;
    }

    /**
     * Convert the constructor to a Java Code String
     * (arguments are replaced by the simple types)
     * @param c Constructor
     * @return
     */
    public static String getJavaCallString(Constructor c) {
        StringBuilder call = new StringBuilder();
        call.append(c.getDeclaringClass().getSimpleName());
        addParamsString(call, c.getParameterTypes());
        return call.toString();
    }

    /**
     * Convert the Method to a Java Code String
     * (arguments are replaced by the simple types)
     * @param method Method
     * @return
     */
    public static String getJavaCallString(Method method) {
        StringBuilder call = new StringBuilder();
        call.append(method.getName());
        addParamsString(call, method.getParameterTypes());
        return call.toString();
    }

    /**
     * Adds the class SimpleNames, comma sepearated and surrounded by paranthesis to the
     * call StringBuffer
     * @param call
     * @param params
     * @return
     */
    public static StringBuilder addParamsString(StringBuilder call, Class[] params) {
        call.append("(");
        boolean firstArg = true;
        for (Class arg : params) {
            if (firstArg) {
                firstArg = false;
            } else {
                call.append(", ");
            }
            call.append(arg.getSimpleName());
        }
        call.append(")");
        return call;
    }

    /**
     * Gets a String array of all method calls for the given class
     * @param aClass
     * @return
     */
    public static String[] getMethodCalls(Class aClass) {
        String[] methods = new String[aClass.getMethods().length];
        int i = 0;
        for (Method method : aClass.getMethods()) {
            methods[i++] = getJavaCallString(method);
        }
        return methods;
    }

    /**
     * Gets an array of all Constructor calls for the given class
     * @param aClass
     * @return
     */
    public static String[] getConstructorCalls(Class aClass) {
        Constructor[] constructors = aClass.getConstructors();
        String[] cons = new String[constructors.length];
        int i = 0;
        for (Constructor c : constructors) {
            cons[i++] = getJavaCallString(c);
        }
        return cons;
    }

    /**
     * Return a paranthesis enclosed, comma sepearated String of all
     * SimpleClass names in params.
     * @param params
     * @return
     */
    public static String getParamsString(Class[] params) {
        StringBuilder sb = new StringBuilder();
        addParamsString(sb, params);
        return sb.toString();
    }

    /**
     * Attempt to find the given className within any of the packages.
     * If the class is not found, then null is returned
     * @param className Fully or partially qualified classname within any of the packages
     * @param packages List of packages for search
     * @return CLass object or null if not found.
     */
    public static Class findClass(String className, List<String> packages) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
        } catch (NoClassDefFoundError ex) {
        }
        for (String pack : packages) {
            try {
                return Class.forName(pack + "." + className);
            } catch (ClassNotFoundException ex) {
            } catch (NoClassDefFoundError ex) {
            }
        }
        return null;
    }

    /**
     * Find a setter method for the give object's property and try to call it.
     * No exceptions are thrown. You typically call this method because either
     * you are sure no exceptions will be thrown, or to silently ignore
     * any that may be thrown.
     * This will also find a setter that accepts an interface that the value
     * implements.
     * <b>This is still not very effcient and should only be called if
     * performance is not of an issue.</b>
     * You can check the return value to see if the call was seuccessful or
     * not.
     * @param obj Object to receive the call
     * @param property property name (without set. First letter will be
     * capitalized)
     * @param value Value of the property.
     * @return
     */
    public static boolean callSetter(Object obj, String property, Object value) {
        String key = String.format("%s.%s(%s)", obj.getClass().getName(),
                property, value.getClass().getName());
        Method m = null;
        boolean result = false;
        if(!SETTERS_MAP.containsKey(key)) {
            m = findMethod(obj, property, value);
            SETTERS_MAP.put(key, m);
        } else {
            m = SETTERS_MAP.get(key);
        }
        if(m != null) {
            try {
                m.invoke(obj, value);
                result = true;
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ReflectUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ReflectUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ReflectUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    private static synchronized Method findMethod(Object obj,
            String property, Object value) {
        Method m = null;
        Class<?> theClass = obj.getClass();
        String setter = String.format("set%C%s",
                property.charAt(0), property.substring(1));
        Class paramType = value.getClass();
        while (paramType != null) {
            try {
                m = theClass.getMethod(setter, paramType);
                return m;
            } catch (NoSuchMethodException ex) {
                // try on the interfaces of this class
                for (Class iface : paramType.getInterfaces()) {
                    try {
                        m = theClass.getMethod(setter, iface);
                        return m;
                    } catch (NoSuchMethodException ex1) {
                    }
                }
                paramType = paramType.getSuperclass();
            }
        }
        return m;
    }
    public static final List<String> DEFAULT_PACKAGES = new ArrayList<String>(3);

    static {
        DEFAULT_PACKAGES.add("java.lang");
        DEFAULT_PACKAGES.add("java.util");
        DEFAULT_PACKAGES.add("jsyntaxpane");
    }
    /**
     * To speed up find setter methods, this map will be used.
     * The Key String will be of the format objectClass.property(valueclass)
     * Where:
     * objectClass = obj.getClass().getName
     * property = property (as passed in to callSetter), before set is appended
     * valueCLass = value.getClass().getName()
     * The Method will be either the method, or null if a search was not and no
     * method is found.
     */
    private static HashMap<String, Method> SETTERS_MAP = new HashMap<String, Method>();
}
