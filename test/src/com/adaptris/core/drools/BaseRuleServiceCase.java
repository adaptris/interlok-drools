package com.adaptris.core.drools;

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

@SuppressWarnings("deprecation")
public abstract class BaseRuleServiceCase extends DroolsServiceExample {

  protected static final String MSG_DATA = "The quick brown fox jumps over the lazy dog";
  private static final String GOODBYE_MSG = "Goodbye cruel world";

  private static final String XPATH_SAMPLE = "/root/document";
  private static final String LINE_SEP = System.getProperty("line.separator");
  private static final String XML_HEADER = "<?xml version=\"1.0\"?>";
  private static final String HELLO_WORLD = "Hello World!";
  private static final String XML_DOC = XML_HEADER + LINE_SEP + "<root>" + LINE_SEP + "<document>" + HELLO_WORLD + "</document>"
      + LINE_SEP + "</root>" + LINE_SEP;

  public BaseRuleServiceCase(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
  }

  public void testWithCustomMediator() throws Exception {
    RuleServiceImpl service = createHelloWorldRule();
    service.setResolver(new CustomResolver());
    start(service, true);
    doServiceWithAssertions(service);
    stop(service);
  }

  public void testWithCustomMediator_SLF4J() throws Exception {
    RuleServiceImpl service = createHelloWorldRule();
    service.setResolver(new CustomResolver());
    start(service, false);
    doServiceWithAssertions(service);
    stop(service);
  }

  public void testWithDynamicResolver() throws Exception {
    RuleServiceImpl service = createHelloWorldRule();
    ReflectionResolver bean = new ReflectionResolver();
    bean.setBeanClassname(Message.class.getCanonicalName());
    bean.addFromBeanMapper(new MetadataFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), Message.MESSAGE_METADATA));
    bean.addFromBeanMapper(new MetadataFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), Message.STATUS_METADATA));
    bean.addToBeanMapper(new XpathFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), XPATH_SAMPLE));
    bean.addToBeanMapper(new ConfiguredFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), String.valueOf(Message.HELLO)));
    service.setResolver(bean);
    start(service, true);
    doServiceWithAssertions(service, AdaptrisMessageFactory.getDefaultInstance().newMessage(XML_DOC));
    stop(service);
  }

  public void testWithDynamicResolver_SLF4J() throws Exception {
    RuleServiceImpl service = createHelloWorldRule();
    ReflectionResolver bean = new ReflectionResolver();
    bean.setBeanClassname(Message.class.getCanonicalName());
    bean.addFromBeanMapper(new MetadataFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), Message.MESSAGE_METADATA));
    bean.addFromBeanMapper(new MetadataFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), Message.STATUS_METADATA));
    bean.addToBeanMapper(new XpathFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), XPATH_SAMPLE));
    bean.addToBeanMapper(new ConfiguredFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), String.valueOf(Message.HELLO)));
    service.setResolver(bean);
    start(service, false);
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

  protected static void start(RuleServiceImpl service, boolean useClog) throws Exception {
    service.setAgendaEventListener(useClog ? new CommonsLoggingEventListener() : new Slf4jLoggingEventListener());
    service.setRuleBaseEventListener(useClog ? new CommonsLoggingEventListener() : new Slf4jLoggingEventListener());
    service.setRuleFlowEventListener(useClog ? new CommonsLoggingEventListener() : new Slf4jLoggingEventListener());
    service.setWorkingMemoryEventListener(useClog ? new CommonsLoggingEventListener() : new Slf4jLoggingEventListener());
    LifecycleHelper.init(service);
    LifecycleHelper.start(service);
  }

  protected abstract RuleServiceImpl createHelloWorldRule();

}
