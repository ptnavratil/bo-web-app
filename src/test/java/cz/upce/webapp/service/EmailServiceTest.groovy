package cz.upce.webapp.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class EmailServiceTest extends Specification {

    @Autowired
    EmailService emailService;

    def "SendEmail"() {
         emailService.sendEmail("Ahoj", "st46567@student.upce.cz", "venaca99@gmail.com")
        expect: true
    }
}
