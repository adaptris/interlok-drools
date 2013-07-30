package com.adaptris.core.drools.dynamic;

import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.DefaultMarshaller;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * TypeWrapper implementation that wraps and unwraps to and from XML using an
 * XML Marshaller (generally Castor).
 * <p>
 * The anticipated use for this is to allow a concrete {@link JavabeanMapper}
 * instance such as {@link ConfiguredFieldMapper} to insert objects of arbitary
 * types into the bean.
 * </p>
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-marshalled-type-wrapper</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @see JavabeanMapper
 */
@XStreamAlias("drools-marshalled-type-wrapper")
public class MarshalledTypeWrapper implements TypeWrapper {
  private AdaptrisMarshaller marshaller;
  private String marshalledClassname;

  public MarshalledTypeWrapper() throws Exception {
    this(null, DefaultMarshaller.getDefaultMarshaller());
  }

  public MarshalledTypeWrapper(AdaptrisMarshaller m) {
    this(null, m);
  }

  public MarshalledTypeWrapper(String classname) throws Exception {
    this(classname, DefaultMarshaller.getDefaultMarshaller());
  }

  public MarshalledTypeWrapper(String classname, AdaptrisMarshaller m) {
    setMarshalledClassname(classname);
    setMarshaller(m);
  }

  /**
   *
   * @see com.adaptris.core.drools.dynamic.TypeWrapper#unwrap(java.lang.Object)
   */
  @Override
  public String unwrap(Object o) throws Exception {
    return marshaller.marshal(o);
  }

  /**
   *
   * @see com.adaptris.core.drools.dynamic.TypeWrapper#wrap(java.lang.String)
   */
  @Override
  public Object wrap(String s) throws Exception {
    Object result = null;
    result = marshaller.unmarshal(s);
    return result;
  }

  /**
   * @return the marshalledClassname
   */
  public String getMarshalledClassname() {
    return marshalledClassname;
  }

  /**
   * Set the classname that will be assumed for marshalling purposes.
   */
  public void setMarshalledClassname(String s) {
    marshalledClassname = s;
  }

  /**
   * @return the marshaller
   */
  public AdaptrisMarshaller getMarshaller() {
    return marshaller;
  }

  /**
   * @param m the marshaller to set
   */
  public void setMarshaller(AdaptrisMarshaller m) {
    marshaller = m;
  }

}
