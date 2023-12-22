package lc.bungeecore.entidades;

import lc.bungeecore.utilidades.Util;

public class RangoInfo {

    private Rango rango;
    private long duration;
    private boolean hide;
    private int puntos;
    private String nameColor;

    public RangoInfo(Rango rango, long duration, boolean hiderank, int puntos, String nameColor){
        this.rango = rango;
        this.duration = duration;
        this.hide = hiderank;
        this.puntos = puntos;
        this.nameColor = nameColor;
    }

    public Rango getRango() {
        return rango;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public long getDuration() {
        return duration;
    }

    public void setRango(Rango rango) {
        this.rango = rango;
    }

    public void setHideRank(boolean hide) {
        this.hide = hide;
    }

    public boolean isHideRank(){
        return hide;
    }

    public String getNameColor() {
        return Util.color(nameColor);
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }
}
