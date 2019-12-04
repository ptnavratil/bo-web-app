package cz.upce.webapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.repository.ItemJdbcRepository;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tomas Kodym
 */

@Service
public class ItemServiceImpl
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemJdbcRepository itemJdbcRepository;

    public Optional<Item> findById(Integer id)
    {
        return itemRepository.findById(id);
    }

    public List<Item> findFilteredByName(String keywords)
    {
        return itemJdbcRepository.findAllUsingSearch(keywords);
    }

    public void deleteAllBySupplier(Integer supplierId)
    {
        LOGGER.info("Starting to delete items from database for supplier: " + supplierId + "!");
        List<Item> itemList = itemRepository.findAll();
        itemList = itemList.stream().filter(item -> item.getSupplier().getId().equals(supplierId)).collect(Collectors.toList());

        for (Item item : itemList)
        {
            itemRepository.delete(item);
            LOGGER.info(item + " was successfully deleted from the database!");
        }

        LOGGER.info("Deleting items from database was finished!");
    }
}
