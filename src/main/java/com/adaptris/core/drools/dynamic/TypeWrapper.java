package com.adaptris.core.drools.dynamic;



/**
 * Interface to wrap specific object types when requested by the
 * ReflectionResolver.
 * 
 * @author lchan
 * 
 */
public interface TypeWrapper {

  /**
   * Convert the wrapped Object type into a string.
   * 
   * @param o the object to convert
   * @return a string representation of the wrapped object.
   * @throws Exception when an exception is encountered
   */
  String unwrap(Object o) throws Exception;

  /**
   * Convert the string into a wrapped object for Oracle AdtMessages
   * 
   * @param s the string to convert
   * @return a string representation of the wrapped object.
   * @throws Exception when an exception is encountered
   */
  Object wrap(String s) throws Exception;

}
