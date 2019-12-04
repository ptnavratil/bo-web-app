package cz.upce.webapp.dao

import cz.upce.webapp.dao.stock.StoredFileStorageService
import cz.upce.webapp.dao.stock.repository.ItemJdbcRepository
import cz.upce.webapp.dao.testutil.Creator
import org.junit.Rule
import org.junit.rules.TestName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@DataJpaTest
@Import([Creator.class, StoredFileStorageService.class, ItemJdbcRepository.class])
abstract class AbstractJPATest extends Specification{
    @Rule TestName testName = new TestName()
    @Autowired Creator creator
}
