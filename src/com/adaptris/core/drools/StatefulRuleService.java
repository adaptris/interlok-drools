package com.adaptris.core.drools;

import org.drools.StatefulSession;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.util.license.License;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Stateful JBoss Rules Engine execution.
 *
 * <p>
 * Requires an ENTERPRISE license.
 * </p>
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-stateful-rule-service</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>

 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-stateful-rule-service")
public class StatefulRuleService extends RuleServiceImpl {

  private SessionManagementStrategy sessionManagementStrategy;
  private boolean dynamicObjects;

  /**
   * Default Constructor.
   *
   * Defaults are:
   * <ul>
   * <li>sessionStrategy is PerMessageSessionStrategy</li>
   * <li>dynamicObjects is true</li>
   * </ul>
   *
   * @see SessionManagementStrategy
   */
  public StatefulRuleService() {
    super();
    setSessionManagementStrategy(new PerMessageSessionStrategy());
    setDynamicObjects(true);
  }

  /**
   *
   * @see com.adaptris.core.Service#doService(com.adaptris.core.AdaptrisMessage)
   */
  @Override
  public final void doService(AdaptrisMessage msg) throws ServiceException {

    try {
      StatefulSession session = sessionManagementStrategy.getSession(ruleBase, msg);
      super.addListeners(session);
      Object[] o = getResolver().create(msg);
      for (Object obj : o) {
        session.insert(obj, getDynamicObjects());
      }
      session.fireAllRules();
      getResolver().resolve(o, msg);
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }

  }

  @Override
  protected boolean serviceIsEnabled(License l) throws CoreException {
    return l.isEnabled(License.ENTERPRISE);
  }

  @Override
  protected void closeService() {
    LifecycleHelper.close(sessionManagementStrategy);
  }

  @Override
  protected void initService() throws CoreException {
    LifecycleHelper.init(sessionManagementStrategy);
  }

  @Override
  protected void startService() throws CoreException {
    LifecycleHelper.start(sessionManagementStrategy);
  }

  @Override
  protected void stopService() {
    LifecycleHelper.stop(sessionManagementStrategy);
  }

  /**
   * @return the sessionStrategy
   */
  public SessionManagementStrategy getSessionManagementStrategy() {
    return sessionManagementStrategy;
  }

  /**
   * Set the strategy for managing sessions.
   * <p>
   * The ruleBase retains a reference to each StatefulSession is creates, so
   * that it can update them when new rules are added, dispose() is needed to
   * release the StatefulSession reference from the RuleBase, without it you can
   * get memory leaks.
   * </p>
   *
   * @see SessionManagementStrategy
   * @param s the sessionStrategy to set
   */
  public void setSessionManagementStrategy(SessionManagementStrategy s) {
    if (s == null) {
      throw new IllegalArgumentException(
          "SessionManagementStrategy may not be null");
    }
    sessionManagementStrategy = s;
  }

  /**
   * @return the dynamicObjects
   */
  public boolean getDynamicObjects() {
    return dynamicObjects;
  }

  /**
   * Specify whether the objects are dynamic with PropertyChangeListeners.
   *
   * @param b the dynamicObjects to set
   * @see org.drools.WorkingMemory#insert(Object, boolean)
   */
  public void setDynamicObjects(boolean b) {
    dynamicObjects = b;
  }
}
