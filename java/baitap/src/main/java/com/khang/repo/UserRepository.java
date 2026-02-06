package com.khang.repo;
// file communicate với database
import com.khang.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    // Lấy tất cả user
    public List<User> getAll() {
        return users;
    }

    // Lưu 1 user (insert)
    public User save(User u) {
        if (u == null) return null; ///Null-check → tránh lỗi NPE

        if (u.getId() <= 0) {
            u.setId(nextId);
            nextId++;
            users.add(u);
        }

        return u;
    }

    // Lưu nhiều user
    public List<User> saveAll(List<User> list) {
        List<User> added = new ArrayList<>();
        if (list == null) return added;

        for (User u : list) {
            User saved = save(u);
            if (saved != null) added.add(saved);
        }
        return added;
    }

    // Tìm theo id
    public User findById(int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    // Tìm theo tên (contains)
    public List<User> findByNameContains(String keyword) {
        List<User> res = new ArrayList<>();
        if (keyword == null) return res;
// so sánh không phân biệt chữ hoa thường
        String low = keyword.toLowerCase();
        for (User u : users) {
            if (u.getName() != null && u.getName().toLowerCase().contains(low)) {
                res.add(u);
            }
        }
        return res;
    }

    // Xóa 1 user
    public boolean deleteById(int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    // Xóa nhiều user
    public List<Integer> deleteByIds(List<Integer> ids) {
        List<Integer> deleted = new ArrayList<>();
        if (ids == null) return deleted;

        for (Integer id : ids) {
            if (deleteById(id)) {
                deleted.add(id);
            }
        }
        return deleted;
    }

    // Reset data (test)
    public void clearAll() {
        users.clear();
        nextId = 1;
    }
}