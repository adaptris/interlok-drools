package com.adaptris.core.drools;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.drools.RuleBase;
import org.drools.WorkingMemoryEventManager;
import org.drools.event.AgendaEventListener;
import org.drools.event.RuleBaseEventListener;
import org.drools.event.WorkingMemoryEventListener;

import com.adaptris.core.CoreException;
import com.adaptris.core.licensing.License;
import com.adaptris.core.licensing.License.LicenseType;
import com.adaptris.core.licensing.LicensedService;
import com.adaptris.core.util.LifecycleHelper;

/**
 * Abstract Entry point into the JBoss Rules Engine.
 * 
 * <p>
 * Requires a Standard license.
 * </p>
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public abstract class RuleServiceImpl extends LicensedService {

  @NotNull
  @Valid
  private Resolver resolver;
  protected transient RuleBase ruleBase;
  @NotNull
  @Valid
  private RuleBaseProxy runtimeRuleBase;
  @Valid
  private RuleBaseEventListener ruleBaseEventListener = null;
  @Valid
  private WorkingMemoryEventListener workingMemoryEventListener = null;
  @Valid
  private AgendaEventListener agendaEventListener = null;

  public RuleServiceImpl() {
  }

  /**
   * Add any configured listeners to the session.
   *
   * @param m the session.
   * @see #setAgendaEventListener(AgendaEventListener)
   * @see #setRuleBaseEventListener(RuleBaseEventListener)
   * @see #setWorkingMemoryEventListener(WorkingMemoryEventListener)
   */
  protected void addListeners(WorkingMemoryEventManager m) {

    if (ruleBaseEventListener != null) {
      if (!m.getRuleBaseEventListeners().contains(ruleBaseEventListener)) {
        m.addEventListener(ruleBaseEventListener);
      }
    }
    if (agendaEventListener != null) {
      if (!m.getAgendaEventListeners().contains(agendaEventListener)) {
        m.addEventListener(agendaEventListener);
      }
    }
    if (workingMemoryEventListener != null) {
      if (!m.getWorkingMemoryEventListeners().contains(workingMemoryEventListener)) {
        m.addEventListener(workingMemoryEventListener);
      }
    }
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#init()
   */
  @Override
  protected final void initService() throws CoreException {
    if (resolver == null) {
      throw new CoreException("Fact Resolver is null");
    }
    if (runtimeRuleBase == null) {
      throw new CoreException("Rulebase is null");
    }
    LifecycleHelper.init(runtimeRuleBase);
    LifecycleHelper.init(resolver);
    initDroolsService();
  }

  /**
   *
   * @see com.adaptris.core.ServiceImp#start()
   */
  @Override
  public final void start() throws CoreException {
    LifecycleHelper.start(runtimeRuleBase);
    LifecycleHelper.start(resolver);
    ruleBase = runtimeRuleBase.createRuleBase();
    startService();
  }

  /**
   *
   * @see com.adaptris.core.ServiceImp#stop()
   */
  @Override
  public final void stop() {
    stopService();
    LifecycleHelper.stop(runtimeRuleBase);
    LifecycleHelper.stop(resolver);
  }

  /**
   *
   * @see com.adaptris.core.AdaptrisComponent#close()
   */
  @Override
  protected final void closeService() {
    closeDroolsService();
    LifecycleHelper.close(runtimeRuleBase);
    LifecycleHelper.close(resolver);
  }

  protected abstract void initDroolsService() throws CoreException;

  protected abstract void startService() throws CoreException;

  protected abstract void stopService();

  protected abstract void closeDroolsService();

  @Override
  protected void prepareService() throws CoreException {
    if (getResolver() != null) {
      getResolver().prepare();
    }
    if (getRuntimeRuleBase() != null) {
      getRuntimeRuleBase().prepare();
    }
  }


  @Override
  public final boolean isEnabled(License l) {
    boolean resolverEnabled = true, ruleBaseEnabled = true;
    if (getResolver() != null) {
      resolverEnabled = getResolver().isEnabled(l);
    }
    if (getRuntimeRuleBase() != null) {
      ruleBaseEnabled = getRuntimeRuleBase().isEnabled(l);
    }
    return l.isEnabled(LicenseType.Standard) && resolverEnabled
        && ruleBaseEnabled && serviceIsEnabled(l);
  }

  protected abstract boolean serviceIsEnabled(License l);

  /**
   * @return the FactResolver
   */
  public Resolver getResolver() {
    return resolver;
  }

  /**
   * Set the resolver responsible for mediating between Facts and
   * AdaptrisMessage
   *
   * @param f the FactResolver to set
   */
  public void setResolver(Resolver f) {
    resolver = f;
  }

  /**
   * @return the ruleBaseFactory
   */
  public RuleBaseProxy getRuntimeRuleBase() {
    return runtimeRuleBase;
  }

  /**
   * Set the factory for creating Rulebases.
   *
   * @param s the ruleBaseFactory to set
   */
  public void setRuntimeRuleBase(RuleBaseProxy s) {
    runtimeRuleBase = s;
  }

  /**
   * @return the ruleBaseEventListener
   */
  public RuleBaseEventListener getRuleBaseEventListener() {
    return ruleBaseEventListener;
  }

  /**
   * @param l the ruleBaseEventListener to set
   */
  public void setRuleBaseEventListener(RuleBaseEventListener l) {
    ruleBaseEventListener = l;
  }

  /**
   * @return the workingMemoryEventListener
   */
  public WorkingMemoryEventListener getWorkingMemoryEventListener() {
    return workingMemoryEventListener;
  }

  /**
   * @param l the workingMemoryEventListener to set
   */
  public void setWorkingMemoryEventListener(WorkingMemoryEventListener l) {
    workingMemoryEventListener = l;
  }

  /**
   * @return the agendaEventListener
   */
  public AgendaEventListener getAgendaEventListener() {
    return agendaEventListener;
  }

  /**
   * @param l the agendaEventListener to set
   */
  public void setAgendaEventListener(AgendaEventListener l) {
    agendaEventListener = l;
  }

}
