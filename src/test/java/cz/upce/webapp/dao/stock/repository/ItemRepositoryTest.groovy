package cz.upce.webapp.dao

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.testutil.Creator
import org.junit.Rule
import org.junit.rules.TestName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Unroll

class ItemRepositoryTest extends AbstractJPATest {

    @Autowired ItemRepository itemRepository;

    def "test"() {

        given:
            // Prepare test data
            creator.save(new Item(itemName: "C mandle uzené"))
            creator.save(new Item(itemName: "A mandle uzené"))
            creator.save(new Item(itemName: "B mandle"))

        when:
            def items = itemRepository.findAllSorted(0, 10)
            def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle uzené")
        then:
            items.size() == 3
            items.get(1).itemName == "B mandle"
            itemsBy.size() == 2
            itemsBy.get(1).itemName == "C mandle uzené"
    }


    @Unroll
    def "When #items in db 'mandle' search returns #expectedItemsSize items"() {

        given:
        def itemsToCreate = items.split(",")
        for (String item  : itemsToCreate) {
            creator.save(new Item(itemName: item))
        }

        when:
            def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle")
        then:
            itemsBy.size() == expectedItemsSize

        where:
            items | expectedItemsSize
            "mandle A,mandle C" | 2
            "mandle,kešu" | 1
            "loupané mandle,kešu,rozinky" | 1

    }
    def "mandle A,mandle C=2"() { expect: oneTest()}
    def "mandle,kešu=1"() { expect: oneTest()}
    def "loupané mandle,kešu,rozinky=1"() { expect: oneTest()}

    def oneTest() {
        def testArguments = testName.methodName.split("=")
        def itemsToCreate = testArguments[0].split(",")
        def expectedSizeStr = testArguments[1]
        def expectedItemsSize = Integer.valueOf(expectedSizeStr)

        for (String item  : itemsToCreate) {
            creator.save(new Item(itemName: item))
        }

        def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle")
        return (itemsBy.size() == expectedItemsSize)

    }

}
