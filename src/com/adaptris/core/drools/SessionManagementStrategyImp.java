package com.adaptris.core.drools;

import com.adaptris.core.CoreException;
import com.adaptris.core.LicensedComponent;
import com.adaptris.util.license.License;

/**
 * Abstract base class for management of a Drools Session.
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public abstract class SessionManagementStrategyImp implements
    SessionManagementStrategy {
  /**
   * @see com.adaptris.core.AdaptrisComponent#close()
   */
  @Override
  public void close() {
  }

  /**
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  @Override
  public void init() throws CoreException {
  }

  /**
   * @see com.adaptris.core.AdaptrisComponent#start()
   */
  @Override
  public void start() throws CoreException {
  }

  /**
   * @see com.adaptris.core.AdaptrisComponent#stop()
   */
  @Override
  public void stop() {
  }

  /**
   * @see LicensedComponent#isEnabled(License)
   */
  @Override
  public boolean isEnabled(License license) throws CoreException {
    return license.isEnabled(License.ENTERPRISE);
  }
}
