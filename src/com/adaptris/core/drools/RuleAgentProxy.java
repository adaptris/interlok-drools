package com.adaptris.core.drools;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.drools.RuleBase;
import org.drools.agent.AgentEventListener;
import org.drools.agent.RuleAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.MarshallingImperative;
import com.adaptris.core.CoreException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.util.license.License;

/**
 * Proxy for creating a rule agent.
 * <p>
 * This keeps a static Map of all the configured RuleAgents keyed against the
 * RuleAgentProperties; this means that an attempt is made to re-use the agent
 * if the same rulebase configuration is specified. This makes an attempt to
 * conform to the requirement laid down in {@link org.drools.agent.RuleAgent}
 * which specifies that <b>You should only have ONE instance of an agent per
 * rulebase configuration</b>
 * </p>
 * <p>
 * Due to flexibility of configuration, it is possible to violate that
 * requirement (simply perhaps by changing RuleAgent name); in such cases
 * results may be undefined.
 * </p>
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-rule-agent-proxy</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-rule-agent-proxy")
@MarshallingImperative(mapTo = "drools-rule-agent-proxy", transientFields = {})
public class RuleAgentProxy implements RuleBaseProxy {
  private static transient Logger logR = LoggerFactory.getLogger(RuleAgentProxy.class);

  private KeyValuePairSet ruleAgentProperties;
  private transient RuleAgent ruleAgent;
  private AgentEventListener agentEventListener;

  private static final Map<Properties, RuleAgent> RULEAGENT_HASH;

  static {
    RULEAGENT_HASH = new Hashtable<Properties, RuleAgent>();
  }

  public RuleAgentProxy() {
    setRuleAgentProperties(new KeyValuePairSet());
  }

  /**
   *
   * @see com.adaptris.core.LicensedComponent#isEnabled(License)
   */
  @Override
  public boolean isEnabled(License l) throws CoreException {
    return l.isEnabled(License.ENTERPRISE);
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  @Override
  public void init() throws CoreException {
    ruleAgent = create(this);
  }

  private synchronized static RuleAgent create(RuleAgentProxy rap) {
    Properties p = KeyValuePairSet.asProperties(rap.getRuleAgentProperties());
    RuleAgent result = null;
    if (RULEAGENT_HASH.containsKey(p)) {
      result = RULEAGENT_HASH.get(p);
    }
    else {
      result = RuleAgent.newRuleAgent(p, rap.getAgentEventListener() != null
          ? rap.getAgentEventListener()
          : new CommonsLoggingEventListener());
      RULEAGENT_HASH.put(p, result);
    }
    return result;
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#start()
   */
  @Override
  public void start() throws CoreException {
    if (ruleAgent.isPolling()) {
      ruleAgent.startPolling();
    }
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#stop()
   */
  @Override
  public void stop() {
    if (ruleAgent.isPolling()) {
      ruleAgent.stopPolling();
    }
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#close()
   */
  @Override
  public void close() {
  }

  /**
   *
   * @see com.adaptris.core.drools.RuleBaseProxy#createRuleBase()
   */
  @Override
  public RuleBase createRuleBase() {
    return ruleAgent.getRuleBase();
  }

  /**
   * @return the ruleAgentProperties
   */
  public KeyValuePairSet getRuleAgentProperties() {
    return ruleAgentProperties;
  }

  /**
   * Set the properties used to create the RuleAgent.
   * <p>
   * Valid properties are (taken from the Drools manual).
   * <ul>
   * <li>newInstance=true</li>
   * <li>file=/foo/bar/boo.pkg /foo/bar/boo2.pkg</li>
   * <li>dir=/my/dir</li>
   * <li>url=http://some.url/here http://some.url/here</li>
   * <li>localCacheDir=/foo/bar/cache</li>
   * <li>poll=30</li>
   * <li>name=MyConfig</li>
   * </ul>
   * </p>
   * <p>
   * <li>
   * <p>
   * newInstance
   * </p>
   * <p>
   * Setting this to "true" means that the RuleBase instance will be created
   * fresh each time there is a change. this means you need to do
   * agent.getRuleBase() to get the new updated rulebase (any existing ones in
   * use will be untouched). The default is false, which means rulebases are
   * updated "in place" - ie you don't need to keep calling getRuleBase() to
   * make sure you have the latest rules (also any StatefulSessions will be
   * updated automatically with rule changes). <b>If set to true, then the
   * adapter will not refresh its rules automatically, and will require a
   * reinitialisation of the service before rule changes are picked up.</b>
   * </p>
   * </li>
   * <li>
   * <p>
   * file
   * </p>
   * <p>
   * This is a space-separated list of files - each file is a binary package as
   * exported by the BRMS. You can have one or many. The name of the file is not
   * important. Each package must be in its own file.
   * </p>
   * <p>
   * NOTE: it is also possible to specify .drl files - and it will compile it
   * into the package. However, note that for this to work, you will need the
   * drools-compiler dependencies in your applications classpath (as opposed to
   * just the runtime dependencies).
   * </p>
   * <p>
   * Please note that if the path has a space in it, you will need to put double
   * quotes around it (as the space is used to separate different items, and it
   * will not work otherwise). Generally spaces in a path name are best to
   * avoid.
   * </p>
   * </li>
   * <li>
   * <p>
   * dir
   * </p>
   * <p>
   * This is similar to file, except that instead of specifying a list of files
   * you specify a directory, and it will pick up all the files in there (each
   * one is a package) and add them to the rulebase. Each package must be in its
   * own file.
   * </p>
   * <p>
   * Please note that if the path has a space in it, you will need to put double
   * quotes around it (as the space is used to separate different items, and it
   * will not work otherwise). Generally spaces in a path name are best to
   * avoid.
   * </p>
   * </li>
   * <li>
   * <p>
   * url
   * </p>
   * <p>
   * This is a space separated list of URLs to the BRMS which is exposing the
   * packages (see below for more details).
   * </p>
   * </li>
   * <li>
   * <p>
   * localCacheDir
   * </p>
   * <p>
   * This is used in conjunction with the url above, so that if the BRMS is down
   * (the url is not accessible) then if the runtime has to start up, it can
   * start up with the last known "good" versions of the packages.
   * </p>
   * </li>
   * <li>
   * <p>
   * poll
   * </p>
   * <p>
   * This is set to the number of seconds to check for changes to the resources
   * (a timer is used).
   * </p>
   * </li>
   * <li>
   * <p>
   * name
   * </p>
   * <p>
   * This is used to specify the name of the agent which is used when logging
   * events (as typically you would have multiple agents in a system).
   * </p>
   * </li></ul>
   * </p>
   *
   * @param kvps the ruleAgentProperties to set
   */
  public void setRuleAgentProperties(KeyValuePairSet kvps) {
    ruleAgentProperties = kvps;
  }

  /**
   * @return the agentEventListener
   */
  public AgentEventListener getAgentEventListener() {
    return agentEventListener;
  }

  /**
   * Specify the listener for handling Agent lifecycle events.
   *
   * @param ael the agentEventListener to set, null to use the default.
   */
  public void setAgentEventListener(AgentEventListener ael) {
    agentEventListener = ael;
  }

  /**
   * Overrides the default AgentEventListener with one that logs to the
   * configured commons logger.
   */
  private static class CommonsLoggingEventListener implements
      AgentEventListener {
    private String name;

    @Override
    public void exception(Exception e) {
      logR.error("RuleAgent(" + name + ") : " + e.getMessage()
          + ". Stack trace should follow.", e);
    }

    @Override
    public void info(String message) {
      logR.info("RuleAgent(" + name + ") : " + message);
    }

    @Override
    public void warning(String message) {
      logR.warn("RuleAgent(" + name + ") : " + message);
    }

    @Override
    public void debug(String message) {
      logR.debug("RuleAgent(" + name + ") : " + message);
    }

    @Override
    public void setAgentName(String s) {
      name = s;
    }
  }
}
