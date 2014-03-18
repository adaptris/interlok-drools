/*
 * $RCSfile: StatelessRuleServiceWithRuleAgentTest.java,v $
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

public class StatelessRuleServiceWithRuleAgentTest extends BaseRuleServiceCase {

  public StatelessRuleServiceWithRuleAgentTest(java.lang.String testName) {
    super(testName);
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    StatelessRuleService service = new StatelessRuleService();
    RuleAgentProxy proxy = new RuleAgentProxy();
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("newInstance", "false"));
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("url", "http://some.url/here http://some.url/here"));
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("localCacheDir", "/foo/bar/cache"));
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("poll", "30"));
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("name", "MyConfig"));
    service.setRuntimeRuleBase(proxy);
    ReflectionResolver bean = new ReflectionResolver();
    bean.setBeanClassname(Message.class.getCanonicalName());
    bean.addFromBeanMapper(new MetadataFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), Message.MESSAGE_METADATA));
    bean.addFromBeanMapper(new MetadataFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), Message.STATUS_METADATA));
    bean.addToBeanMapper(new ConfiguredFieldMapper("Message", new SimpleType(SimpleType.Type.STRING), "Hello World"));
    bean.addToBeanMapper(new ConfiguredFieldMapper("Status", new SimpleType(SimpleType.Type.INTEGER), String.valueOf(Message.HELLO)));
    service.setResolver(bean);
    service.setAgendaEventListener(new Slf4jLoggingEventListener());
    service.setRuleBaseEventListener(new Slf4jLoggingEventListener());
    service.setWorkingMemoryEventListener(new Slf4jLoggingEventListener());
    return service;
  }

  @Override
  protected String createBaseFileName(Object object) {
    return object.getClass().getName() + "-WithRuleAgent";
  }

  @Override
  protected RuleServiceImpl createHelloWorldRule() {
    RuleServiceImpl service = new StatelessRuleService();
    String drlFile = PROPERTIES.getProperty("drools.HelloWorld.drl.file");
    RuleAgentProxy proxy = new RuleAgentProxy();
    service.setResolver(new CustomResolver());
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("newInstance", "false"));
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("file", drlFile));
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("poll", "30"));
    proxy.getRuleAgentProperties().addKeyValuePair(new KeyValuePair("name", "HelloWorld"));
    service.setRuntimeRuleBase(proxy);
    return service;
  }
}
