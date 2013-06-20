/*
 * $RCSfile: CompilingRuleBaseProxyImp.java,v $
 * $Revision: 1.2 $
 * $Date: 2008/11/12 10:25:02 $
 * $Author: lchan $
 */
package com.adaptris.core.drools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.CoreException;
import com.adaptris.core.util.ProxyUtil;
import com.adaptris.util.URLString;
import com.adaptris.util.license.License;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Abstract Entry point for creating a RuleBase for using the JBoss Rules
 * Engine.
 *
 * @author lchan
 * @author $Author: lchan $
 */
public abstract class CompilingRuleBaseProxyImp implements RuleBaseProxy {
  protected transient Logger logR;
  protected transient PackageBuilder builder;
  private transient RuleBase ruleBase;
  @XStreamImplicit
  private List<String> ruleFlows;

  public CompilingRuleBaseProxyImp() {
    logR = LoggerFactory.getLogger(this.getClass().getName());
    setRuleFlows(new ArrayList<String>());
  }

  /**
   *
   * @see com.adaptris.core.LicensedComponent#isEnabled(License)
   */
  public boolean isEnabled(License l) throws CoreException {
    return l.isEnabled(License.ENTERPRISE);
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  public final void init() throws CoreException {
    builder = new PackageBuilder();
    ruleBase = RuleBaseFactory.newRuleBase();

    try {
      initComponent();
      for (String s : ruleFlows) {
        Reader source = resolve(new URLString(s));
        builder.addRuleFlow(source);
        source.close();
      }
      // Check the builder for errors
      if (builder.hasErrors()) {
        logR.error(builder.getErrors().toString());
        throw new RuleException("Unable to compile all rules");
      }
      // get the compiled package (which is serializable)
      Package pkg = builder.getPackage();
      ruleBase.addPackage(pkg);
    }
    catch (Exception e) {
      throw new CoreException(e);
    }
  }

  protected abstract void initComponent() throws CoreException;

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
   * @see com.adaptris.core.drools.RuleBaseProxy#createRuleBase()
   */
  public RuleBase createRuleBase() {
    return ruleBase;
  }

  protected Reader resolve(URLString loc) throws IOException {
    if (loc.getProtocol() == null || "file".equals(loc.getProtocol())) {
      return resolve(loc.getFile());
    }
    URL url = new URL(loc.toString());
    URLConnection conn = url.openConnection();
    ProxyUtil.applyBasicProxyAuthorisation(conn);
    return new InputStreamReader(conn.getInputStream());
  }

  private Reader resolve(String localFile) throws IOException {
    InputStream in = null;
    File f = new File(localFile);
    if (f.exists()) {
      in = new FileInputStream(f);
    }
    else {
      ClassLoader c = this.getClass().getClassLoader();
      URL u = c.getResource(localFile);
      if (u != null) {
        logR.trace("Found " + u.toExternalForm());
        in = u.openStream();
      }
    }
    if (in == null) {
      throw new IOException("Could not find " + localFile);
    }
    return new InputStreamReader(in);
  }

  /**
   * @return the ruleFlows
   */
  public List<String> getRuleFlows() {
    return ruleFlows;
  }

  /**
   * @param ruleFlows the ruleFlows to set
   */
  public void setRuleFlows(List<String> ruleFlows) {
    this.ruleFlows = ruleFlows;
  }

  /**
   * Add a RuleFlow source to the rulebase.
   * <p>
   * The rule source is a list of URLs that that contain jboss rule engine
   * source files ".rfm". These will be compiled into package files upon
   * component initialisation.
   * </p>
   *
   * @param r
   */
  public void addRuleFlow(String r) {
    if (r != null) {
      ruleFlows.add(r);
    }
    else {
      logR.debug("Ignoring null RuleFlow parameter");
    }
  }
}
