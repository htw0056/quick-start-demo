# Activiti快速入门指南

> 本文翻译自 https://www.activiti.org/quick-start



## 0. 约定

本教程假定您有以下经验：

1. 熟悉Maven和Java
2. 熟悉Java的开发环境

本教程将使用以下变量：

| 变量                         | 描述                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| `$mvnProject`                | Mavne项目根目录地址                                          |
| `$actUnzipedPack`            | 在[http://www.activiti.org/download.html](http://www.activiti.org/download.html)页面下载解压后的文件根目录地址 |
| `$quickStartJavaProjectName` | Java项目的名称，推荐使用`ActivitiDeveloperQuickStart`        |
| `…`                          | 为简洁起见，省略部分信息                                     |
| `$actVer`                    | 当前运行的Activiti版本                                       |



## 1. 介绍

本教程展示了使用Activiti将业务流程管理（BPM）嵌入到您的应用程序中的简单性。 您将构建一个命令行应用程序，并将BPMN逻辑嵌入到该应用程序中。

Activiti拥有先进的流程设计工具，可将复杂的BPM逻辑嵌入到您的应用程序中。 这些工具包括基于Eclipse和基于Web的BPMN编辑器等。 为简洁起见，本快速入门仅使用Activiti的Java API。

> 有关其他Activiti BPM工具的介绍，请参阅：
>
> - 简易入门教程
> - Activiti用户指南

> 本教程：
>
> - 一般来说，不依赖于特定的IDE（尽管它们能做的很少）。 Activiti适用于任何Java友好的IDE。
> - 使用Maven，但Gradle和Ivy等其他构建和依赖管理系统也可以工作。



## 2. 创建初始化Maven项目

创建一个名为“ActivitiDeveloperQuickStart”的Java项目（以下称为`$quickStartJavaProjectName`），其中包含以下Maven依赖项：

**File: $mvnProject/pom.xml**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>$quickStartJavaProjectName</groupId>
  <artifactId>$quickStartJavaProjectName</artifactId>
  <version>0.0.1-SNAPSHOT</version>

  <!-- ... other configurations may exist, such as a build stanza, depending your environment ... -->

  <dependencies>
    <dependency>
      <groupId>org.activiti</groupId>
      <artifactId>activiti-engine</artifactId>
      <version>$actVer</version>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
      <version>1.7.21</version>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.21</version>
    </dependency>
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>1.4.193</version>
    </dependency>
  </dependencies>
</project>
```

当然，`$actVer`需要替换为所下载的Activiti实际版本号。比如，如果您下载的Activiti软件包是“activiti-5.22.0”，那么`$actVer`的值将是5.22.0。

请注意以下依赖项：

- Activiti (org.activiti) – Activiti’s BPM engine
- Database (com.h2database) – the H2 database
- Logging (org.slf4j) – Simple Logging Facade for Java

在创建项目目录时，本教程假定您的目录结构遵循标准的Maven目录结构：

| 路径                             | 描述         |
| -------------------------------- | ------------ |
| `$mvnProject`/src/main/java      | 源代码目录   |
| `$mvnProject`/src/main/resources | 资源目录     |
| `$mvnProject`/src/test/java      | 测试代码目录 |
| `$mvnProject`/src/test/resources | 测试资源目录 |

此时，您应该能够构建空白项目。在继续之前，请确保整体状态为“BUILD SUCCESS”。

```
在$mvnProject目录下执行命令: mvn compile

输出结果如下:

[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building $quickStartJavaProjectName 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ $quickStartJavaProjectName ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 0 resource
[INFO] 
[INFO] --- maven-compiler-plugin:3.5.1:compile (default-compile) @ HelloProcess2 ---
[INFO] Nothing to compile - all classes are up to date
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.592s
[INFO] Finished at: Sun Nov 27 05:09:59 EST 2016
[INFO] Final Memory: 10M/309M
[INFO] ------------------------------------------------------------------------
```

> 注意：
>
> 你的输出可能和上文不同。最明显的，Maven可能需要检索项目依赖项。



## 3. 创建Process Engine

正如前文在maven依赖关系章节中所建议的那样，Activiti利用Simple Logging Facade for Java（slf4j）进行日志记录。在此示例应用程序中，我们将使用log4j管理日志。将log4j.properties文件添加到项目中。

**File: $mvnProject/src/main/resources/log4j.properties**

```properties
log4j.rootLogger=DEBUG, ACT

log4j.appender.ACT=org.apache.log4j.ConsoleAppender
log4j.appender.ACT.layout=org.apache.log4j.PatternLayout
log4j.appender.ACT.layout.ConversionPattern= %d{hh:mm:ss,SSS} [%t] %-5p %c %x - %m%n
```

创建一个包含空main的Java类。

**File: $mvnProject/src/main/java/com/example/OnboardingRequest.java**

```java
package com.example;

public class OnboardingRequest {
  public static void main(String[] args) {
    
  }
}
```

添加创建Process Engine的入口方法。修改OnboardingRequest.java如下：

**File: $mvnProject/src/main/java/com/example/OnboardingRequest.java**

```java
package com.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class OnboardingRequest {
  public static void main(String[] args) {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
      .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
      .setJdbcUsername("sa")
      .setJdbcPassword("")
      .setJdbcDriver("org.h2.Driver")
      .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    ProcessEngine processEngine = cfg.buildProcessEngine();
    String pName = processEngine.getName();
    String ver = ProcessEngine.VERSION;
    System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");
  }
}
```

| 行号  | 注释                                             |
| ----- | ------------------------------------------------ |
| 3-4   | Activiti流程引擎和配置                           |
| 5,9   | 独立环境的配置助手（例如，不使用依赖关系管理器） |
| 9-15  | 使用基于内存的嵌入式数据库h2创建Process Engine   |
| 16-18 | 显示Process Engine配置和Activiti版本             |

> Activiti支持依赖注入
>
> - Activiti专为可以轻松利用依赖注入而构建。有关详细信息，请查看Activiti用户指南。

> Activiti提供多种数据库支持
>
> - 数据库脚本可在“`$actUnzipedPack`/database/create”查看
> - Activiti用户指南（提供多种开发和管理信息）

为了支持IDE和平台独立以及快速入门的简单性，添加以下[“fat jar”](http://stackoverflow.com/questions/19150811/what-is-a-fat-jar)配置：

**File: $mvnProject/pom.xml**

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
...
  <build>
...
    <plugins>
...
      <!-- Maven Assembly Plugin -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>2.4.1</version>
        <configuration>
          <!-- get all project dependencies -->
          <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
          </descriptorRefs>
          <!-- MainClass in mainfest make a executable jar -->
          <archive>
            <manifest>
              <mainClass>com.example.OnboardingRequest</mainClass>
            </manifest>
          </archive>
        </configuration>
        <executions>
          <execution>
            <id>make-assembly</id>
            <!-- bind to the packaging phase -->
            <phase>package</phase>
            <goals>
              <goal>single</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
...
    </plugins>
...
  </build>
...
</project>
```

```
对项目进行打包,在$mvnProject目录下执行命令: mvn package
```

```
输出结果如下：

[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building $quickStartJavaProjectName 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ $quickStartJavaProjectName ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 1 resource
[INFO] 
[INFO] --- maven-compiler-plugin:3.5.1:compile (default-compile) @ HelloProcess2 ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ HelloProcess2 ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 0 resource
[INFO] 
[INFO] --- maven-compiler-plugin:3.5.1:testCompile (default-testCompile) @ HelloProcess2 ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ HelloProcess2 ---
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ HelloProcess2 ---
[INFO] Building jar: $mvnProject/target/$quickStartJavaProjectName-0.0.1-SNAPSHOT.jar
[INFO] META-INF/maven/$quickStartJavaProjectName/HelloProcess2/pom.xml already added, skipping
[INFO] META-INF/maven/$quickStartJavaProjectName/$quickStartJavaProjectName/pom.properties already added, skipping
[INFO] 
[INFO] --- maven-assembly-plugin:2.4.1:single (make-assembly) @ $quickStartJavaProjectName ---
[INFO] Building jar: $mvnProject/target/$quickStartJavaProjectName-0.0.1-SNAPSHOT-jar-with-dependencies.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.029s
[INFO] Finished at: Sun Nov 27 07:23:43 EST 2016
[INFO] Final Memory: 33M/702M
[INFO] ------------------------------------------------------------------------
```

> 注意：
>
> - 你的输出可能看起来会有不同。重要的输出是“BUILD SUCCESS”的上一行，显示了打jar包成功：[INFO] Building jar: $mvnProject/target/$quickStartJavaProjectName-0.0.1-SNAPSHOT-jar-with-dependencies.jar

支持IDE和平台独立以及本快速入门的简单性，从命令行运行Java程序，如下：

```
执行命令: java -jar target/ActivitiDeveloperQuickStart-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

```
输出结果:

11:45:32,849 [main] DEBUG org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl  - initializing datasource to db: jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000
11:45:32,856 [main] DEBUG org.apache.ibatis.logging.LogFactory  - Logging initialized using 'class org.apache.ibatis.logging.slf4j.Slf4jImpl' adapter.

...

11:45:33,777 [main] DEBUG org.activiti.engine.impl.db.DbSqlSession  - SQL: create table ACT_PROCDEF_INFO ( 
ID_ varchar(64) not null, 
PROC_DEF_ID_ varchar(64) not null, 
REV_ integer, 
INFO_JSON_ID_ varchar(64), 
primary key (ID_) 
)

...

11:45:33,835 [main] DEBUG org.activiti.engine.impl.db.DbSqlSession  - activiti db schema create for component identity successful
11:45:33,835 [main] DEBUG org.activiti.engine.impl.db.DbSqlSession  - flush summary: 0 insert, 0 update, 0 delete.
11:45:33,835 [main] DEBUG org.activiti.engine.impl.db.DbSqlSession  - now executing flush...
11:45:33,835 [main] DEBUG org.activiti.engine.impl.cfg.standalone.StandaloneMybatisTransactionContext  - firing event committing...
11:45:33,835 [main] DEBUG org.activiti.engine.impl.cfg.standalone.StandaloneMybatisTransactionContext  - committing the ibatis sql session...
11:45:33,835 [main] DEBUG org.activiti.engine.impl.cfg.standalone.StandaloneMybatisTransactionContext  - firing event committed...
11:45:33,836 [main] DEBUG org.apache.ibatis.transaction.jdbc.JdbcTransaction  - Resetting autocommit to true on JDBC Connection [conn0: url=jdbc:h2:mem:activiti user=SA]
11:45:33,836 [main] DEBUG org.apache.ibatis.transaction.jdbc.JdbcTransaction  - Closing JDBC Connection [conn0: url=jdbc:h2:mem:activiti user=SA]
11:45:33,836 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource  - Returned connection 667346055 to pool.
11:45:33,836 [main] DEBUG org.activiti.engine.impl.interceptor.LogInterceptor  - --- SchemaOperationsProcessEngineBuild finished --------------------------------------------------------
11:45:33,836 [main] DEBUG org.activiti.engine.impl.interceptor.LogInterceptor  - 

11:45:33,836 [main] INFO  org.activiti.engine.impl.ProcessEngineImpl  - ProcessEngine default created
ProcessEngine [default] Version: [$actVer]
```

> 注意：
>
> - 最关键的输出是打印出的Activiti版本： `$actVer` ，它应该跟你的maven配置有关

你也可以在IDE中运行相同的程序。比如，在Eclipse中，你可以选择OnboardingRequest.java文件，然后执行`Run As > Java Application`。如果从IDE中运行程序，结果应该相同（通常显示在IDE的控制台视图中）。

至此，您已经在这个简单的Java程序中成功嵌入了Activiti的BPM引擎。

## 4. 发布Process Definition

我们现在准备为Activiti Engine添加额外的BPM逻辑。

为此，正如我们的OnboardingRequest Java类的名称所示，我们将使用简单的Onboarding过程。在这个例子中，我们将输入数据。然后，如果经验年数超过3，则将发布个性化入职欢迎消息的任务。在该任务中，用户将手动将数据输入到虚拟后端系统中。如果经验是3年或更短，那么简单地，自动将数据与虚拟后端系统集成。

Activiti的Process Engine符合BPMN 2.0标准。如果采用图形化展示，那么上面的过程可以这样建模：



![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/t1.png)



> 这个例子非常简单。并且根据要求，可以通过多种不同的方式对其进行建模。Activiti可以编排简单的流程，但也可以处理非常复杂的流程，包括数十个，数百个甚至数千个步骤。

上面的可视化过程模型的基础是BPMN的XML结构。在该例子中，XML文档是onboarding.bpmn20.xml。本教程不会深入讲解XML BPMN结构的细节，而是专注于针对Activiti API进行开发并将Activiti嵌入到应用程序中。为了支持下面的逻辑，以下表格是BPMN中图形形状和基础XML中编码的定义逻辑的摘要：

| BPMN形状                                                     | Onboarding.bpmn20.xml 行号 | 注释                                                         |
| ------------------------------------------------------------ | -------------------------- | ------------------------------------------------------------ |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/2.png) | 8                          | 事件开始                                                     |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/3.png) | 9-15                       | 用户任务收集2个表单属性：“fullName”和“yearsOfExperience”。请注意，第9行的候选组设置为“管理员”。 |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/4.png) | 21-27                      | 用户任务收集1表单属性：“personalWelcomeTime”。请注意，第22行的候选组设置为“管理员”。 |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/5.png) | 31-35                      | 脚本任务表示自动数据输入到虚拟后端。请注意，虽然简单，但这是一个简单的脚本可以设置一个过程变量autoWelcomeTime（第34-35行）:`var dateAsString = new Date().toString(); execution.setVariable("autoWelcomeTime", dateAsString);` |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/6.png) | 18                         | 定义“Years of Experience”排他网关。 （决定执行其中一条或另一条路径。） |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/6.png) | 37-39                      | 表示">3"时的逻辑，使用变量yearsOfExperience :`${yearsOfExperience > 3}` |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/6.png) | 18,36                      | 在排他网关第18行，这里指向默认的执行路径”automatedIntroPath" (line 36)，表示不满足条件">3"时的执行路径 |
| ![Alt text](https://github.com/htw0056/Activiti-quick-start-guide/raw/master/picture/7.png) | 298                        | 事件结束                                                     |

> 有关BPMN及其在Activiti中的使用的更多信息，请参阅Activiti用户指南中的各个部分。

下载下文中整个XML结构onboarding.bpmn20.xml文件，并将onboarding.bpmn20.xml文件复制到路径`$mvnProject`/src/main/resources/ 下。

**File: $mvnProject/src/main/resources/onboarding.bpmn20.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/processdef">
  <process id="onboarding" name="Onboarding" isExecutable="true">
    <startEvent id="startOnboarding" name="Start" activiti:initiator="initiator"></startEvent>
    <userTask id="enterOnboardingData" name="Enter Data" activiti:assignee="${initiator}" activiti:candidateGroups="managers">
      <extensionElements>
        <activiti:formProperty id="fullName" name="Full Name" type="string"></activiti:formProperty>
        <activiti:formProperty id="yearsOfExperience" name="Years of Experience" type="long" required="true"></activiti:formProperty>
      </extensionElements>
    </userTask>
    <sequenceFlow id="sid-1337EA98-7364-4198-B5D9-30F5341D6918" sourceRef="startOnboarding" targetRef="enterOnboardingData"></sequenceFlow>
    <exclusiveGateway id="decision" name="Years of Experience" default="automatedIntroPath"></exclusiveGateway>
    <sequenceFlow id="sid-42BE5661-C3D5-4DE6-96F5-73D34822727A" sourceRef="enterOnboardingData" targetRef="decision"></sequenceFlow>
    <userTask id="personalizedIntro" name="Personalized Introduction and Data Entry" activiti:assignee="${initiator}" activiti:candidateGroups="managers">
      <extensionElements>
        <activiti:formProperty id="personalWelcomeTime" name="Personal Welcome Time" type="date" datePattern="MM-dd-yyyy hh:mm"></activiti:formProperty>
      </extensionElements>
    </userTask>
    <endEvent id="endOnboarding" name="End"></endEvent>
    <sequenceFlow id="sid-37A73ACA-2E23-400B-96F3-71F77738DAFA" sourceRef="automatedIntro" targetRef="endOnboarding"></sequenceFlow>
    <scriptTask id="automatedIntro" name="Generic and Automated Data Entry" scriptFormat="javascript" activiti:autoStoreVariables="false">
      <script><![CDATA[var dateAsString = new Date().toString();
execution.setVariable("autoWelcomeTime", dateAsString);]]></script>
    </scriptTask>
    <sequenceFlow id="automatedIntroPath" sourceRef="decision" targetRef="automatedIntro"></sequenceFlow>
    <sequenceFlow id="personalizedIntroPath" name="&gt;3" sourceRef="decision" targetRef="personalizedIntro">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${yearsOfExperience > 3}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="sid-BA6F061B-47B6-428B-8CE6-739244B14BD6" sourceRef="personalizedIntro" targetRef="endOnboarding"></sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_onboarding">
    <bpmndi:BPMNPlane bpmnElement="onboarding" id="BPMNPlane_onboarding">
      <bpmndi:BPMNShape bpmnElement="startOnboarding" id="BPMNShape_startOnboarding">
        <omgdc:Bounds height="30.0" width="30.0" x="155.0" y="145.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="enterOnboardingData" id="BPMNShape_enterOnboardingData">
        <omgdc:Bounds height="80.0" width="100.0" x="240.0" y="120.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="decision" id="BPMNShape_decision">
        <omgdc:Bounds height="40.0" width="40.0" x="385.0" y="140.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="personalizedIntro" id="BPMNShape_personalizedIntro">
        <omgdc:Bounds height="80.0" width="100.0" x="519.0" y="15.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="endOnboarding" id="BPMNShape_endOnboarding">
        <omgdc:Bounds height="28.0" width="28.0" x="725.0" y="165.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="automatedIntro" id="BPMNShape_automatedIntro">
        <omgdc:Bounds height="80.0" width="100.0" x="520.0" y="255.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="sid-37A73ACA-2E23-400B-96F3-71F77738DAFA" id="BPMNEdge_sid-37A73ACA-2E23-400B-96F3-71F77738DAFA">
        <omgdi:waypoint x="570.0" y="255.0"></omgdi:waypoint>
        <omgdi:waypoint x="570.0" y="179.0"></omgdi:waypoint>
        <omgdi:waypoint x="725.0" y="179.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-1337EA98-7364-4198-B5D9-30F5341D6918" id="BPMNEdge_sid-1337EA98-7364-4198-B5D9-30F5341D6918">
        <omgdi:waypoint x="185.0" y="160.0"></omgdi:waypoint>
        <omgdi:waypoint x="240.0" y="160.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="automatedIntroPath" id="BPMNEdge_automatedIntroPath">
        <omgdi:waypoint x="405.0" y="180.0"></omgdi:waypoint>
        <omgdi:waypoint x="405.0" y="295.0"></omgdi:waypoint>
        <omgdi:waypoint x="520.0" y="295.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="personalizedIntroPath" id="BPMNEdge_personalizedIntroPath">
        <omgdi:waypoint x="405.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="405.0" y="55.0"></omgdi:waypoint>
        <omgdi:waypoint x="519.0" y="55.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-42BE5661-C3D5-4DE6-96F5-73D34822727A" id="BPMNEdge_sid-42BE5661-C3D5-4DE6-96F5-73D34822727A">
        <omgdi:waypoint x="340.0" y="160.0"></omgdi:waypoint>
        <omgdi:waypoint x="385.0" y="160.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-BA6F061B-47B6-428B-8CE6-739244B14BD6" id="BPMNEdge_sid-BA6F061B-47B6-428B-8CE6-739244B14BD6">
        <omgdi:waypoint x="619.0" y="55.0"></omgdi:waypoint>
        <omgdi:waypoint x="739.0" y="55.0"></omgdi:waypoint>
        <omgdi:waypoint x="739.0" y="165.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>
```

在OnboardingRequest.java 添加如下代码：

**File: $mvnProject/src/main/java/com/example/OnboardingRequest.java**

```java
package com.example;

import java.text.ParseException;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

public class OnboardingRequest {
  public static void main(String[] args) throws ParseException {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
        .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
        .setJdbcUsername("sa")
        .setJdbcPassword("")
        .setJdbcDriver("org.h2.Driver")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    ProcessEngine processEngine = cfg.buildProcessEngine();
    String pName = processEngine.getName();
    String ver = ProcessEngine.VERSION;
    System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");

    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment = repositoryService.createDeployment()
        .addClasspathResource("onboarding.bpmn20.xml").deploy();
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
        .deploymentId(deployment.getId()).singleResult();
    System.out.println(
        "Found process definition [" 
            + processDefinition.getName() + "] with id [" 
            + processDefinition.getId() + "]");
  }
}
```

| 添加行 | 解释                                                  |
| ------ | ----------------------------------------------------- |
| 25-27  | 加载提供的BPMN模型并将其部署到Activiti Process Engine |
| 28-33  | 检索已部署的模型，证明它位于Activiti存储库中          |

```
对项目进行打包,执行命令: mvn package
```

```
运行命令: java -jar target/ActivitiDeveloperQuickStart-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

```
输出结果：

...

02:01:19,277 [main] INFO  org.activiti.engine.impl.ProcessEngineImpl  - ProcessEngine default created
processEngine [default] version: [5.22.0.0]

...

02:01:19,327 [main] DEBUG org.activiti.engine.impl.bpmn.deployer.BpmnDeployer  - Processing deployment null
02:01:19,327 [main] INFO  org.activiti.engine.impl.bpmn.deployer.BpmnDeployer  - Processing resource onboarding.bpmn20.xml
02:01:19,444 [main] DEBUG org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler  - Parsing process 

...

02:01:21,696 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource  - Returned connection 667346055 to pool.
02:01:21,696 [main] DEBUG org.activiti.engine.impl.interceptor.LogInterceptor  - --- DeployCmd finished --------------------------------------------------------

...

02:01:21,696 [main] DEBUG org.activiti.engine.impl.interceptor.LogInterceptor  - --- starting ProcessDefinitionQueryImpl --------------------------------------------------------

...

02:01:21,710 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource  - Returned connection 667346055 to pool.
02:01:21,710 [main] DEBUG org.activiti.engine.impl.interceptor.LogInterceptor  - --- ProcessDefinitionQueryImpl finished --------------------------------------------------------
02:01:21,710 [main] DEBUG org.activiti.engine.impl.interceptor.LogInterceptor  - 

Found process definition [Onboarding] with id [onboarding:1:4]
```

> 注意：
>
> - 输出结果中最重要的内容是：onboarding:1:4，它表示流程名称“Onboarding”和唯一流程ID

接下来，你可以运行发布后的Onboarding流程。



## 5. 运行流程实例

部署流程可以使用Activiti API来启动，运行，查看历史记录以及以其他方式管理流程实例。本教程使用Java代码运行流程实例。

> 有关使用Restful Services管理流程实例的示例，请参阅Sample Onboarding Quick Start。

```
将Activiti的日志记录级别从DEBUG设置为WARN，如下面的第1行所示
```

**File: $mvnProject/src/main/resources/log4j.properties**

```
log4j.rootLogger=WARN, ACT

log4j.appender.ACT=org.apache.log4j.ConsoleAppender
log4j.appender.ACT.layout=org.apache.log4j.PatternLayout
log4j.appender.ACT.layout.ConversionPattern= %d{hh:mm:ss,SSS} [%t] %-5p %c %x - %m%n
```

> 请注意：以下示例代码说明了Activiti基于标准的可嵌入Process Engine的灵活性和强大功能。但是，代码在错误处理，代码组织和一般设计方面并不代表最佳实践。相反，该代码目的是为了快速说明许多想法，以便熟悉Activiti的能力。请根据您自己的需要重新考虑以下示例。

```
添加如下代码到OnboardingRequest.java
```

**File: $mvnProject/src/main/java/com/example/OnboardingRequest.java**

```java
package com.example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class OnboardingRequest {
  public static void main(String[] args) throws ParseException {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
        .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
        .setJdbcUsername("sa")
        .setJdbcPassword("")
        .setJdbcDriver("org.h2.Driver")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    ProcessEngine processEngine = cfg.buildProcessEngine();
    String pName = processEngine.getName();
    String ver = ProcessEngine.VERSION;
    System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");

    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment = repositoryService.createDeployment()
        .addClasspathResource("onboarding.bpmn20.xml").deploy();
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
        .deploymentId(deployment.getId()).singleResult();
    System.out.println(
        "Found process definition [" 
            + processDefinition.getName() + "] with id [" 
            + processDefinition.getId() + "]");
  
    RuntimeService runtimeService = processEngine.getRuntimeService();
    ProcessInstance processInstance = runtimeService
        .startProcessInstanceByKey("onboarding");
    System.out.println("Onboarding process started with process instance id [" 
        + processInstance.getProcessInstanceId()
        + "] key [" + processInstance.getProcessDefinitionKey() + "]");
    
    TaskService taskService = processEngine.getTaskService();
    FormService formService = processEngine.getFormService();
    HistoryService historyService = processEngine.getHistoryService();

    Scanner scanner = new Scanner(System.in);
    while (processInstance != null && !processInstance.isEnded()) {
      List<Task> tasks = taskService.createTaskQuery()
          .taskCandidateGroup("managers").list();
      System.out.println("Active outstanding tasks: [" + tasks.size() + "]");
      for (int i = 0; i < tasks.size(); i++) {
        Task task = tasks.get(i);
        System.out.println("Processing Task [" + task.getName() + "]");
        Map<String, Object> variables = new HashMap<String, Object>();
        FormData formData = formService.getTaskFormData(task.getId());
        for (FormProperty formProperty : formData.getFormProperties()) {
          if (StringFormType.class.isInstance(formProperty.getType())) {
            System.out.println(formProperty.getName() + "?");
            String value = scanner.nextLine();
            variables.put(formProperty.getId(), value);
          } else if (LongFormType.class.isInstance(formProperty.getType())) {
            System.out.println(formProperty.getName() + "? (Must be a whole number)");
            Long value = Long.valueOf(scanner.nextLine());
            variables.put(formProperty.getId(), value);
          } else if (DateFormType.class.isInstance(formProperty.getType())) {
            System.out.println(formProperty.getName() + "? (Must be a date m/d/yy)");
            DateFormat dateFormat = new SimpleDateFormat("m/d/yy");
            Date value = dateFormat.parse(scanner.nextLine());
            variables.put(formProperty.getId(), value);
          } else {
            System.out.println("<form type not supported>");
          }
        }
        taskService.complete(task.getId(), variables);

        HistoricActivityInstance endActivity = null;
        List<HistoricActivityInstance> activities = 
            historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(processInstance.getId()).finished()
            .orderByHistoricActivityInstanceEndTime().asc()
            .list();
        for (HistoricActivityInstance activity : activities) {
          if (activity.getActivityType() == "startEvent") {
            System.out.println("BEGIN " + processDefinition.getName() 
                + " [" + processInstance.getProcessDefinitionKey()
                + "] " + activity.getStartTime());
          }
          if (activity.getActivityType() == "endEvent") {
            // Handle edge case where end step happens so fast that the end step
            // and previous step(s) are sorted the same. So, cache the end step 
            //and display it last to represent the logical sequence.
            endActivity = activity;
          } else {
            System.out.println("-- " + activity.getActivityName() 
                + " [" + activity.getActivityId() + "] "
                + activity.getDurationInMillis() + " ms");
          }
        }
        if (endActivity != null) {
          System.out.println("-- " + endActivity.getActivityName() 
                + " [" + endActivity.getActivityId() + "] "
                + endActivity.getDurationInMillis() + " ms");
          System.out.println("COMPLETE " + processDefinition.getName() + " ["
                + processInstance.getProcessDefinitionKey() + "] " 
                + endActivity.getEndTime());
        }
      }
      // Re-query the process instance, making sure the latest state is available
      processInstance = runtimeService.createProcessInstanceQuery()
          .processInstanceId(processInstance.getId()).singleResult();
    }
    scanner.close();
  }
}
```

| 添加行              | 解释                                                         |
| ------------------- | ------------------------------------------------------------ |
| 12-13, 17-21, 28-39 | 导入Activiti API中用于流程管理的服务                         |
| 54-59               | 启动Onboarding流程的实例                                     |
| 61-62, 67, 71-93    | 从符合“管理员”角色和完成任务的任务中收集命令行输入           |
| 23-25, 76,80, 84    | 基于流程模型中定义的表单属性类型，提示用户输入特定于类型的输入 |
| 63, 95-125          | 显示流程历史记录                                             |
| 23-25               | 检索已部署的模型，证明它位于Activiti存储库中                 |

```
打包项目:mvn package
运行命令: java -jar target/ActivitiDeveloperQuickStart-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

```
输出结果：
ProcessEngine [default] Version: [5.22.0.0]
Found process definition [Onboarding] with id [onboarding:1:4]
Onboarding process started with process instance id [5] key [onboarding]
Active outstanding tasks: [1]
Processing Task [Enter Data]
Full Name?
John Doe
Years of Experience? (Must be a whole number)
2
BEGIN Onboarding [onboarding] Sun Nov 27 21:36:21 EST 2016
-- Start [startOnboarding] 4 ms
-- Enter Data [enterOnboardingData] 16855 ms
-- Years of Experience [decision] 3 ms
-- Generic and Automated Data Entry [automatedIntro] 322 ms
-- End [endOnboarding] 0 ms
COMPLETE Onboarding [onboarding] Sun Nov 27 21:36:38 EST 2016
```

> 当years-experience输入为2时，在经过“Years of Experience”判断后，选择执行脚本任务 “Generic and Automated Data Entry”并结束流程。



```
再次运行命令: java -jar target/ActivitiDeveloperQuickStart-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

```
ProcessEngine [default] Version: [5.22.0.0]
Found process definition [Onboarding] with id [onboarding:1:4]
Onboarding process started with process instance id [5] key [onboarding]
Active outstanding tasks: [1]
Processing Task [Enter Data]
Full Name?
John Doe
Years of Experience? (Must be a whole number)
5
BEGIN Onboarding [onboarding] Sun Nov 27 21:39:26 EST 2016
-- Start [startOnboarding] 5 ms
-- Enter Data [enterOnboardingData] 7810 ms
-- Years of Experience [decision] 2 ms
Active outstanding tasks: [1]
Processing Task [Personalized Introduction and Data Entry]
Personal Welcome Time? (Must be a date m/d/yy)
12/9/16  
BEGIN Onboarding [onboarding] Sun Nov 27 21:39:26 EST 2016
-- Start [startOnboarding] 5 ms
-- Enter Data [enterOnboardingData] 7810 ms
-- Years of Experience [decision] 2 ms
-- Personalized Introduction and Data Entry [personalizedIntro] 20231 ms
-- End [endOnboarding] 0 ms
COMPLETE Onboarding [onboarding] Sun Nov 27 21:39:54 EST 2016
```

> 当years-experience输入为5时，在经过“Years of Experience”判断后，执行“Personalized Introduction and Data Entry”流程并结束流程。

本示例虽然简单，但此嵌入式示例展示了您的应用程序如何将流程逻辑外部化为基于标准的建模和代码友好的开发模型。



## 6. 编写Service Task

如前所述，我们的流程有一个任务“Generic and Automated Data Entry”（一种虚拟的后端服务），当入职年数不超过3时，执行该活动。这是一个“脚本任务”。在这种情况下，执行了一小段Javascript代码，来表示系统处理步骤。现在，我们将把这个脚本任务移植到Java中，以演示利用Java如何满足各种流程要求。

创建一个新的Java类，如下所示：

**File: $mvnProject/src/main/java/com/example/AutomatedDataDelegate.java**

```
package com.example;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AutomatedDataDelegate implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    Date now = new Date();
    execution.setVariable("autoWelcomeTime", now);
    System.out.println("Faux call to backend for [" 
    + execution.getVariable("fullName") + "]");
  }

}
```

| 添加行 | 解释                                                         |
| ------ | ------------------------------------------------------------ |
| 13     | 设置过程变量。在该例子中，设置变量autoWelcomeTime为当前时间。 |
| 14     | 输出流程变量                                                 |

将脚本任务更改为指向AutomatedDataDelegate的服务任务。

**File: $mvnProject/src/main/resources/onboarding.bpmn20.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/processdef">
  <process id="onboarding" name="Onboarding" isExecutable="true">
...
    <scriptTask id="automatedIntro" name="Generic and Automated Data Entry" scriptFormat="javascript" activiti:autoStoreVariables="false">
      <script><![CDATA[var dateAsString = new Date().toString();
execution.setVariable("autoWelcomeTime", dateAsString);]]></script>
    </scriptTask>
...
```

