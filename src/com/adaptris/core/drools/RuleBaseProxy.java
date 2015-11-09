package com.adaptris.core.drools;

import org.drools.RuleBase;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.licensing.LicensedComponent;

/**
 * Interface for creating RuleBases
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public interface RuleBaseProxy extends AdaptrisComponent, LicensedComponent {

  /**
   * Get a rulebase for executing the rules.
   * 
   * @return a Rulebase.
   * @throws RuleException wrapping any underlying exception.
   */
  RuleBase createRuleBase() throws RuleException;
}
