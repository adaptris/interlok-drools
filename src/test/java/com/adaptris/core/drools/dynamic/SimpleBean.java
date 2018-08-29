/*
 * $RCSfile: SimpleBean.java,v $
 * $Revision: 1.2 $
 * $Date: 2008/08/20 15:03:43 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.dynamic;

import java.math.BigDecimal;

import com.adaptris.core.jdbc.DatabaseConnection;

public class SimpleBean {
  private String stringField;
  private boolean booleanField;
  private int intField;
  private BigDecimal bigDecimalField;
  private float floatField;
  private double doubleField;
  private long longField;
  private DatabaseConnection databaseConnection;

  /**
   * @return the stringField1
   */
  public String getStringField() {
    return stringField;
  }

  /**
   * @param stringField1 the stringField1 to set
   */
  public void setStringField(String stringField1) {
    this.stringField = stringField1;
  }

  /**
   * @return the booleanField1
   */
  public boolean getBooleanField() {
    return booleanField;
  }

  /**
   * @param booleanField1 the booleanField1 to set
   */
  public void setBooleanField(boolean booleanField1) {
    this.booleanField = booleanField1;
  }

  /**
   * @return the intField1
   */
  public int getIntField() {
    return intField;
  }

  /**
   * @param intField1 the intField1 to set
   */
  public void setIntField(int intField1) {
    this.intField = intField1;
  }

  /**
   * @return the bigDecimalField
   */
  public BigDecimal getBigDecimalField() {
    return bigDecimalField;
  }

  /**
   * @param bigDecimalField the bigDecimalField to set
   */
  public void setBigDecimalField(BigDecimal bigDecimalField) {
    this.bigDecimalField = bigDecimalField;
  }

  /**
   * @return the floatField
   */
  public float getFloatField() {
    return floatField;
  }

  /**
   * @param floatField the floatField to set
   */
  public void setFloatField(float floatField) {
    this.floatField = floatField;
  }

  /**
   * @return the doubleField
   */
  public double getDoubleField() {
    return doubleField;
  }

  /**
   * @param doubleField the doubleField to set
   */
  public void setDoubleField(double doubleField) {
    this.doubleField = doubleField;
  }

  /**
   * @return the longField
   */
  public long getLongField() {
    return longField;
  }

  /**
   * @param longField the longField to set
   */
  public void setLongField(long longField) {
    this.longField = longField;
  }

  /**
   * @return the databaseConnection
   */
  public DatabaseConnection getDatabaseConnection() {
    return databaseConnection;
  }

  /**
   * Set the database connection for the bean.
   * <p>
   * Databaseconnection is chosen explicitly because we can marshal it, and
   * there are lots of implementations that we could use ;)
   * </p>
   * 
   * @param d the databaseConnection to set
   */
  public void setDatabaseConnection(DatabaseConnection d) {
    this.databaseConnection = d;
  }

}
