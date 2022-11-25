package org.jberet.schedule._private;

import jakarta.ejb.ScheduleExpression;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class ScheduleExpressionAdapter extends XmlAdapter<JaxbScheduleExpression, ScheduleExpression> {
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSSX");

    @Override
    public ScheduleExpression unmarshal(JaxbScheduleExpression scheduleExpression) throws Exception {
        if (scheduleExpression == null) {
            return null;
        }
        ScheduleExpression expression = new ScheduleExpression();

        Optional.ofNullable(scheduleExpression.getDayOfMonth())
                .map(String::valueOf)
                .ifPresent(expression::dayOfMonth);

        Optional.ofNullable(scheduleExpression.getDayOfWeek())
                .map(String::valueOf)
                .ifPresent(expression::dayOfWeek);

        Object endValue = scheduleExpression.getEnd();
        if (endValue != null) {
            Date date = DATE_FORMAT.parse(endValue.toString());
            expression.end(date);
        }

        Optional.ofNullable(scheduleExpression.getHour())
                .map(String::valueOf)
                .ifPresent(expression::hour);

        Optional.ofNullable(scheduleExpression.getMinute())
                .map(String::valueOf)
                .ifPresent(expression::minute);

        Optional.ofNullable(scheduleExpression.getMonth())
                .map(String::valueOf)
                .ifPresent(expression::month);

        Optional.ofNullable(scheduleExpression.getSecond())
                .map(String::valueOf)
                .ifPresent(expression::second);

        Object startValue = scheduleExpression.getStart();
        if (startValue != null) {
            Date date = DATE_FORMAT.parse(startValue.toString());
            expression.start(date);
        }

        Optional.ofNullable(scheduleExpression.getTimezone())
                .map(String::valueOf)
                .ifPresent(expression::timezone);

        Optional.ofNullable(scheduleExpression.getYear())
                .map(String::valueOf)
                .ifPresent(expression::year);
        return expression;
    }


    @Override
    public JaxbScheduleExpression marshal(ScheduleExpression scheduleExpression) throws Exception {
        JaxbScheduleExpression jaxbScheduleExpression = new JaxbScheduleExpression();
        String dayOfMonth = scheduleExpression.getDayOfMonth();
        String dayOfWeek = scheduleExpression.getDayOfWeek();
        Date end = scheduleExpression.getEnd();
        String hour = scheduleExpression.getHour();
        String minute = scheduleExpression.getMinute();
        String month = scheduleExpression.getMonth();
        String second = scheduleExpression.getSecond();
        Date start = scheduleExpression.getStart();
        String timezone = scheduleExpression.getTimezone();
        String year = scheduleExpression.getYear();


        if (dayOfMonth != null) {
            jaxbScheduleExpression.setDayOfMonth(dayOfMonth);
        }
        if (dayOfWeek != null) {
            jaxbScheduleExpression.setDayOfWeek(dayOfWeek);
        }
        if (end != null) {
            String endString = DATE_FORMAT.format(end);
            jaxbScheduleExpression.setEnd(endString);
        }
        if (hour != null) {
            jaxbScheduleExpression.setHour(hour);
        }
        if (minute != null) {
            jaxbScheduleExpression.setMinute(minute);
        }
        if (month != null) {
            jaxbScheduleExpression.setMonth(month);
        }
        if (second != null) {
            jaxbScheduleExpression.setSecond(second);
        }
        if (start != null) {
            String startString = DATE_FORMAT.format(start);
            jaxbScheduleExpression.setStart(startString);
        }
        if (timezone != null) {
            jaxbScheduleExpression.setTimezone(timezone);
        }
        if (year != null) {
            jaxbScheduleExpression.setYear(year);
        }
        return jaxbScheduleExpression;
    }
}
