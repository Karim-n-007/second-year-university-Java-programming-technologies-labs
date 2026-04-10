package ru.nursafin.dao;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;
import ru.nursafin.persistence.entity.BankUserEntity;
import ru.nursafin.persistence.mapper.PersistenceDomainMapper;
import ru.nursafin.persistence.repository.BankUserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class JpaUserDao implements UserDao {
    private final BankUserRepository bankUserRepository;
    private final PersistenceDomainMapper mapper;


    @Override
    public BankUser findById(Long id) {
        return bankUserRepository.findDetailedById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public BankUser findByLogin(String login) {
        return bankUserRepository.findByLogin(login)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public BankUser save(BankUser bankUser) {
        BankUserEntity entity;

        if (bankUser.getUserId() == null) {
            entity = new BankUserEntity (
                    bankUser.getLogin(),
                    bankUser.getName(),
                    bankUser.getAge(),
                    bankUser.getGender(),
                    bankUser.getHairColor()
            );
            entity = bankUserRepository.save(entity);
        } else {
            entity = bankUserRepository.findDetailedById(bankUser.getUserId()).orElseThrow();
            entity.updateProfile(
                    bankUser.getName(),
                    bankUser.getAge(),
                    bankUser.getGender(),
                    bankUser.getHairColor()
            );
        }

        syncFriends(entity, bankUser.getFriends());

        entity = bankUserRepository.save(entity);

        return mapper.toDomain(bankUserRepository.save(entity));
    }

    @Override
    public List<BankUser> findAllByFilter(Gender gender, HairColor hairColor) {
        return bankUserRepository.findAllByFilter(gender, hairColor)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Set<BankUser> findFriends(Long userId) {
        return bankUserRepository.findWithFriendsById(userId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    private void syncFriends(BankUserEntity entity, Set<Long> friendIds) {
        Set<Long> ids = friendIds == null ? Set.of() : new HashSet<>(friendIds);
        ids.remove(entity.getUserId());

        List<BankUserEntity> desiredFriends = bankUserRepository.findAllById(ids);

        entity.getFriends().clear();

        for (BankUserEntity friend : desiredFriends) {
            if (!Objects.equals(friend.getUserId(), entity.getUserId())) {
                entity.getFriends().add(friend);
            }
        }
    }
}
