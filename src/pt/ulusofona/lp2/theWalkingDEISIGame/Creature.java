package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.util.ArrayList;

public abstract class Creature {
    protected int id;
    protected int idTipo;
    protected int idEquipa = 10;
    protected int idEquipaZombie = 20;
    protected String nome;

    private String tipo;
    private String equipa;

    protected int xAtual;
    protected int yAtual;
    protected int xAnterior;
    protected int yAnterior;

    protected ArrayList<Equipamento> equipamentos = new ArrayList<>();
    protected ArrayList<Equipamento> destruidos = new ArrayList<>();

   // public Creature(){}

    public Creature(int id, int idTipo, String nome, int xAtual, int yAtual) {
        this.id = id;
        this.idTipo = idTipo;
        this.nome = nome;
        this.xAtual = xAtual;
        this.yAtual = yAtual;
    }

    public abstract boolean move(int xO, int yO, int xD, int yD,
                                 Creature creature, ArrayList<Creature> creatures);



    // Devolve o ID da criatura.
    public int getId() {
        return id;
    }

    public int getIdEquipa() {
        return idEquipa;
    }

    public String getNome() {
        return nome;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public void setIdEquipa(int idEquipa) {
        this.idEquipa = idEquipa;
    }

    public int getXAtual() {
        return xAtual;
    }

    public int getYAtual() {
        return yAtual;
    }

    public void setxAtual(int xAtual) {}

    public void setyAtual(int yAtual) {}

    public ArrayList<Equipamento> getEquipamentosVivos(){
        return equipamentos;
    }

    public ArrayList<Equipamento> getEquipamentosZombies(){
        return destruidos;
    }

    // Metodo onde <Tipo> corresponde aos nomes (p.e. “Humano” ou “Zombie”).
    protected void getTipo(){
        if(this.idTipo == 0){
            tipo = "Criança (Zombie)";
        } else if (this.idTipo == 1) {
            tipo = "Adulto (Zombie)";
        } else if (this.idTipo == 2) {
            tipo = "Militar (Zombie)";
        } else if (this.idTipo == 3) {
            tipo = "Idoso (Zombie)";
        } else if (this.idTipo == 4) {
            tipo = "Zombie Vampiro";
        } else if (this.idTipo == 5) {
            tipo = "Criança (Vivo)";
        } else if (this.idTipo == 6) {
            tipo = "Adulto (Vivo)";
        } else if (this.idTipo == 7) {
            tipo = "Militar (Vivo)";
        } else if (this.idTipo == 8) {
            tipo = "Idoso (Vivo)";
        } else if (this.idTipo == 9) {
            tipo = "Cão";
        }
    }

    // Metodo onde <Equipa Vivos> corresponde ao nome (p.e. “Os Vivos”).
    public void getEquipaVivos() {
        if(this.idEquipa == 10){
            equipa = "Os Vivos";
        }
    }

    // Metodo onde <Equipa Zombie> corresponde ao nome (p.e. “Os Outros”).
    public void getEquipaZombie(){
        if (this.idEquipaZombie == 20){
            equipa = "Os Outros";
        }
    }

    // Metodo que devolve o nome do ficheiro de imagem (formato PNG) que representa a criatura.
    public String getImagePNG(){
        switch (idTipo){
            case 0:
                return "zombieCrianca.png";
            case 1:
                return "zombieAdulto.png";
            case 2:
                return "zombieMilitar.png";
            case 3:
                return "zomb.png";
            case 4:
                return "zombieVampiro.png";
            case 5:
                return "crianca.png";
            case 6:
                return "adulto.png";
            case 7:
                return "militar.png";
            case 8:
                return "idoso.png";
            case 9:
                return "dog.png";
        }
        return null;
    }

    @Override
    public String toString() {
        if (equipa.equals("Os Vivos")){
            return id + " | " + tipo + " | " + equipa + " | " + nome + " " + equipamentos.size() + " @ (" + xAtual + ", " + yAtual + ")";
        } else if (equipa.equals("Os Outros")){
            return id + " | " + tipo + " | " + equipa + " | " + nome + " " + destruidos.size() + " @ (" + xAtual + ", " + yAtual + ")";
        } else {
            return id + " | " + tipo + " | " + equipa + " | " + nome + " @ (" + xAtual + ", " + yAtual + ")";
        }
    }

}