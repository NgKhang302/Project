#!/bin/sh
set -e

# Volume uploads có thể được tạo/mount sau khi image build, hoặc còn sở hữu bởi
# user cũ từ lần chạy trước -> chown lại mỗi lần start rồi mới hạ quyền xuống
# user "spring" (không chạy app bằng root).
chown -R spring:spring /app/uploads

exec su-exec spring java -jar app.jar
