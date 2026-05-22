package ru.nursafin.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nursafin.dao.UserDao;
import ru.nursafin.exception.DuplicateLoginException;
import ru.nursafin.exception.NotFoundException;
import ru.nursafin.exception.ValidationException;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public BankUser createUser(String login, String rawPassword, String name, int age, Gender gender, HairColor hairColor) {
            if (userDao.findByLogin(login) != null) {
                throw new DuplicateLoginException("duplicate login");
            }
            String hash = passwordEncoder.encode(rawPassword);

            BankUser user = new BankUser(login, hash, name, age, gender, hairColor);

            return userDao.save(user);
    }

    @Transactional(readOnly = true)
    public BankUser getUser(Long userId) {
        return requireUser(userId);
    }

    @Transactional
    public BankUser addFriend(Long userId, Long friendId) {
            if (Objects.equals(userId, friendId)) {
                throw new ValidationException("cannot add yourself as a friend");
            }

            BankUser user = requireUser(userId);
            BankUser friend = requireUser(friendId);

            user.addFriend(friend);
            userDao.save(user);
            userDao.save(friend);

            return requireUser(userId);
    }

    @Transactional
    public BankUser removeFriend(Long userId, Long friendId) {
            if (Objects.equals(userId, friendId)) {
                throw new ValidationException("cannot remove yourself");
            }

            BankUser user = requireUser(userId);
            BankUser friend = requireUser(friendId);

            user.removeFriend(friend);
            userDao.save(user);
            userDao.save(friend);

            return requireUser(userId);
    }

    @Transactional(readOnly = true)
    public Set<BankUser> getFriends(Long userId) {
        requireUser(userId);
        return userDao.findFriends(userId);
    }

    @Transactional(readOnly = true)
    public List<BankUser> getUsers(Gender gender, HairColor hairColor) {
        return userDao.findAllByFilter(gender, hairColor);
    }

    @Transactional(readOnly = true)
    public BankUser findByLogin(String login) {
        return userDao.findByLogin(login);
    }

    private BankUser requireUser(Long id) {
        if (id == null) {
            throw new NotFoundException("user not found");
        }

        BankUser user = userDao.findById(id);
        if (user == null) {
            throw new NotFoundException("user not found");
        }

        return user;
    }
}
