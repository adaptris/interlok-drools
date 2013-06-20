package com.adaptris.core.drools.dynamic;

import static com.adaptris.core.util.XmlHelper.createXmlUtils;

import java.io.ByteArrayOutputStream;

import com.adaptris.annotation.MarshallingImperative;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.adaptris.util.XmlUtils;

/**
 * Resolve an XPath on the AdaptrisMessage payload and use that value as a
 * field.
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-xpath-field</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 *
 */
@XStreamAlias("drools-xpath-field")
@MarshallingImperative(mapTo = "drools-xpath-field", transientFields = {})
public class XpathFieldMapper extends JavabeanMapper {

  private String xpath;

  public XpathFieldMapper() {
    super();
  }

  public XpathFieldMapper(String s, TypeWrapper type, String value) {
    super(s, type);
    setXpath(value);
  }

  @Override
  void translate(AdaptrisMessage msg, Object obj) throws Exception {
    XmlUtils xmlUtils = createXmlUtils(msg);

    try {
      String xpathValue = xmlUtils.getSingleTextItem(getXpath());
      Object o = getFieldType().wrap(xpathValue);
      logR.trace("Calling set" + getFieldName() + "("
          + o.getClass().getCanonicalName() + ")");
      invokeSetter(obj, getFieldName(), o);
    }
    catch (Exception e) {
      throw new CoreException(e);
    }
  }

  @Override
  void translate(Object obj, AdaptrisMessage msg) throws Exception {
    XmlUtils xmlUtils = createXmlUtils(msg);
    try {
      Object o = invokeGetter(obj, getFieldName());
      xmlUtils.setNodeValue(getXpath(), getFieldType().unwrap(o));
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      xmlUtils.writeDocument(out);
      out.flush();
      msg.setPayload(out.toByteArray());
    }
    catch (Exception e) {
      throw new CoreException(e);
    }
  }

  /**
   * Return the XPath expression.
   *
   * @return the value
   */
  public String getXpath() {
    return xpath;
  }

  /**
   * Set the XPath Expression.
   * <p>
   * If the XPath will resolve to more than 1 item, then any one of items is
   * used to populate the field
   * </p>
   *
   * @param s the value to set
   */
  public void setXpath(String s) {
    xpath = s;
  }
}
