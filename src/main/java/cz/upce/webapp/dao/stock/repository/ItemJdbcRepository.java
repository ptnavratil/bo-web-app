package cz.upce.webapp.dao.stock.repository;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.naming.directory.Attributes;
import javax.persistence.OrderBy;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Tomas Kodym
 */

@Repository
public class ItemJdbcRepository {
    JdbcTemplate jdbcTemplate;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<Item> findAllUsingSearch(String keywords) {

        List suppliers = supplierRepository.findAll();

        Map<Integer, Supplier> supplierMap = (Map<Integer, Supplier>) suppliers.stream().collect(
                Collectors.toMap(Supplier::getId, s -> s));

        String[] words = keywords.split("\\s");
        List<String> wordsWithPercents = new ArrayList<>();
        StringBuffer sql = new StringBuffer("select * from item i where 1=1");
        for (String word : words) {
            if (word.startsWith("-")) {
                sql.append(" AND NOT(upper(item_name) like ?)");
                wordsWithPercents.add("%" + word.substring(1).toUpperCase() + "%");
            } else {
                sql.append(" AND upper(item_name) like ?");
                wordsWithPercents.add("%" + word.toUpperCase() + "%");
            }
        }
        String[] wordsWithPercentsArr = wordsWithPercents.toArray(new String[0]);
        return jdbcTemplate.query(sql.toString(),  wordsWithPercentsArr, new ItemRowMapper(supplierMap));
    }


    private class ItemRowMapper implements RowMapper<Item> {

        private Map<Integer, Supplier> supplierMap;

        public ItemRowMapper(Map<Integer, Supplier> supplierMap) {
            this.supplierMap = supplierMap;
        }

        @Override
        public Item mapRow(ResultSet resultSet, int i) throws SQLException {
                Supplier s = supplierMap.get(resultSet.getInt("supplier_id"));
                return new Item(
                        resultSet.getString("item_name"),
                        resultSet.getDouble("item_quantity"),
                        resultSet.getDouble("item_price"),
                        resultSet.getInt("item_tax"),
                        s
                );
        }
    }
}
