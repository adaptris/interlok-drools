package com.adaptris.core.drools.dynamic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.IOException;
import java.math.BigDecimal;


/**
 * Wraps simple types
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-simple-type</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @see JavabeanMapper
 */
@XStreamAlias("drools-simple-type")
public class SimpleType implements TypeWrapper {
  /**
   * The Types wrapped by this SimpleType implementation.
   */
  public enum Type {
    /**
     * Handles String fields.
     *
     */
    STRING {
      @Override
      String unwrap(Object o) {
        return (String) o;
      }

      @Override
      Object wrap(String s) {
        return s;
      }
    },
    /**
     * Handles Integer fields.
     *
     */
    INTEGER {
      @Override
      String unwrap(Object o) {
        return ((Integer) o).toString();
      }

      @Override
      Object wrap(String s) {
        return Integer.valueOf(s);
      }
    },
    /**
     * Handles BigDecimal fields.
     *
     */
    BIGDECIMAL {
      @Override
      String unwrap(Object o) {
        return ((BigDecimal) o).toString();
      }

      @Override
      Object wrap(String s) {
        return new BigDecimal(s);
      }
    },
    /**
     * Handles Float fields.
     *
     */
    FLOAT {
      @Override
      String unwrap(Object o) {
        return ((Float) o).toString();
      }

      @Override
      Object wrap(String s) {
        return Float.valueOf(s);
      }
    },
    /**
     * Handles Boolean fields.
     *
     */
    BOOLEAN {
      @Override
      String unwrap(Object o) {
        return ((Boolean) o).toString();
      }

      @Override
      Object wrap(String s) {
        return Boolean.valueOf(s);
      }
    },
    /**
     * Handles Long fields.
     *
     */
    LONG {
      @Override
      String unwrap(Object o) {
        return ((Long) o).toString();
      }

      @Override
      Object wrap(String s) {
        return Long.valueOf(s);
      }
    },
    /**
     * Handles Double fields.
     *
     */
    DOUBLE {
      @Override
      String unwrap(Object o) {
        return ((Double) o).toString();
      }

      @Override
      Object wrap(String s) {
        return Double.valueOf(s);
      }
    };

    abstract String unwrap(Object o);

    abstract Object wrap(String s);
  }

  private String type;

  public SimpleType() {

  }

  public SimpleType(Type s) {
    this();
    setType(s.name());
  }

  public SimpleType(String s) {
    this();
    setType(s);
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * The type.
   *
   * @param s the specific type of this field.
   * @see Type
   */
  public void setType(String s) {
    type = s;
  }

  /**
   *
   * @see com.adaptris.core.drools.dynamic.TypeWrapper#unwrap(java.lang.Object)
   */
  @Override
  public String unwrap(Object o) throws Exception {
    Type f = null;
    try {
      f = Type.valueOf(type.toUpperCase());
    }
    catch (IllegalArgumentException e) {
      IOException ioe = new IOException(type
          + " is not supported as a SimpleType");
      ioe.initCause(e);
      throw ioe;
    }
    return f.unwrap(o);
  }

  /**
   *
   * @see com.adaptris.core.drools.dynamic.TypeWrapper#wrap(java.lang.String)
   */
  @Override
  public Object wrap(String s) throws Exception {
    Type f = null;
    try {
      f = Type.valueOf(type.toUpperCase());
    }
    catch (IllegalArgumentException e) {
      IOException ioe = new IOException(type
          + " is not supported as a SimpleType");
      ioe.initCause(e);
      throw ioe;
    }
    return f.wrap(s);
  }

}
