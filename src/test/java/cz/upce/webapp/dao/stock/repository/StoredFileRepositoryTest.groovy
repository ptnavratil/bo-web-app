package cz.upce.webapp.dao.stock.repository

import com.google.common.net.MediaType
import cz.upce.webapp.dao.AbstractJPATest
import cz.upce.webapp.dao.stock.StoredFileStorageService
import cz.upce.webapp.dao.stock.model.StoredFile
import cz.upce.webapp.dao.stock.model.Supplier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile

import java.nio.charset.StandardCharsets

class StoredFileRepositoryTest extends AbstractJPATest {

    @Autowired StoredFileStorageService storedFileService

    def "saveBlob"() {
        given:
            def supplier = new Supplier()
        def filePath = this.getClass().getResource("/CountryLife_Objednavkovy_cenik_VO.xls").getFile()
        File myfile = new File(filePath)

        def bytes = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(filePath))
        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "file",
                        bytes);

        when:
            def stored = storedFileService.storeFile(mockFile)
        then:
            stored.getData().length == myfile.length()

    }
}
