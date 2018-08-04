# excel-spring-boot-starter

## introduction
整合Apache POI，使用SXSSFWorkBook对象生成Excel文件

## how to use

1. 下载源码，并**安装到本地仓库**，在项目中引入依赖

    ```
    <dependency>
        <groupId>com.itmajing</groupId>
        <artifactId>excel-spring-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    ```
    
2. 新建resources/excel目录，在excel目录下新建如下格式的yaml文件  

    ```
    header:             文件标题
    columns:
     - name:            列名称
       key:             字段名
       converter:       转换器(可选)
       width:           列宽(可选)，根据列名称自适应宽度
       wrap:            换行(可选)，默认自动换行
       align:           对齐(可选)，默认居左对齐
    ```
    请使用合适的文件名，文件名会解析成模板名称，如user.yml, 模板名称为user  
    ```
    #demo
    header: 用户
    columns:
     - name: 编号
       key: id
     - name: 用户名
       key: username
     - name: 密码
       key: password
       width: 10
    ```
    
3. 生成excel文件  
    SimpleGenerator API说明
    ```
    //生成excel文件
    File generate();
    
    //生成excel到指定文件
    void generate(File file);
    
    //写入数据到工作簿
    void writeData(Collection<T> data);
    ```
    ```
    //demo
    SimpleGenerator<User> generator = new SimpleGenerator<>("user");//实例化
    List<User> users = this.initUser();
    generator.writeData(users);//此处可以使用分页查询，循环多次写入数据
    generator.generate();//所有数据写入完成后，调用该方法生成excel文件
    ```
## show custom info
如果需要显示自定义信息，可重写writeInfo方法

