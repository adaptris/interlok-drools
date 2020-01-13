/*
 * $RCSfile: RuleFlowServiceTest.java,v $
 * $Revision: 1.2 $
 * $Date: 2008/08/11 15:26:40 $
 * $Author: lchan $
 */
package com.adaptris.core.drools;

import com.adaptris.core.drools.helloworld.CustomResolver;

public class RuleFlowServiceTest extends DroolsServiceExample {

  public RuleFlowServiceTest() {
    super();
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    StatefulRuleFlowService service = new StatefulRuleFlowService();
    service.setResolver(new CustomResolver());
    service.setRuleFlowName("MyFlowName");
    CompilingRuleBaseProxy compiler = new CompilingRuleBaseProxy();
    compiler.addRuleSource("http://MyServer.com/HelloWorld.drl");
    compiler.addRuleSource("http://MyServer.com/HelloWorld2.drl");
    compiler.addRuleFlow("http://MyServer.com/HelloWorld2.frm");
    service.setRuntimeRuleBase(compiler);
    return service;
  }

  @Override
  protected String createBaseFileName(Object object) {
    return  object.getClass().getName() + "-WithCompiler";
  }

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
}
