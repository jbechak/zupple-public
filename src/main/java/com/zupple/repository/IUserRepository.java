package com.zupple.repository;

import com.zupple.model.UserModel;

import java.util.List;

public interface IUserRepository {

    List<UserModel> findAll();

    UserModel getUserById(int userId);

    UserModel findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password, String role);

    String getRoleById(int id);

    List<UserModel> getRentersByLandlordId(int landlordId);

    List<UserModel> getAllWorkers();

    UserModel getUserByIdAndRole(int id, String role);
}
