import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvUpdateReader {
    private static final char SEPARATOR = ',';
    private static final char QUOTE = '"';
    private long lastFileSize = 0L;
    private int lastCheckedRow = 0;
    private File file = null;
    private List<String> headers;
    private boolean isLogging = false;

    public CsvUpdateReader(String fileName) {
        file = new File(fileName);
        lastFileSize = file.length();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            headers = parseLine(reader.readLine());
            lastCheckedRow = 1;

            while (reader.readLine() != null) {
                lastCheckedRow++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isLogging) System.out.println("Файл загружен. Обнаружено " + headers.size() + " заголовков, считано строк: " + lastCheckedRow + " размер: " + file.length());
    }


    public List<HashMap<String, String>> read() {
        List<HashMap<String, String>> result = new ArrayList<>();
        if (file.length() != lastFileSize) {
            if (isLogging) System.out.println("Обнаружено изменение файла.");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                // сбрасываем счётчик линии, если файл стал меньше
                if (file.length() < lastFileSize) {
                    lastCheckedRow = 1;
                }

                skipReaded(reader);

                String line;
                while ((line = reader.readLine()) != null) {
                    List<String> valList = parseLine(line);
                    if (valList.size() == headers.size()) {
                        lastCheckedRow++;
                        HashMap<String, String> hashMapFromLine = new HashMap<>();
                        for (int i = 0; i < headers.size(); i++) {
                            hashMapFromLine.put(headers.get(i), valList.get(i));
                        }
                        result.add(hashMapFromLine);
                    } else {
                        if (isLogging) System.err.println("В строке [" + (lastCheckedRow + 1) + "] несовпадение количества аргументов. (" + valList.size() + " из " + headers.size() + ")");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            lastFileSize = file.length();
            if (isLogging) System.out.println("Считано " + result.size() + " строк.");
        }
        return result;
    }
    public void disableLog() {
        isLogging = false;
    }
    public void enableLog() {
        isLogging = true;
    }


    private void skipReaded(BufferedReader reader) throws IOException {
        for (int currentRow = 0; currentRow < lastCheckedRow; currentRow++) {
            // пропускаем считанное
            reader.readLine();
        }
    }

    public List<String> parseLine(String cvsLine) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;

        char[] chars = cvsLine.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (inQuotes) {
                if (chars[i] == QUOTE) {
                    inQuotes = false;
                } else {
                    curVal.append(chars[i]);
                }
            } else {
                if (chars[i] == SEPARATOR) {
                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                } else {
                    if (chars[i] == QUOTE) {
                        inQuotes = true; // входим в кавычки
                    } else {
                        curVal.append(chars[i]); // любой знак кноме сепаратора и кавычки
                    }
                }
            }
        }
        result.add(curVal.toString()); // добавляем последнее значение в строке
        return result;
    }
}