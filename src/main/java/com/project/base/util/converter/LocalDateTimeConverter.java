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

import static com.project.base.util.Constants.TIMEZONE_HEADER;

@Component
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Autowired
    private HttpServletRequest request;

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime zonedDateTime) {
        return zonedDateTime == null ? null : Timestamp.valueOf(zonedDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        String timezone = request.getHeader(TIMEZONE_HEADER);
        return timestamp == null ? null : timestamp.toInstant().atZone(ZoneId.of(timezone)).toLocalDateTime();
    }
}
