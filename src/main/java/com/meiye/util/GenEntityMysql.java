package com.meiye.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: ryner
 * @Description: 生成Mysql所有表对应的实体类， 注意：用完请删除目录（com.meiye.mysql.entity），勿提交
 * @Date: Created in 22:06 2018/8/15
 * @Modified By:
 */
public class GenEntityMysql {

    private static final GenEntityMysql INSTANCE = new GenEntityMysql();

    private String tableName;// 表名
    private String[] colNames; // 列名数组
    private String[] colTypes; // 列名类型数组
    private int[] colSizes; // 列名大小数组
    private boolean needUtil = true; // 是否需要导入包java.util.*
    private boolean needSql = false; // 是否需要导入包java.sql.*
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String SQL = "SELECT * FROM ";


    // TODO 需要修改的地方
    private static final String URL = "jdbc:mysql://192.168.31.172:8805/store";
    private static final String NAME = "root";
    private static final String PASS = "Hj_1987121";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private String packageOutPath = "com.meiye.model.store";
    private String repostiryPackageOutPath="com.meiye.repository.store";
    private String boPackageOutPath="com.meiye.bo.store";
    private String authorName = "ryne";
    private String[] generateTables = null;
    private String[] excludeProperty=new String[]{"statusFlag",
            "creatorName",
            "creatorId",
            "updatorName",
            "updatorId",
            "serverCreateTime",
            "serverUpdateTime"};

    /**
     * 类的构造方法
     */
    private GenEntityMysql() {
    }

    /**
     * @return
     * @description 生成class的所有内容
     * @author paul
     * @date 2017年8月18日 下午5:30:07
     * @update 2017年8月18日 下午5:30:07
     * @version V1.0
     */
    private String parse() {
        StringBuffer sb = new StringBuffer();
        sb.append("package " + packageOutPath + ";\r\n");
        sb.append("\r\n");
        // 判断是否导入工具包
        if (needUtil) {
            sb.append("import java.util.Date;\r\n");
        }
        if (needSql) {
            sb.append("import java.sql.*;\r\n");
        }
        sb.append("import com.meiye.model.ParentEntity;\n" +
                "import lombok.Data;\n" +
                "\n" +
                "import javax.persistence.Entity;\n" +
                "import javax.persistence.GeneratedValue;\n" +
                "import javax.persistence.GenerationType;\n" +
                "import javax.persistence.Id;\n" +
                "import java.io.Serializable;");

        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * table name:  " + tableName + "\r\n");
        sb.append(" * author name: " + authorName + "\r\n");
        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
        sb.append(" */ \r\n");
        // 实体部分
        sb.append("@Data\n");
        sb.append("@Entity\n");
        sb.append("public class " + getTransStr(tableName, true) + " extends ParentEntity implements Serializable{\r\n\r\n");
        processAllAttrs(sb);// 属性
       // sb.append("\r\n");
        //processAllMethod(sb);// get set方法
        //processToString(sb);
        sb.append("}\r\n");
        return sb.toString();
    }


    private String parseBo() {
        StringBuffer sb = new StringBuffer();
        sb.append("package " + boPackageOutPath + ";\r\n");
        sb.append("\r\n");
        // 判断是否导入工具包
        if (needUtil) {
            sb.append("import java.util.Date;\r\n");
        }
        if (needSql) {
            sb.append("import java.sql.*;\r\n");
        }
        sb.append("import com.alibaba.fastjson.annotation.JSONField;\n" +
                "import com.meiye.bo.ParentBo;\n" +
                "import com.meiye.system.util.WebUtil;\n" +
                "import lombok.Data;\n" +
                "import java.io.Serializable;\n" );
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * table name:  " + tableName + "\r\n");
        sb.append(" * author name: " + authorName + "\r\n");
        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
        sb.append(" */ \r\n");
        // 实体部分
        sb.append("@Data\n");
        sb.append("public class " + getTransStr(tableName, true) + "Bo extends ParentBo implements Serializable{\r\n\r\n");
        processAllAttrsForBo(sb);// 属性
        // sb.append("\r\n");
        //processAllMethod(sb);// get set方法
        //processToString(sb);
        sb.append("}\r\n");
        return sb.toString();
    }


