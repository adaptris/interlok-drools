package com.adaptris.core.drools;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import com.adaptris.annotation.GenerateBeanInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Implementation of all the event listener interfaces that simply logs with the specified category using the slf4j interfaces.
 ** <p>
 * In the adapter configuration file this class is aliased as <b>slf4j-logging-event-listener</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @author $Author: lchan $
 */
@XStreamAlias("drools-slf4j-logging-event-listener")
@GenerateBeanInfo
public class Slf4jLoggingEventListener extends LoggingEventListenerImpl {
  private enum LogHandler {
    TRACE {
      @Override
      void log(Logger log, String s) {
        log.trace(s);
      }
    },
    DEBUG {
      @Override
      void log(Logger log, String s) {
        log.debug(s);
      }
    },
    INFO {
      @Override
      void log(Logger log, String s) {
        log.info(s);
      }
    },
    WARN {
      @Override
      void log(Logger log, String s) {
        log.warn(s);
      }
    },
    ERROR {
      @Override
      void log(Logger log, String s) {
        log.error(s);
      }
    },
    FATAL {
      @Override
      void log(Logger log, String s) {
        log.error(MarkerFactory.getMarker("FATAL"), s);
      }
    };
    abstract void log(Logger log, String s);
  }

  public Slf4jLoggingEventListener() {
    super();
  }

  public Slf4jLoggingEventListener(String category, LogLevel level) {
    super(category, level);
  }

  @Override
  protected void log(EventObject e) {
    LogHandler.valueOf(getLogLevel()).log(LoggerFactory.getLogger(getCategory()), e.toString());
  }

  @Override
  protected void log(String s) {
    LogHandler.valueOf(getLogLevel()).log(LoggerFactory.getLogger(getCategory()), s);
  }

}
