package cz.upce.webapp.dao.stock.repository;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.StoredFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.OrderBy;
import java.util.List;
import java.util.Optional;

/**
 * @author Tomas Kodym
 */

@Repository
public interface StoredFileRepository extends JpaRepository<StoredFile, Integer>
{


}
