package org.jberet.schedule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;
import org.junit.Assert;
import org.junit.Test;

import jakarta.ejb.ScheduleExpression;
import jakarta.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

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

        Assert.assertEquals(sourceConfig.getJobName(), configJson.get("jobName").asText());

        ScheduleExpression sourceScheduleExpression = sourceConfig.getScheduleExpression();
        JsonNode scheduleExpressionJson = configJson.get("scheduleExpression");
        Assert.assertNotNull(sourceScheduleExpression);
        Assert.assertNotNull(scheduleExpressionJson);

        Assert.assertEquals(sourceScheduleExpression.getDayOfMonth(), scheduleExpressionJson.get("dayOfMonth").asText());
        Assert.assertEquals(sourceScheduleExpression.getDayOfWeek(), scheduleExpressionJson.get("dayOfWeek").asText());
    }

    private void verifyConfig(JobScheduleConfig sourceConfig, JobScheduleConfig targetConfig) {
        Assert.assertEquals(sourceConfig.getJobName(), targetConfig.getJobName());

        Properties sourceParameters = sourceConfig.getJobParameters();
        Properties targetParameters = targetConfig.getJobParameters();
        Assert.assertNotNull(sourceParameters);
        Assert.assertNotNull(targetParameters);
        Set<String> sourceNames = sourceParameters.stringPropertyNames();
        for (String sourceName : sourceNames) {
            String sourcePropertyValue = sourceParameters.getProperty(sourceName);
            String targetPropertyValue = targetParameters.getProperty(sourceName);
            Assert.assertEquals(sourcePropertyValue, targetPropertyValue);
        }

        ScheduleExpression sourceScheduleExpression = sourceConfig.getScheduleExpression();
        ScheduleExpression targetScheduleExpression = targetConfig.getScheduleExpression();
        Assert.assertNotNull(sourceScheduleExpression);
        Assert.assertNotNull(targetScheduleExpression);

        Assert.assertEquals(sourceScheduleExpression.getDayOfMonth(), targetScheduleExpression.getDayOfMonth());
        Assert.assertEquals(sourceScheduleExpression.getDayOfWeek(), targetScheduleExpression.getDayOfWeek());
        Assert.assertEquals(sourceScheduleExpression.getEnd(), targetScheduleExpression.getEnd());
        Assert.assertEquals(sourceScheduleExpression.getHour(), targetScheduleExpression.getHour());
        Assert.assertEquals(sourceScheduleExpression.getMinute(), targetScheduleExpression.getMinute());
        Assert.assertEquals(sourceScheduleExpression.getMonth(), targetScheduleExpression.getMonth());
        Assert.assertEquals(sourceScheduleExpression.getSecond(), targetScheduleExpression.getSecond());
        Assert.assertEquals(sourceScheduleExpression.getStart(), targetScheduleExpression.getStart());
        Assert.assertEquals(sourceScheduleExpression.getTimezone(), targetScheduleExpression.getTimezone());
        Assert.assertEquals(sourceScheduleExpression.getYear(), targetScheduleExpression.getYear());
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
