package cz.upce.webapp.utils.xlsprocessors;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractSheetProcessor implements ISheetProcessor {

    @Autowired
    SupplierRepository supplierRepository;

    @Override
    public Supplier supplier() {
        return supplierRepository.getOne(supplerId());
    }

    public abstract Integer supplerId();


    @Override
    abstract public List<Item> disintegrateIntoItem(int rowIdx, List<String> rowData);

    @Override
    public int getOrderColumnIdx() {
        return -1;
    }
}
