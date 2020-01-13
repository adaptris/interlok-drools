/*
 * $RCSfile: TestJavabeanMapper.java,v $
 * $Revision: 1.5 $
 * $Date: 2008/08/21 10:57:54 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.dynamic;

import static com.adaptris.core.util.XmlHelper.createXmlUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.BaseCase;
import com.adaptris.core.DefaultMarshaller;
import com.adaptris.core.MetadataElement;
import com.adaptris.core.jdbc.FailoverJdbcConnection;
import com.adaptris.util.XmlUtils;

@SuppressWarnings("deprecation")
public class TestJavabeanMapper extends BaseCase {
  private static final String HELLO_WORLD = "Hello World!";
  private static final String GOODBYE = "Goodbye Cruel World";
  private static final String TRUE_VALUE = "true";
  private static final String STRING_VALUE = "String";
  private static final String STRING_FIELD = "StringField";
  private static final String DB_CONNECT_FIELD = "DatabaseConnection";
  private static final String XPATH_SAMPLE = "/root/document";
  private static final String LINE_SEP = System.getProperty("line.separator");
  private static final String HELLO_WORLD_XML = "<root>" + LINE_SEP
      + "<document>" + HELLO_WORLD + "</document>" + LINE_SEP + "</root>"
      + LINE_SEP;

  private SimpleBean testBean;

  public TestJavabeanMapper() {
    super();
  }

  @Before
  public void setUp() {
    testBean = new SimpleBean();
  }

  @Test
  public void testConfiguredField() throws Exception {
    ConfiguredFieldMapper cf = new ConfiguredFieldMapper(STRING_FIELD,
        new SimpleType(SimpleType.Type.STRING), STRING_VALUE);
    AdaptrisMessage msg = create();

    cf.translate(msg, testBean);
    assertEquals(STRING_VALUE, testBean.getStringField());
    try {
      cf.translate(testBean, msg);
      fail("Expecting exception");
    }
    catch (UnsupportedOperationException e) {
      ;
    }
  }

  @Test
  public void testMessageIdMapper() throws Exception {
    MessageIdMapper cf = new MessageIdMapper(STRING_FIELD, new SimpleType(
        SimpleType.Type.STRING));
    AdaptrisMessage msg = create();
    cf.translate(msg, testBean);
    assertEquals(msg.getUniqueId(), testBean.getStringField());
    AdaptrisMessage msg2 = AdaptrisMessageFactory.getDefaultInstance()
        .newMessage();
    cf.translate(testBean, msg2);
    assertEquals(msg.getUniqueId(), msg2.getUniqueId());
  }

  @Test
  public void testPayloadMapper() throws Exception {
    PayloadMapper mapper = new PayloadMapper(STRING_FIELD, new SimpleType(
        SimpleType.Type.STRING));
    AdaptrisMessage msg = create();
    mapper.translate(msg, testBean);
    assertEquals(msg.getStringPayload(), testBean.getStringField());
    AdaptrisMessage msg2 = AdaptrisMessageFactory.getDefaultInstance()
        .newMessage();
    mapper.translate(testBean, msg2);
    assertEquals(msg.getStringPayload(), msg2.getStringPayload());
  }

  @Test
  public void testMetadataMapper() throws Exception {
    MetadataFieldMapper mapper = new MetadataFieldMapper(STRING_FIELD,
        new SimpleType(SimpleType.Type.STRING), "booleanField");
    AdaptrisMessage msg = create();
    mapper.translate(msg, testBean);
    assertEquals(TRUE_VALUE, testBean.getStringField());
    AdaptrisMessage msg2 = AdaptrisMessageFactory.getDefaultInstance()
        .newMessage();
    mapper.translate(testBean, msg2);
    assertEquals(TRUE_VALUE, msg2.getMetadataValue("booleanField"));
  }

  @Test
  public void testXpathFieldMapper() throws Exception {
    XpathFieldMapper cf = new XpathFieldMapper(STRING_FIELD, new SimpleType(
        SimpleType.Type.STRING), XPATH_SAMPLE);
    AdaptrisMessage msg = create();
    cf.translate(msg, testBean);
    assertEquals(HELLO_WORLD, testBean.getStringField());
    testBean.setStringField(GOODBYE);
    cf.translate(testBean, msg);
    XmlUtils xmlUtils = createXmlUtils(msg);
    assertEquals(GOODBYE, xmlUtils.getSingleTextItem(XPATH_SAMPLE));
  }

  @Test
  public void testConfiguredFieldObjectMapper() throws Exception {
    AdaptrisMarshaller cm = DefaultMarshaller.getDefaultMarshaller();
    FailoverJdbcConnection dbConn = new FailoverJdbcConnection();
    dbConn.addConnectUrl("url1");
    String xml = cm.marshal(dbConn);
    ConfiguredFieldMapper cf = new ConfiguredFieldMapper(DB_CONNECT_FIELD,
        new MarshalledTypeWrapper(), xml);
    AdaptrisMessage msg = create();
    cf.translate(msg, testBean);
    assertRoundtripEquality(dbConn, testBean.getDatabaseConnection());
  }

  private static AdaptrisMessage create() {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance()
        .newMessage(HELLO_WORLD_XML);
    msg.addMetadata(new MetadataElement("booleanField", TRUE_VALUE));
    return msg;
  }

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
}
