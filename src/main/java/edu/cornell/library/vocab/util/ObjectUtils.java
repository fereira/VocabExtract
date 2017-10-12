/*
 * $Id$
 * CONFIDENTIAL AND PROPRIETARY. © 2007 Revolution Health Group LLC. All rights reserved.
 * This source code may not be disclosed to others, used or reproduced without the written permission of Revolution Health Group.
 *
 */
package edu.cornell.library.vocab.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

public class ObjectUtils {
    public static final String VERSION = "$Rev: 63219 $";
    private static final Log logger = LogFactory.getLog(ObjectUtils.class);

    @SuppressWarnings("unchecked")
   private static final Set<Class> SIMPLE_TYPES = new HashSet<Class>() {
        /**
       *
       */
      private static final long serialVersionUID = 1L;

      {
            add(Boolean.class);
            add(boolean.class);
            add(Float.class);
            add(float.class);
            add(Double.class);
            add(double.class);
            add(Integer.class);
            add(int.class);
            add(Long.class);
            add(long.class);
            add(Short.class);
            add(short.class);
            add(Byte.class);
            add(byte.class);
            add(String.class);
            add(BigDecimal.class);
            add(BigInteger.class);
        }
    };

    private ObjectUtils() {
    }

    @SuppressWarnings("unchecked")
   public static boolean isMap(Object obj) {
        if (obj instanceof Map) {
            return true;
        }
        return false;
    }

    public static boolean isArray(Object obj) {
        if (obj != null) {
            return isClassArray(obj.getClass());
        }
        return false;
    }

    @SuppressWarnings("unchecked")
   public static boolean isClassArray(Class clazz) {
        if (clazz.isArray()) {
            return true;
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    public static boolean isSimpleType(Object obj) {
        boolean result = false;
        if (obj != null) {
            result = isClassSimpleType(obj.getClass());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
   public static boolean isClassSimpleType(Class clazz) {
        boolean result = false;
        if (clazz != null && SIMPLE_TYPES.contains(clazz)) {
            result = true;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
   public static boolean isComplex(Object value) {
        Class type = value.getClass();
        return !(isSimpleType(value) || type.isEnum());
    }

     

    public static void printBusinessObject(Object o) {
       if (o == null) {
    	   System.err.println("Object is null");
    	   return;
       }
       Field[] fields = o.getClass().getDeclaredFields();

       for (int i = 0; i < fields.length; i++) {
          Field field = fields[i];
          try {
             field.setAccessible(true);
             System.out.println(field.getName()+": "+field.get(o));
          }
          catch (IllegalAccessException e) {
             System.err.println("Illegal access exception");
          } catch (NullPointerException e) {
             System.err.println("Nullpointer Exception");
          }

       }
       System.out.println(); 
    }
    
    @SuppressWarnings("unchecked")
    public static String getXmlEnumValue(Object enumObj) {
         String result = null;
         try {
             if (ClassUtils.isXmlEnum((Class<Object>) enumObj.getClass())) {
                 // XmlEnum will have a "fromValue" method to convert the const
                 // to the enum const
                 Method value = enumObj.getClass().getMethod("value");
                 result = value.invoke(enumObj).toString();
             }
         } catch (Exception e) {
             // could happen..just give up and return null
         }
         return result;
     }

    public static void logBusinessObject(Object o) {
       Field[] fields = o.getClass().getDeclaredFields();

       for (int i = 0; i < fields.length; i++) {
          Field field = fields[i];
          try {
             field.setAccessible(true);
             logger.info(field.getName()+": "+field.get(o));
          }
          catch (IllegalAccessException e) {
             logger.error("Illegal access exception");
          } catch (NullPointerException e) {
             logger.error("Nullpointer Exception");
          }

       }

    }


}
