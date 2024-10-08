package eu.domibus.connector.test.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.Test;

class MemoryAppenderTest {
    private static final Logger LOGGER = LogManager.getLogger(MemoryAppender.class);
    private static final Marker BUSINESS = MarkerManager.getMarker("BUSINESS");

    @Test
    void testLogging() {
        LOGGER.info("Hello World!");
        LOGGER.info(BUSINESS, "Hello Business World!");
        LOGGER.info("Test");

        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender())
                            .containsLogLine("Hello World!");

        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender()).filterOnMarker("BUSINESS")
                            .hasLogLines(1);
    }
}
