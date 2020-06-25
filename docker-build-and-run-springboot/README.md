# 利用Docker构建并运行Springboot应用

### 1. 启动方式

```bash
docker build -t myapp:v1 .
docker run -p8080:8080 myapp:v1
```

### 2. 验证启动成功

```
curl '127.0.0.1:8080/hello'
hello world!
```