改为:

```
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/processdef">
  <process id="onboarding" name="Onboarding" isExecutable="true">
...
   <serviceTask id="automatedIntro" name="Generic and Automated Data Entry" activiti:class="com.example.AutomatedDataDelegate"></serviceTask>
```

```
打包项目:mvn package
运行命令: java -jar target/ActivitiDeveloperQuickStart-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

```
ProcessEngine [default] Version: [5.22.0.0]
Found process definition [Onboarding] with id [onboarding:1:4]
Onboarding process started with process instance id [5] key [onboarding]
Active outstanding tasks: [1]
Processing Task [Enter Data]
Full Name?
John Doe
Years of Experience? (Must be a whole number)
3
Faux call to backend for [John Doe]
BEGIN Onboarding [onboarding] Sun Nov 27 22:57:32 EST 2016
-- Start [startOnboarding] 4 ms
-- Enter Data [enterOnboardingData] 10153 ms
-- Years of Experience [decision] 2 ms
-- Generic and Automated Data Entry [automatedIntro] 0 ms
-- End [endOnboarding] 0 ms
COMPLETE Onboarding [onboarding] Sun Nov 27 22:57:42 EST 2016
```

> 注意：
>
> - 可以观察到输出`Faux call to backend for [John Doe]`,这显示了访问先前设置的过程变量。

## 7. 结论

这个简单的例子展示了在应用程序中添加流程驱动抽象的简单性和强大功能。



> 源码[下载](https://github.com/htw0056/Activiti-quick-start-guide)