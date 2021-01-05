# 通用签名机制实现-SpringBoot版

## 1. 功能点

1. `ContentCachingRequestFilter`实现request body可重读

2. `SignatureInterceptor`拦截实现签名验证

   > 问：为什么不基于Filter实现签名验证？
   >
   > 答：首先Interceptor和Filter均可用于拦截实现签名验证。选择interceptor的首要原因：Filter为Servlet的组件，而Interceptor为Spring MVC的组件。在Spring MVC中Interceptor抛出的异常可直接被`ExceptionHandler`拦截，减少手动处理异常的麻烦
   
3. 签名验证包含时间戳有效性验证，避免重放攻击

## 2. 签名算法

> 参考[阿里云日志服务签名](https://help.aliyun.com/document_detail/29012.html)

### 验证流程

验证流程图如下：[![全验证流程](http://static-aliyun-doc.oss-cn-hangzhou.aliyuncs.com/assets/img/zh-CN/1579930061/p5911.png)](http://static-aliyun-doc.oss-cn-hangzhou.aliyuncs.com/assets/img/zh-CN/1579930061/p5911.png)

验证操作流程如下：

1. 请求端根据API请求内容（包括HTTP Header和Body）生成签名字符串。

2. 请求端使用阿里云的访问密钥对（AccessKeyID和AccessKeySecret），对步骤1生成的签名字符串进行签名，形成该API请求的数字签名。

3. 请求端把API请求内容和数字签名一同发送给服务端。

4. 服务端在接到请求后会重复如上的步骤1和步骤2的工作，并在服务端计算出该请求期望的数字签名。

   **说明** 服务端会在后台取得该请求使用的用户访问密钥对。

5. 服务端用期望的数字签名和请求端发送过来的数字签名做比对，如果一致则认为该请求通过安全验证，否则直接拒绝该请求。

### 签名API请求

为了通过API请求的安全验证，用户需要在客户端对其API请求进行签名（即生成正确的数字签名），并且使用HTTP头Authorization在网络上传输该请求的数字签名。Authorization头示例如下：

```
Authorization = AccessKeyId + ":" + Signature
```

如上格式所示，Authorization头的值包含用户访问密钥对中的AccessKeyId，且与之对应的AccessKeySecret将用于Signature值的构造。下面将详细解释如何构造该Signature值。

1. 准备阿里云访问密钥
   为API请求生成签名，需使用一对访问密钥

2. 生成请求的签名字符串
   日志服务API的签名字符串由HTTP请求中的Method、Header和Body信息一同生成，具体方式如下：

   ```
   SignString = VERB + "\n"
                + CONTENT-MD5 + "\n"
                + CONTENT-TYPE + "\n"
                + DATE + "\n"
                + CanonicalizedResource
   ```

   上面公式中的`\n`表示换行转义字符，加号（`+`）表示字符串连接操作，其他各个部分定义如下所示。

| 名称                    | 说明                                                         | 示例                             |
| :---------------------- | :----------------------------------------------------------- | :------------------------------- |
| `VERB`                  | HTTP请求的方法名称。                                         | PUT、GET、POST等                 |
| `CONTENT-MD5`           | HTTP请求中Body部分的MD5值（必须为小写字符串）。              | d41d8cd98f00b204e9800998ecf8427e |
| `CONTENT-TYPE`          | HTTP请求中Body部分的类型。                                   | application/json; charset=UTF-8  |
| `DATE`                  | HTTP请求中的标准时间戳头（遵循RFC 1123格式，使用GMT标准时间）。 | Thu, 10 Dec 2020 07:53:16 GMT    |
| `CanonicalizedResource` | 由HTTP请求资源构造的字符串（具体构造方法见下面详述）。       | /test/path                       |

​	对于部分无Body的HTTP请求，其**CONTENT-MD5**和**CONTENT-TYPE**两个域为空字符串，这时整个签名字符串的生成方式如下：

```
SignString = VERB + "\n"
             + "\n"
             + "\n"
             + DATE + "\n"
             + CanonicalizedResource
```

- CanonicalizedResource创建规则：

  ```
  CanonicalizedResource = path + "?" + QUERY_STRING
  ```

  构造方式如下：

  1. 将CanonicalizedResource设置为空字符串`""`。
  2. 放入要访问的LOG资源，如/logstores/logstorename（如果没有`logstorename`则可不填写）。
  3. 如果请求包含查询字符串`QUERY_STRING`，则在CanonicalizedResource字符串尾部添加`?`和查询字符串。

  `QUERY_STRING`是URL中请求参数按字典顺序排序后的字符串，其中参数名和值之间用`=`相隔组成字符串，并对参数名-值对按照**字典顺序升序排序**，然后以`&`符号连接构成字符串。其公式化描述如下：

  ```
  QUERY_STRING = "KEY1=VALUE1" + "&" + "KEY2=VALUE2"
  ```

3. 生成请求的数字签名

   完整签名公式如下：

   ```
   Signature = base64(hmac-sha256(UTF8-Encoding-Of(SignString)，AccessKeySecret))
   ```

### 请求签名过程示例

为了帮助您更好地理解整个请求签名的流程，我们用两个示例来演示整个过程。首先，假设您的访问密钥对如下：

```
AccessKeyId = "htw"
AccessKeySecret = "abcd123"
```

##### 1. 示例一

您需要发送如下GET请求，其HTTP请求如下：

```
GET /test/get?b=1&a=2 HTTP 1.1
Date: Tue, 05 Jan 2021 11:38:21 GMT
```

请求生成的签名字符串:

```
GET\n\n\nTue, 05 Jan 2021 11:38:21 GMT\n/test/get?a=2&b=1
```

由于是GET请求，该请求无任何HTTP Body，所以生成的签名字符串中CONTENT-TYPE与CONTENT-MD5域为空字符串。如果以前面指定的AccessKeySecret做签名运算后得到的签名为。签名值计算:

```
4UhrBtdAV+lZTWaPHXFSiPL/Q8+RSSEh139rgu4wXNM=
```

最后发送经数字签名的HTTP请求内容如下：

```
GET /test/get?b=1&a=2 HTTP 1.1
Date: Tue, 05 Jan 2021 11:38:21 GMT
Authorization: htw:4UhrBtdAV+lZTWaPHXFSiPL/Q8+RSSEh139rgu4wXNM=
```

##### 2. 示例二

您需要发送如下GET请求，其HTTP请求如下：

```
POST /test/post?b=1&a=2 HTTP 1.1
Date: Tue, 05 Jan 2021 11:45:58 GMT
Content-Type: application/json; charset=UTF-8
body:
{"hello":"world","test":"哈哈"}
```

请求生成的签名字符串:

```
GET\n87f46297af0a8c97c70bd79b68a854ba\napplication/json; charset=UTF-8\nTue, 05 Jan 2021 11:45:58 GMT\n/test/post?a=2&b=1
```
签名值计算:

```
nPr0eBo0WeGIxnX4ltGAre5JFWCRojpcT6NliSNTxhU=
```

最后发送经数字签名的HTTP请求内容如下：

```
POST /test/post?b=1&a=2 HTTP 1.1
Date: Tue, 05 Jan 2021 11:45:58 GMT
Content-Type: application/json; charset=UTF-8
Authorization: htw:nPr0eBo0WeGIxnX4ltGAre5JFWCRojpcT6NliSNTxhU=
body:
{"hello":"world","test":"哈哈"}
```






