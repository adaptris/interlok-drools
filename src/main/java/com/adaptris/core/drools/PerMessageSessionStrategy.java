package com.adaptris.core.drools;

import org.drools.RuleBase;
import org.drools.StatefulSession;

import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Default management of the session.
 * <p>
 * The strategy here is to create a new session each time {@link #getSession(RuleBase, AdaptrisMessage)} is invoked.
 * </p>
 * 
 * @config drools-per-message-session-strategy
 * @license STANDARD
 * 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-per-message-session-strategy")
public class PerMessageSessionStrategy extends SessionManagementStrategyImp {

  private transient StatefulSession session = null;

  /**
   * @see SessionManagementStrategy#getSession(RuleBase, AdaptrisMessage)
   */
  @Override
  public synchronized StatefulSession getSession(RuleBase b, AdaptrisMessage msg)
      throws RuleException {
    if (session != null) {
      session.dispose();
    }
    session = b.newStatefulSession();
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
