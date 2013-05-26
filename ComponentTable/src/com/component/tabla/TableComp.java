package com.component.tabla;
import com.component.coneccion.Coneccion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class TableComp extends JFrame
{
    int cantidadAPMax=20;
    int cantidadAPMin=0;
    int cantidadColumnas;
    Object[] nombres;
    Object[] fila;
    Coneccion con = new Coneccion();
    String tabla;
    Vector filas = new Vector();
    
    DefaultTableModel modelo=new DefaultTableModel();
    JButton regresar=new JButton("Regresar");
    JButton adelante=new JButton("Adelante");
    JTable tabla1 = new JTable();
    private int INDEX_TABLA;

    public TableComp()
    {
        
    }
    public TableComp(String tabla)
    {
        this.tabla = tabla;
        JPanel panel=new JPanel();
        panel.setLayout(null);
        
        regresar.setBounds(10,100,100,30);
        panel.add(regresar);
        adelante.setBounds(550,150,100,30);
        panel.add(adelante);
        
        tabla1.setBounds(120,10,300,300);
        tabla1.setModel(modelo);
        panel.add(tabla1);
        
        JScrollPane scrol=new JScrollPane(tabla1);
        scrol.setBounds(120,20,420,350);
        panel.add(scrol);
        
        JLabel label=new JLabel("Total:");
        label.setBounds(300,400,80,20);
        panel.add(label);
        
        add(panel);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
        setLocation(200,200);
        setSize(700,500);
        
        ManejadorBotones botones = new ManejadorBotones() ;
        adelante.addActionListener(botones);
        regresar.addActionListener(botones);
    }
    public void contNameTable()
    {
        try 
        {
            con.contNameTable();
            nombres = new Object[con.getContNameTAble()];
            System.out.println(con.getContNameTAble());
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Object[] getModeloTabla()
    {
        try 
        {
            nombres = con.setModeloTablas();
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, e, "Error Cargando Lista de Tablas", JOptionPane.ERROR_MESSAGE);
        }
        return nombres;
    }
    public void llenaTabla()
    {
        tabla1.removeAll();
        limpiaTabla();
        try
        {
            ResultSetMetaData rsMd;
            ResultSet rs;
            
            cantidadColumnas=0;
            con.setTabla(getTabla());
            rs = con.consulta();
            rsMd=rs.getMetaData();
            cantidadColumnas= rsMd.getColumnCount();
                    for (int i=1; i<= cantidadColumnas;i++)
                    {
                        modelo.addColumn(rsMd.getColumnLabel(i));
                    }
                    
                    while (rs.next())
                    {
                        fila=new Object[cantidadColumnas];
                        for (int i=0; i< cantidadColumnas;i++)
                        {
                            fila[i]=rs.getObject(i+1);
                            
                        }
                        filas.add(fila);
                    }
                    
                    agregaFilas(0, 20);
                    rs.close();
            con.desconecta();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e, "Error Llenando tabla ", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void setIndexTabla(int index)
    {
        this.INDEX_TABLA = index;
    }
    public int getIndexTabla()
    {
        return INDEX_TABLA;
    }
    public void setTabla(String tabla)
    {
        this.tabla = tabla;
    }
    public String getTabla() 
    {
        return this.tabla;
    } 
    public void agregaFilas(int min, int max) 
    {
        for (int j = min; j < max; j++) 
        {
            Object[] temp = (Object[]) filas.get(j);
            modelo.addRow(temp);
        }
    }
    
    public void setDatosBD(String bd,String user, String pass, String schema)
    {
        con.setDatos(bd, user, pass, schema);
    }
    
    public void limpiaTabla()
    {
        while(tabla1.getRowCount()>0)
        {
            ((DefaultTableModel)tabla1.getModel()).removeRow(0);
        }        
    }
    private class ManejadorBotones implements ActionListener
    {
        public void actionPerformed(ActionEvent botones)
        {
     
            if(botones.getSource()==adelante)
            {
                try 
                {
                    limpiaTabla();
                    cantidadAPMax=cantidadAPMax+20;
                    cantidadAPMin=cantidadAPMin+20;
                    agregaFilas(cantidadAPMin, cantidadAPMax);
                } 
                catch (Exception e) 
                {
                    JOptionPane.showMessageDialog(null, "No hay mas datos", "Alerta!!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if(botones.getSource()==regresar)
            {
                try 
                {
                    limpiaTabla();
                    cantidadAPMax=cantidadAPMax-20;
                    cantidadAPMin=cantidadAPMin-20;
                    agregaFilas(cantidadAPMin, cantidadAPMax);
                } 
                catch (Exception e) 
                {
                    JOptionPane.showMessageDialog(null, "No hay mas datos", "Alerta!!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}
