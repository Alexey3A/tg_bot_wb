package com.example.tg_bot_wb.dao;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public void saveOrUpdateProduct(Product product, Person person) {
        product.addPersonToProduct(person);
        entityManager.merge(product);
    }

    @Override
    public void deleteProduct(long id) {
        Query query = entityManager.createQuery("delete from Product "
                + "where id =: productId");
        query.setParameter("productId", id);
        query.executeUpdate();

        /*Product product = getProduct(id);
        if (product != null) {
            entityManager.remove(product);
        }*/
    }

    @Override
    public Product getProduct(long id) {

        return entityManager.find(Product.class, id);
    }

    @Override
    public List<Product> getAllProduct() {
        Query query = entityManager.createQuery("FROM Product");
        return query.getResultList();
    }
}
