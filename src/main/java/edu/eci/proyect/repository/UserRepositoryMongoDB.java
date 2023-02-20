package edu.eci.proyect.repository;

import edu.eci.proyect.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRepositoryMongoDB implements UserRepository{

    @Autowired
    private final MongoTemplate mongoTemplate;

    public UserRepositoryMongoDB(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User save(User user) {
        return mongoTemplate.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, User.class));
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }

    public User update(User user, String id) {
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update()
                .set("name", user.getName())
                .set("lastName", user.getLastName())
                .set("email", user.getEmail());

        mongoTemplate.updateFirst(query, update, User.class);

        return findById(id).orElse(null);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, User.class);
    }
}
