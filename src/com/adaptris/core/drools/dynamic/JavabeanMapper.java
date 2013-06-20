package com.adaptris.core.drools.dynamic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.AdaptrisMessage;

/**
 * Map AdaptrisMessage fields to and from arbitary javabean fields.
 *
 * @author lchan
 *
 */
public abstract class JavabeanMapper {

  private static final String GET = "get";

  private static final String SET = "set";

  private static final Class[] PRIMITIVE_PRIMITIVES =
  { // Oh this is all sorts of weakness, but it's allowed.
      int.class, boolean.class, float.class, double.class, long.class
  };

  private static final Class[] OBJECT_PRIMITIVES =
  {
      Integer.class, Boolean.class, Float.class, Double.class, Long.class
  };

  private static final Map<Class, Class> PRIMITIVES;

  static {
    Map<Class, Class> map = new HashMap<Class, Class>();
    for (int i = 0; i < OBJECT_PRIMITIVES.length; i++) {
      map.put(OBJECT_PRIMITIVES[i], PRIMITIVE_PRIMITIVES[i]);
    }
    PRIMITIVES = Collections.unmodifiableMap(map);
  }

  protected transient Logger logR = LoggerFactory.getLogger(JavabeanMapper.class);

  private TypeWrapper fieldType;
  private String fieldName;

  public JavabeanMapper() {
    setFieldType(new SimpleType(SimpleType.Type.STRING));
  }

  public JavabeanMapper(String field, TypeWrapper t) {
    this();
    setFieldName(field);
    setFieldType(t);
  }

  /**
   * Copy certain elements from AdaptrisMessage into an arbitary object.
   *
   * @param src the adaptrisMessage object
   * @param result the java bean.
   * @throws Exception on error.
   */
  abstract void translate(AdaptrisMessage src, Object result) throws Exception;

  /**
   * Copy certain elements from an arbitary object into the AdaptrisMessage.
   *
   * @param result the adaptrisMessage object
   * @param src the java bean.
   * @throws Exception on error.
   */
  abstract void translate(Object src, AdaptrisMessage result) throws Exception;

  /**
   * Invoke the getter method.
   *
   * @param obj the object.
   * @param field the field name, the "get" prefix will be added.
   * @return the result of the method call.
   * @throws Exception on error.
   */
  protected Object invokeGetter(Object obj, String field) throws Exception {
    Method m = obj.getClass().getMethod(GET + field, (Class[]) null);
    if (m != null) {
      return m.invoke(obj, (Object[]) null);
    }
    return null;
  }

  /**
   * Invoke the setter method.
   *
   * @param obj the object.
   * @param field the field name, the "set" prefix will be added.
   * @param param the object to pass as a parameter to the setter.
   * @throws Exception on error.
   */
  protected void invokeSetter(Object obj, String field, Object param)
      throws Exception {
    Method m = null;
    Class[] clazzes = getPossibles(param.getClass());
    for (Class c : clazzes) {
      m = findSetter(obj, field, c);
      if (m != null) {
        break;
      }
    }
    if (m == null) {
      // So setX(Integer) doesn't exist eh.
      Class clazz = PRIMITIVES.get(param.getClass());
      if (clazz != null) {
        m = obj.getClass().getMethod(SET + field, new Class[]
        {
          clazz
        });
      }
    }
    if (m != null) {
      m.invoke(obj, param);
    }
    else {
      throw new NoSuchMethodException(SET + field + "("
          + param.getClass().getCanonicalName() + ") not found");
    }
  }

  private Class[] getPossibles(Class clazz) {
    Class c = clazz;
    ArrayList<Class> result = new ArrayList<Class>();
    result.add(clazz);
    result.addAll(Arrays.asList(clazz.getInterfaces()));
    c = clazz.getSuperclass();
    while (c != null) {
      result.add(c);
      c = c.getSuperclass();
    }
    return result.toArray(new Class[0]);
  }

  private Method findSetter(Object obj, String field, Class clazz) {
    Method m = null;
    try {
      m = obj.getClass().getMethod(SET + field, new Class[]
      {
        clazz
      });
      logR.trace("Found " + m.getName() + "(" + clazz.getCanonicalName()
          + ")");
    }
    catch (NoSuchMethodException e) {
      m = null;
    }
    return m;
  }

  /**
   * @return the fieldName
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Set the field name that matches the Adt object field.
   *
   * @param s the fieldName to set
   */
  public void setFieldName(String s) {
    fieldName = s;
  }

  /**
   * @return the typeWrapper
   */
  public TypeWrapper getFieldType() {
    return fieldType;
  }

  /**
   * @param tw the typeWrapper to set
   */
  public void setFieldType(TypeWrapper tw) {
    fieldType = tw;
  }
}
