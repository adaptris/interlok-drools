package com.adaptris.core.drools;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.AdaptrisMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Session management strategy that retains the same session until a certain metadata key is set on the message.
 * <p>
 * The StatefulSession is created upon the first invocation of {@link #getSession(RuleBase, AdaptrisMessage)} and is disposed when
 * the AdaptrisMessage contains the configurable metadata key.
 * </p>
 * 
 * @config drools-metadata-session-strategy
 * @license STANDARD
 * 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-metadata-session-strategy")
public class MetadataSessionStrategy extends SessionManagementStrategyImp {

  private static final String DEFAULT_METADATA_KEY = "jboss.drools.session.dispose";

  private transient StatefulSession session = null;
  @NotBlank
  @AutoPopulated
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
