/*
 * $RCSfile: CustomResolver.java,v $
 * $Revision: 1.1 $
 * $Date: 2008/08/11 10:49:44 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.helloworld;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.drools.Resolver;
import com.adaptris.core.drools.RuleException;

public class CustomResolver implements Resolver {
  
  private String uniqueId;

  public void resolve(Object[] src, AdaptrisMessage result)
      throws RuleException {
    Message hw = (Message) src[0];
    result.addMetadata(Message.MESSAGE_METADATA, hw.getMessage());
    result.addMetadata(Message.STATUS_METADATA, String.valueOf(hw.getStatus()));
  }

  public Object[] create(AdaptrisMessage msg) throws RuleException {
    return new Object[]
    {
      new Message(Message.HELLO, "Hello World")
    };
  }

  /**
   * 
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  public void init() throws CoreException {

  }

  /**
   * 
   * @see com.adaptris.core.AdaptrisComponent#start()
   */
  public void start() throws CoreException {

  }

  /**
   * 
   * @see com.adaptris.core.AdaptrisComponent#stop()
   */
  public void stop() {

  }

  /**
   * 
   * @see com.adaptris.core.AdaptrisComponent#close()
   */
  public void close() {

  }

  @Override
  public void prepare() throws CoreException {}

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

}
