package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;

public class InstantAdapter extends TypeAdapter<Instant> {
    @Override
    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
        if (instant == null)
            jsonWriter.nullValue(); // Обработка null
        else
            jsonWriter.value(instant.toEpochMilli());
    }

    @Override
    public Instant read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == com.google.gson.stream.JsonToken.NULL) {
            jsonReader.nextNull(); // Обработка null
            return null;
        }
        return Instant.ofEpochMilli(jsonReader.nextLong()); // Используйте nextLong()
    }
}
