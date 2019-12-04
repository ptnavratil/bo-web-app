package cz.upce.webapp.utils.xlsprocessors;

import cz.upce.webapp.dao.stock.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tomas Kodym
 */

@Component
public class BionebioSheetProcessor extends AbstractSheetProcessor
{
    public static final Double EUR_TO_CZK = new Double(26);

    @Override
    public String getSheetName() {
        return "Velká balení";
    }

    @Override
    public Integer supplerId() {
        return 4;
    }

    Pattern nameWithWeight = Pattern.compile("^(?<itemName>.*?)(?<weight>[\\d\\,]+)\\skg$");

    Double parsedEurValue = null;

    @Override
    public List<Item> disintegrateIntoItem(int rowIdx, List<String> sheetData) {
        List<Item> itemsList = new ArrayList<>();

        //split values from list to array
        String[] values = sheetData.toArray(new String[0]);


        if (values.length>=4) {
            if (rowIdx==3 && values.length>=4) {
                parsedEurValue = Double.parseDouble(values[3]);
            }
            String itemName = values[1].trim();
            if (itemName.endsWith(" kg")) {
                Matcher matcher = nameWithWeight.matcher(itemName);
                if (matcher.matches()) {
                    String itemNameToUse = matcher.group("itemName").trim();
                    String itemQuantityStr = matcher.group("weight");
                    itemQuantityStr = itemQuantityStr.replaceFirst("\\,","\\.");
                    double itemQuantity = Double.parseDouble(itemQuantityStr)*1000;
                    double itemPrice;
                    if (values[2].length()>0) {
                        itemPrice = Double.parseDouble(values[2])/1000;
                    } else {
                        double eurValue = Double.parseDouble(values[3]);
                        Double eurToCzk = parsedEurValue==null?EUR_TO_CZK:parsedEurValue;
                        itemPrice = eurValue * eurToCzk;
                    }
                    int itemTax = 15;
                    itemsList.add(new Item(itemNameToUse, itemQuantity, itemPrice, itemTax, null));
                }

            }
        }
        return itemsList;
    }

    public int getOrderColumnIdx() {
        return 4;
    }

}
