package com.daviddo.pruebadelogin;

import android.widget.ImageView;

public class Trozo {

    ImageView imagen;
    int coorx;
    int coory;
    boolean eshueco;

    public Trozo(ImageView imagen, int coorx, int coory, boolean eshueco) {
        this.imagen = imagen;
        this.coorx = coorx;
        this.coory = coory;
        this.eshueco = eshueco;
    }
}
