package com.adaptris.core.drools.dynamic;

import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Map between AdaptrisMessage and a specific field within an object.
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-metadata-field</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @see ReflectionResolver
 * @see JavabeanMapper
 */
@XStreamAlias("drools-metadata-field")
public class MetadataFieldMapper extends JavabeanMapper {

  private String metadataKey;

  public MetadataFieldMapper() {
    super();
  }

  public MetadataFieldMapper(String s, TypeWrapper type, String value) {
    super(s, type);
    setMetadataKey(value);
  }

  @Override
  void translate(AdaptrisMessage msg, Object obj) throws Exception {
    if (!msg.containsKey(getMetadataKey())) {
      logR.trace("Nothing to do, " + getMetadataKey()
          + " not found in message metadata.");
      return;
    }
    Object o = getFieldType().wrap(msg.getMetadataValue(getMetadataKey()));
    logR.trace("Calling set" + getFieldName() + "("
        + o.getClass().getCanonicalName() + ")");
    invokeSetter(obj, getFieldName(), o);
  }

  @Override
  void translate(Object obj, AdaptrisMessage msg) throws Exception {
    logR.trace("Calling get" + getFieldName() + "()");
    Object o = invokeGetter(obj, getFieldName());
    msg.addMetadata(getMetadataKey(), getFieldType().unwrap(o));
  }

  /**
   * @return the metadataKey
   */
  public String getMetadataKey() {
    return metadataKey;
  }

  /**
   * The metadata key associated with the field.
   *
   * @param s the metadataKey to set
   */
  public void setMetadataKey(String s) {
    metadataKey = s;
  }

}
