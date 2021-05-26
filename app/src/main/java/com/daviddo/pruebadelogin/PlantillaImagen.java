package com.daviddo.pruebadelogin;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

public class PlantillaImagen implements Serializable {

    private String imagen;
    private String nombre;

    public PlantillaImagen(String imagen) {
        this.nombre = "JAJAJAJAJAJA";
        this.imagen = imagen;
    }

    public PlantillaImagen() {
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}





