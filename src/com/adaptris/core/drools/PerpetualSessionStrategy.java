package com.adaptris.core.drools;

import org.drools.RuleBase;
import org.drools.StatefulSession;

import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Session management strategy that retains the same session for the lifetime of
 * the component.
 * <p>
 * The StatefulSession is created upon the first invocation of
 * {@link #getSession(RuleBase, AdaptrisMessage)} and is disposed when this
 * component's {@link #stop()} method is invoked as part of the parent
 * {@link StatefulRuleService} lifecycle.
 * </p>
 * <p>
 * Requires an ENTERPRISE license.
 * </p>
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-perpetual-session-strategy</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-perpetual-session-strategy")
public class PerpetualSessionStrategy extends SessionManagementStrategyImp {

  private transient StatefulSession session = null;

  /**
   * @see SessionManagementStrategy#getSession(RuleBase, AdaptrisMessage)
   */
  @Override
  public synchronized StatefulSession getSession(RuleBase b, AdaptrisMessage msg)
      throws RuleException {
    if (session == null) {
      session = b.newStatefulSession();
    }
    return session;
  }

  /**
   * @see com.adaptris.core.AdaptrisComponent#stop()
   */
  @Override
  public void stop() {
    if (session != null) {
      session.dispose();
      session = null;
    }
  }

  // Dummy for test purposes (yes we know it breaks OO encapulation
  StatefulSession retrieveSessionForTests() {
    return session;
  }
}
