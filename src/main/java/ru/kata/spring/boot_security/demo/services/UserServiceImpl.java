package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userDao.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) { return userDao.findUserByUsername(username); }

    @Override
    @Transactional
    public void update(long id, User updateUser) {
        User userToBeUpdate = entityManager.find(User.class, id);

        userToBeUpdate.setUsername(updateUser.getUsername());
        userToBeUpdate.setPassword(updateUser.getPassword());
        userToBeUpdate.setEmail(updateUser.getEmail());
    }

    @Override
    @Transactional
    public void delete(long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }
}
