package net.rakugakibox.spring.boot.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.spi.FilterReply;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueueAppender;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueueAppenderRule;
import net.rakugakibox.spring.boot.logback.access.test.TestControllerConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The base class for testing to filter Logback-access events.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        value = "logback.access.config=classpath:logback-access.filtered.queue.xml",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractLogbackAccessFilteringTest {

    /**
     * The REST template.
     */
    @Autowired
    protected TestRestTemplate rest;

    /**
     * Creates a test rule.
     *
     * @return a test rule.
     */
    @Rule
    public TestRule rule() {
        return new LogbackAccessEventQueueAppenderRule();
    }

    /**
     * Tests an accepted Logback-access event.
     */
    @Test
    public void acceptedLogbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Filter-Reply", FilterReply.ACCEPT.name())
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = LogbackAccessEventQueueAppender.appendedEventQueue.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).isNotNull();

    }

    /**
     * Tests an neutral Logback-access event.
     */
    @Test
    public void neutralLogbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Filter-Reply", FilterReply.NEUTRAL.name())
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = LogbackAccessEventQueueAppender.appendedEventQueue.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).isNotNull();

    }

    /**
     * Tests a denied Logback-access event.
     */
    @Test
    public void deniedLogbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Filter-Reply", FilterReply.DENY.name())
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);

        assertThat(response).hasStatusCode(HttpStatus.OK);

    }

    /**
     * The base class of context configuration.
     */
    @EnableAutoConfiguration
    @Import(TestControllerConfiguration.class)
    public static abstract class AbstractContextConfiguration {
    }

}