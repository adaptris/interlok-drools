/*
 * $RCSfile: StatefulRuleServiceWithCompilerTest.java,v $
 * $Revision: 1.4 $
 * $Date: 2008/08/13 13:28:43 $
 * $Author: lchan $
 */
package com.adaptris.core.drools;

import org.drools.StatefulSession;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.drools.helloworld.CustomResolver;
import com.adaptris.core.drools.ticket.Customer;
import com.adaptris.core.drools.ticket.Ticket;
import com.adaptris.core.drools.ticket.TicketResolver;
import com.adaptris.util.KeyValuePair;

public class StatefulRuleServiceWithCompilerTest extends BaseRuleServiceCase {

  private static final String METADATA_KEY = "junit.drools.dispose";

  public StatefulRuleServiceWithCompilerTest(java.lang.String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
  }

  public void testTicketingRules() throws Exception {
    StatefulRuleService s1 = (StatefulRuleService) createTicketingRules();
    s1.setSessionManagementStrategy(new PerpetualSessionStrategy());
    s1.setResolver(new TicketResolver());
    start(s1);
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance()
        .newMessage(MSG_DATA);
    s1.doService(msg);
    assertEquals(Ticket.NEW, msg.getMetadataValue(Customer.CUSTOMER_SILVER));
    assertEquals(Ticket.NEW, msg.getMetadataValue(Customer.CUSTOMER_BRONZE));
    Thread.sleep(5000);
    AdaptrisMessage msg2 = AdaptrisMessageFactory.getDefaultInstance()
        .newMessage(MSG_DATA);
    s1.doService(msg2);
    assertEquals("Ticket is Escalated", Ticket.ESCALATED, msg2
        .getMetadataValue(Customer.CUSTOMER_SILVER));
    assertEquals("Ticket is not Escalated", Ticket.NEW, msg2
        .getMetadataValue(Customer.CUSTOMER_BRONZE));
    stop(s1);
  }

  public void testWithPerMessageStrategy() throws Exception {
    StatefulRuleService s1 = (StatefulRuleService) createHelloWorldRule();
    PerMessageSessionStrategy strategy = new PerMessageSessionStrategy();
    s1.setSessionManagementStrategy(strategy);
    s1.setResolver(new CustomResolver());
    start(s1);
    doServiceWithAssertions(s1);
    StatefulSession session1 = strategy.retrieveSessionForTests();
    doServiceWithAssertions(s1);
    StatefulSession session2 = strategy.retrieveSessionForTests();
    stop(s1);
    assertTrue("Sessions should be different", (session1 != session2));
  }

  public void testWithPerpertualStrategy() throws Exception {
    StatefulRuleService s1 = (StatefulRuleService) createHelloWorldRule();
    PerpetualSessionStrategy strategy = new PerpetualSessionStrategy();
    s1.setSessionManagementStrategy(strategy);
    s1.setResolver(new CustomResolver());
    start(s1);
    doServiceWithAssertions(s1);
    StatefulSession session1 = strategy.retrieveSessionForTests();
    doServiceWithAssertions(s1);
    StatefulSession session2 = strategy.retrieveSessionForTests();
    stop(s1);
    assertTrue("Sessions should not be different", (session1 == session2));
  }

  public void testWithTimedStrategy() throws Exception {
    StatefulRuleService s1 = (StatefulRuleService) createHelloWorldRule();
    TimedSessionStrategy strategy = new TimedSessionStrategy(2);
    s1.setSessionManagementStrategy(strategy);
    s1.setResolver(new CustomResolver());
    start(s1);
    doServiceWithAssertions(s1);
    StatefulSession session1 = strategy.retrieveSessionForTests();
    Thread.sleep(3000);
    doServiceWithAssertions(s1);
    StatefulSession session2 = strategy.retrieveSessionForTests();
    stop(s1);
    assertTrue("Sessions should be different", (session1 != session2));
  }

  public void testWithMetadataStrategy() throws Exception {
    StatefulRuleService s1 = (StatefulRuleService) createHelloWorldRule();
    MetadataSessionStrategy strategy = new MetadataSessionStrategy(METADATA_KEY);
    s1.setSessionManagementStrategy(strategy);
    s1.setResolver(new CustomResolver());
    start(s1);
    doServiceWithAssertions(s1);
    StatefulSession session1 = strategy.retrieveSessionForTests();
    doServiceWithAssertions(s1);
    StatefulSession session2 = strategy.retrieveSessionForTests();
    assertTrue("Sessions should not be different", (session1 == session2));
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance()
        .newMessage(MSG_DATA);
    msg.addMetadata(METADATA_KEY, "true");
    doServiceWithAssertions(s1, msg);
    session2 = strategy.retrieveSessionForTests();
    assertTrue("Sessions should be different", (session1 != session2));
    stop(s1);
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    RuleServiceImpl service = new StatefulRuleService();
    service.setResolver(new CustomResolver());
    CompilingRuleBaseWithDsl compiler = new CompilingRuleBaseWithDsl();
    compiler.getRuleSources().addKeyValuePair(
        new KeyValuePair("http://myserver.com/myDomainRuleFile",
            "http://myserver.com/MyDomainSpecificLanguageFile"));
    compiler.getRuleSources().addKeyValuePair(
        new KeyValuePair("http://myserver.com/myDomainRuleFile2",
            "http://myserver.com/MyDomainSpecificLanguageFile"));
    service.setRuntimeRuleBase(compiler);
    service.setAgendaEventListener(new Slf4jLoggingEventListener());
    service.setRuleBaseEventListener(new Slf4jLoggingEventListener());
    service.setRuleFlowEventListener(new Slf4jLoggingEventListener());
    service.setWorkingMemoryEventListener(new Slf4jLoggingEventListener());
    return service;
  }

  @Override
  protected String createBaseFileName(Object object) {
    return object.getClass().getName() + "-WithCompilerAndDsl";
  }

  @Override
  protected RuleServiceImpl createHelloWorldRule() {
    RuleServiceImpl service = new StatefulRuleService();
    String drlFile = PROPERTIES.getProperty("drools.HelloWorld.drl.url");
    CompilingRuleBaseProxy compiler = new CompilingRuleBaseProxy();
    compiler.addRuleSource(drlFile);
    service.setRuntimeRuleBase(compiler);
    return service;
  }

  protected RuleServiceImpl createTicketingRules() {
    RuleServiceImpl service = new StatefulRuleService();
    String drlFile = PROPERTIES.getProperty("drools.Ticketing.drl.url");
    String dslFile = PROPERTIES.getProperty("drools.Ticketing.drl.dsl.url");
    CompilingRuleBaseWithDsl compiler = new CompilingRuleBaseWithDsl();
    compiler.getRuleSources().addKeyValuePair(
        new KeyValuePair(drlFile, dslFile));
    service.setRuntimeRuleBase(compiler);
    return service;
  }
}
