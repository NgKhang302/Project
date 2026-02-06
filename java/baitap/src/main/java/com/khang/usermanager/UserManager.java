package com.khang.usermanager;

import com.khang.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private List<User> users = new ArrayList<>();

    // Thêm nhiều user cùng lúc
    public void addUsers(List<User> newUsers) {
        users.addAll(newUsers);
    }

    // Xóa nhiều user cùng lúc
    public void deleteUsers(List<Integer> ids) {
        users.removeIf(user -> ids.contains(user.getId()));
    }

    // Tìm user bằng tên (dùng chung cho query và path)
    public User findByName(String name) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }

    public List<User> getAll() {
        return users;
    }
}
