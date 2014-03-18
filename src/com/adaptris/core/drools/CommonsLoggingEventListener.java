package com.adaptris.core.drools;

import java.util.EventObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Implementation of all the event listener interfaces that simply logs with the specified category using the commons-logging
 * interfaces.
 ** <p>
 * In the adapter configuration file this class is aliased as <b>drools-commons-logging-event-listener</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 
 * @author lchan
 * @author $Author: lchan $
 * @deprecated commons-logging has been deprecated in favour of slf4j since 2.8.0.
 */
@Deprecated
@XStreamAlias("drools-commons-logging-event-listener")
public class CommonsLoggingEventListener extends LoggingEventListenerImpl {
  private enum LogHandler {
    TRACE {
      @Override
      void log(Log log, String s) {
        log.trace(s);
      }
    },
    DEBUG {
      @Override
      void log(Log log, String s) {
        log.debug(s);
      }
    },
    INFO {
      @Override
      void log(Log log, String s) {
        log.info(s);
      }
    },
    WARN {
      @Override
      void log(Log log, String s) {
        log.warn(s);
      }
    },
    ERROR {
      @Override
      void log(Log log, String s) {
        log.error(s);
      }
    },
    FATAL {
      @Override
      void log(Log log, String s) {
        log.fatal(s);
      }
    };
    abstract void log(Log log, String s);
  }

  public CommonsLoggingEventListener() {
    super();
  }

  @Override
  protected void log(EventObject e) {
    LogHandler.valueOf(getLogLevel()).log(LogFactory.getLog(getCategory()), e.toString());
  }


  @Override
  protected void log(String s) {
    LogHandler.valueOf(getLogLevel()).log(LogFactory.getLog(getCategory()), s);
  }

}