    private String parseRepository() {
        StringBuffer sb = new StringBuffer();
        sb.append("package " + repostiryPackageOutPath + ";\r\n");
        sb.append("\r\n");
        sb.append("import  "+packageOutPath+"."+ getTransStr(tableName, true)+";\n"+
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import org.springframework.data.jpa.repository.Modifying;\n" +
                "import org.springframework.data.jpa.repository.Query;\n" +
                "import org.springframework.stereotype.Repository;\n");
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * table name:  " + tableName + "\r\n");
        sb.append(" * author name: " + authorName + "\r\n");
        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
        sb.append(" */ \r\n");
        // 实体部分
        sb.append("@Repository\n");
        sb.append("public interface " + getTransStr(tableName, true) + "Repository extends JpaRepository<"+getTransStr(tableName, true) +",Long>{\r\n\r\n");
        // sb.append("\r\n");
        //processAllMethod(sb);// get set方法
        //processToString(sb);
        sb.append("}\r\n");
        return sb.toString();
    }

    /**
     * @param sb
     * @description 生成所有成员变量
     * @author paul
     * @date 2017年8月18日 下午5:15:04
     * @update 2017年8月18日 下午5:15:04
     * @version V1.0
     */
    private void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            if(Arrays.asList(excludeProperty).contains(getTransStr(colNames[i], false)))
                continue;
            if(colNames[i].equals("id"))
                sb.append("\t @Id\n" +
                        "\t@GeneratedValue(strategy = GenerationType.IDENTITY)");
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + getTransStr(colNames[i], false) + ";\r\n");
        }
    }

    private void processAllAttrsForBo(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            if(Arrays.asList(excludeProperty).contains(getTransStr(colNames[i], false)))
                continue;
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + getTransStr(colNames[i], false) + ";\r\n");
        }
    }
    /**
     * 重写toString()方法
     * @param sb
     */
