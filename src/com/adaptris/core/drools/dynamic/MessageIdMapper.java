package com.adaptris.core.drools.dynamic;

import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Map between AdaptrisMessage unique id and a specific field within an Object.
 * 
 * @config message-id-mapper
 * 
 * @author lchan
 */
@XStreamAlias("drools-message-id-field")
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
