package cz.upce.webapp.utils.xlsprocessors


import cz.upce.webapp.dao.stock.model.Supplier
import cz.upce.webapp.dao.stock.repository.SupplierRepository

class BionebioSheetProcessorTest extends AbstractSheetProcessorTest {

    SupplierRepository supplierRepo = Mock()

    @Override
    protected String getPricelistResourcePath() {
        return  "/OL_bio_nebio_11_2019.xls"
    }

    def "IterateSheetValues"() {
        def f = getClass().getResource(getPricelistResourcePath()).getFile()
        supplierRepo.getOne(_) >> new Supplier()

        when:
        def items = new BionebioSheetProcessor(supplierRepository: supplierRepo).parseItemsAsMap(new File(f))
        then:

        items.size() > 0
        def item1 = items["Přírodní třtinový cukr  SUROVÝ MU_50000"]
        item1.itemQuantity == 50000
        item1.itemTax == 15
        item1.itemPrice == 0.0305

        def item2 = items["Bio dýňové semínko CZ_25000"]
        item2.itemQuantity == 25000
        item2.itemTax == 15
        item2.itemPrice == 1

        def item3 = items["Bio parmezánové krekry s olivovým olejem bio*nebio_2700"]
        item3.itemQuantity == 2700
        item3.itemTax == 15
        item3.itemPrice == 0.61


    }

    def "Make Order"() {
        given:
        def sheetRead = fillWriteAndReadSheet(new BionebioSheetProcessor(supplierRepository: supplierRepo))

        expect:
        sheetRead.getRow(5).getCell(4).getNumericCellValue() == 3
        sheetRead.getRow(8).getCell(4).getNumericCellValue() == 1

    }


}
