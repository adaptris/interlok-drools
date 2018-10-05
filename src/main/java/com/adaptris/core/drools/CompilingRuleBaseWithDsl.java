package com.adaptris.core.drools;

import java.io.Reader;
import java.util.Iterator;

import javax.validation.constraints.NotNull;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.CoreException;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.util.URLString;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Entry point for creating a RuleBase for using the JBoss Rules Engine.
 * <p>
 * This compiles the Rules with the specific domain specific extensions from the configured source <code>.drl</code> and domain
 * specific files upon initialisation.
 * </p>
 * <p>
 * Generally speaking you should be using {@link RuleAgentProxy} in production as this allows you to use precompiled rules and
 * RuleFlows that have already been packaged for use.
 * <p>
 * <p>
 * A single PackageBuilder instance is used within this service; this means that all the referenced rule sources should belong in
 * the same package.
 * </p>
 * 
 * @config drools-compiling-rule-base-with-dsl
 * @license STANDARD
 * 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-compiling-rule-base-with-dsl")
public class CompilingRuleBaseWithDsl extends CompilingRuleBaseProxyImp {
  @NotNull
  @AutoPopulated
  private KeyValuePairSet ruleSources;

  public CompilingRuleBaseWithDsl() {
    super();
    setRuleSources(new KeyValuePairSet());
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  @Override
  protected void initComponent() throws CoreException {
    try {
      for (Iterator i = ruleSources.getKeyValuePairs().iterator(); i.hasNext();) {
        KeyValuePair kvp = (KeyValuePair) i.next();
        Reader drl = resolve(new URLString(kvp.getKey()));
        Reader dsl = resolve(new URLString(kvp.getValue()));
        builder.addPackageFromDrl(drl, dsl);
        drl.close();
        dsl.close();
      }
    }
    catch (Exception e) {
      throw new CoreException(e);
    }
  }

  /**
   * @return the ruleSources
   */
  public KeyValuePairSet getRuleSources() {
    return ruleSources;
  }

  /**
   * Set the list of rules that will be used.
   * <p>
   * The Key of the {@link KeyValuePair} is the source .drl file, and the value
   * is the specific domain specific extension that is required for the rule.
   * </p>
   *
   * @param l the ruleSources to set
   */
  public void setRuleSources(KeyValuePairSet l) {
    ruleSources = l;
  }

}
