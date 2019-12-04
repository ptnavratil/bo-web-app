package cz.upce.webapp.utils.xlsprocessors;

import cz.upce.webapp.dao.stock.model.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tomas Kodym
 */

@Component
public class KServisSheetProcessor extends AbstractSheetProcessor
{

    @Override
    public List<Item> disintegrateIntoItem(int rowIdx, List<String> sheetData) {
        List<Item> itemsList = new ArrayList<>();
        //split values from list to array
        String[] values = sheetData.toArray(new String[0]);

        if (values.length>4) {
            String priceStr = values[4].replaceFirst("\\s*Kč","");

            if (StringUtils.isNumeric(priceStr)) {
                String itemName = values[0].trim();
                String itemQuantityStr = values[3];
                Pattern weightPattern = Pattern.compile("^(?<weight>.+?)(\\(.*\\)|\\/.*)?$");
                Matcher matcher = weightPattern.matcher(itemQuantityStr);
                if (matcher.matches()) {
                    String itemQuantityParsed = matcher.group("weight")
                            .replaceFirst("\\,", "\\.")
                            .replaceFirst("x1kg", "");

                    double itemQuantity = Double.parseDouble(itemQuantityParsed)*1000;
                    double itemPrice = Double.parseDouble(priceStr)/1000;
                    int itemTax = 15;
                    itemsList.add(new Item(itemName, itemQuantity, itemPrice, itemTax, null));
                }


            }
        }
        return itemsList;
    }

    @Override
    public Integer supplerId() {
        return 5;
    }

    public int getOrderColumnIdx() {
        return 5;
    }

    @Override
    public Workbook fillOrder(File fileToParse, Map<Item, Integer> orderedItems) {
        Workbook workbook = super.fillOrder(fileToParse, orderedItems);
        getProductsSheetFromWorkbook(workbook).getRow(0).getCell(5).setCellValue("Objednávám tolik balení");
        return workbook;
    }
}
