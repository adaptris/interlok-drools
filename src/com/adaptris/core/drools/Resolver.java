package com.adaptris.core.drools;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.AdaptrisMessage;

/**
 * Handles the transformation of the AdaptrisMessage to Facts used within the
 * Jboss Rules engine and vice versa.
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public interface Resolver extends AdaptrisComponent {

  /**
   * Create facts for insertion into the Rule Engine
   * 
   * @param msg the AdaptrisMessage
   * @return a list object which will be passed in to the rule engine.
   * @throws RuleException wrapping any underlying exception.
   */
  Object[] create(AdaptrisMessage msg) throws RuleException;

  /**
   * Result result of the rule engine to the AdaptrisMessage
   * <p>
   * This simply provides the objects previously created with
   * {@link #create(AdaptrisMessage)}, so it may have no meaning if your
   * concrete implementation maintains its own state.
   * </p>
   * 
   * @param result the AdaptrisMessage
   * @param src the objects that were inserted into the rule engine.
   * @throws RuleException wrapping any underlying exception.
   */
  void resolve(Object[] src, AdaptrisMessage result) throws RuleException;
}
