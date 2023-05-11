package info.phj233.onlinechat.dao;

import info.phj233.onlinechat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, Integer> {
    User findUserByUsername(String username);

    List<User> findByRoleLike(String role);
}
