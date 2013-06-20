package com.adaptris.core.drools;

import java.util.EventObject;

import org.drools.WorkingMemory;
import org.drools.event.ActivationCancelledEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.AfterActivationFiredEvent;
import org.drools.event.AfterFunctionRemovedEvent;
import org.drools.event.AfterPackageAddedEvent;
import org.drools.event.AfterPackageRemovedEvent;
import org.drools.event.AfterRuleAddedEvent;
import org.drools.event.AfterRuleBaseLockedEvent;
import org.drools.event.AfterRuleBaseUnlockedEvent;
import org.drools.event.AfterRuleRemovedEvent;
import org.drools.event.AgendaEventListener;
import org.drools.event.AgendaGroupPoppedEvent;
import org.drools.event.AgendaGroupPushedEvent;
import org.drools.event.BeforeActivationFiredEvent;
import org.drools.event.BeforeFunctionRemovedEvent;
import org.drools.event.BeforePackageAddedEvent;
import org.drools.event.BeforePackageRemovedEvent;
import org.drools.event.BeforeRuleAddedEvent;
import org.drools.event.BeforeRuleBaseLockedEvent;
import org.drools.event.BeforeRuleBaseUnlockedEvent;
import org.drools.event.BeforeRuleRemovedEvent;
import org.drools.event.ObjectInsertedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.ObjectUpdatedEvent;
import org.drools.event.RuleBaseEventListener;
import org.drools.event.RuleFlowCompletedEvent;
import org.drools.event.RuleFlowEventListener;
import org.drools.event.RuleFlowGroupActivatedEvent;
import org.drools.event.RuleFlowGroupDeactivatedEvent;
import org.drools.event.RuleFlowStartedEvent;
import org.drools.event.WorkingMemoryEventListener;

/**
 * Abstract Implementation of all the event listener interfaces that simply logs to the configured logging system with the specified
 * category.
 *
 * @author lchan
 * @author $Author: lchan $
 */
