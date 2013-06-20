package com.adaptris.core.drools.dynamic;

import com.adaptris.annotation.MarshallingImperative;
import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * XStream marshalling tag - <drools-message-id-mapper>
 * Map between AdaptrisMessage unique id and a specific field within an
 * Object.
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-message-id-field</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 */
@XStreamAlias("drools-message-id-field")
@MarshallingImperative(mapTo = "drools-message-id-field", transientFields = {})
public class MessageIdMapper extends JavabeanMapper {

  public MessageIdMapper() {
    super();
  }

  public MessageIdMapper(String s, TypeWrapper type) {
    super(s, type);
  }

  @Override
  void translate(AdaptrisMessage msg, Object obj) throws Exception {
    Object o = getFieldType().wrap(msg.getUniqueId());
    logR.trace("Calling set" + getFieldName() + "("
        + o.getClass().getCanonicalName() + ")");
    invokeSetter(obj, getFieldName(), o);
  }

  @Override
  void translate(Object obj, AdaptrisMessage msg) throws Exception {
    logR.trace("Calling get" + getFieldName() + "()");
    Object o = invokeGetter(obj, getFieldName());
    msg.setUniqueId(getFieldType().unwrap(o));
  }

}
