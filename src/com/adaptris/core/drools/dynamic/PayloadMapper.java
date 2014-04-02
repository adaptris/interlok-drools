package com.adaptris.core.drools.dynamic;

import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Map the AdaptrisMessage payload to a specific field.
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-payload-field</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 *
 */
@XStreamAlias("drools-payload-field")
public class PayloadMapper extends JavabeanMapper {

  public PayloadMapper() {
    super();
  }

  public PayloadMapper(String s, TypeWrapper type) {
    super(s, type);
  }

  @Override
  void translate(AdaptrisMessage msg, Object obj) throws Exception {
    Object o = getFieldType().wrap(msg.getStringPayload());
    logR.trace("Calling set" + getFieldName() + "("
        + o.getClass().getCanonicalName() + ")");
    invokeSetter(obj, getFieldName(), o);
  }

  @Override
  void translate(Object obj, AdaptrisMessage msg) throws Exception {
    logR.trace("Calling get" + getFieldName() + "()");
    Object o = invokeGetter(obj, getFieldName());
    msg.setStringPayload(getFieldType().unwrap(o), msg.getCharEncoding());
  }

}
