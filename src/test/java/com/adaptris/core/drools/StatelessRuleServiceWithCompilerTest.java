/*
 * $RCSfile: StatelessRuleServiceWithCompilerTest.java,v $
 * $Revision: 1.2 $
 * $Date: 2008/08/11 15:26:40 $
 * $Author: lchan $
 */
package com.adaptris.core.drools;

import com.adaptris.core.drools.helloworld.CustomResolver;

public class StatelessRuleServiceWithCompilerTest extends BaseRuleServiceCase {

  public StatelessRuleServiceWithCompilerTest() {
    super();
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    RuleServiceImpl service = new StatelessRuleService();
    service.setResolver(new CustomResolver());
    CompilingRuleBaseProxy compiler = new CompilingRuleBaseProxy();
    compiler.addRuleSource("http://MyServer.com/HelloWorld.drl");
    compiler.addRuleSource("http://MyServer.com/HelloWorld2.drl");
    service.setRuntimeRuleBase(compiler);
    return service;
  }

  @Override
  protected RuleServiceImpl createHelloWorldRule() {
    RuleServiceImpl service = new StatelessRuleService();
    String drlFile = PROPERTIES.getProperty("drools.HelloWorld.drl.url");
    CompilingRuleBaseProxy compiler = new CompilingRuleBaseProxy();
    compiler.addRuleSource(drlFile);
    service.setRuntimeRuleBase(compiler);
    return service;
  }

  @Override
  protected String createBaseFileName(Object object) {
    return object.getClass().getName() + "-WithCompiler";
  }

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
}
