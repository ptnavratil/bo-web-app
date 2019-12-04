package cz.upce.webapp.utils;

import static org.junit.Assert.*;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import org.junit.Test;

import cz.upce.webapp.utils.xlsprocessors.CountrySheetProcessor;
import cz.upce.webapp.utils.xlsprocessors.ISheetProcessor;

/**
 * @author Tomas Kodym
 */
public class ISheetProcessorTest {
    private ISheetProcessor countrySheetProcessor = new CountrySheetProcessor();

    @Test
    public void itemIsNotValidatedTest() {
        Item item = new Item("", 0.0, 0.0, 0, new Supplier());
        assertFalse(countrySheetProcessor.validateImportedObject(item));
    }

    @Test
    public void itemIsValidatedTest() {
        Item item = new Item("orech", 2000.0, 256.0, 25, new Supplier());
        assertTrue(countrySheetProcessor.validateImportedObject(item));
    }

    @Test
    public void countValueForOneGramTestOkay() {
        assertEquals(4.0, countrySheetProcessor.countValueForOneGram(800.0, 200.0), 2);
    }
}