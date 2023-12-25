package lc.bungeecore.entidades.minijuegos;

public enum CHGRank {
    NUEVO(0),
    APRENDIZ(1),
    HÉROE(2),
    FEROZ(3),
    PODEROSO(4),
    MORTAL(5),
    TERRORÍFICO(6),
    CONQUISTADOR(7),
    RENOMBRADO(8),
    ILUSTRE(9),
    EMINENTE(10),
    REY(11),
    EMPERADOR(12),
    LEGENDARIO(13),
    MÍTICO(14);

    private int priority;
    CHGRank(int priority){
        this.priority = priority;
    }
}
