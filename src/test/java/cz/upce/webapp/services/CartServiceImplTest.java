package cz.upce.webapp.services;

import static org.junit.Assert.*;

import cz.upce.webapp.controller.dto.SupplierItemsDTO;
import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.service.OrderFormServiceImpl;
import cz.upce.webapp.service.OrderedProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cz.upce.webapp.service.CartServiceImpl;

import java.util.Map;

/**
 * @author Tomas Kodym
 */
public class CartServiceImplTest {

    private Supplier orisek = new Supplier();

    private Supplier county = new Supplier();

    private Item item = new Item( "Aloe", 22.0, 250.0, 10, orisek);

    private Item item1 = new Item("Mandle", 22.0, 250.0, 10, county);

    private OrderFormServiceImpl orderFormService;
    private OrderedProductServiceImpl orderedProductService;
    private CartServiceImpl cartService = new CartServiceImpl(orderFormService, orderedProductService, null);

    @Before
    public void setUp() {
        item.setItemId(1);
        item1.setItemId(2);
        item.setRowIdx(1);
        item1.setRowIdx(1);
        orisek.setId(1);
        county.setId(2);
        orisek.setName("Orisek");
        county.setName("Country");
        cartService.addItem(item);
        cartService.addItem(item);
        cartService.addItem(item1);
    }

    @Test
    public void addItem() {
        assertEquals(2, this.cartService.getItemsInCart().size());
    }

    @Test
    public void removeSameItem() {
        this.cartService.removeItem(item);
        assertEquals(2, this.cartService.getItemsInCart().size());
    }

    @Test
    public void removeDifferentItem() {
        assertEquals(1, this.cartService.getItemsInCart().get(2).size());
        this.cartService.removeItem(item1);
        assertEquals(0, this.cartService.getItemsInCart().get(2).size());
    }

    @Test
    public void getItemsInCart() {
        assertEquals(2, this.cartService.getItemsInCart().size());
    }

    @Test
    public void getNutItemsOnly() {

        assertEquals(1, this.cartService.getItemsInCart().get(1).size());
    }

    @Test
    public void getCountryLifeItemsOnly() {

        assertEquals(1, this.cartService.getItemsInCart().get(2).size());
    }

    @Test
    public void getTotal() {
        assertEquals(16500, this.cartService.getTotal(), 2);
    }

    @Test
    public void getTotalNuts() {

        SupplierItemsDTO itemIntegerMap = this.cartService.getItemsInCart().get(2);
        assertEquals(5500, itemIntegerMap.getTotal(false), 2);
    }

    @Test
    public void getTotalNutsTax() {
        assertEquals(12100, this.cartService.getItemsInCart().get(1).getTotal(true), 2);
    }

    @Test
    public void getTotalCountryLife() {
        assertEquals(5500, this.cartService.getItemsInCart().get(2).getTotal(false), 2);
    }

    @Test
    public void getTotalCountryLifeTax() {
        assertEquals(6050, this.cartService.getItemsInCart().get(2).getTotal(true), 2);
    }

    @Test
    public void getWeightNuts() {
        assertEquals(44, this.cartService.getItemsInCart().get(1).getWeight(), 2);
    }

    @Test
    public void getWeightCountryLife() {
        assertEquals(22, this.cartService.getItemsInCart().get(2).getWeight(), 2);
    }
}