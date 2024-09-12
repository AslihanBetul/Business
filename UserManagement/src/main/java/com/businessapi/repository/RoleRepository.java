package com.businessapi.repository;

import com.businessapi.entity.Role;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.views.GetAllRoleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByIdIn(List<Long> ids);

    @Query("select new com.businessapi.views.GetAllRoleView(r.id,r.roleName,r.roleDescription) from Role r where r.status=?1")
    List<GetAllRoleView> getAllRoles(EStatus status);







}
