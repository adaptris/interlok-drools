package com.adaptris.core.drools;

import com.adaptris.core.CoreException;

/**
 * Exception encapsulating any underlying exception thrown by the JBoss Rules
 * engine.
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public class RuleException extends CoreException {

  /**
   * 
   */
  private static final long serialVersionUID = -5741986239147575650L;

  public RuleException() {
    super();
  }

  public RuleException(Throwable cause) {
    super(cause);
  }

  public RuleException(String description) {
    super(description);
  }

  public RuleException(String description, Throwable cause) {
    super(description, cause);
  }
}
