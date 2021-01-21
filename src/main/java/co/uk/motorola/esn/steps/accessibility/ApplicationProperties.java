package co.uk.motorola.esn.steps.accessibility;

import co.uk.motorola.esn.context.ScenarioContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

	Properties properties;

	public ApplicationProperties(String fileName, ScenarioContext scenarioContext) {
		this.properties = readFile(fileName, scenarioContext);
	}

	public Properties readFile(String fileName, ScenarioContext scenarioContext) {
		properties = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
			if (inputStream != null) {
				properties.load(inputStream);
				inputStream.close();
			}
		} catch (IOException e) {
			scenarioContext.getLOG().error(e.getMessage());
		}
		return properties;
	}

	public String getProperty(String property) {
		return this.properties.getProperty(property);
	}

}