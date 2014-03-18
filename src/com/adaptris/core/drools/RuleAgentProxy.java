package com.adaptris.core.drools;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.drools.RuleBase;
import org.drools.agent.AgentEventListener;
import org.drools.agent.RuleAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.CoreException;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.util.license.License;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Proxy for creating a rule agent.
 * <p>
 * This keeps a static Map of all the configured RuleAgents keyed against the RuleAgentProperties; this means that an attempt is
 * made to re-use the agent if the same rulebase configuration is specified. This makes an attempt to conform to the requirement
 * laid down in {@link org.drools.agent.RuleAgent} which specifies that <b>You should only have ONE instance of an agent per
 * rulebase configuration</b>
 * </p>
 * <p>
 * Due to flexibility of configuration, it is possible to violate that requirement (simply perhaps by changing RuleAgent name); in
 * such cases results may be undefined.
 * </p>
 * 
 * <p>
 * In the adapter configuration file this class is aliased as <b>drools-rule-agent-proxy</b> which is the preferred alternative to
 * the fully qualified classname when building your configuration.
 * </p>
 * 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-rule-agent-proxy")
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
          : new DefaultLoggingEventListenerImpl());
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
   * Check the Drools manual for the valid properties; the key of the key-value-pair is the property name, the value the value.
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
   * Overrides the default AgentEventListener with one that logs to the configured logger.
   */
  private static class DefaultLoggingEventListenerImpl implements AgentEventListener {
    private String name;

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

    @Override
    public void info(String message, Object object) {
      logR.info("RuleAgent(" + name + ") : Message[" + message + "] Object[" + object + "]");
    }

    @Override
    public void warning(String message, Object object) {
      logR.warn("RuleAgent(" + name + ") : Message[" + message + "] Object[" + object + "]");
    }

    @Override
    public void debug(String message, Object object) {
      logR.debug("RuleAgent(" + name + ") : Message[" + message + "] Object[" + object + "]");

    }

    @Override
    public void exception(String message, Throwable e) {
      logR.error("RuleAgent(" + name + ") : Message[" + message + "]", e);
    }

    @Override
    public void exception(Throwable e) {
      logR.error("RuleAgent(" + name + ") : " + e.getMessage() + ". Stack trace should follow.", e);
    }

  }
}
