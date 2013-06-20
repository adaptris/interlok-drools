/*
 * $RCSfile: Message.java,v $
 * $Revision: 1.1 $
 * $Date: 2008/08/11 10:49:44 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.helloworld;

/**
 * Simple bean to demonstrate rules.
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public class Message {
  public static final int HELLO = 0;
  public static final int GOODBYE = 1;

  public static final String MESSAGE_METADATA = "HelloWorldMessage";
  public static final String STATUS_METADATA = "HelloWorldStatus";

  private String message;

  private int status;

  public Message() {

  }

  public Message(int status, String msg) {
    setStatus(status);
    setMessage(msg);
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
