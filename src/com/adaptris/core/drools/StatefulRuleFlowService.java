package com.adaptris.core.drools;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.drools.StatefulSession;
import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.util.license.License;
import com.adaptris.util.license.License.LicenseType;
import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * Stateful JBoss Rules Engine execution using a Ruleflow.
 * 
 * <p>
 * Requires an Standard license.
 * </p>
 * 
 * <p>
 * In the adapter configuration file this class is aliased as <b>drools-stateful-rule-flow-service</b> which is the preferred
 * alternative to the fully qualified classname when building your configuration.
 * </p>
 * 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-stateful-rule-flow-service")
public class StatefulRuleFlowService extends RuleServiceImpl {

  @NotNull
  @Valid
  @AutoPopulated
  private SessionManagementStrategy sessionStrategy;
  @NotBlank
  private String ruleFlowName;

  /**
   *
   */
  public StatefulRuleFlowService() {
    super();
    setSessionManagementStrategy(new PerMessageSessionStrategy());
  }

  /**
   *
   * @see com.adaptris.core.Service#doService(com.adaptris.core.AdaptrisMessage)
   */
  @Override
  public final void doService(AdaptrisMessage msg) throws ServiceException {

    try {
      StatefulSession session = sessionStrategy.getSession(ruleBase, msg);
      super.addListeners(session);
      Object[] o = getResolver().create(msg);
      for (Object obj : o) {
        session.insert(obj);
      }
      session.startProcess(getRuleFlowName());
      session.fireAllRules();
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
    LifecycleHelper.close(sessionStrategy);
  }

  @Override
  protected void initService() throws CoreException {
    LifecycleHelper.init(sessionStrategy);
    if (getRuleFlowName() == null) {
      throw new CoreException("Ruleflow name is null");
    }
  }

  @Override
  protected void startService() throws CoreException {
    LifecycleHelper.start(sessionStrategy);
  }

  @Override
  protected void stopService() {
    LifecycleHelper.stop(sessionStrategy);
  }

  /**
   * @return the sessionStrategy
   */
  public SessionManagementStrategy getSessionManagementStrategy() {
    return sessionStrategy;
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
    sessionStrategy = s;
  }

  /**
   * @return the ruleFlowName
   */
  public String getRuleFlowName() {
    return ruleFlowName;
  }

  /**
   * Set the Ruleflow name that will be triggered.
   *
   * @param s the ruleFlowName to set
   */
  public void setRuleFlowName(String s) {
    ruleFlowName = s;
  }
}
