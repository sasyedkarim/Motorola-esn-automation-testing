package co.uk.motorola.esn.steps.accessibility;

import co.uk.motorola.esn.context.ScenarioContext;
import org.jsoup.Jsoup;
import java.io.IOException;

class JsFactory {

    ApplicationProperties applicationProperties;

    private static JsFactory instance = null;
    private String accessibilityContent = null;

    private String jqueryContent = null;

    private JsFactory(ScenarioContext scenarioContext) {
        this.applicationProperties = new ApplicationProperties("application.properties", scenarioContext);
    }

    static synchronized JsFactory getInstance(ScenarioContext scenarioContext) throws IOException {
        if (instance == null) {
            instance = new JsFactory(scenarioContext);
            instance.load();
        }
        return instance;
    }

    private void load() throws IOException {
        jqueryContent = Jsoup.connect(applicationProperties.getProperty("jquerycdnurl")).ignoreContentType(true).execute().body();
        accessibilityContent = Jsoup.connect(applicationProperties.getProperty("applicationcdnurl")).ignoreContentType(true).execute().body();
    }

    String getAccessibilityContent() {
        return accessibilityContent;
    }

    String getJqueryContent() {
        return jqueryContent;
    }

}
