package com.daviddo.pruebadelogin;

import java.io.Serializable;

public class RondaRecord implements Serializable, Comparable<RondaRecord> {
        private int ronda;
        private  String nombre ;
        private   int puntuacion;

        public RondaRecord(int ronda, String nombre, int puntuacion) {
            this.ronda = ronda;
            this.nombre = nombre;
            this.puntuacion = puntuacion;
        }

    public RondaRecord() {
    }

    @Override
        public String toString() {
            return
                    "Ronda " + ronda +
                            ": RECORD ; nombre='" + nombre + '\'' +
                            ", puntuacion=" + puntuacion +
                            '.';
        }

        public int getRonda() {
            return ronda;
        }

        public void setRonda(int ronda) {
            this.ronda = ronda;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getPuntuacion() {
            return puntuacion;
        }

        public void setPuntuacion(int puntuacion) {
            this.puntuacion = puntuacion;
        }

    @Override
    public int compareTo(RondaRecord o) {
        return this.puntuacion- o.puntuacion;
    }
}


