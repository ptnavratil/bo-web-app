package cz.upce.webapp.controller;

import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import cz.upce.webapp.dao.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Martin Volenec / st46661
 */

@Controller
public class HomepageController
{

    @GetMapping("/")
    public String getHomepage(Model model)
    {
        return "redirect:/items";
    }
}
