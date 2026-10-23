package controle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/** Converts dates entered in the forms into the database date format. */
final class DataInput {
    private static final List<DateTimeFormatter> FORMATS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("dd-MM-uuuu"),
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
    );

    private DataInput() {}

    static LocalDate parse(String text) {
        String value = text == null ? "" : text.trim();
        for (DateTimeFormatter format : FORMATS) {
            try {
                return LocalDate.parse(value, format);
            } catch (DateTimeParseException ignored) {
                // Try the next format accepted by the interface.
            }
        }
        throw new IllegalArgumentException(
                "Data inválida. Use DD-MM-AAAA, DD/MM/AAAA ou AAAA-MM-DD (ex.: 19-08-2009).");
    }
}
