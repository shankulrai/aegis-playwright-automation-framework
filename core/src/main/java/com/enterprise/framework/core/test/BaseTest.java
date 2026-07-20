package com.enterprise.framework.core.test;

import com.enterprise.framework.core.driver.DriverManager;
import com.enterprise.framework.core.logging.LoggerManager;
import org.apache.logging.log4j.Logger;

/**
 * Base browser test support.
 */
public abstract class BaseTest {
    protected final Logger logger = LoggerManager.getLogger(getClass());

    protected void setUp() {
        DriverManager.initialize();
    }

    protected void tearDown() {
        DriverManager.quit();
    }
}
