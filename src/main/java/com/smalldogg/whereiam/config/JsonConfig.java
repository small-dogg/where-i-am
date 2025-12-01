package com.smalldogg.whereiam.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class JsonConfig {
    private static final List<DateTimeFormatter> DATETIME_FORMATTER_LIST = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    );



    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            DateTimeFormatter formatter = DATETIME_FORMATTER_LIST.get(0);
            String dateTimeString = localDateTime.format(formatter);
            jsonGenerator.writeString(dateTimeString);
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            String dateTimeString = jsonParser.getValueAsString();

            Optional<LocalDateTime> localDateTime = DATETIME_FORMATTER_LIST.stream()
                    .map(formatter -> tryParse(dateTimeString, formatter))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();

            return localDateTime.orElseThrow(() -> new BadRequestException("날짜 형식이 잘못됐습니다."));
        }

        private Optional<LocalDateTime> tryParse(String dateTimeString, DateTimeFormatter dateTimeFormatter) {
            try {
                return Optional.of(LocalDateTime.parse(dateTimeString, dateTimeFormatter));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }
}
