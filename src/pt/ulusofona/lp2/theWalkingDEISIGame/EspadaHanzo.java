package pt.ulusofona.lp2.theWalkingDEISIGame;

public class EspadaHanzo extends Equipamento{

    public EspadaHanzo(int id, int idTipo, int xAtual, int yAtual) {
        super(id, idTipo, xAtual, yAtual);
    }

    public EspadaHanzo() {

    }

    @Override
    public int getiD() {
        return id;
    }

    @Override
    public int getIdTipo() {
        return idTipo;
    }

    @Override
    public int getXAtual() {
        return xAtual;
    }

    @Override
    public int getYAtual() {
        return yAtual;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }
}
