package com.khang.dto;
// file CRUD user
import java.util.List;

public class UserCrudDTO {

    // POST: tạo 1 user
    public static class CreateUserRequest {
        public String name;
        public String email;
    }

    // POST: tạo nhiều user
    public static class CreateManyUsersRequest {
        public List<CreateUserRequest> users;
    }

    // PATCH: update từng phần
    public static class UpdateUserRequest {
        public String name;
        public String email;
    }

    //  DELETE: xóa nhiều user
    public static class DeleteManyUsersRequest {
        public List<Integer> ids;
    }
}
// DTO chỉ để req rep
// static không cần tạo UserCrudDTO, truy cập trực tiếp class con