package com.adaptris.core.drools;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.Service;
import com.adaptris.core.drools.dynamic.ConfiguredFieldMapper;
import com.adaptris.core.drools.dynamic.MetadataFieldMapper;
import com.adaptris.core.drools.dynamic.ReflectionResolver;
import com.adaptris.core.drools.dynamic.SimpleType;
import com.adaptris.core.drools.dynamic.XpathFieldMapper;
import com.adaptris.core.drools.helloworld.CustomResolver;
import com.adaptris.core.drools.helloworld.Message;
import com.adaptris.core.util.LifecycleHelper;

public abstract class BaseRuleServiceCase extends DroolsServiceExample {

  protected static final String MSG_DATA = "The quick brown fox jumps over the lazy dog";
  private static final String GOODBYE_MSG = "Goodbye cruel world";

  private static final String XPATH_SAMPLE = "/root/document";
  private static final String LINE_SEP = System.getProperty("line.separator");
  private static final String XML_HEADER = "<?xml version=\"1.0\"?>";
  private static final String HELLO_WORLD = "Hello World!";
  private static final String XML_DOC = XML_HEADER + LINE_SEP + "<root>" + LINE_SEP + "<document>" + HELLO_WORLD + "</document>"
      + LINE_SEP + "</root>" + LINE_SEP;

  public BaseRuleServiceCase() {
    super();
  }

  @Test
  public void testWithCustomMediator() throws Exception {
    RuleServiceImpl service = createHelloWorldRule();
    service.setResolver(new CustomResolver());
    start(service);
    doServiceWithAssertions(service);
    stop(service);
  }

  @Test
  public void testWithDynamicResolver() throws Exception {
    RuleServiceImpl service = createHelloWorldRule();
    ReflectionResolver bean = new ReflectionResolver();
    bean.setBeanClassname(Message.class.getCanonicalName());
    bean.addFromBeanMapper(new MetadataFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), Message.MESSAGE_METADATA));
    bean.addFromBeanMapper(new MetadataFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), Message.STATUS_METADATA));
    bean.addToBeanMapper(new XpathFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), XPATH_SAMPLE));
    bean.addToBeanMapper(new ConfiguredFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), String.valueOf(Message.HELLO)));
    service.setResolver(bean);
    start(service);
    doServiceWithAssertions(service, AdaptrisMessageFactory.getDefaultInstance().newMessage(XML_DOC));
    stop(service);
  }

  protected static void doServiceWithAssertions(Service service) throws Exception {
    doServiceWithAssertions(service, AdaptrisMessageFactory.getDefaultInstance().newMessage(MSG_DATA));
  }

  protected static void doServiceWithAssertions(Service service, AdaptrisMessage msg) throws Exception {
    service.doService(msg);
    assertEquals("HelloWorldMessageMetadata", GOODBYE_MSG, msg.getMetadataValue(Message.MESSAGE_METADATA));
    assertEquals("HelloWorldStatusMetadata", String.valueOf(Message.GOODBYE), msg.getMetadataValue(Message.STATUS_METADATA));
    if (isXml(msg.getStringPayload())) {
      assertEquals("Payload Unchanged", XML_DOC, msg.getStringPayload());
    }
    else {
      assertEquals("Payload Unchanged", MSG_DATA, msg.getStringPayload());
    }
  }

  private static boolean isXml(String xml) {
    if (xml.startsWith(XML_HEADER)) {
      return true;
    }
    return false;
  }

  protected static void start(RuleServiceImpl service) throws Exception {
    LifecycleHelper.init(service);
    LifecycleHelper.start(service);
  }

  protected abstract RuleServiceImpl createHelloWorldRule();

}
