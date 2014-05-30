package com.adaptris.core.drools;

import org.drools.StatelessSession;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.util.license.License;
import com.adaptris.util.license.License.LicenseType;
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
  protected boolean serviceIsEnabled(License l) throws CoreException {
    return l.isEnabled(LicenseType.Standard);
  }

  @Override
  protected void closeService() {
  }

  @Override
  protected void initService() throws CoreException {
  }

  @Override
  protected void startService() throws CoreException {
  }

  @Override
  protected void stopService() {
  }
}
