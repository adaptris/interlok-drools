package com.adaptris.core.drools;

import org.drools.RuleBase;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.ComponentLifecycle;
import com.adaptris.core.ComponentLifecycleExtension;

/**
 * Interface for creating RuleBases
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public interface RuleBaseProxy extends ComponentLifecycle, ComponentLifecycleExtension {

  /**
   * Get a rulebase for executing the rules.
   * 
   * @return a Rulebase.
   * @throws RuleException wrapping any underlying exception.
   */
  RuleBase createRuleBase() throws RuleException;
}
