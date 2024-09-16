package com.businessapi.repository;

import com.businessapi.entity.Role;
import com.businessapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthId(Long authId);

    //@Query("SELECT new com.usermanagement.views.GetAllUsersView(u.firstName,u.lastName,u.role) from User u ")
    //List<GetAllUsersView> getAllUsers();

    @Query("SELECT u.role from User u where u.authId=?1")
    List<Role> getUserRoles(Long authId);



}
