package com.component.coneccion;

import java.sql.*;
import javax.swing.JOptionPane;

public class Coneccion 
{
    private String bd;
    private String schema;
    private String url;
    private String driver;
    private Connection con=null;
    private String user;
    private String password;
    private Statement estancia;
    private PreparedStatement sta;
    private String tabla;
    private ResultSet rs;
    private int contNameTAble = 0;
    Object[] nombres;

    public Coneccion()
    {
    }
    public void setDatos(String bd,String user,String pass,String schema)
    {
        setBd(bd);
        setUser(user);
        setPassword(pass);
        setSchema(schema);
        this.url="jdbc:postgresql://localhost/"+bd;
        this.driver="org.postgresql.Driver";
    }
    public void setSchema(String schema)
    {
        this.schema = schema;
    }
    public String getBd()
    {
        return bd;
    }
    public String getUrl()
    {
        return url;
    }
    public String getDriver()
    {
        return driver;
    }
    public String getUser()
    {
        return user;
    }
    public String getPassword()
    {
        return password;
    }
    public String getTabla()
    {
        return this.tabla;
    }
    public void setBd(String bd)
    {
        
        this.bd = bd;
    }
    public void setUrl(String url)
    {
        
        this.url = url;
    }
    public void setDriver(String driver)
    {
        
        this.driver = driver;
    }
    public void setUser(String user)
    {
        
        this.user = user;
    }
    public void setPassword(String pass)
    {
        
        this.password = pass;
    }
    public void setTabla(String tabla)
    {
        this.tabla = tabla;
    }
    public boolean conecta() throws SQLException
    {
        boolean c;
        try
        {
            Class.forName(getDriver());
            con=DriverManager.getConnection(getUrl(),getUser(),getPassword());
            System.out.println("Conexion Exitosa!!!");
            c = true;
        }
        catch(ClassNotFoundException | SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(),"Error de Conexion!!!",JOptionPane.ERROR_MESSAGE);
            c = false;
        }
        return c;
    }
    public void desconecta() throws SQLException
    {
            try
            {
                this.con.close();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
    }
    public int getContNameTAble()
    {
        return this.contNameTAble;
    }
    public Object[] getModeloTablas()
    {
        return nombres;
    }
    public void contNameTable()
    {
        ResultSet rs1;
        String sql="select tablename from pg_tables where schemaname = '"+schema+"'";
        try 
        {
            rs1= consulta(sql);
            while(rs1.next())
            {
                contNameTAble++;
            }
            nombres = new Object[contNameTAble];
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Object[] setModeloTablas()
    {

        ResultSet rs1;
        String sql="select tablename from pg_tables where schemaname = '"+schema+"'";
        try 
        {
            conecta();
            contNameTable();
            nombres = new Object[getContNameTAble()];
            int i = 0;
            rs1= consulta(sql);
            while(rs1.next())
            {
                nombres[i]=(String) rs1.getString(1);
                i++;
            }
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return nombres;
    }
    public Connection getCon()
    {
        return con;
    }
    public ResultSet consulta(String sql)
    {
        try
        {
            sta = con.prepareStatement(sql);
            rs = sta.executeQuery();
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error en consulta",JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }
    public ResultSet consulta()
    {
        
        try
        {
            conecta();
            sta = con.prepareStatement("Select *from "+getTabla()+"");
            rs = sta.executeQuery();
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error en consulta",JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }
}