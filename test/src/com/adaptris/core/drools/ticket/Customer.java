/*
 * $RCSfile: Customer.java,v $
 * $Revision: 1.1 $
 * $Date: 2008/08/11 15:26:40 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.ticket;


// This is directly lifted from the drools TroubleTicket example.
public class Customer {

  public static final String CUSTOMER_BRONZE = "Bronze";
  public static final String CUSTOMER_SILVER = "Silver";
  public static final String CUSTOMER_GOLD = "Gold";
  public static final String CUSTOMER_PLATINUM = "Platinum";
  
  
  private String name;
  private String subscription;

  public Customer() {

  }

  public Customer(final String name,
                  final String subscription) {
      super();
      this.name = name;
      this.subscription = subscription;
  }

  public String getName() {
      return this.name;
  }

  public String getSubscription() {
      return this.subscription;
  }

  public String toString() {
      return "[Customer " + this.name + " : " + this.subscription + "]";
  }

}

