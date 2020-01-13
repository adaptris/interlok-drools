package com.adaptris.core.drools;

import com.adaptris.core.ServiceCase;

public abstract class DroolsServiceExample extends ServiceCase {

  private static final String BASE_DIR_KEY = "DroolsServiceExamples.baseDir";

  public DroolsServiceExample() {
    super();
    if (PROPERTIES.getProperty(BASE_DIR_KEY) != null) {
      setBaseDir(PROPERTIES.getProperty(BASE_DIR_KEY));
    }
  }

  @Override
  protected boolean doStateTests() {
    return false;
  }
}
