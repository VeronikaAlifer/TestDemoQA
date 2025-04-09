package utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import testdata.Person;

import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

public class CSVReader {

    public static List<Person> readPeopleFromCsv(String path) {
        return readFromSCV(path, Person.class);
    }

    private static <T> List<T> readFromSCV(String path, Class<T> type) {
        try (Reader reader = new FileReader(path)) {
            // Чтение CSV файла и маппинг данных в объекты Person
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true) // Игнорирование пробелов в начале данных
                    .build();

            return csvToBean.parse();  // Получаем список объектов Person

        } catch (Exception e) {
            System.err.println("⚠ Ошибка при чтении CSV: " + path + ", тип: " + type.getSimpleName());
        }
        return Collections.emptyList();
    }
}

