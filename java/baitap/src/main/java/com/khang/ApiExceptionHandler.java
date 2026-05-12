package com.khang;
// file bắt lỗi chung cho API.
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler; //@E từng loaij lõi
import org.springframework.web.bind.annotation.RestControllerAdvice; //@R lỗi full sytem

import java.time.Instant; // time hiện tại của syxtem
import java.util.LinkedHashMap;  //json có trật tự đẹp
import java.util.Map;
import java.util.NoSuchElementException; // lõi khi tìm k tháy dữ liụ

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest( //trả về http response  json,key,value
            IllegalArgumentException ex,  // chứa tt lỗi
            HttpServletRequest request  //lấy url api path để req chạy đúng function
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    } //lỗi input sai, thiếu dữ liệu báo lỗi 400

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(
            SecurityException ex,  //chứa tt lỗi
            HttpServletRequest request
    ) {                                            //throw"k phải là adim"  lấy api bị lỗi api/products/1
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
    } // báo lỗi 403 lỗi quyền truy cập. không phải admin token không đủ quyền

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoSuchElementException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }  //lỗi khi không tìm thấy dữ liệu (product id không tồn tại category không có) trả về HTTP 404

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleServerError(
            IllegalStateException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
    }  //lỗi khi server xử lý sai logic và trả về HTTP 500

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }
} //tạo rep về cho client thống nhất
