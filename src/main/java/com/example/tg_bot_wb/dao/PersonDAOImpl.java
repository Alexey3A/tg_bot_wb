package com.example.tg_bot_wb.dao;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PersonDAOImpl implements PersonDAO{

    @Autowired
    EntityManager entityManager;

    @Override
    public void saveOrUpdatePerson(Person person) {
        entityManager.merge(person);
    }

    @Override
    @Transactional
    public void saveOrUpdatePerson(Person person, Message personMessage) {

    }

    @Override
    @Transactional
    public void saveOrUpdatePerson(Person person, Message personMessage, Product product) {

        person.addMessageToPerson(personMessage);
        person.addProductToPerson(product);
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setProduct(product);
        requestDetails.setStartPrice(product.getCurrentPrice());
        requestDetails.setMessage(personMessage);
        personMessage.setRequestDetails(requestDetails);
        personMessage.setPerson(person);

        entityManager.merge(person);
    }

    @Override
    public void deletePerson(long id) {
        Person person = getPerson(id);
        if (person!=null) {
            entityManager.remove(person);
        }

        /*Query query = entityManager.createQuery("delete from Person "
                + "where id =:personId");
        query.setParameter("personId", id);
        query.executeUpdate();*/
    }

    @Override
    public Person getPerson(long id) {
        return entityManager.find(Person.class, id);
    }

    @Override
    public List<Person> getAllPerson() {
        Query query = entityManager.createQuery("FROM Person");
        List<Person> personList = query.getResultList();
        return personList;
    }


}
