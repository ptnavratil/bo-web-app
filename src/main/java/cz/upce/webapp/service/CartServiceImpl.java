package cz.upce.webapp.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import cz.upce.webapp.controller.dto.SupplierItemsDTO;
import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.OrderForm;
import cz.upce.webapp.dao.stock.model.OrderState;
import cz.upce.webapp.dao.stock.model.OrderedProducts;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Tomas Kodym
 */

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl
{
    private OrderFormServiceImpl orderFormService;

    private OrderedProductServiceImpl orderedProductService;

    private SupplierRepository supplierRepository;
    private String message;

    @Autowired
    public CartServiceImpl(OrderFormServiceImpl orderFormService, OrderedProductServiceImpl orderedProductService, SupplierRepository supplierRepository) {
        this.orderFormService = orderFormService;
        this.orderedProductService = orderedProductService;
        this.supplierRepository = supplierRepository;
    }

    private Map<Integer, SupplierItemsDTO> cart = new HashMap<>();

    public void addItem(Item item)
    {
        changeItemAmount(item, 1);
    }

    public void removeItem(Item item)
    {
        changeItemAmount(item, -1);
    }


    public void changeItemAmount(Item item, int relativeAmountChange) {
        Integer supplierId = item.getSupplier().getId();
        Map<Item, Integer> supplierItems = supplierItems(supplierId);
        Integer bought = supplierItems.get(item);
        if (bought==null) {
            bought = 0;
        }
        bought+= relativeAmountChange;
        if (bought==0) {
            supplierItems.remove(item);
        } else {
            supplierItems.put(item, bought);
        }
    }

    public Map<Item, Integer> supplierItems(Integer supplierId) {
        SupplierItemsDTO supplierItems = cart.get(supplierId);
        if (!cart.containsKey(supplierId)) {
            // TODO During tests, it could be null, change to mock
            supplierItems = new SupplierItemsDTO();
            if (supplierRepository!=null) {
                supplierItems.setSupplier(supplierRepository.getOne(supplierId));
            }
            cart.put(supplierId, supplierItems);
        }
        return supplierItems;
    }

    public Map<Integer,SupplierItemsDTO> getItemsInCart()
    {
        return Collections.unmodifiableMap(cart);
    }

    public void checkout()
    {
        OrderForm order = new OrderForm();
        order.setState(OrderState.NEW);
        orderFormService.addNewOrder(order);

        List<OrderedProducts> list = new ArrayList<>();

        cart.forEach((supplierId, supplierItems) -> {
            supplierItems.forEach((item, amount) -> {
                OrderedProducts orderedProduct = new OrderedProducts();
                orderedProduct.setAmount(amount);
                orderedProduct.setOrder(order);
                orderedProduct.setItem(item);

                list.add(orderedProduct);
            });
        });

        orderedProductService.addAll(list);

        cart.clear();
    }

    public Double getTotal()
    {
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        cart.forEach((supplierId, supplierItems) -> {
            supplierItems.forEach((item, amount) -> {
                sum.updateAndGet(v -> v + item.getItemPrice() * item.getItemQuantity() * amount);
            });
        });

        return sum.get();
    }

    private Double countPrice(Item item, Integer value, boolean withTax)
    {
        Double price;
        Double pricePerQuantity = item.getItemPrice() * item.getItemQuantity();
        if(withTax){
            Double tax = pricePerQuantity * (item.getItemTax() / 100.0);
            price = (pricePerQuantity + tax) * value;
        }else {
            price =  pricePerQuantity * value;
        }

        return price;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAndClearMessage() {
        String result = this.message;
        this.message = null;
        return result;
    }

    public void removeSupplier(Integer supplierId) {
        cart.remove(supplierId);
    }
}