public abstract class LoggingEventListenerImpl implements RuleBaseEventListener, RuleFlowEventListener, WorkingMemoryEventListener,
    AgendaEventListener {
  private enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL;
  }

  private String logLevel;
  private String category;

  public LoggingEventListenerImpl() {
    setCategory(this.getClass().getCanonicalName());
    setLogLevel(LogLevel.TRACE.name());
  }

  /**
   * Set the logging category for the underlying logger.
   *
   * @param s
   */
  public void setCategory(String s) {
    if (s != null && !"".equals(s)) {
      category = s;
    }
  }

  /**
   * Get the category of the underlying logger.
   *
   * @return the category
   */
  public String getCategory() {
    return category;
  }

  /**
   * Get the log level at which the underlying logger logs.
   *
   * @return the log level
   */
  public String getLogLevel() {
    return logLevel;
  }

  /**
   * Set the log level at which the underlying logger logs.
   *
   * @param s the log level, one of "TRACE", "DEBUG", "INFO" "WARN", "ERROR", "FATAL". The default is "TRACE"
   */
  public void setLogLevel(String s) {
    try {
      logLevel = LogLevel.valueOf(s.toUpperCase()).name();
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(s + " not supported as a LogLevel");
    }
  }

  protected abstract void log(EventObject e);

  /**
   *
   * @see RuleBaseEventListener#afterFunctionRemoved(AfterFunctionRemovedEvent)
   */
  @Override
  public void afterFunctionRemoved(AfterFunctionRemovedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#afterPackageAdded(AfterPackageAddedEvent)
   */
  @Override
  public void afterPackageAdded(AfterPackageAddedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#afterPackageRemoved(AfterPackageRemovedEvent)
   */
  @Override
  public void afterPackageRemoved(AfterPackageRemovedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#afterRuleAdded(AfterRuleAddedEvent)
   */
  @Override
  public void afterRuleAdded(AfterRuleAddedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#afterRuleBaseLocked(AfterRuleBaseLockedEvent)
   */
  @Override
  public void afterRuleBaseLocked(AfterRuleBaseLockedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#afterRuleBaseUnlocked(AfterRuleBaseUnlockedEvent)
   */
  @Override
  public void afterRuleBaseUnlocked(AfterRuleBaseUnlockedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#afterRuleRemoved(AfterRuleRemovedEvent)
   */
  @Override
  public void afterRuleRemoved(AfterRuleRemovedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#beforeFunctionRemoved(BeforeFunctionRemovedEvent)
   */
  @Override
  public void beforeFunctionRemoved(BeforeFunctionRemovedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#beforePackageAdded(BeforePackageAddedEvent)
   */
  @Override
  public void beforePackageAdded(BeforePackageAddedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#beforePackageRemoved(BeforePackageRemovedEvent)
   */
  @Override
  public void beforePackageRemoved(BeforePackageRemovedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#beforeRuleAdded(BeforeRuleAddedEvent)
   */
  @Override
  public void beforeRuleAdded(BeforeRuleAddedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#beforeRuleBaseLocked(BeforeRuleBaseLockedEvent)
   */
  @Override
  public void beforeRuleBaseLocked(BeforeRuleBaseLockedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#beforeRuleBaseUnlocked(BeforeRuleBaseUnlockedEvent)
   */
  @Override
  public void beforeRuleBaseUnlocked(BeforeRuleBaseUnlockedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleBaseEventListener#beforeRuleRemoved(BeforeRuleRemovedEvent)
   */
  @Override
  public void beforeRuleRemoved(BeforeRuleRemovedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see RuleFlowEventListener#ruleFlowCompleted(RuleFlowCompletedEvent, WorkingMemory)
   */
  @Override
  public void ruleFlowCompleted(RuleFlowCompletedEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see RuleFlowEventListener#ruleFlowGroupActivated(RuleFlowGroupActivatedEvent, WorkingMemory)
   */
  @Override
  public void ruleFlowGroupActivated(RuleFlowGroupActivatedEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see RuleFlowEventListener#ruleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent, WorkingMemory)
   */
  @Override
  public void ruleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see RuleFlowEventListener#ruleFlowStarted(RuleFlowStartedEvent, WorkingMemory)
   */
  @Override
  public void ruleFlowStarted(RuleFlowStartedEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see WorkingMemoryEventListener#objectInserted(ObjectInsertedEvent)
   */
  @Override
  public void objectInserted(ObjectInsertedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see WorkingMemoryEventListener#objectRetracted(ObjectRetractedEvent)
   */
  @Override
  public void objectRetracted(ObjectRetractedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see WorkingMemoryEventListener#objectUpdated(ObjectUpdatedEvent)
   */
  @Override
  public void objectUpdated(ObjectUpdatedEvent arg0) {
    log(arg0);
  }

  /**
   *
   * @see AgendaEventListener#activationCancelled(ActivationCancelledEvent, WorkingMemory)
   */
  @Override
  public void activationCancelled(ActivationCancelledEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see AgendaEventListener#activationCreated(ActivationCreatedEvent, WorkingMemory)
   */
  @Override
  public void activationCreated(ActivationCreatedEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see AgendaEventListener#afterActivationFired(AfterActivationFiredEvent, WorkingMemory)
   */
  @Override
  public void afterActivationFired(AfterActivationFiredEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see AgendaEventListener#agendaGroupPopped(AgendaGroupPoppedEvent, WorkingMemory)
   */
  @Override
  public void agendaGroupPopped(AgendaGroupPoppedEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see AgendaEventListener#agendaGroupPushed(AgendaGroupPushedEvent,WorkingMemory)
   */
  @Override
  public void agendaGroupPushed(AgendaGroupPushedEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

  /**
   *
   * @see AgendaEventListener#beforeActivationFired(BeforeActivationFiredEvent, WorkingMemory)
   */
  @Override
  public void beforeActivationFired(BeforeActivationFiredEvent arg0, WorkingMemory arg1) {
    log(arg0);
  }

}
