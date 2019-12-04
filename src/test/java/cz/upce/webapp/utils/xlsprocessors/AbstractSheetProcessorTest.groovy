package cz.upce.webapp.utils.xlsprocessors

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.model.Supplier
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import spock.lang.Specification;

abstract class AbstractSheetProcessorTest extends Specification{
    protected Sheet fillWriteAndReadSheet(AbstractSheetProcessor processor, Integer additionalParsedIdx = null) {
        def sheetRead
        def filePath = getPricelistResourcePath()
        def f = getClass().getResource(filePath).getFile()

        def items = processor.parseItems(new File(f))

        supplierRepo.getOne(_) >> new Supplier()

        Map<Item, Integer> orderedItems = new TreeMap<>();
        orderedItems.put(items.get(0), 3)
        orderedItems.put(items.get(3), 1)
        if (additionalParsedIdx!=null) {
            orderedItems.put(items.get(additionalParsedIdx), 1)
        }

        Workbook workbook = processor.fillOrder(new File(f), orderedItems)
        def outputFilePath = System.getProperty("user.dir") + filePath
        def outputStream = new FileOutputStream(outputFilePath)
        workbook.write(outputStream)
        outputStream.close()
        sheetRead = processor.getProductsSheetFromWorkbook(new File(outputFilePath))
        sheetRead
    }

    protected String getPricelistResourcePath() {
        return null;
    }
}
