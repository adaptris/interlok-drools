package com.adaptris.core.drools;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.adaptris.annotation.MarshallingImperative;
import com.adaptris.core.CoreException;
import com.adaptris.util.URLString;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Entry point for creating a RuleBase for using the JBoss Rules Engine.
 * <p>
 * This compiles the Rules from the configured source <code>.drl</code> files
 * upon initialisation.
 * </p>
 * <p>
 * Generally speaking you should be using {@link RuleAgentProxy} in
 * production as this allows you to use precompiled rules and RuleFlows that
 * have already been packaged for use.
 * <p>
 * <p>
 * A single PackageBuilder instance is used within this service; this means that
 * all the referenced rule sources should belong in the same package.
 * </p>
 * <p>
 * Requires an ENTERPRISE license.
 * </p>
 *
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-compiling-rule-base-proxy</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>

 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-compiling-rule-base-proxy")
@MarshallingImperative(mapTo = "drools-compiling-rule-base-proxy", transientFields = {})
public class CompilingRuleBaseProxy extends CompilingRuleBaseProxyImp {
  @XStreamImplicit(itemFieldName = "rule-source")
  private List<String> ruleSources;

  public CompilingRuleBaseProxy() {
    super();
    setRuleSources(new ArrayList<String>());
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  @Override
  protected void initComponent() throws CoreException {
    try {
      for (String s : ruleSources) {
        Reader source = resolve(new URLString(s));
        builder.addPackageFromDrl(source);
        source.close();
      }
    }
    catch (Exception e) {
      throw new CoreException(e);
    }
  }

  /**
   * @return the ruleSources
   */
  public List<String> getRuleSources() {
    return ruleSources;
  }

  /**
   * Set the list of rules that will be used.
   * <p>
   * The rule source is a list of URLs that that contain jboss rule engine
   * source files ".drl". These will be compiled into package files upon
   * component initialisation.
   * </p>
   *
   * @param l the ruleSources to set
   */
  public void setRuleSources(List<String> l) {
    ruleSources = l;
  }

  public void addRuleSource(String r) {
    if (r != null) {
      ruleSources.add(r);
    }
    else {
      logR.debug("Ignoring null RuleSource parameter");
    }
  }

}
