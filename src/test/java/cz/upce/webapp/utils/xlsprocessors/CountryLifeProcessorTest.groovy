package cz.upce.webapp.utils.xlsprocessors


import cz.upce.webapp.dao.stock.model.Supplier
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import spock.lang.Specification

class CountryLifeProcessorTest extends AbstractSheetProcessorTest {

    SupplierRepository supplierRepo = Mock()

    @Override
    protected String getPricelistResourcePath() {
        return  "/CountryLife_Objednavkovy_cenik_VO.xls"
    }

    def "IterateSheetValues"() {
        def f = getClass().getResource(getPricelistResourcePath()).getFile()
        supplierRepo.getOne(_) >> new Supplier()

        when:
        def items = new CountrySheetProcessor(supplierRepository: supplierRepo).parseItemsAsMap(new File(f))
        then:

        items.size() > 0
        def jahlyBio = items["Jáhly   COUNTRY LIFE_5000_BIO"]
        jahlyBio.itemQuantity == 5000
        jahlyBio.itemTax == 15
        jahlyBio.itemPrice == 0.0462
        def jahly = items["Jáhly   COUNTRY LIFE_5000"]
        jahly.itemQuantity == 5000
        jahly.itemTax == 15
        jahly.itemPrice == 0.0349
        def merunky = items["Meruňky sušené_12500_BIO"]
        merunky.itemQuantity == 12500
        merunky.itemTax == 15
        merunky.itemPrice == 0.129
    }

    def "Make Order"() {
        given:
        def sheetRead = fillWriteAndReadSheet(new CountrySheetProcessor(supplierRepository: supplierRepo))

        expect:
        sheetRead.getRow(13).getCell(22).getNumericCellValue() == 3
        sheetRead.getRow(19).getCell(22).getNumericCellValue() == 1

    }
}
