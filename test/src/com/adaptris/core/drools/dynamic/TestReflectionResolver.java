/*
 * $RCSfile: TestReflectionResolver.java,v $
 * $Revision: 1.2 $
 * $Date: 2008/08/21 10:57:54 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.dynamic;

import static com.adaptris.core.util.XmlHelper.createXmlUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.CoreException;
import com.adaptris.core.MetadataElement;
import com.adaptris.core.drools.RuleException;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.util.XmlUtils;

@SuppressWarnings("deprecation")
public class TestReflectionResolver {
  private static final String METADATA_KEY_NEW_DOUBLE = "NewDoubleMetadata";
  private static final String METADATA_KEY_NEW_FLOAT = "NewFloatMetadata";
  private static final String METADATA_KEY_NEW_INT = "NewIntMetadata";
  private static final String METADATA_KEY_NEW_BOOLEAN = "NewBooleanMetadata";
  private static final String METADATA_KEY_NEW_STRING = "NewStringMetadata";

  private static final String METADATA_DOUBLE_FIELD = "doubleField";
  private static final String METADATA_FLOAT_FIELD = "floatField";
  private static final String METADATA_LONG_FIELD = "longField";
  private static final String METADATA_INT_FIELD = "intField";
  private static final String METADATA_BOOLEAN_FIELD = "booleanField";

  private static final String FIELDNAME_DOUBLE = "DoubleField";
  private static final String FIELDNAME_FLOAT = "FloatField";
  private static final String FIELDNAME_BOOLEAN = "BooleanField";
  private static final String FIELDNAME_LONG = "LongField";
  private static final String FIELDNAME_INT = "IntField";
  private static final String FIELDNAME_STRING = "StringField";

  private static final String HELLO_WORLD = "Hello World!";

  private static final String VALUE_BOOLEAN = "true";
  private static final String VALUE_INT_OR_LONG = "1";
  private static final String VALUE_DOUBLE_OR_FLOAT = "1.0";

  private static final String NON_XML_DATA = "The quick brown fox jumps over the lazy dog";
  private static final String XPATH_SAMPLE = "/root/document";
  private static final String LINE_SEP = System.getProperty("line.separator");
  private static final String XML_DOC = "<root>" + LINE_SEP + "<document>"
  + HELLO_WORLD + "</document>" + LINE_SEP + "</root>" + LINE_SEP;

  private SimpleBean testBean;

  @Before
  public void setUp() {
    testBean = new SimpleBean();
  }

  @Test
  public void testBasicResolver() throws Exception {
    ReflectionResolver r = createResolver();
    start(r);
    AdaptrisMessage msg = create(XML_DOC);
    Object[] o = r.create(msg);
    assertInternals(o);
    r.resolve(o, msg);
    assertInternals(msg);
    stop(r);

  }

  @Test
  public void testXPathResolverNonXmlDoc() throws Exception {
    ReflectionResolver r = createResolver();
    AdaptrisMessage msg = create(NON_XML_DATA);
    start(r);
    try {
      r.create(msg);
      fail("Expected CoreException from XMLHelper.create()");
    }
    catch (RuleException e) {
      Throwable t = e;
      Throwable rootCause = e.getCause();
      while (t.getCause() != null) {
        t = t.getCause();
        rootCause = t;
      }
      if (rootCause == null) {
        throw e;
      }
      else {
        if (!rootCause.getClass().equals(CoreException.class)) {
          throw e;
        }
      }
    }
    stop(r);
  }

  @Test
  public void testXPathFromBean() throws Exception {
    ReflectionResolver r = createResolver();
    r.addFromBeanMapper(new XpathFieldMapper(FIELDNAME_STRING, new SimpleType(
        SimpleType.Type.STRING), XPATH_SAMPLE));
    AdaptrisMessage msg = create(XML_DOC);
    start(r);
    Object[] o = r.create(msg);
    r.resolve(o, msg);
    XmlUtils xmlUtils = createXmlUtils(msg);
    assertEquals(HELLO_WORLD, xmlUtils.getSingleTextItem(XPATH_SAMPLE));
    stop(r);
  }

  @Test
  public void testConfiguredFieldFromBean() throws Exception {
    ReflectionResolver r = createResolver();
    r.addFromBeanMapper(new ConfiguredFieldMapper(FIELDNAME_LONG,
        new SimpleType(SimpleType.Type.LONG), VALUE_INT_OR_LONG));
    AdaptrisMessage msg = create(XML_DOC);
    start(r);
    Object[] o = r.create(msg);
    try {
      r.resolve(o, msg);
      fail("Expected UnsupportedOperationException");
    }
    catch (CoreException e) {
      if (e.getCause() == null) {
        throw e;
      }
      else {
        if (!e.getCause().getClass()
            .equals(UnsupportedOperationException.class)) {
          throw e;
        }
      }
    }
    stop(r);
  }

  private static void start(AdaptrisComponent c) throws Exception {
    LifecycleHelper.init(c);
    LifecycleHelper.start(c);
  }

  private static void stop(AdaptrisComponent c) {
    LifecycleHelper.stop(c);
    LifecycleHelper.close(c);
  }

  private static void assertInternals(Object[] obj) {
    assertNotNull(obj);
    assertEquals("Array Size", 1, obj.length);
    assertEquals("Object Class", SimpleBean.class, obj[0].getClass());
  }

  private static void assertInternals(AdaptrisMessage msg) {
    assertEquals("String Metadata", HELLO_WORLD, msg
        .getMetadataValue(METADATA_KEY_NEW_STRING));
    assertEquals("Boolean Metadata", VALUE_BOOLEAN, msg
        .getMetadataValue(METADATA_KEY_NEW_BOOLEAN));
    assertEquals("Integer Metadata", VALUE_INT_OR_LONG, msg
        .getMetadataValue(METADATA_KEY_NEW_INT));
    assertEquals("Float Metadata", VALUE_DOUBLE_OR_FLOAT, msg
        .getMetadataValue(METADATA_KEY_NEW_FLOAT));
    assertEquals("Double Metadata", VALUE_DOUBLE_OR_FLOAT, msg
        .getMetadataValue(METADATA_KEY_NEW_DOUBLE));
  }

  private static AdaptrisMessage create(String payload) {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance()
    .newMessage(payload);
    msg.addMetadata(new MetadataElement(METADATA_BOOLEAN_FIELD, VALUE_BOOLEAN));
    msg.addMetadata(new MetadataElement(METADATA_INT_FIELD, VALUE_INT_OR_LONG));
    msg
    .addMetadata(new MetadataElement(METADATA_LONG_FIELD, VALUE_INT_OR_LONG));
    msg.addMetadata(new MetadataElement(METADATA_FLOAT_FIELD,
        VALUE_DOUBLE_OR_FLOAT));
    msg.addMetadata(new MetadataElement(METADATA_DOUBLE_FIELD,
        VALUE_DOUBLE_OR_FLOAT));
    return msg;
  }

  private static ReflectionResolver createResolver() {
    ReflectionResolver result = new ReflectionResolver();
    result.setBeanClassname(SimpleBean.class.getCanonicalName());
    result.addFromBeanMapper(new MetadataFieldMapper(FIELDNAME_STRING,
        new SimpleType(SimpleType.Type.STRING), METADATA_KEY_NEW_STRING));
    result.addFromBeanMapper(new MetadataFieldMapper(FIELDNAME_BOOLEAN,
        new SimpleType(SimpleType.Type.BOOLEAN), METADATA_KEY_NEW_BOOLEAN));
    result.addFromBeanMapper(new MetadataFieldMapper(FIELDNAME_INT,
        new SimpleType(SimpleType.Type.INTEGER), METADATA_KEY_NEW_INT));
    result.addFromBeanMapper(new MetadataFieldMapper(FIELDNAME_FLOAT,
        new SimpleType(SimpleType.Type.FLOAT), METADATA_KEY_NEW_FLOAT));
    result.addFromBeanMapper(new MetadataFieldMapper(FIELDNAME_DOUBLE,
        new SimpleType(SimpleType.Type.DOUBLE), METADATA_KEY_NEW_DOUBLE));

    result.addToBeanMapper(new XpathFieldMapper(FIELDNAME_STRING,
        new SimpleType(SimpleType.Type.STRING), XPATH_SAMPLE));
    result.addToBeanMapper(new MetadataFieldMapper(FIELDNAME_BOOLEAN,
        new SimpleType(SimpleType.Type.BOOLEAN), METADATA_BOOLEAN_FIELD));
    result.addToBeanMapper(new MetadataFieldMapper(FIELDNAME_INT,
        new SimpleType(SimpleType.Type.INTEGER), METADATA_INT_FIELD));
    result.addToBeanMapper(new MetadataFieldMapper(FIELDNAME_FLOAT,
        new SimpleType(SimpleType.Type.FLOAT), METADATA_FLOAT_FIELD));
    result.addToBeanMapper(new MetadataFieldMapper(FIELDNAME_DOUBLE,
        new SimpleType(SimpleType.Type.DOUBLE), METADATA_DOUBLE_FIELD));
    result.addToBeanMapper(new ConfiguredFieldMapper(FIELDNAME_LONG,
        new SimpleType(SimpleType.Type.LONG), VALUE_INT_OR_LONG));

    return result;

  }

}
