package org.jberet.schedule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import jakarta.ejb.ScheduleExpression;
import jakarta.xml.bind.JAXB;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
public class JobScheduleConfigSerializationTest {

    @Test
    public void testJacksonJaxbSerialization() throws IOException {
        JobScheduleConfig jobScheduleConfig = createScheduleConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JakartaXmlBindAnnotationModule());

        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, jobScheduleConfig);

        String jsonString = stringWriter.toString();
        verifyJsonString(jobScheduleConfig, jsonString);


        StringReader stringReader = new StringReader(jsonString);
        JobScheduleConfig unmarshalledConfig = objectMapper.readValue(stringReader, JobScheduleConfig.class);

        verifyConfig(jobScheduleConfig, unmarshalledConfig);
    }

    @Test
    public void testJaxbSerialization() {
        JobScheduleConfig jobScheduleConfig = createScheduleConfig();

        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(jobScheduleConfig, stringWriter);

        String xmlString = stringWriter.toString();
        StringReader stringReader = new StringReader(xmlString);

        JobScheduleConfig unmarshalledConfig = JAXB.unmarshal(stringReader, JobScheduleConfig.class);
        verifyConfig(jobScheduleConfig, unmarshalledConfig);
    }

    private void verifyJsonString(JobScheduleConfig sourceConfig, String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode configJson = objectMapper.reader()
                .readTree(jsonString);

        assertEquals(sourceConfig.getJobName(), configJson.get("jobName").asText());

        ScheduleExpression sourceScheduleExpression = sourceConfig.getScheduleExpression();
        JsonNode scheduleExpressionJson = configJson.get("scheduleExpression");
        assertNotNull(sourceScheduleExpression);
        assertNotNull(scheduleExpressionJson);

        assertEquals(sourceScheduleExpression.getDayOfMonth(), scheduleExpressionJson.get("dayOfMonth").asText());
        assertEquals(sourceScheduleExpression.getDayOfWeek(), scheduleExpressionJson.get("dayOfWeek").asText());
    }

    private void verifyConfig(JobScheduleConfig sourceConfig, JobScheduleConfig targetConfig) {
        assertEquals(sourceConfig.getJobName(), targetConfig.getJobName());

        Properties sourceParameters = sourceConfig.getJobParameters();
        Properties targetParameters = targetConfig.getJobParameters();
        assertNotNull(sourceParameters);
        assertNotNull(targetParameters);
        Set<String> sourceNames = sourceParameters.stringPropertyNames();
        for (String sourceName : sourceNames) {
            String sourcePropertyValue = sourceParameters.getProperty(sourceName);
            String targetPropertyValue = targetParameters.getProperty(sourceName);
            assertEquals(sourcePropertyValue, targetPropertyValue);
        }

        ScheduleExpression sourceScheduleExpression = sourceConfig.getScheduleExpression();
        ScheduleExpression targetScheduleExpression = targetConfig.getScheduleExpression();
        assertNotNull(sourceScheduleExpression);
        assertNotNull(targetScheduleExpression);

        assertEquals(sourceScheduleExpression.getDayOfMonth(), targetScheduleExpression.getDayOfMonth());
        assertEquals(sourceScheduleExpression.getDayOfWeek(), targetScheduleExpression.getDayOfWeek());
        assertEquals(sourceScheduleExpression.getEnd(), targetScheduleExpression.getEnd());
        assertEquals(sourceScheduleExpression.getHour(), targetScheduleExpression.getHour());
        assertEquals(sourceScheduleExpression.getMinute(), targetScheduleExpression.getMinute());
        assertEquals(sourceScheduleExpression.getMonth(), targetScheduleExpression.getMonth());
        assertEquals(sourceScheduleExpression.getSecond(), targetScheduleExpression.getSecond());
        assertEquals(sourceScheduleExpression.getStart(), targetScheduleExpression.getStart());
        assertEquals(sourceScheduleExpression.getTimezone(), targetScheduleExpression.getTimezone());
        assertEquals(sourceScheduleExpression.getYear(), targetScheduleExpression.getYear());
    }

    private JobScheduleConfig createScheduleConfig() {
        Date startDate = new Date();
        Properties properties = new Properties();
        properties.setProperty("prop1", "a");
        properties.setProperty("prop2", "12");
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.dayOfMonth(2);
        scheduleExpression.dayOfWeek("3");
        scheduleExpression.start(startDate);
        scheduleExpression.hour("*/5");
        scheduleExpression.timezone("");
        return JobScheduleConfigBuilder.newInstance()
                .jobName("jobname")
                .jobParameters(properties)
                .scheduleExpression(scheduleExpression)
                .build();
    }
}
