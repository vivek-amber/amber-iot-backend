package com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.UserEntity;

public interface UserRepository  extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmailAndCompanyId(String email,String companyId);

    boolean existsByEmailAndCompanyId(String email,String companyId);

    Optional<UserEntity> findByIdAndCompanyId(String id,String companyId);

    List<UserEntity> findByCompanyId(String companyId);

    List<UserEntity> findByCompanyIdAndActive(String companyId,Boolean active);

    Optional<UserEntity> findByPhoneNumberAndCompanyId(String phoneNumber,String companyId);

    boolean existsByPhoneNumberAndCompanyId(String phoneNumber, String companyId);
    
}