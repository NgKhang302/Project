# Backend Starter

Day la huong backend nen tach rieng tu code cu `java/baitap` nhung giu cung stack:

- Spring Boot
- JPA
- PostgreSQL
- JWT
- Swagger

Package de xuat:

```text
com.songnhip24
  config
  auth
  user
  article
  category
  tag
  author
  media
  analytics
  newsletter
  common
```

Thu tu lam:
- Tao project Spring Boot moi.
- Cau hinh DB PostgreSQL.
- Tao entity `User`, `Article`, `Category`.
- Lam auth admin.
- Lam CRUD bai viet.

## Cau hinh PostgreSQL

Project doc thong tin DB tu bien moi truong:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/songnhip24_db
export DB_USERNAME=postgres
export DB_PASSWORD=mat_khau_that_cua_anh
```

Neu anh da co file `schema.sql` va da tu tao bang trong PostgreSQL, project dang de:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Nghia la Spring chi kiem tra schema co khop hay khong, khong tu sua bang.
