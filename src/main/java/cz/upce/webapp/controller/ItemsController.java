package cz.upce.webapp.controller;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import cz.upce.webapp.service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author Tomas Kodym
 */

@Controller
public class ItemsController
{

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemRepository itemRepository;

    private final int itemsPerPage = 75;


    @GetMapping("/items/{page}")
    public String showAllItems(@PathVariable Integer page, Model model)
    {
        List<Item> items = itemRepository.findAllSorted(page-1, itemsPerPage);

        model.addAttribute("items", items);
        List<Integer> pages = pages(itemRepository.findAll().size());
        model.addAttribute("pages", pages);
        model.addAttribute("actualPage", page);

        return "items/items_list";
    }

    @GetMapping("/item/detail/{itemId}")
    public String showItemById(@PathVariable Integer itemId, Model model)
    {
        model.addAttribute("item", itemRepository.findById(itemId).get());

        return "items/items_detail";
    }

    @PostMapping(value = "/items/filter/")
    public String showFilteringItemByName(Model model, @RequestParam("productName") String productName)
    {
        List<Item> items = itemService.findFilteredByName(productName);
        model.addAttribute("items", items);
        model.addAttribute("productName", productName);
        return "items/items_list";
    }

    private List<Integer> pages(int countOfItems)
    {
        int lastPageNumber = (int) Math.ceil((double)countOfItems / itemsPerPage);
        List<Integer> pages = IntStream.rangeClosed(1, lastPageNumber)
                .boxed()
                .collect(Collectors.toList());

        return pages;
    }
}
