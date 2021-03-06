package com.adaptris.core.drools;

import org.drools.StatelessSession;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Stateless JBoss Rules Engine execution.
 * 
 * @config drools-stateless-rule-service
 * @license STANDARD
 * 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-stateless-rule-service")
@AdapterComponent
@ComponentProfile(summary = "Stateless JBoss Rules Engine execution", tag = "service,drools")
public class StatelessRuleService extends RuleServiceImpl {

  public StatelessRuleService() {
  }

  /**
   *
   * @see com.adaptris.core.Service#doService(com.adaptris.core.AdaptrisMessage)
   */
  @Override
  public final void doService(AdaptrisMessage msg) throws ServiceException {
    try {
      StatelessSession session = ruleBase.newStatelessSession();
      super.addListeners(session);
      Object[] o = getResolver().create(msg);
      session.execute(o);
      getResolver().resolve(o, msg);
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  @Override
  protected void closeDroolsService() {
  }

  @Override
  protected void initDroolsService() throws CoreException {
  }

  @Override
  protected void startService() throws CoreException {
  }

  @Override
  protected void stopService() {
  }
}
