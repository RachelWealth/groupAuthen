package db;
import printServer.PasswordEncrypter;

import java.security.NoSuchAlgorithmException;
import  java.sql.*;


public class userDatabase {
    private String db = "user";

    private Connection connect(){
        Connection con=null;
        try{
            Class.forName("org.sqlite.JDBC");

            String PATH_DB = "E:\\0Postgraduate\\study\\data security\\slide_2022\\authentication\\groupAuthen\\src\\db\\user.db";
            String url = "jdbc:sqlite:" + PATH_DB;
            con=DriverManager.getConnection(url);
            System.out.println();
        }catch(Exception e){
            System.out.println("数据库连接失败"+e.getMessage());;
        }
        return con;
    }

    private void __insert__(String name,String pw) throws SQLException, NoSuchAlgorithmException {
        Connection con = this.connect();
        Statement st=con.createStatement();
        String sql="insert into user values(" + "u2" + ","+String.valueOf(PasswordEncrypter.getEncryptedPassword(pw))+");";
        System.out.println(String.valueOf(PasswordEncrypter.getEncryptedPassword(pw)));
        st.executeUpdate(sql);
    }
    public void add() throws SQLException, NoSuchAlgorithmException {
        //this.__insert__("user1","000");
        this.__insert__("user2","111");
    }
    public synchronized boolean search(String name, String pw) throws SQLException, NoSuchAlgorithmException {
        Connection con = this.connect();
        String sql="select * from " + this.db+ " where user= '"+name+"'"+ " and pw = '"+PasswordEncrypter.getEncryptedPassword(pw)+"'";

        Statement st=con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        return rs.next();
    }
}
