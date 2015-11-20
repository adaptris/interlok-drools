package com.adaptris.core.drools;

import org.drools.RuleBase;
import org.drools.StatefulSession;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.licensing.LicensedComponent;

/**
 * Strategy for managing sessions.
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public interface SessionManagementStrategy extends AdaptrisComponent, LicensedComponent {

  /**
   * Get the session from the rulebase based on the strategy in use.
   * 
   * @param b the rulebase.
   * @param msg the AdaptrisMessage
   * @return a Session
   * @throws RuleException
   */
  StatefulSession getSession(RuleBase b, AdaptrisMessage msg)
      throws RuleException;
}
