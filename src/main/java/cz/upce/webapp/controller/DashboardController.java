package cz.upce.webapp.controller;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.OrderState;
import cz.upce.webapp.dao.stock.model.OrderedProducts;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import cz.upce.webapp.service.OrderedProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Martin Volenec / st46661
 */

@Controller
public class DashboardController
{
    private OrderedProductServiceImpl orderedProductService;

    private SupplierRepository supplierRepository;

    @Autowired
    public DashboardController(OrderedProductServiceImpl orderedProductService, SupplierRepository supplierRepository) {

        this.orderedProductService = orderedProductService;
        this.supplierRepository = supplierRepository;

    }

    @GetMapping("/price_list")
    public String getPriceList(Model model)
    {
        List<Supplier> suppliers = supplierRepository.findAll();
        model.addAttribute("suppliers", suppliers);

        return "importer/price_list";
    }
    public static class Order {
        private Item item;
        private OrderState state;
        private int amount;

        public Order(Item item, OrderState state, int amount) {
            this.item = item;
            this.state = state;
            this.amount = amount;
        }

        public Item getItem() {
            return item;
        }

        public int getAmount() {
            return amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Order order = (Order) o;
            return amount == order.amount &&
                    item.equals(order.item) &&
                    state == order.state;
        }

    }
}
