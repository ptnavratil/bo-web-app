package cz.upce.webapp.dao.stock;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class TestDataLocalhost implements InitializingBean {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        if ("jety-17".equals(InetAddress.getLocalHost().getHostName())) {
            Supplier supplier = supplierRepository.getOne(4);
            Item item = new Item();
            item.setBio (false);
            item.setItemId (null);
            item.setItemName ("Přírodní třtinový cukr  SUROVÝ MU");
            item.setItemQuantity (50000.0);
            item.setItemPrice (0.0305);
            item.setItemTax (15);
            item.setSupplier (supplier);
            item.setRowIdx (5);
            itemRepository.save(item);
        }
    }
}
