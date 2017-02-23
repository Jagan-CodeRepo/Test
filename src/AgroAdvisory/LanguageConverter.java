/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

import java.sql.*;
/**
 *
 * @author jagan
 */
public class LanguageConverter 
{ 
    Connection con;
    Statement stmt;
    ResultSet rs;
    public LanguageConverter()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://ec2-50-19-213-178.compute-1.amazonaws.com/lang",
            "jaganlang", "weather123");
            stmt = con.createStatement();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public String englishTotamil(String text)
    {
        try 
        {
            text=text.replaceAll("\\s+", " ");
            rs = stmt.executeQuery("select tamil from english_tamil where english='"+text+"'");
            if(rs.next())
            {
                return rs.getString("tamil");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }
    protected void finalize() throws Throwable 
    {
        if(rs!=null)
        {
            rs.close();
        }
        if(stmt!=null)
        {
            stmt.close();
        }
        if(con!=null)
        {
            con.close();
        }
    }
     public static void main(String[] args) {
     LanguageConverter lc=new LanguageConverter();
     System.out.println(lc.englishTotamil(" Harvesting of the matured paddy can be taken up by using the dry weather."));
     }
}
