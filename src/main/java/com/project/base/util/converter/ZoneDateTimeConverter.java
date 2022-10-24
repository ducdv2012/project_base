package com.project.base.util.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@Converter(autoApply = true)
public class ZoneDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {
    @Autowired
    private HttpServletRequest request;

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : Timestamp.valueOf(toUtcZoneId(zonedDateTime).toLocalDateTime());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        String timezone = request.getHeader("timezone");
        return timestamp == null ? null : toDefaultZoneId(timestamp.toLocalDateTime().atZone(ZoneId.of(timezone)));
    }

    private ZonedDateTime toUtcZoneId(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }

    private ZonedDateTime toDefaultZoneId(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
    }
}
