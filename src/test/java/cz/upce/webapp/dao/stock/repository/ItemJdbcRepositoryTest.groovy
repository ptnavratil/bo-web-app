package cz.upce.webapp.dao.stock.repository

import cz.upce.webapp.dao.AbstractJPATest
import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.testutil.Creator
import org.junit.Rule
import org.junit.rules.TestName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Unroll

class ItemJdbcRepositoryTest extends AbstractJPATest {

    @Autowired ItemJdbcRepository itemJdbcRepository;

    def "test"() {

        given:
            // Prepare test data
            creator.save(new Item(itemName: "BIO mandle uzené"))
            creator.save(new Item(itemName: "CountryLife mandle uzené",bio: true))
            creator.save(new Item(itemName: "mandle"))

        expect:
            //itemJdbcRepository.findAllUsingSearch("BIO").size() == 2
            itemJdbcRepository.findAllUsingSearch("-CountryLife uzené mandle").size() == 1
            itemJdbcRepository.findAllUsingSearch("mandle").size() == 3
            itemJdbcRepository.findAllUsingSearch("mandle uzené").size() == 2
            itemJdbcRepository.findAllUsingSearch("uzené mandle").size() == 2
            itemJdbcRepository.findAllUsingSearch("uzené MANDLE").size() == 2
            itemJdbcRepository.findAllUsingSearch("MANDLE BIO").size() == 2
    }

}
