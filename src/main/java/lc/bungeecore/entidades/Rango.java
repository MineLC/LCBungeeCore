package lc.bungeecore.entidades;

import lc.bungeecore.utilidades.Util;
import org.apache.commons.lang3.StringUtils;

public enum Rango {
    DEFAULT("&7"),
    VIP("&a&lVIP "),
    SVIP("&e&lSVIP "),
    ELITE("&6&lELITE "),
    RUBY("&c&lRUBY "),
    MINIYT("&f&lMINI&c&lYT "),
    YOUTUBER("&f&lYOU&c&lTUBE "),
    STREAMER("&5&lSTREAMER"),
    HELPER("&d&lHELPER "),
    MOD("&9&lMOD "),
    ADMIN("&4&lADMIN "),
    OWNER("&b&lOWNER ");

    private final String prefix;
    Rango(String prefix){
        this.prefix = prefix;
    }
    @Override
    public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
    }

    public String getAsPrefix(){
        return Util.color(prefix);
    }
}
