package com.adaptris.core.drools.dynamic;

import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Provide a statically configured field as a field.
 * <p>
 * This only provides a one-way mapping from AdaptrisMessage only. Mapping back
 * to AdaptrisMessage is not possible.
 * </p>
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-configured-field</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-configured-field")
public class ConfiguredFieldMapper extends JavabeanMapper {

  private String configuredValue;

  public ConfiguredFieldMapper() {
    super();
  }

  public ConfiguredFieldMapper(String s, TypeWrapper type, String value) {
    super(s, type);
    setConfiguredValue(value);
  }

  @Override
  void translate(AdaptrisMessage msg, Object obj) throws Exception {
    Object o = getFieldType().wrap(getConfiguredValue());
    logR.trace("Calling set" + getFieldName() + "("
        + o.getClass().getCanonicalName() + ")");
    invokeSetter(obj, getFieldName(), o);
  }

  @Override
  void translate(Object adtObj, AdaptrisMessage msg) throws Exception {
    throw new UnsupportedOperationException(
        "Mapping to AdaptrisMessage not supported by "
            + this.getClass().getName());
  }

  /**
   * @return the configuredValue
   */
  public String getConfiguredValue() {
    return configuredValue;
  }

  /**
   * @param s the configuredValue to set
   */
  public void setConfiguredValue(String s) {
    configuredValue = s;
  }

}
