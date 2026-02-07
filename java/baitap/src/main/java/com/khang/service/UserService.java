// xử lý logic và gọi repository, validate dữ liệu
package com.khang.service;

import com.khang.model.User;
import com.khang.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    // Lấy tất cả user
    public List<User> getAllUsers() {
        return repo.getAll();
    }

    // Tìm user theo tên (query param)
    public List<User> searchByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return repo.getAll();
        }
        return repo.findByNameContains(keyword);
    }

    // Tạo 1 user
    public User createUser(String name, String email) {
        if (name == null || email == null) return null;

        User u = new User();
        u.setName(name);
        u.setEmail(email);

        return repo.save(u);
    }

    // Tạo nhiều user
    public List<User> createManyUsers(List<User> users) {
        if (users == null) return new ArrayList<>();
        return repo.saveAll(users);
    }

    // Xóa 1 user theo id
    public boolean deleteUser(int id) {
        return repo.deleteById(id);
    }

    // Xóa nhiều user
    public List<Integer> deleteManyUsers(List<Integer> ids) {
        return repo.deleteByIds(ids);
    }

    // PATCH - update từng phần
    public User updateUserPartial(int id, String name, String email) {
        User u = repo.findById(id);
        if (u == null) return null;

        // MERGE DATA (nếu client gửi name → update
        //không gửi → giữ nguyên)
        if (name != null) {
            u.setName(name);
        }
        if (email != null) {
            u.setEmail(email);
        }

        return u;
    }
}
