package com.neuedu.test;

import com.neuedu.pojo.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManger {
    private Scanner input=new Scanner(System.in);
    public void show() {
        while (true) {
            System.out.println("欢迎使用学生管理系统");
            System.out.println("1  增加学生");
            System.out.println("2  删除学生");
            System.out.println("3  修改学生");
            System.out.println("4  查找学生");
            System.out.println("5  退出系统");
            System.out.println("输入编号选择功能");
            int key = input.nextInt();
            switch (key) {
                case 1:
                    System.out.println("输入学号");
                    int sno = input.nextInt();
                    System.out.println("输入姓名");
                    String sname = input.next();
                    System.out.println("输入年龄");
                    int sage = input.nextInt();
                    System.out.println("输入性别");
                    String ssex = input.next();
                    System.out.println("输入身高");
                    int sheight = input.nextInt();
                    System.out.println("输入体重");
                    int sweidht = input.nextInt();
                    System.out.println("输入生日");
                    String sbirthday = input.next();
                    System.out.println("输入密码");
                    String spassword = input.next();
                    excuteUpdate("insert into student(sno,sname,sage,ssex,sheight,sweight,sbirthday,spassword) values(?,?,?,?,?,?,?,?)", sno, sname, sage, ssex, sheight, sweidht, sbirthday, spassword);
                    System.out.println("添加成功");
                    break;
                case 2:
                    System.out.println("输入要删除的学生的编号");
                    int id = input.nextInt();
                    excuteUpdate("delete from student where id=?", id);
                    System.out.println("删除成功");
                    break;
                case 3:
                    System.out.println("输入要修改的学生的编号");
                    int id1 = input.nextInt();
                    System.out.println("输入要修改的学号");
                    int sno1 = input.nextInt();
                    System.out.println("输入要修改的姓名");
                    String sname1 = input.next();
                    System.out.println("输入要修改的年龄");
                    int sage1 = input.nextInt();
                    System.out.println("输入要修改的性别");
                    String ssex1 = input.next();
                    System.out.println("输入要修改的身高");
                    int sheight1 = input.nextInt();
                    System.out.println("输入要修改的体重");
                    int sweight1 = input.nextInt();
                    System.out.println("输入要修改的生日");
                    String sbirthday1 = input.next();
                    System.out.println("输入要修改的密码");
                    String spassword1 = input.next();
                    excuteUpdate("update  student set sno=" + sno1 + ",sname='" + sname1 + "',sage=" + sage1 + ",ssex='" + ssex1 + "',sheight=" + sheight1 + ",sweight=" + sweight1 + ",sbirthday='" + sbirthday1 + "',spassword='" + spassword1 + "' where id=?", id1);
                    System.out.println("修改成功");
                    break;
                case 4:
                    List<Student> list = StudentManger.excuteQuery("select * from student", new RowMap<Student>() {
                        @Override
                        public Student rowMaping(ResultSet resultSet) {
                            Student students = new Student();
                            try {
                                int id = resultSet.getInt("id");
                                int sno = resultSet.getInt("sno");
                                String sname = resultSet.getString("sname");
                                int sage = resultSet.getInt("sage");
                                String ssex = resultSet.getString("ssex");
                                int sheight = resultSet.getInt("sheight");
                                int sweight = resultSet.getInt("sweight");
                                String sbirthday = resultSet.getString("sbirthday");
                                String spassword = resultSet.getString("spassword");
                                int sflag = resultSet.getInt("sflag");
                                students.setId(id);
                                students.setSno(sno);
                                students.setSname(sname);
                                students.setSage(sage);
                                students.setSsex(ssex);
                                students.setSheight(sheight);
                                students.setSweight(sweight);
                                students.setSbirthday(sbirthday);
                                students.setSpassword(spassword);
                                students.setSflag(sflag);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            return students;
                        }
                    });
                    for (Student s : list
                    ) {
                        System.out.println(s);
                    }
                    break;
                case 5:
                    break;
            }
        }
    }
    //加载驱动
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static final String URL = "jdbc:mysql://localhost:3306/studentmanager?useUnicode=true&characterEncoding=utf8";
    private static final String ROOT = "root";
    private static final String PASSWOED = "root";

    private static Connection conn() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, ROOT, PASSWOED);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    private static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //增删改
    public static int excuteUpdate(String sql, Object... objects) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        try {
            connection = conn();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                preparedStatement.setObject(i + 1, objects[i]);
            }
            result = preparedStatement.executeUpdate();
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, preparedStatement, null);
        }
        return result;
    }
    //查
    public static <T> List<T> excuteQuery(String sql, RowMap<T> rowMap , Object... objects){
        List<T> list=new ArrayList<>();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        connection=conn();
        try {
            preparedStatement=connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                preparedStatement.setObject(i+1,objects[i]);
            }
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                //拿到resultSet  返回出当前类型的对象  T
//                list.add();
                T t=  rowMap.rowMaping(resultSet);
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}