//    private void processToString(StringBuffer sb) {
//        sb.append("\t@Override\r\n\tpublic String toString() {\r\n");
//        sb.append("\t\treturn \"" + StringUtils.trans2Hump(tableName, true) + "{\" + \r\n");
//        for (int i = 0; i < colNames.length; i++) {
//            if (i != 0)
//                sb.append("\t\t\t\", ");
//            if (i == 0)
//                sb.append("\t\t\t\"");
//            sb.append(StringUtils.trans2Hump(colNames[i], false) + "=\" + "
//                    + StringUtils.trans2Hump(colNames[i], false)).append(" + \r\n");
//            if (i == colNames.length - 1) {
//                sb.append("\t\t\t\"}\";\r\n");
//            }
//        }
//        sb.append("\t}\r\n");
//    }

    /**
     * @param sb
     * @description 生成所有get/set方法
     * @author paul
     * @date 2017年8月18日 下午5:14:47
     * @update 2017年8月18日 下午5:14:47
     * @version V1.0
     */
    private void processAllMethod(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tpublic void set" + getTransStr(colNames[i], true) + "(" + sqlType2JavaType(colTypes[i]) + " "
                    + getTransStr(colNames[i], false) + "){\r\n");
            sb.append("\t\tthis." + getTransStr(colNames[i], false) + "=" + getTransStr(colNames[i], false) + ";\r\n");
            sb.append("\t}\r\n");
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + getTransStr(colNames[i], true) + "(){\r\n");
            sb.append("\t\treturn " + getTransStr(colNames[i], false) + ";\r\n");
            sb.append("\t}\r\n");
        }
    }

    /**
     * @param str 传入字符串
     * @return
     * @description 将传入字符串的首字母转成大写
     * @author paul
     * @date 2017年8月18日 下午5:12:12
     * @update 2017年8月18日 下午5:12:12
     * @version V1.0
     */
    private String initCap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z')
            ch[0] = (char) (ch[0] - 32);
        return new String(ch);
    }

    /**
     * @return
     * @description 将mysql中表名和字段名转换成驼峰形式
     * @author paul
     * @date 2017年8月18日 下午4:55:07
     * @update 2017年8月18日 下午4:55:07
     * @version V1.0
     */
    private String getTransStr(String before, boolean firstChar2Upper) {
        //不带"_"的字符串,则直接首字母大写后返回
        if (!before.contains("_"))
            return firstChar2Upper ? initCap(before) : before;
        String[] strs = before.split("_");
        StringBuffer after = null;
        if (firstChar2Upper) {
            after = new StringBuffer(initCap(strs[0]));
        } else {
            after = new StringBuffer(strs[0]);
        }
        if (strs.length > 1) {
            for (int i=1; i<strs.length; i++)
                after.append(initCap(strs[i]));
        }
        return after.toString();
    }

    /**
     * @return
     * @description 查找sql字段类型所对应的Java类型
     * @author paul
     * @date 2017年8月18日 下午4:55:41
     * @update 2017年8月18日 下午4:55:41
     * @version V1.0
     */
    private String sqlType2JavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "Boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "Float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        } else if (sqlType.equalsIgnoreCase("timestamp")) {
            return "Date";
        }else if (sqlType.equalsIgnoreCase("date")) {
            return "Date";
        }
        return null;
    }

    /**
     *
     * @description 生成方法
     * @author paul
     * @date 2017年8月18日 下午2:04:20
     * @update 2017年8月18日 下午2:04:20
     * @version V1.0
     * @throws Exception
     */
    private void generate() throws Exception {
        //与数据库的连接
        Connection con;
        PreparedStatement pStemt = null;
        Class.forName(DRIVER);
        con = DriverManager.getConnection(URL, NAME, PASS);
        System.out.println("connect database success...");
        //获取数据库的元数据
        DatabaseMetaData db = con.getMetaData();
        //是否有指定生成表，有指定则直接用指定表，没有则全表生成
        List<String> tableNames = new ArrayList<>();
        if (generateTables == null) {
            //从元数据中获取到所有的表名
            ResultSet rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while (rs.next()) tableNames.add(rs.getString(3));
        } else {
            for (String tableName : generateTables) tableNames.add(tableName);
        }
        String tableSql;
        PrintWriter pw = null;
        for (int j = 0; j < tableNames.size(); j++) {
            tableName = tableNames.get(j);
            tableSql = SQL + tableName;
            pStemt = con.prepareStatement(tableSql);
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();
            colNames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];
            //获取所需的信息
            for (int i = 0; i < size; i++) {
                colNames[i] = rsmd.getColumnName(i + 1);
                colTypes[i] = rsmd.getColumnTypeName(i + 1);
                if (colTypes[i].equalsIgnoreCase("datetime"))
                    needUtil = true;
                if (colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text"))
                    needSql = true;
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }
            //解析生成class的所有内容
            String content = parse();
            //输出生成文件
            File directory = new File("");
            String dirName = directory.getAbsolutePath() + "/src/main/java/" + packageOutPath.replace(".", "/");
            File dir = new File(dirName);
            if (!dir.exists() && dir.mkdirs()) System.out.println("generate dir 【" + dirName + "】");
            String javaPath = dirName + "/" + getTransStr(tableName, true) + ".java";
            FileWriter fw = new FileWriter(javaPath);
            pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();

            String repostoryContent=parseRepository();
            //输出生成文件
            directory = new File("");
            dirName = directory.getAbsolutePath() + "/src/main/java/" + repostiryPackageOutPath.replace(".", "/");
            dir = new File(dirName);
            if (!dir.exists() && dir.mkdirs()) System.out.println("generate dir 【" + dirName + "】");
            javaPath = dirName + "/" + getTransStr(tableName, true) + "Repository.java";
            fw = new FileWriter(javaPath);
            pw = new PrintWriter(fw);
            pw.println(repostoryContent);
            pw.flush();


            String boContent=parseBo();
            //输出生成文件
            directory = new File("");
            dirName = directory.getAbsolutePath() + "/src/main/java/" + boPackageOutPath.replace(".", "/");
            dir = new File(dirName);
            if (!dir.exists() && dir.mkdirs()) System.out.println("generate dir 【" + dirName + "】");
            javaPath = dirName + "/" + getTransStr(tableName, true) + "Bo.java";
            fw = new FileWriter(javaPath);
            pw = new PrintWriter(fw);
            pw.println(boContent);
            pw.flush();

            System.out.println("create class 【" + tableName + "】");
        }
        if (pw != null)
            pw.close();
    }

    /**
     * @param args
     * @description 执行方法
     * @author paul
     * @date 2017年8月18日 下午2:03:35
     * @update 2017年8月18日 下午2:03:35
     * @version V1.0
     */
    public static void main(String[] args) {
        try {
            INSTANCE.generateTables=new String[]{"payment_item_extra"};
            INSTANCE.packageOutPath = "com.meiye.model.pay";
            INSTANCE.repostiryPackageOutPath="com.meiye.repository.pay";
            INSTANCE.boPackageOutPath="com.meiye.bo.pay";
            INSTANCE.generate();
            System.out.println("generate classes success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
