package ru.lanit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParseLineTest {
    CsvUpdateReader reader;

    @BeforeEach
    void init() {
        reader = new CsvUpdateReader("C:\\Users\\danilishe\\Documents\\csvUpdateWatcher\\src\\ru\\lanit\\example.csv");
    }
    @Test
    public void stringTest() {
        List<String> result = reader.parseLine("test1,tes sd es2,\"test3\",\"test,test4\"" );
        Assertions.assertEquals("test1", result.get(0));
        Assertions.assertEquals("tes sd es2", result.get(1));
        Assertions.assertEquals("test3", result.get(2));
        Assertions.assertEquals("test,test4", result.get(3));
    }
}
