/*
 * $RCSfile: Ticket.java,v $
 * $Revision: 1.1 $
 * $Date: 2008/08/11 15:26:40 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.ticket;

//This is directly lifted from the drools TroubleTicket example.
public class Ticket {
  
  public static final String ESCALATED = "Escalate";
  public static final String NEW = "New";
  private Customer customer;
  private String status;

  public Ticket() {

  }

  public Ticket(final Customer customer) {
    super();
    this.customer = customer;
    this.status = NEW;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public String toString() {
    return "[Ticket " + this.customer + " : " + this.status + "]";
  }
}
