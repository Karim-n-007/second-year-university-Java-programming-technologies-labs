package ru.nursafin.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nursafin.dao.UserDao;
import ru.nursafin.exception.DuplicateLoginException;
import ru.nursafin.exception.NotFoundException;
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

    @Transactional
    public BankUser createUser(String login, String name, int age, Gender gender, HairColor hairColor) {
            BankUser bankUser = userDao.findByLogin(login);
            if (bankUser != null) {
                throw new DuplicateLoginException("duplicate login");
            }

            BankUser user = new BankUser(login, name, age, gender, hairColor);

            return userDao.save(user);
    }

    @Transactional
    public BankUser getUser(Long userId) {
        return requireUser(userId);
    }

    @Transactional
    public BankUser addFriend(Long userId, Long friendId) {
            if (Objects.equals(userId, friendId)) {
                throw new DuplicateLoginException("duplicate login");
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
                throw new DuplicateLoginException("duplicate login");
            }

            BankUser user = requireUser(userId);
            BankUser friend = requireUser(friendId);

            user.removeFriend(friend);
            userDao.save(user);
            userDao.save(friend);

            return requireUser(userId);
    }

    @Transactional
    public Set<BankUser> getFriends(Long userId) {
        requireUser(userId);
        return userDao.findFriends(userId);
    }

    @Transactional
    public List<BankUser> getUsers(Gender gender, HairColor hairColor) {
        return userDao.findAllByFilter(gender, hairColor);
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
