package generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author Gary Fu
 * @date 2021/9/28 20:23
 */
public class SagaDatabaseGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/transaction_db", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("Gary Fu") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("c:/generate"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.mengstudy.boot.tx.saga.server.entity") // 设置父包名
                            .moduleName("transaction") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "c:/generate")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_simple_transaction", "t_simple_sub_transaction", "t_simple_transaction_log") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
