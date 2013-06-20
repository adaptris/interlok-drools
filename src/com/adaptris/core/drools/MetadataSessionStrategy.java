package com.adaptris.core.drools;

import org.drools.RuleBase;
import org.drools.StatefulSession;

import com.adaptris.annotation.MarshallingImperative;
import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Session management strategy that retains the same session until a certain
 * metadata key is set on the message.
 * <p>
 * The StatefulSession is created upon the first invocation of
 * {@link #getSession(RuleBase, AdaptrisMessage)} and is disposed when the
 * AdaptrisMessage contains the configurable metadata key.
 * </p>
 * <p>
 * Requires an ENTERPRISE license.
 * </p>
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-metadata-session-strategy</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-metadata-session-strategy")
@MarshallingImperative(mapTo = "drools-metadata-session-strategy", transientFields = {})
public class MetadataSessionStrategy extends SessionManagementStrategyImp {

  private static final String DEFAULT_METADATA_KEY = "jboss.drools.session.dispose";

  private transient StatefulSession session = null;
  private String metadataKey;

  /**
   * Default Constructor.
   * <p>
   * The default metadata key is "jboss.drools.session.dispose";
   * </p>
   */
  public MetadataSessionStrategy() {
    setMetadataKey(DEFAULT_METADATA_KEY);
  }

  /**
   * Constructor with key that expires the session
   *
   * @param s the metadata key.
   */
  public MetadataSessionStrategy(String s) {
    this();
    setMetadataKey(s);
  }

  /**
   * @see SessionManagementStrategy#getSession(RuleBase, AdaptrisMessage)
   */
  @Override
  public synchronized StatefulSession getSession(RuleBase b, AdaptrisMessage msg)
      throws RuleException {
    if (session == null) {
      session = b.newStatefulSession();
    }
    else {
      if (msg.containsKey(getMetadataKey())) {
        session.dispose();
        session = b.newStatefulSession();
      }
    }
    return session;
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
   * @return the metadataKey
   */
  public String getMetadataKey() {
    return metadataKey;
  }

  /**
   * Set the metadata key which will force disposal of the session.
   *
   * @param s the metadataKey to set
   */
  public void setMetadataKey(String s) {
    metadataKey = s;
  }
}
