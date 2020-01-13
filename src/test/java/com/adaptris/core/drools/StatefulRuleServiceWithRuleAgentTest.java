/*
 * $RCSfile: StatefulRuleServiceWithRuleAgentTest.java,v $
 * $Revision: 1.2 $
 * $Date: 2008/08/11 15:26:40 $
 * $Author: lchan $
 */
package com.adaptris.core.drools;

import com.adaptris.core.drools.dynamic.ConfiguredFieldMapper;
import com.adaptris.core.drools.dynamic.MetadataFieldMapper;
import com.adaptris.core.drools.dynamic.ReflectionResolver;
import com.adaptris.core.drools.dynamic.SimpleType;
import com.adaptris.core.drools.helloworld.CustomResolver;
import com.adaptris.core.drools.helloworld.Message;
import com.adaptris.util.KeyValuePair;

public class StatefulRuleServiceWithRuleAgentTest extends
    StatefulRuleServiceWithCompilerTest {

  public StatefulRuleServiceWithRuleAgentTest() {
    super();
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    StatefulRuleService srv = new StatefulRuleService();
    RuleAgentProxy proxy = new RuleAgentProxy();
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("newInstance", "false"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("url", "http://some.url/here http://some.url/here"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("localCacheDir", "/foo/bar/cache"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("poll", "30"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("name", "MyConfig"));
    srv.setRuntimeRuleBase(proxy);
    ReflectionResolver mediator = new ReflectionResolver();
    mediator.setBeanClassname(Message.class.getCanonicalName());
    mediator.addFromBeanMapper(new MetadataFieldMapper("Message",
        new SimpleType(SimpleType.Type.STRING), Message.MESSAGE_METADATA));
    mediator.addFromBeanMapper(new MetadataFieldMapper("Status",
        new SimpleType(SimpleType.Type.INTEGER), Message.STATUS_METADATA));
    mediator.addToBeanMapper(new ConfiguredFieldMapper("Message",
        new SimpleType(SimpleType.Type.STRING), "Hello World"));
    mediator
        .addToBeanMapper(new ConfiguredFieldMapper("Status", new SimpleType(
            SimpleType.Type.INTEGER), String.valueOf(Message.HELLO)));
    srv.setResolver(mediator);
    srv.setAgendaEventListener(new Slf4jLoggingEventListener("my.category", LoggingEventListenerImpl.LoggingLevel.INFO));
    srv.setRuleBaseEventListener(new Slf4jLoggingEventListener());
    srv.setWorkingMemoryEventListener(new Slf4jLoggingEventListener());

    return srv;
  }

  @Override
  protected String createBaseFileName(Object object) {
    return  object.getClass().getName() + "-WithRuleAgent";
  }

  @Override
  protected RuleServiceImpl createHelloWorldRule() {
    RuleServiceImpl service = new StatefulRuleService();
    String drlFile = PROPERTIES.getProperty("drools.HelloWorld.drl.file");
    RuleAgentProxy proxy = new RuleAgentProxy();
    service.setResolver(new CustomResolver());
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("newInstance", "false"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("file", drlFile));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("poll", "30"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("name", "HelloWorld"));
    service.setRuntimeRuleBase(proxy);
    return service;
  }

  @Override
  protected RuleServiceImpl createTicketingRules() {
    RuleServiceImpl service = new StatefulRuleService();
    String drlFile = PROPERTIES.getProperty("drools.Ticketing.drl.file");
    RuleAgentProxy proxy = new RuleAgentProxy();
    service.setResolver(new CustomResolver());
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("newInstance", "false"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("file", drlFile));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("poll", "30"));
    proxy.getRuleAgentProperties().addKeyValuePair(
        new KeyValuePair("name", "TroubleTicket"));
    service.setRuntimeRuleBase(proxy);
    return service;
  }  
  
  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }  
}
