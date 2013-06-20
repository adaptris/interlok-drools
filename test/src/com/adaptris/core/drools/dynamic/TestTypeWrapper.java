/*
 * $RCSfile: TestTypeWrapper.java,v $
 * $Revision: 1.2 $
 * $Date: 2008/08/20 15:03:43 $
 * $Author: lchan $
 */
package com.adaptris.core.drools.dynamic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.DefaultMarshaller;
import com.adaptris.core.jdbc.FailoverJdbcConnection;
import com.adaptris.core.jdbc.JdbcConnection;

public class TestTypeWrapper {

  @Test
  public void testString() throws Exception {
    SimpleType st = new SimpleType(SimpleType.Type.STRING);
    assertEquals("String", st.unwrap(st.wrap("String")));
  }

  @Test
  public void testInteger() throws Exception {
    SimpleType st = new SimpleType(SimpleType.Type.INTEGER);
    assertEquals("1", st.unwrap(st.wrap("1")));
  }

  @Test
  public void testBoolean() throws Exception {
    SimpleType st = new SimpleType(SimpleType.Type.BOOLEAN);
    assertEquals("false", st.unwrap(st.wrap("false")));
  }

  @Test
  public void testLong() throws Exception {
    SimpleType st = new SimpleType(SimpleType.Type.LONG);
    assertEquals("10", st.unwrap(st.wrap("10")));
  }

  @Test
  public void testFloat() throws Exception {
    SimpleType st = new SimpleType(SimpleType.Type.STRING);
    assertEquals("1.0", st.unwrap(st.wrap("1.0")));
  }

  @Test
  public void testDouble() throws Exception {
    SimpleType st = new SimpleType(SimpleType.Type.STRING);
    assertEquals("1.0", st.unwrap(st.wrap("1.0")));
  }

  @Test
  public void testBigDecimal() throws Exception {
    SimpleType st = new SimpleType(SimpleType.Type.STRING);
    assertEquals("10.0", st.unwrap(st.wrap("10.0")));
  }

  @Test
  public void testObjectWrapper() throws Exception {
    AdaptrisMarshaller cm = DefaultMarshaller.getDefaultMarshaller();
    FailoverJdbcConnection c = new FailoverJdbcConnection();
    c.addConnectUrl("url1");
    String xml = cm.marshal(c);
    MarshalledTypeWrapper w = new MarshalledTypeWrapper();
    assertEquals(xml, w.unwrap(w.wrap(xml)));
  }

  @Test
  public void testObjectWrapperWithClass() throws Exception {
    AdaptrisMarshaller cm = DefaultMarshaller.getDefaultMarshaller();
    JdbcConnection c = new JdbcConnection();
    c.setConnectUrl("connecturl");
    String xml = cm.marshal(c);
    MarshalledTypeWrapper w = new MarshalledTypeWrapper(c.getClass()
        .getCanonicalName());
    assertEquals(xml, w.unwrap(w.wrap(xml)));
  }
}
