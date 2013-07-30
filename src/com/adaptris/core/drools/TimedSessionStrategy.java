package com.adaptris.core.drools;

import java.util.Calendar;

import org.drools.RuleBase;
import org.drools.StatefulSession;

import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Session management strategy that retains the same session for a configurable
 * length of time.
 * <p>
 * The StatefulSession is created upon the first invocation of
 * {@link #getSession(RuleBase, AdaptrisMessage)} and is disposed when the
 * interval between calls to {@link #getSession(RuleBase, AdaptrisMessage)}
 * exceeds the configured number of seconds..
 * </p>
 * <p>
 * Requires an ENTERPRISE license.
 * </p>
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-timed-session-strategy</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-timed-session-strategy")
public class TimedSessionStrategy extends SessionManagementStrategyImp {

  private transient StatefulSession session = null;
  private int sessionLifetimeSecs;
  private transient Calendar sessionStartDate;
  private transient Calendar sessionEndDate;

  /**
   * Default Constructor.
   * <p>
   * The default Session Lifetime is 600 seconds.
   * </p>
   */
  public TimedSessionStrategy() {
    setSessionLifetimeSecs(600);
  }

  /**
   * Constructor with session lifetime.
   *
   * @param lifetime the session lifetime.
   */
  public TimedSessionStrategy(int lifetime) {
    this();
    setSessionLifetimeSecs(lifetime);
  }

  /**
   * @see SessionManagementStrategy#getSession(RuleBase, AdaptrisMessage)
   */
  @Override
  public synchronized StatefulSession getSession(RuleBase b, AdaptrisMessage msg)
      throws RuleException {
    if (hasSessionExpired()) {
      createSession(b, msg);
    }
    return session;
  }

  private void createSession(RuleBase b, AdaptrisMessage msg)
      throws RuleException {
    session = b.newStatefulSession();
    sessionStartDate = Calendar.getInstance();
    sessionEndDate = Calendar.getInstance();
    sessionEndDate.add(Calendar.SECOND, getSessionLifetimeSecs());

  }

  private boolean hasSessionExpired() {
    boolean result = false;
    if (sessionEndDate == null) {
      result = true;
    }
    else {
      Calendar cal = Calendar.getInstance();
      if (cal.after(sessionEndDate)) {
        result = true;
      }
    }
    return result;
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

  /**
   * @return the sessionLifetimeSecs
   */
  public int getSessionLifetimeSecs() {
    return sessionLifetimeSecs;
  }

  /**
   * Set the session lifetime in seconds.
   *
   * @param i the sessionLifetimeSecs to set
   */
  public void setSessionLifetimeSecs(int i) {
    sessionLifetimeSecs = i;
  }
}
