package cz.upce.webapp.controller.dto;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;

import java.util.Map;
import java.util.TreeMap;

public class SupplierItemsDTO extends TreeMap<Item,Integer> {
    private Supplier supplier;

    public SupplierItemsDTO() {
    }
    public SupplierItemsDTO(Supplier supplier) {
        this.supplier = supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Double getTotal(boolean withTax)
    {
        double sum = 0;
        for (Map.Entry<Item, Integer> itemIntegerEntry : this.entrySet()) {
            Item item = itemIntegerEntry.getKey();
            sum += (withTax?(1+0.01*item.getItemTax()):1)*item.getItemQuantity()*item.getItemPrice()*itemIntegerEntry.getValue();

        }
        return sum;
    }

    public Double getWeight()
    {
        return entrySet().stream()
                .map(entry -> Double.valueOf(entry.getKey().getItemQuantity()) * entry.getValue())
                .reduce(Double::sum)
                .orElse(0.0);
    }
}
