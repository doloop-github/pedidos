package com.example.login;

public class Clientes {
    private String RazonSocial;
    private int id;
    private String domicilio;
    private String cp;
    private String localidad;
    private String provincia;

    public Clientes (String RazonSocial, int id, String domicilio, String cp, String localidad, String provincia){
        this.RazonSocial=RazonSocial;
        this.id=id;
        this.domicilio=domicilio;
        this.cp=cp;
        this.localidad=localidad;
        this.provincia=provincia;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.RazonSocial = razonSocial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
