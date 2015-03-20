package com.adaptris.core.drools.dynamic;

import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMarshaller;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * TypeWrapper implementation that wraps and unwraps to and from XML using an XML Marshaller (generally Castor).
 * <p>
 * The anticipated use for this is to allow a concrete {@link JavabeanMapper} instance such as {@link ConfiguredFieldMapper} to
 * insert objects of arbitary types into the bean.
 * </p>
 * 
 * @config drools-marshaled-type-wrapper
 * 
 * @author lchan
 * @see JavabeanMapper
 */
@XStreamAlias("drools-marshalled-type-wrapper")
public class MarshalledTypeWrapper implements TypeWrapper {
  private AdaptrisMarshaller marshaller;

  public MarshalledTypeWrapper() throws Exception {
    this(DefaultMarshaller.getDefaultMarshaller());
  }

  public MarshalledTypeWrapper(AdaptrisMarshaller m) {
    setMarshaller(m);
  }


  /**
   *
   * @see com.adaptris.core.drools.dynamic.TypeWrapper#unwrap(java.lang.Object)
   */
  @Override
  public String unwrap(Object o) throws Exception {
    return currentMarshaller().marshal(o);
  }

  /**
   *
   * @see com.adaptris.core.drools.dynamic.TypeWrapper#wrap(java.lang.String)
   */
  @Override
  public Object wrap(String s) throws Exception {
    return currentMarshaller().unmarshal(s);
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

  private AdaptrisMarshaller currentMarshaller() throws CoreException {
    return getMarshaller() != null ? getMarshaller() : DefaultMarshaller.getDefaultMarshaller();
  }
}
