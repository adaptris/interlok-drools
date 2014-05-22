/*
 * $RCSfile: TicketResolver.java,v $
 * $Revision: 1.1 $
 * $Date: 2008/08/11 15:26:40 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.ticket;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.drools.Resolver;
import com.adaptris.core.drools.RuleException;
import com.adaptris.util.license.License;
import com.adaptris.util.license.License.LicenseType;

public class TicketResolver implements Resolver {

  final Customer platinum = new Customer(Customer.CUSTOMER_PLATINUM,
      Customer.CUSTOMER_PLATINUM);
  final Customer gold = new Customer(Customer.CUSTOMER_GOLD,
      Customer.CUSTOMER_GOLD);
  final Customer silver = new Customer(Customer.CUSTOMER_SILVER,
      Customer.CUSTOMER_SILVER);
  final Customer bronze = new Customer(Customer.CUSTOMER_BRONZE,
      Customer.CUSTOMER_BRONZE);
  final Ticket silverTicket = new Ticket(silver);
  final Ticket bronzeTicket = new Ticket(bronze);

  public void resolve(Object[] src, AdaptrisMessage result)
      throws RuleException {
    result.addMetadata(silverTicket.getCustomer().getName(), silverTicket.getStatus());
    result.addMetadata(bronzeTicket.getCustomer().getName(), bronzeTicket.getStatus());
  }

  public Object[] create(AdaptrisMessage msg) throws RuleException {
    return new Object[]
    {
        platinum, gold, silver, bronze, silverTicket, bronzeTicket
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

  /**
   * 
   * @see com.adaptris.core.LicensedComponent#isEnabled(License)
   */
  public boolean isEnabled(License l) {
    return l.isEnabled(LicenseType.Standard);
  }
}
