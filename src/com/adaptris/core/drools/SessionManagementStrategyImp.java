package com.adaptris.core.drools;

import com.adaptris.core.CoreException;

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

  @Override
  public void prepare() throws CoreException {}

}
