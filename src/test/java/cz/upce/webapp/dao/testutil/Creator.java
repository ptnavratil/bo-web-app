package cz.upce.webapp.dao.testutil;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Creator implements ApplicationContextAware {

    Log log = LogFactory.getLog(Creator.class);

    private ApplicationContext applicationContext;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    SupplierRepository supplierRepository;


    private Map<Class, JpaRepository> repositories = new HashMap<>();
    @PostConstruct
    public void postConstruct() {
        Reflections ref = new Reflections("cz.upce.webapp.dao");
        for (Class<?> entityClass : ref.getTypesAnnotatedWith(Entity.class)) {
            String entityName = entityClass.getSimpleName();
            String repoBeanName = entityName.substring(0,1).toLowerCase() + entityName.substring(1) + "Repository";
            repositories.put(entityClass, (JpaRepository) applicationContext.getBean(repoBeanName));
        }
    }

    public void save(Object... entities) {
        for (Object entity : entities) {
            save(entity);
        }
    }

    public Object save(Object entity) {
        try {
            Map props = PropertyUtils.describe(entity);
            List<Field> allFields = FieldUtils.getAllFieldsList(entity.getClass());
            for (Field field : allFields) {
                try {
                    field.setAccessible(true);
                    Object propValue = FieldUtils.readField(field, entity);
                    boolean notEmptyField = isNotEmptyField(field);
                    boolean manyToOne = fieldHasAnnotation(field, ManyToOne.class);;
                    if (
                            (propValue == null) &&
                            (notEmptyField || manyToOne) &&
                            !fieldHasAnnotation(field, GeneratedValue.class)
                    ) {
                        if (field.getType().isAssignableFrom(String.class)) {
                            propValue = "Test " + field.getName();
                        } else if (field.getType().isAssignableFrom(Integer.class)) {
                            propValue = new Integer(1);
                        } else if (field.getType().isAssignableFrom(Double.class)) {
                            propValue = new Double(1);
                        } else {
                            propValue = field.getType().newInstance();
                        }
                        PropertyUtils.setProperty(entity, field.getName(), propValue);
                    }
                    saveChildEntity(propValue);
                } catch (IllegalAccessException e) {
                    log.info("Skipping " + field.getName() + ", as it is probably private");
                }
            }

            for (Object propName : props.keySet()) {
                Object propValue = props.get(propName);
                saveChildEntity(propValue);
            }

            JpaRepository dao = getDao(entity);
            dao.save(entity);
        } catch (Exception e) {
            throw new IllegalStateException("Problem", e);
        }
        return entity;

    }

    public boolean isNotEmptyField(Field field) {
        Column[] columns = field.getDeclaredAnnotationsByType(Column.class);
        if (columns.length>0) {
            return columns[0].nullable() == false;
        } else {
            ManyToOne[] manyToOnes = field.getDeclaredAnnotationsByType(ManyToOne.class);
            if (manyToOnes.length>0) {
                return manyToOnes[0].optional()==false;
            }
        }
        return false;

    }

    private boolean fieldHasAnnotation(Field field, Class annotationClass) {
        return field.getDeclaredAnnotationsByType(annotationClass).length > 0;
    }

    private JpaRepository getDao(Object entity) {
        return repositories.get(entity.getClass());
    }

    private void saveChildEntity(Object propValue) {
        if (propValue!=null)  {
            Class<?> valueClass = propValue.getClass();
            final boolean isEntity = isEntity(valueClass);
            if ((isEntity)) {

                save(propValue);
                String className = propValue.getClass().getSimpleName();
                String daoName = className.substring(0,1).toLowerCase() + className.substring(1) + "Repository";

                JpaRepository jpaRepository = applicationContext.getBeansOfType(JpaRepository.class).get(daoName);
                jpaRepository.save(propValue);
            }
        }
    }

    private boolean isEntity(Class<?> valueClass) {
        return valueClass.getDeclaredAnnotationsByType(Entity.class).length > 0;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }
}


