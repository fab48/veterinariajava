/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import base.Conexion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author fabricio
 */
public class Mascota {
    
    //ATRIBUTOS
    private String nombre;
    private Persona propietario;
    private String tipo;
    private Connection conexion;
    private Conexion conexion_hab;
    
    //CONSTRUCTOR
    public Mascota(){
        
    }
    
    //METODOS

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    /** registrar
     * Este es un metodo que se encarga de registrar una mascota
     * @return 
     */
    public boolean registrar(){
        boolean registro = false;
        Sesion sesion = Sesion.getSesion();
        setPropietario(sesion.getPersona());
        try{
            Conexion conexion_hab = Conexion.getConexion();
            conexion = conexion_hab.PrepararBaseDatos();
            PreparedStatement a = conexion.prepareStatement("INSERT INTO MASCOTA VALUES (?,?,?,?)");
            a.setString(1,getPropietario().getIdentificacion()+"_"+getNombre());
            a.setInt(2, getPropietario().getIdentificacion());
            a.setString(3, getNombre());
            a.setString(4, getTipo());
            a.executeUpdate(); 
            registro=true;
        }
        catch(Exception e){
            registro=false;
        }
        return registro;
    }
    
    /** listaMascota
     *  retorna la lista de mascotas de un usuario
     * @return 
     */
    public ArrayList<Mascota> listaMascota() throws SQLException{
        ArrayList<Mascota> lista = new ArrayList<>();
        
        conexion_hab = Conexion.getConexion();
        Connection con = conexion_hab.PrepararBaseDatos();
        java.sql.Statement statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM `MASCOTA` WHERE `propietario`="+getPropietario().getIdentificacion());
        while (result.next()) 
        {
            Mascota mascota_aux = new Mascota();
            mascota_aux.setNombre(result.getString(3));
            mascota_aux.setTipo(result.getString(4));
            lista.add(mascota_aux);
            mascota_aux=null;
            
        }
        return lista;
    }
    /** traerMascota
     * Se asigna los datos faltantes
     * @param idUsuario 
     */
    public void traerMascota(int idUsuario){
        
        //se comprueba si el usuario existe
        File archivo = new File ("/Users/fabricio/Documents/fis/archivo_v/"+idUsuario+"/"+getNombre()+".txt");
        try{
            FileReader fr = new FileReader (archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            String[] contenido = new String[6];
            contenido[0]=linea;
            int i =0;
            while((linea=br.readLine())!=null){
                contenido[i]=linea;
                i++;
            }
            setTipo(contenido[2].split("tipo:")[1]);
        }
        catch(Exception e){
            
        }
    }
}
