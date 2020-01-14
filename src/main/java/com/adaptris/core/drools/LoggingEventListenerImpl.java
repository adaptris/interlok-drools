package com.adaptris.core.drools;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.EventObject;
import org.apache.commons.lang3.ObjectUtils;
import org.drools.WorkingMemory;
import org.drools.common.RuleFlowGroupListener;
import org.drools.event.ActivationCancelledEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.AfterActivationFiredEvent;
import org.drools.event.AfterFunctionRemovedEvent;
import org.drools.event.AfterPackageAddedEvent;
import org.drools.event.AfterPackageRemovedEvent;
import org.drools.event.AfterProcessAddedEvent;
import org.drools.event.AfterProcessRemovedEvent;
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
import org.drools.event.BeforeProcessAddedEvent;
import org.drools.event.BeforeProcessRemovedEvent;
import org.drools.event.BeforeRuleAddedEvent;
import org.drools.event.BeforeRuleBaseLockedEvent;
import org.drools.event.BeforeRuleBaseUnlockedEvent;
import org.drools.event.BeforeRuleRemovedEvent;
import org.drools.event.ObjectInsertedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.ObjectUpdatedEvent;
import org.drools.event.RuleBaseEventListener;
import org.drools.event.RuleFlowGroupActivatedEvent;
import org.drools.event.RuleFlowGroupDeactivatedEvent;
import org.drools.event.WorkingMemoryEventListener;

/**
 * Abstract Implementation of all the event listener interfaces that simply logs to the configured logging system with the specified
 * category.
 * 
 * @author lchan
 * @author $Author: lchan $
 */
public abstract class LoggingEventListenerImpl implements RuleBaseEventListener, RuleFlowGroupListener, WorkingMemoryEventListener,
    AgendaEventListener {
  public enum LoggingLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL;
  }

  private LoggingLevel logLevel;
  private String category;

  public LoggingEventListenerImpl() {
  }


  /**
   * Set the logging category for the underlying logger.
   * 
   * @param s
   */
  public void setCategory(String s) {
    category = s;
  }

  /**
   * Get the category of the underlying logger.
   * 
   * @return the category
   */
  public String getCategory() {
    return category;
  }

  protected String category() {
    return ObjectUtils.defaultIfNull(getCategory(), this.getClass().getCanonicalName());
  }

  /**
   * Get the log level at which the underlying logger logs.
   * 
   * @return the log level
   */
  public LoggingLevel getLogLevel() {
    return logLevel;
  }

  /**
   * Set the log level at which the underlying logger logs.
   * 
   * @param s the log level, one of "TRACE", "DEBUG", "INFO" "WARN", "ERROR", "FATAL". The default is "TRACE"
   */
  public void setLogLevel(LoggingLevel s) {
    logLevel = s;
  }

  protected LoggingLevel logLevel() {
    return ObjectUtils.defaultIfNull(getLogLevel(), LoggingLevel.TRACE);
  }

  protected abstract void log(EventObject e);

  protected abstract void log(String s);

  @Override
  public void beforePackageAdded(BeforePackageAddedEvent event) {
    log(event);
  }

  @Override
  public void afterPackageAdded(AfterPackageAddedEvent event) {
    log(event);
  }

  @Override
  public void beforePackageRemoved(BeforePackageRemovedEvent event) {
    log(event);
  }

  @Override
  public void afterPackageRemoved(AfterPackageRemovedEvent event) {
    log(event);
  }

  @Override
  public void beforeRuleBaseLocked(BeforeRuleBaseLockedEvent event) {
    log(event);
  }

  @Override
  public void afterRuleBaseLocked(AfterRuleBaseLockedEvent event) {
    log(event);
  }

  @Override
  public void beforeRuleBaseUnlocked(BeforeRuleBaseUnlockedEvent event) {
    log(event);
  }

  @Override
  public void afterRuleBaseUnlocked(AfterRuleBaseUnlockedEvent event) {
    log(event);
  }

  @Override
  public void beforeRuleAdded(BeforeRuleAddedEvent event) {
    log(event);
  }

  @Override
  public void afterRuleAdded(AfterRuleAddedEvent event) {
    log(event);
  }

  @Override
  public void beforeRuleRemoved(BeforeRuleRemovedEvent event) {
    log(event);
  }

  @Override
  public void afterRuleRemoved(AfterRuleRemovedEvent event) {
    log(event);
  }

  @Override
  public void beforeProcessAdded(BeforeProcessAddedEvent event) {
    log(event);
  }

  @Override
  public void afterProcessAdded(AfterProcessAddedEvent event) {
    log(event);
  }

  @Override
  public void beforeProcessRemoved(BeforeProcessRemovedEvent event) {
    log(event);
  }

  @Override
  public void afterProcessRemoved(AfterProcessRemovedEvent event) {
    log(event);
  }

  @Override
  public void beforeFunctionRemoved(BeforeFunctionRemovedEvent event) {
    log(event);
  }

  @Override
  public void afterFunctionRemoved(AfterFunctionRemovedEvent event) {
    log(event);
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    log("WriteExternal to : " + out);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    log("readExternal from : " + in);
  }

  @Override
  public void objectInserted(ObjectInsertedEvent event) {
    log(event);
  }

  @Override
  public void objectUpdated(ObjectUpdatedEvent event) {
    log(event);
  }

  @Override
  public void objectRetracted(ObjectRetractedEvent event) {
    log(event);
  }

  @Override
  public void activationCreated(ActivationCreatedEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void activationCancelled(ActivationCancelledEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void beforeActivationFired(BeforeActivationFiredEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void afterActivationFired(AfterActivationFiredEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void agendaGroupPopped(AgendaGroupPoppedEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void agendaGroupPushed(AgendaGroupPushedEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event, WorkingMemory workingMemory) {
    log(event);
  }

  @Override
  public void ruleFlowGroupDeactivated() {
    log("ruleFlowGroupDeactivated");
  }

}
