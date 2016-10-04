import org.junit.Test;
import play.twirl.api.Content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApplicationTest {

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render();
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("a"));
    }

    @Test
    public void failThis() {
        assertTrue(false);
    }

}
