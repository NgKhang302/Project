package com.khang.controller;
// file Controller: NHẬN REQUEST → GỌI SERVICE
import com.khang.dto.UserCrudDTO;
import com.khang.model.User;
import com.khang.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// nhờ service chống lưng
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //GET ALL / SEARCH
    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) String name) {
        return service.searchByName(name);
    }

    //POST
    @PostMapping
    public User createUser(@RequestBody UserCrudDTO.CreateUserRequest req) {
        return service.createUser(req.name, req.email);
    }
// req object chứa data client gửi
    //POST: tạo nhiều user
    @PostMapping("/many")
    public List<User> createManyUsers(
            @RequestBody UserCrudDTO.CreateManyUsersRequest req)// list user DTO
    {
        return service.createManyUsers(
                req.users.stream().map(u -> {    //stream duyệt từng phần tử
                    User user = new User();
                    user.setName(u.name);
                    user.setEmail(u.email);
                    return user;
                }).toList()
        );
    }

    //PATCH: update từng cái
    @PatchMapping("/{id}")
    public User updateUser(
            @PathVariable int id,
            @RequestBody UserCrudDTO.UpdateUserRequest req) {
        return service.updateUserPartial(id, req.name, req.email);
    }

    // ===== DELETE: xóa nhiều =====
    @DeleteMapping
    public List<Integer> deleteMany(
            @RequestBody UserCrudDTO.DeleteManyUsersRequest req) {
        return service.deleteManyUsers(req.ids);
    }
}
