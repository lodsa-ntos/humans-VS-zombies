package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

public class TWDGameManager {

    /*------------------------------------------The Walking DEISI----------------------------------------------
    Tema:
          Este projecto tem como objetivo o desenvolvimento de uma aplicação (programa) em linguagem Java.

          No bairro do Freddy M. passa-se algo de muito estranho. Algumas pessoas têm desaparecido
          sem deixar rasto. O Freddy resolveu investigar e descobriu que está no meio de um surto de zombies.
          O Freddy pretende salvar os seus vizinhos, mas não sabe qual é a melhor forma de o fazer.

          A simulação irá consistir na aplicação de várias regras que definem os movimentos dos
          humanos (e seus animais de companhia) ao longo de vários dias, e várias noites e tem como
          objetivo tentar planear os movimentos que permitam salvar o maior número de companheiros.

    ----------------------------------------------------------------------------------------------------------*/

    //Lista de Creatures
    private static ArrayList<Creature> creatures = new ArrayList<>();
    //Lista para o safeHaven
    private static ArrayList<Creature> safe = new ArrayList<>();
    //Lista de criaturas envenenadas
    private static ArrayList<Creature> criaturasEnvenenadas = new ArrayList<>();
    //Lista de Equipamento
    private static ArrayList<Equipamento> equipamentos = new ArrayList<>();
    //Lista de zombies destruidos
    static ArrayList<Creature> zombiesDestruidos = new ArrayList<>();
    //Lista de Equipamento encontrados no caminho
    static ArrayList<Equipamento> equipamentosEncontrados = new ArrayList<>();
    //Lista de Portas em Jogo
    private static ArrayList<Porta> portasEmJogo = new ArrayList<>();

    private static int numLinha;
    private static int numColuna;
    private int xPortas;
    private int yPortas;
    private int linhaAtual = 0;
    private int idEquipaInicial;
    private int idEquipaAtual;
    private int nC;
    private int nE;
    private int nP;
    private int nrTurno = 0;
    private int nrTurnoDoVeneno = 0;
    private boolean diurno = true;

    public TWDGameManager() {
    }

    public void startGame(File ficheiroInicial) throws InvalidTWDInitialFileException, FileNotFoundException {

        /* Cada vez que o ficheiro é lido o programa vai fazer o reset das variaveis para o valor inicial */
        incrementarReset();

        String[] dados;

        try {

            FileInputStream initialFile = new FileInputStream(ficheiroInicial);

            Scanner leitor;

            leitor = new Scanner(initialFile);

            String linha;

            // Primeira linha que indica as linhas e as colunas do mapa.
            // ler uma linha do ficheiro
            linha = leitor.nextLine();

            // vai quebrando a string em varias substrings a partir do espaco vazio
            String[] mapa = linha.split(" ");

            if (getWorldSize().length == 2) {
                numLinha = Integer.parseInt(mapa[0].trim()); // guarda na primeira posicao do array o numLinha
                numColuna = Integer.parseInt(mapa[1].trim()); // guarda na segunda posicao do array o numColuna
            }

            // Segunda linha que indica qual é o ID da equipa que começa o jogo.
            // ler uma linha do ficheiro
            linha = leitor.nextLine();
            idEquipaInicial = Integer.parseInt(linha);
            idEquipaAtual = idEquipaInicial;

            // Terceira linha que indica o número de criaturas em jogo.
            // ler uma linha do ficheiro
            linha = leitor.nextLine();
            nC = Integer.parseInt(linha);

            /* Obter o valor de numero de criaturas e a partir desse valor contar o numero de linhas de criaturas */
            int nLinhas;
            nLinhas = nC;

            if (nC < 2) {
                throw new InvalidTWDInitialFileException(nC, true, "");
            }

            /* enquanto o ficheiro tiver linhas não-lidas */
            while (leitor.hasNextLine()) {

                if (linhaAtual < nLinhas) {
                    // lê uma linha do ficheiro até achar uma quebra de linha
                    linha = leitor.nextLine();

                    // vai quebrando a string em varias substrings a partir do caracter dois pontos (separador)
                    dados = linha.split(":");

                    if (dados.length == 5) {

                        // Converte as Strings lidas para os tipos esperados
                        // "trim()" --> retira espaços a mais que estejam no inicio e no fim do texto (espaços padrao)
                        int id = Integer.parseInt(dados[0].trim());
                        int idTipo = Integer.parseInt(dados[1].trim());
                        String nome = dados[2].trim();
                        int posX = Integer.parseInt(dados[3].trim());
                        int posY = Integer.parseInt(dados[4].trim());

                        //Verificar se o idTipo é zombie ou humano e adiciona na lista de criaturas
                        switch (idTipo) {
                            case 0: /* Crianca Zombie */
                            case 5: /* Crianca VIvo */
                                Creature crianca = new Crianca(id, idTipo, nome, posX, posY);
                                creatures.add(crianca); // adiciona crianca
                                crianca.setTipo(idTipo);
                                crianca.setEquipa(idTipo);
                                crianca.getNrCriaturasZombies();
                                System.out.println(crianca.toString()); // imprime crianca
                                break;

                            case 1: /* Adulto Zombie */
                            case 6: /* Adulto Vivo */
                                Creature adulto = new Adulto(id, idTipo, nome, posX, posY);
                                creatures.add(adulto); // adiciona aduLto
                                adulto.setTipo(idTipo);
                                adulto.setEquipa(idTipo);
                                adulto.getNrCriaturasZombies();
                                System.out.println(adulto.toString()); // imprime adulto
                                break;

                            case 2: /* Militar Zombie */
                            case 7: /* Militar Vivo */
                                Creature militar = new Militar(id, idTipo, nome, posX, posY);
                                creatures.add(militar); // adiciona militar
                                militar.setTipo(idTipo);
                                militar.setEquipa(idTipo);
                                militar.getNrCriaturasZombies();
                                System.out.println(militar.toString()); // imprime militar
                                break;

                            case 3: /* Idoso Zombie */
                            case 8: /* Idoso Vivo */
                                Creature idoso = new Idoso(id, idTipo, nome, posX, posY);
                                creatures.add(idoso); // adiciona idoso
                                idoso.setTipo(idTipo);
                                idoso.setEquipa(idTipo);
                                idoso.getNrCriaturasZombies();
                                System.out.println(idoso.toString()); // imprime idoso
                                break;

                            case 4: /* Zombie Vampiro */
                                Creature zombieVamp = new ZombieVampiro(id, idTipo, nome, posX, posY);
                                creatures.add(zombieVamp); // adiciona zombie vampiro
                                zombieVamp.setTipo(idTipo);
                                zombieVamp.setEquipa(idTipo);
                                System.out.println(zombieVamp.toString()); // imprime zombie vampiro
                                break;

                            case 9: /* Cao */
                                Creature cao = new Cao(id, idTipo, nome, posX, posY);
                                creatures.add(cao); // adiciona cao
                                cao.setTipo(idTipo);
                                cao.setEquipa(idTipo);
                                System.out.println(cao.toString()); // imprime cao
                                break;

                            case 12: /* Coelho Vivo */
                            case 13: /* Coelho Zombie */
                                Creature coelho = new Coelho(id, idTipo, nome, posX, posY);
                                creatures.add(coelho); // adiciona coelho
                                coelho.setTipo(idTipo);
                                coelho.setEquipa(idTipo);
                                System.out.println(coelho.toString()); // imprime coelho
                                break;

                            default:
                                System.out.println("Erro! Criatura não adicionada!");
                                break;
                        }
                    } else {
                        throw new InvalidTWDInitialFileException(nC, false, linha);
                    }

                    linhaAtual++;

                } else if (linhaAtual == nLinhas) { // verifica se as primeiras linhas ja foram lidas
                    // Setima linha que indica o número de equipamentos em jogo.
                    // ler uma linha do ficheiro
                    linha = leitor.nextLine();
                    nE = Integer.parseInt(linha);
                    System.out.println(nE);

                    int linhaPorta;
                    linhaPorta = nE;
                    linhaPorta += nLinhas;

                    // enquanto o ficheiro tiver linhas não-lidas depois da anterior, lê as linhas com equipamentos
                    while (leitor.hasNextLine()) {

                        if (linhaAtual < linhaPorta) {
                            // lê uma linha do ficheiro até achar uma quebra de linha
                            linha = leitor.nextLine();

                            // vai quebrando a string em varias substrings a partir do caracter dois pontos (separador)
                            String[] novaFila = linha.split(":");

                            if (novaFila.length == 4) {

                                // Converte as Strings lidas para os tipos esperados
                                // "trim()" --> retira espaços a mais que estejam no inicio e no fim do texto (espaços padrao)
                                int id = Integer.parseInt(novaFila[0].trim());
                                int idTipo = Integer.parseInt(novaFila[1].trim());
                                int posX = Integer.parseInt(novaFila[2].trim());
                                int posY = Integer.parseInt(novaFila[3].trim());

                                // Verificar o idTipo e adiciona na lista
                                if (idTipo == 0 || idTipo == 1 || idTipo == 2 || idTipo == 3 || idTipo == 4 || idTipo == 5
                                        || idTipo == 6 || idTipo == 7 || idTipo == 8 || idTipo == 9 || idTipo == 10) {
                                    Equipamento allEquipments = new Equipamento(id, idTipo, posX, posY);
                                    equipamentos.add(allEquipments); // adiciona equipamento
                                    allEquipments.setIdTipo(idTipo); // chama o tipo de equipamento
                                    System.out.println(allEquipments.toString());
                                }
                            } else {
                                throw new InvalidTWDInitialFileException(nE, false, linha);
                            }

                            linhaAtual++;

                        } else if (linhaAtual == linhaPorta) {
                            linha = leitor.nextLine();
                            nP = Integer.parseInt(linha);
                            System.out.println(nP);

                            while (leitor.hasNextLine()) {
                                // lê uma linha do ficheiro até achar uma quebra de linha
                                linha = leitor.nextLine();

                                // vai quebrando a string em varias substrings a partir do caracter dois pontos (separador)
                                String[] filaDasPortas = linha.split(":");

                                if (filaDasPortas.length == 2) {

                                    // Converte as Strings lidas para os tipos esperados
                                    // "trim()" --> retira espaços a mais que estejam no inicio e no fim do texto (espaços padrao)
                                    xPortas = Integer.parseInt(filaDasPortas[0].trim()); // guarda na primeira posicao do array o x
                                    yPortas = Integer.parseInt(filaDasPortas[1].trim()); // guarda na segunda posicao do array o y

                                    Porta portaSafeHaven = new Porta(xPortas, yPortas);
                                    portasEmJogo.add(portaSafeHaven);
                                    System.out.println(portaSafeHaven.toString());

                                } else {
                                    throw new InvalidTWDInitialFileException(nP, false, linha);
                                }
                            }
                        }
                    }
                }
            }

            leitor.close();

        } catch (FileNotFoundException ex) {
            System.out.println("\n" + "Erro.: " + ficheiroInicial.getName() + " não encontrado.");
            throw new FileNotFoundException();
        }
    }

    public int[] getWorldSize() {
        int [] tamanho = new int[2];
        tamanho[0] = numLinha;
        tamanho[1] = numColuna;
        return tamanho;
    }

    public int getInitialTeam() {
        return idEquipaInicial;
    }

    public List<Creature> getCreatures(){
        return creatures;
    }

    // TODO falta implementar os movimentos em turnos do coelho - 5 erros no DropProjet
    public boolean move(int xO, int yO, int xD, int yD) {

        if (!gameIsOver()) {
            /* VALIDAÇÕES PARA COORDENADAS DE ORIGEM E DESTINO FORA DO MAPA */
            if (xO < 0 || xD < 0) {
                return false; // estão fora do mapa
            }

            else if (yO < 0 || yD < 0) {
                return false; // estão fora do mapa
            }

            boolean encontrouEquip = false;

            for (Creature creatureOrigem : creatures) {
                if (creatureOrigem.getIdEquipa() == idEquipaAtual &&
                        creatureOrigem.getXAtual() == xO && creatureOrigem.getYAtual() == yO) {

                    /* ENTRADA PARA O SAFEHAVEN */
                    if (creatureOrigem.getIdEquipa() == 10) {
                        if (!saltarPorCima(xO, yO, xD, yD) && creatureOrigem.getIdTipo() != 5 && creatureOrigem.getIdTipo() != 8) {
                            return false;
                        }

                        if (creatureOrigem.getIdTipo() == 8 && !isDay()) {
                            return false;
                        }

                        if (creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                            /* Se existir uma porta safeHaven */
                            if (isDoorToSafeHaven(xD, yD)) {
                                /* Vamos colocar o vivo lá dentro */
                                creatureOrigem.inSafeHaven(true);
                                /*  E vamos remove-lo do mapa */
                                //creatureOrigem.id = 0;
                                creatures.get(creatures.indexOf(creatureOrigem)).inSafeHaven(true);
                                /* E adiciona-mos o vivo na lista do safeHaven */
                                safe.add(creatureOrigem);
                                creatureOrigem.setxAtual(xD);
                                creatureOrigem.setyAtual(yD);
                                incrementarTurno();
                                return true;
                            }
                        }
                    }

                    // COMBATE = VIVO NA DEFENSIVA
                    for (Creature creatureDestino : creatures) {
                        // Se o elemento de uma equipa cair em cima de um outro da mesma equipa
                        // retorna falso
                        if (creatureDestino.getXAtual() == xD && creatureDestino.getYAtual() == yD) {
                            if (creatureDestino.getIdEquipa() == idEquipaAtual) {
                                return false;
                            } else {

                                /* Zombie VS Vivo sem equipamento */
                                if (creatureOrigem.getIdEquipa() == 20) {

                                    if (creatureOrigem.getIdTipo() == 0 || creatureOrigem.getIdTipo() == 1 ||
                                            creatureOrigem.getIdTipo() == 2 || creatureOrigem.getIdTipo() == 3 ||
                                            creatureOrigem.getIdTipo() == 4 || creatureOrigem.getIdTipo() == 13) {

                                        // O cão não se transforma
                                        if (creatureDestino.getIdTipo() == 9) {
                                            return false;
                                        }

                                        /* verifica se o vivo tem equipamentos // se nao tiver nenhum equipamento */
                                        if (creatureDestino.getEquipamentosVivos().size() == 0) {
                                            switch (creatureDestino.getIdTipo()) {
                                                case 5:
                                                case 6:
                                                case 7:
                                                case 8:
                                                case 12:
                                                    /* Vivo tranforma-se (->) em Zombie */
                                                    creatureOrigem.transformaEmZombie(creatureDestino);
                                                    creatureDestino.setTransformado(true);
                                                    creatureOrigem.countTransformacoesFeitasPorZombies();
                                                    incrementarTurno();
                                                    return true;
                                            }

                                        } else {

                                            /* Zombie VS Vivo com equipamentos defensivos */
                                            switch (creatureDestino.getEquipamentosVivos().get(0).getIdTipo()) {

                                                case 0:
                                                    /* Interação com o Escudo de madeira */

                                                    /* Quando militar defende, alteramos o estado de uso do escudo de madeira */
                                                    if (creatureDestino.getIdTipo() == 7) {
                                                        creatureDestino.getEquipamentosVivos().get(0).escudoFoiUsado();
                                                    }

                                                    /* diminuimos ... */
                                                    creatureDestino.getEquipamentosVivos().get(0).diminuiProtecaoDoEscudo();

                                                    /* incrementa o numero de salvações */
                                                    creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();

                                                    if (creatureDestino.equipamentos.get(0).getCountProtecaoDoEscudo() == 0) {
                                                        // destrui-mos o equipamento
                                                        creatureDestino.getEquipamentosVivos().remove(creatureDestino.equipamentos.get(0));
                                                    }

                                                    incrementarTurno();
                                                    return true;

                                                case 1:
                                                    /* Interação com a Espada */
                                                    if (creatureDestino.getIdTipo() == 5) {
                                                        if (creatureOrigem.getIdTipo() != 0) {
                                                            /* vamos transformar o vivo em zombie */
                                                            creatureOrigem.transformaEmZombie(creatureDestino);
                                                            creatureDestino.setTransformado(true);
                                                            creatureOrigem.countTransformacoesFeitasPorZombies();
                                                            /* e destrui-mos o equipamento */
                                                            creatureDestino.getEquipamentosVivos().remove(creatureDestino.equipamentos.get(0));
                                                            /* e colocamos o numero de equipamentos que tinha antes a zero */
                                                            creatureDestino.incrementaSemEquipamentoDepoisDeTransformado(0);
                                                        } else {
                                                            /* vamos destruir o zombie */
                                                            creatures.remove(creatureOrigem);
                                                            zombiesDestruidos.add(creatureOrigem);
                                                            /* incrementa o numero de zombies destruidos */
                                                            creatureDestino.countZombiesDestruidos();
                                                            creatureOrigem.setZombieIsDestroyed(true);
                                                            if (creatureOrigem.zombieIsDestroyed()) {
                                                                System.out.println(creatureOrigem.toString());
                                                            }
                                                        }
                                                        incrementarTurno();
                                                        return true;
                                                    }

                                                    if (creatureDestino.getIdTipo() == 6 || creatureDestino.getIdTipo() == 7 ||
                                                            creatureDestino.getIdTipo() == 8) {
                                                        /* vamos destruir o zombie */
                                                        creatures.remove(creatureOrigem);
                                                        zombiesDestruidos.add(creatureOrigem);

                                                        /* incrementa o numero de zombies destruidos */
                                                        creatureDestino.countZombiesDestruidos();
                                                        creatureOrigem.setZombieIsDestroyed(true);
                                                        if (creatureOrigem.zombieIsDestroyed()) {
                                                            System.out.println(creatureOrigem.toString());
                                                        }
                                                        incrementarTurno();
                                                        return true;
                                                    }

                                                case 2:
                                                    /* Interação com a Pistola */
                                                    if (creatureDestino.getIdTipo() == 5 || creatureDestino.getIdTipo() == 6
                                                            || creatureDestino.getIdTipo() == 7 || creatureDestino.getIdTipo() == 8) {

                                                        if (creatureDestino.equipamentos.get(0).getCountUsos() == 0) {
                                                            /* A pistola não tem efeito contra Zombies Vampiros */
                                                            /* vamos transformar o vivo em zombie */
                                                            creatureOrigem.transformaEmZombie(creatureDestino);
                                                            creatureDestino.setTransformado(true);
                                                            creatureOrigem.countTransformacoesFeitasPorZombies();
                                                            /* e destrui-mos o equipamento*/
                                                            creatureDestino.getEquipamentosVivos().remove(creatureDestino.equipamentos.get(0));
                                                            /* e colocamos o numero de equipamentos que tinha antes a zero */
                                                            creatureDestino.incrementaSemEquipamentoDepoisDeTransformado(0);
                                                            incrementarTurno();
                                                            return true;
                                                        }

                                                        if (creatureOrigem.getIdTipo() != 4) {
                                                            /* diminui uma bala */
                                                            creatureDestino.equipamentos.get(0).diminuiCountUsos();

                                                            /* incrementa o numero de salvacao feita pelo equipamento */
                                                            creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();

                                                            /* vamos destruir o zombie */
                                                            creatures.remove(creatureOrigem);
                                                            zombiesDestruidos.add(creatureOrigem);

                                                            /* incrementa o numero de zombies destruidos */
                                                            creatureDestino.countZombiesDestruidos();
                                                            creatureOrigem.setZombieIsDestroyed(true);
                                                            if (creatureOrigem.zombieIsDestroyed()) {
                                                                System.out.println(creatureOrigem.toString());
                                                            }
                                                            incrementarTurno();
                                                            return true;

                                                        } else if (creatureOrigem.getIdTipo() == 4 && !isDay()) {
                                                            /* A pistola não tem efeito contra Zombies Vampiros */
                                                            /* vamos transformar o vivo em zombie */
                                                            creatureOrigem.transformaEmZombie(creatureDestino);
                                                            creatureDestino.setTransformado(true);
                                                            creatureOrigem.countTransformacoesFeitasPorZombies();
                                                            /* e destrui-mos o equipamento*/
                                                            creatureDestino.getEquipamentosVivos().remove(creatureDestino.equipamentos.get(0));
                                                            /* e colocamos o numero de equipamentos que tinha antes a zero */
                                                            creatureDestino.incrementaSemEquipamentoDepoisDeTransformado(0);
                                                            incrementarTurno();
                                                            return true;
                                                        } else if (creatureOrigem.getIdTipo() == 4 && isDay()) {
                                                            return false;
                                                        }
                                                    }

                                                case 3:
                                                    /* Interação com o Escudo Tático */
                                                    /* Protecao contra varios ataques */
                                                    /* incrementa o numero de salvações */
                                                    creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();
                                                    incrementarTurno();
                                                    return true;

                                                case 4:
                                                    /* Interação com a Revista Maria */
                                                    /* Protege contra ataques de zombies idosos */
                                                    if (creatureOrigem.getIdTipo() == 3) {
                                                        /* incrementa o numero de salvações */
                                                        creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();
                                                    } else {
                                                        /* Não protege de outros zombies */
                                                        /* Vivo tranforma-se (->) em Zombie */
                                                        creatureOrigem.transformaEmZombie(creatureDestino);
                                                        creatureDestino.setTransformado(true);
                                                        creatureOrigem.countTransformacoesFeitasPorZombies();
                                                        /* e destrui-mos o equipamento */
                                                        creatureDestino.getEquipamentosVivos().remove(creatureDestino.equipamentos.get(0));
                                                        /* e colocamos o numero de equipamentos que tinha antes a zero */
                                                        creatureDestino.incrementaSemEquipamentoDepoisDeTransformado(0);
                                                    }
                                                    incrementarTurno();
                                                    return true;

                                                case 5:
                                                    /* Interação com a Cabeça de alho */
                                                    /* Se forem outros zombies */
                                                    if (creatureOrigem.getIdTipo() != 4) {
                                                        /* Não protege de outros zombies */
                                                        /* Vivo tranforma-se (->) em Zombie */
                                                        creatureOrigem.transformaEmZombie(creatureDestino);
                                                        creatureDestino.setTransformado(true);
                                                        creatureOrigem.countTransformacoesFeitasPorZombies();
                                                        /* e destrui-mos o equipamento */
                                                        creatureDestino.getEquipamentosVivos().remove(creatureDestino.equipamentos.get(0));
                                                        /* e colocamos o numero de equipamentos que tinha antes a zero */
                                                        creatureDestino.incrementaSemEquipamentoDepoisDeTransformado(0);
                                                        incrementarTurno();
                                                        return true;
                                                    } else if (creatureOrigem.getIdTipo() == 4 && !isDay()) {
                                                        /* Protege contra ataques de zombies Vampiros */
                                                        /* incrementa o numero de salvações */
                                                        creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();
                                                        incrementarTurno();
                                                        return true;
                                                    } else if (creatureOrigem.getIdTipo() == 4 && isDay()) {
                                                        return false;
                                                    }

                                                case 6:
                                                    /* Interação com a Estaca de madeira */
                                                    if (creatureDestino.getIdTipo() == 5 || creatureDestino.getIdTipo() == 6
                                                            || creatureDestino.getIdTipo() == 7 || creatureDestino.getIdTipo() == 8) {
                                                        /* vamos destruir o zombie */
                                                        creatures.remove(creatureOrigem);
                                                        zombiesDestruidos.add(creatureOrigem);

                                                        /* incrementa o numero de zombies destruidos */
                                                        creatureDestino.countZombiesDestruidos();
                                                        creatureOrigem.setZombieIsDestroyed(true);
                                                        if (creatureOrigem.zombieIsDestroyed()) {
                                                            System.out.println(creatureOrigem.toString());
                                                        }

                                                        /* incrementa o numero de salvacao feita pelo equipamento */
                                                        creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();

                                                        incrementarTurno();
                                                        return true;
                                                    }

                                                case 7:/* Interação com a Lixivia */
                                                    if (creatureDestino.equipamentos.get(0).getCountUsos() == 0) {
                                                        /* vamos transformar o vivo em zombie */
                                                        creatureOrigem.transformaEmZombie(creatureDestino);
                                                        creatureDestino.setTransformado(true);
                                                        creatureOrigem.countTransformacoesFeitasPorZombies();
                                                        /* e destrui-mos o equipamento */
                                                        creatureDestino.getEquipamentosVivos().remove(creatureDestino.equipamentos.get(0));
                                                        /* e colocamos o numero de equipamentos que tinha antes a zero */
                                                        creatureDestino.incrementaSemEquipamentoDepoisDeTransformado(0);
                                                        incrementarTurno();
                                                        return true;
                                                    }

                                                    /* diminuimos a protecao ... */
                                                    creatureDestino.equipamentos.get(0).diminuiCountUsos();

                                                    /* incrementa o numero de salvações */
                                                    creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();

                                                    incrementarTurno();
                                                    return true;

                                                case 8:
                                                    /* Interação com o veneno */
                                                    incrementarTurno();
                                                    return true;
                                                case 9:
                                                case 10:
                                                    /* Interação com o antidoto */
                                                    /* Interação com o capacete Beskar Helmet */
                                                    /* incrementa o numero de salvações */
                                                    creatureDestino.equipamentos.get(0).incrementaNrSalvacoes();
                                                    incrementarTurno();
                                                    return true;
                                            }
                                        }
                                    }
                                }

                                /* processa o combate com equipamento ofensivo */
                                /* VIVO vs ZOMBIE */
                                boolean movimentoValido = creatureOrigem.processarCombateOfensivo(xO, yO, xD, yD, creatureDestino,
                                        creatures);

                                if (!movimentoValido) {
                                    return false;
                                } else {
                                    creatureDestino.incrementaCreaturesNoBolso();
                                    break;
                                }
                            }
                        }
                    }

                    /* AÇÃO = APANHAR E LARGAR OU DESTRUIR EQUIPAMENTO */
                    /* caso na posicao destino nao tiver uma criatura
                     verifica se é um equipamento */
                    if (creatureOrigem.getIdEquipa() == 10) {
                        if (creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                            for (Equipamento eq : equipamentos) {
                                if (eq.getXAtual() == xD && eq.getYAtual() == yD) {
                                    // verificar se o humano tem equipamentos
                                    if (creatureOrigem.getEquipamentosVivos().size() == 0) {

                                        // se for militar e escudo de madeira, entao a protecao aumenta
                                        if (creatureOrigem.getIdTipo() == 7 && eq.getIdTipo() == 0) {
                                            // verifica se foi a primeira vez a usar
                                            if (!eq.isEscudoUsado()) {
                                                eq.aumentaProtecaoDoEscudo();
                                            }
                                        }

                                        /* Apenas podem apanhar o antidoto os 'vivos' que estejam envenenados */
                                        if (!creatureOrigem.isEnvenenado()) {
                                            if (eq.getIdTipo() == 9) {
                                                return false;
                                            }
                                        }

                                        /* Se idoso tentar apanhar equipamento a noite retorna falso */
                                        if (creatureOrigem.getIdTipo() == 8 && isDay() == false) {
                                            if (eq.getIdTipo() == 0 || eq.getIdTipo() == 1 || eq.getIdTipo() == 2 || eq.getIdTipo() == 3 || eq.getIdTipo() == 4 ||
                                                    eq.getIdTipo() == 5 || eq.getIdTipo() == 6 || eq.getIdTipo() == 7 || eq.getIdTipo() == 8 || eq.getIdTipo() == 9 || eq.getIdTipo() == 10) {
                                                return false;
                                            }
                                        }

                                        /* Se coelho tentar apanhar algum equipamento, retorna falso */
                                        if (creatureOrigem.getIdTipo() == 12) {
                                            if (eq.getIdTipo() == 0 || eq.getIdTipo() == 1 || eq.getIdTipo() == 2 || eq.getIdTipo() == 3 || eq.getIdTipo() == 4 ||
                                                    eq.getIdTipo() == 5 || eq.getIdTipo() == 6 || eq.getIdTipo() == 7 || eq.getIdTipo() == 8 || eq.getIdTipo() == 9 || eq.getIdTipo() == 10) {
                                                return false;
                                            }
                                        }

                                        /* senão, se tiver equipamento vamos removê-lo antes de apanhar o novo */
                                    } else {

                                        // se for militar e escudo de madeira, entao a protecao aumenta
                                        if (creatureOrigem.getIdTipo() == 7 && eq.getIdTipo() == 0) {
                                            // verifica se foi a primeira vez a usar
                                            if (!eq.isEscudoUsado()) {
                                                eq.aumentaProtecaoDoEscudo();
                                            }
                                        }

                                        /* Apenas podem apanhar o antidoto os 'vivos' que estejam envenenados */
                                        if (!creatureOrigem.isEnvenenado()) {
                                            if (eq.getIdTipo() == 9) {
                                                return false;
                                            }
                                        }

                                        // guardamos o equipamento existente na lista de equipamentos
                                        Equipamento eqAntigo = creatureOrigem.equipamentos.get(0);
                                        equipamentos.add(eqAntigo);
                                        // removemos esse equipamento e devolvemos na posicao original
                                        creatureOrigem.equipamentos.get(0).setXAtual(creatureOrigem.getXAtual());
                                        creatureOrigem.equipamentos.get(0).setYAtual(creatureOrigem.getYAtual());
                                        creatureOrigem.getEquipamentosVivos().remove(eqAntigo);
                                    }

                                    // depois de removido, o vivo apanha o novo equipamento
                                    // e fica com o equipamento novo na mão
                                    creatureOrigem.equipamentos.add(eq);
                                    System.out.println("Apanhou: " + eq);

                                    // Incrementa o equipamento no bolso
                                    creatureOrigem.incrementaEquipamentosNoBolso();

                                    if (creatureOrigem.getEquipamentosVivos().get(0).getIdTipo() == 8) {
                                        System.out.println("\n" + creatureOrigem.getTipo() + " está envenenado." + "\n"
                                                + "Warning: Se o 'Vivo' estiver envenenado durante 3 turnos, morre. " +
                                                "\n" + "Tem mais uma jogada, tente encontrar o antidoto.");
                                    }

                                    if (creatureOrigem.getEquipamentosVivos().get(0).getIdTipo() == 9) {
                                        System.out.println("\n" + creatureOrigem.getTipo() + " conseguiu o antídoto a tempo, " +
                                                "está curado. Encontre o safeHaven e salve-se...");
                                    }

                                    encontrouEquip = true;
                                    break;
                                }
                            }
                        } else {
                            return false;
                        }

                        // se for da equipa dos zombies // e se for para cima do equipamento // vamos destrui-lo
                    } else if (creatureOrigem.getIdEquipa() == 20) {
                        if (creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                            for (Equipamento eq : equipamentos) {
                                if (eq.getXAtual() == xD && eq.getYAtual() == yD) {

                                    /* Veneno não pode ser destruido, zombies não podem mover para casas com veneno */
                                    if (eq.getIdTipo() == 8) {
                                        return false;
                                    }

                                    /* Zombie Vampiro nao gosta de alho, logo não pode ser destruido */
                                    if (creatureOrigem.getIdTipo() == 4 && !isDay() && eq.getIdTipo() == 5) {
                                        return false;
                                    }

                                    /* Zombie Vampiro de dia, se tentar destruir algum equipamento, retorna false */
                                    if (creatureOrigem.getIdTipo() == 4 && isDay()) {
                                        if (eq.getIdTipo() == 0 || eq.getIdTipo() == 1 || eq.getIdTipo() == 2 || eq.getIdTipo() == 3 || eq.getIdTipo() == 4 ||
                                                eq.getIdTipo() == 5 || eq.getIdTipo() == 6 || eq.getIdTipo() == 7 || eq.getIdTipo() == 8 || eq.getIdTipo() == 9 || eq.getIdTipo() == 10) {
                                            return false;
                                        }
                                    }

                                    // Adiciona nos equipamentos destruidos
                                    // Destroi os equipamento
                                    // Move uma posicao
                                    creatureOrigem.destruidos.add(eq);

                                    /* Removemos o equipamento */
                                    equipamentos.remove(eq);

                                    /* Incrementa o equipamento no bolso */
                                    creatureOrigem.incrementaEquipamentosNoBolso();

                                    encontrouEquip = true;
                                    break;
                                }
                            }
                        } else {
                            return false;
                        }
                    }

                    /* Interação com o Veneno e Antidoto */
                    if (creatureOrigem.getIdEquipa() == 10) {
                        for (Equipamento eq : equipamentos) {
                            if (!encontrouEquip) {
                                if (eq.getXAtual() == xO && eq.getYAtual() == yO) {
                                    /* Se o vivo apanhar um veneno */
                                    if (eq.getIdTipo() == 8) {
                                        //nrTurnoDoVeneno++;
                                        /* dizemos que o vivo está envenenado */
                                        creatureOrigem.setEnvenenado(true);

                                        if (nrTurnoDoVeneno > 1) {
                                            if (creatureOrigem.isEnvenenado()) {
                                                /* Se for idoso e for dia morre envenenado */
                                                if (creatureOrigem.getIdTipo() == 8) {
                                                    if (isDay() == true) {
                                                        nrTurnoDoVeneno = 0;
                                                        creatures.remove(creatureOrigem);
                                                        criaturasEnvenenadas.add(creatureOrigem);
                                                        creatureOrigem.setHumanDeadPorEnvenenamento(true);
                                                        incrementarTurno();
                                                        return true;
                                                    } else {
                                                        return false;
                                                    }
                                                } else if (creatureOrigem.getIdTipo() != 8) {
                                                    nrTurnoDoVeneno = 0;
                                                    creatures.remove(creatureOrigem);
                                                    criaturasEnvenenadas.add(creatureOrigem);
                                                    creatureOrigem.setHumanDeadPorEnvenenamento(true);
                                                    incrementarTurno();
                                                    return true;
                                                }

                                            } else {
                                                return false;
                                            }
                                        }
                                        /* Se apanhar o antidoto, fica curado */
                                    } else if (eq.getIdTipo() == 9) {
                                            nrTurnoDoVeneno = 0;
                                            creatureOrigem.setEnvenenado(false);
                                        }
                                    }
                                }
                            }
                        }

                    /* TIRAR EQUIPAMENTO DA ORIGEM */
                    // se encontrou equipamento removemos esse o equipamento da sua casa original e
                    // quando o "vivo" sai da posicao que apanhou o equipamento
                    // o equipamento que estava na origem antes, desaparece do mapa, para coordenadas que possam
                    // não existir no jogo (dimensao do mapa)
                    for (Equipamento eq : equipamentos) {
                        if (creatureOrigem.getIdEquipa() == 10 && creatureOrigem.getIdTipo() != 8) {
                            if (!encontrouEquip) {
                                if (eq.getXAtual() == xO && eq.getYAtual() == yO) {
                                    if (creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                        // removemos o equipamento para coordenadas que possam
                                        // não existir no jogo (dimensao do mapa), para que ele não desapareca do mapa
                                        eq.setXAtual(xO+30);
                                        eq.setYAtual(yO+30);
                                    }
                                }
                            }
                        }
                    }

                    /* Movimentação a partir do idoso */
                    if (creatureOrigem.getIdEquipa() == 10) {
                        /* Idosos humanos só se movem em turnos diurnos */
                        if (creatureOrigem.getIdTipo() == 8 && isDay()) {
                            creatureOrigem.setxAtual(xD);
                            creatureOrigem.setyAtual(yD);
                            for (Equipamento eq : equipamentos) {
                                if (eq.getXAtual() == xD && eq.getYAtual() == yD) {
                                    /* caso o idoso encontre o equipamento, deve-o apanhar */
                                    creatureOrigem.equipamentos.add(eq);
                                }
                                /* quando se mover para fora dessa casa, deve-o largar */
                                creatureOrigem.getEquipamentosVivos().remove(eq);
                            }
                            incrementarTurno();
                            return true;

                        } else {

                            /* Se forem outras criaturas vivas */
                            if (creatureOrigem.getIdTipo() != 8) {

                                if (!saltarPorCima(xO, yO, xD, yD) && creatureOrigem.getIdTipo() != 5 && creatureOrigem.getIdTipo() != 8) {
                                    return false;
                                }

                                /* CRIANÇA VIVA */
                                if (creatureOrigem.getIdTipo() == 5 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* ADULTO VIVO */
                                if (creatureOrigem.getIdTipo() == 6 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* MILITAR VIVO */
                                if (creatureOrigem.getIdTipo() == 7 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* CAO */
                                if (creatureOrigem.getIdTipo() == 9 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* COELHO */
                                if (creatureOrigem.getIdTipo() == 12 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                creatureOrigem.setxAtual(xD);
                                creatureOrigem.setyAtual(yD);
                                incrementarTurno();
                                return true;
                            }
                            /* se idoso andar de noite retorna falso */
                            return false;
                        }
                    }

                    /* Movimentação a partir do Zombie Vampiro */
                    if (creatureOrigem.getIdEquipa() == 20) {
                        /* Zombie Vampiro só se movem em turnos nocturnos */
                        if (creatureOrigem.getIdTipo() == 4 && !isDay()) {

                            if (!saltarPorCima(xO, yO, xD, yD) && creatureOrigem.getIdTipo() != 0 && creatureOrigem.getIdTipo() != 3) {
                                return false;
                            }

                            if (!creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                return false;
                            }

                            /* Zombies nao se podem mover para o Safe Haven */
                            if (isDoorToSafeHaven(xD, yD)) {
                                return false;
                            }

                            creatureOrigem.setxAtual(xD);
                            creatureOrigem.setyAtual(yD);
                            incrementarTurno();
                            return true;

                        } else {

                            /* Se forem outros zombies */
                            if (creatureOrigem.getIdTipo() != 4) {

                                if (!saltarPorCima(xO, yO, xD, yD) && creatureOrigem.getIdTipo() != 5 && creatureOrigem.getIdTipo() != 8) {
                                    return false;
                                }

                                /* CRIANÇA ZOMBIE */
                                if (creatureOrigem.getIdTipo() == 0 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* ADULTO ZOMBIE */
                                if (creatureOrigem.getIdTipo() == 1 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* MILITAR ZOMBIE */
                                if (creatureOrigem.getIdTipo() == 2 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* IDOSO ZOMBIE */
                                if (creatureOrigem.getIdTipo() == 3 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* COELHO ZOMBIE */
                                if (creatureOrigem.getIdTipo() == 13 && !creatureOrigem.moveDirecao(xO, yO, xD, yD, creatureOrigem)) {
                                    return false;
                                }

                                /* Zombies nao se podem mover para o Safe Haven */
                                if (isDoorToSafeHaven(xD, yD)) {
                                    return false;
                                }

                                creatureOrigem.setxAtual(xD);
                                creatureOrigem.setyAtual(yD);
                                incrementarTurno();
                                return true;
                            }
                            /* se Zombie Vampiro andar de dia retorna falso */
                            return false;
                        }
                    }



                   /* TODO falta implementar bem o deslocamento em turnos do coelho - erros no DropProjet */

                }
            }
        }
        return false;
    }

    protected boolean saltarPorCima(int xO, int yO, int xD, int yD) {
        /* verifica direcao em que a critura está a tentar saltar por cima */
        String direcao = moverTodasDirecoes(xO, xD, yO, yD);
        int diffX = 0;
        int diffY = 0;

        int meioX = 0;
        int meioY = 0;
        /* se for horizontal significa que a diferenca do Y é o meio */
        switch (direcao) {
            case "horizontal":
                diffX = Math.abs(xD - xO);
                meioY = yO;
                if (xD < xO) {
                    meioX = xD + (diffX / 2);
                } else {
                    meioX = xO + (diffX / 2);
                }

                break;

            case "vertical":
                diffY = Math.abs(yD - yO);
                meioX = xO;

                if (yD < yO) {
                    meioY = yD + (diffY / 2);
                } else {
                    meioY = yO + (diffY / 2);
                }

                break;

            case "diagonal":
                diffX = Math.abs(xD - xO);
                diffY = Math.abs(yD - yO);

                if (xD < xO) {
                    meioX = xD + (diffX / 2);
                } else {
                    meioX = xO + (diffX / 2);
                }

                if (yD < yO) {
                    meioY = yD + (diffY / 2);
                } else {
                    meioY = yO + (diffY / 2);
                }

                break;
        }

        /* Verificar se o deslocamento da criatura que está a ser movida
        * Verificar se ao mover existe uma criatura ou equipamento no meio */
        if (Math.abs(xO - xD) <= 2 && Math.abs(yO - yD) <= 4 || Math.abs(xD - xO) <= 3 && Math.abs(yD - yO) <= 3 ||
                xO - xD > 2 && yD - yO > 2 || xD - xO > 2 && yD - yO > 2 || xD - xO > 2 && Math.abs(yD - yO) == 0
                || xD - xO > 1 && yO - yD > 1 || xO - xD > 1 && yD - yO > 1 || xO - xD > 1 && yO - yD > 1
                || xD - xO > 1 && yD - yO > 1 || xD - xO > 1 && Math.abs(yD - yO) == 0 || xO - xD > 1 && Math.abs(yD - yO) == 0
                || yD - yO > 1 && Math.abs(xD - xO) == 0 || yO - yD > 1 && Math.abs(xD - xO) == 0) {
            // verifica se uma criatura ou equipamento esta naquela posicao
            for (Creature creature : getCreatures()) {
                if (creature.getXAtual() == meioX && creature.getYAtual() == meioY) {
                    return false;
                }
            }

            for (Equipamento equipamento : equipamentos) {
                if (equipamento.getXAtual() == meioX && equipamento.getYAtual() == meioY) {
                    return false;
                }
            }
        }

        return true;
    }

    protected String moverTodasDirecoes(int xO, int xD, int yO, int yD) {
        if (Math.abs(xD - xO) > 1 && Math.abs(yD - yO) == 0) {
            return "horizontal";
        } else if (Math.abs(xD - xO) == 0 && Math.abs(yD - yO) > 1) {
            return "vertical";
        } else if (Math.abs(xD - xO) > 1 && Math.abs(yD - yO) > 1) {
            return "diagonal";
        }

        return "";
    }

    public boolean gameIsOver() {
        int nrMaxDiaENoite = 6;
        int numeroVivosEmJogo = 0;
        int countTodosMenosIdosoEmJogo = 0;

        for (Creature creatureOrigem : creatures) {
            /* se houver transformacão o jogo continua */
            if (creatureOrigem.getNumTransformacoesFeitasPorZombies() >= 1 && nrTurno >= 12) {
                nrTurno--;
            }

            /* se até ao nrturno 12 não houver nenhuma transformação o jogo termina */
            if (creatureOrigem.getNumTransformacoesFeitasPorZombies() == 0 && nrTurno >= 12) {
                return true;
            }
        }

        /* Sem vivos em jogo */
        for (Creature creatureOrigem : creatures) {
            if (creatureOrigem.getIdEquipa() == 10) {
                /* Se existirem "Vivos" e não passaram para o SafeHaven ou não foram transformado em Zombie */
                if (!creatureOrigem.isInSafeHaven() && !creatureOrigem.isTransformado()
                        && !creatureOrigem.isEnvenenado()) {
                    /* Ou não morreram envenenados, conta os "Vivos" que ainda estão em Jogo */

                    switch (creatureOrigem.getIdTipo()) {
                        case 5:
                        case 6:
                        case 7:
                        case 9:
                            countTodosMenosIdosoEmJogo++;
                    }

                    numeroVivosEmJogo++;
                }
            }
        }

        /* Se ficarem apenas os vivos em jogo, o jogo termina */
        if (numeroVivosEmJogo == 0) {
            return true;
        }

        /* Apenas idosos vivos em jogo no turno nocturno, o jogo termina */
        if (getCurrentTeamId() == 10) {
            if (!isDay()) {
                return countTodosMenosIdosoEmJogo == 0;
            } else {
                return false;
            }
        }

        int numeroZombiesEmJogo = 0;
        int countTodosMenosZombieVampEmJogo = 0;

        /* Sem zombies em jogo */
        for (Creature creatureOrigem : creatures) {
            if (creatureOrigem.getIdEquipa() == 20) {
                /* Se existirem 'zombies' e ainda não foram destruidos */
                if (!creatureOrigem.zombieIsDestroyed()) {
                    /* conta os "zombies" que ainda estão em Jogo*/

                    switch (creatureOrigem.getIdTipo()) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            countTodosMenosZombieVampEmJogo++;
                    }

                    numeroZombiesEmJogo++;
                }
            }
        }

        /* Se ficarem apenas os zombies em jogo, o jogo termina */
        if (numeroZombiesEmJogo == 0) {
            return true;
        }

        /* Apenas Zombie Vampiro em jogo no turno diurno , jogo termina*/
        if (getCurrentTeamId() == 20) {
            if (isDay()) {
                return countTodosMenosZombieVampEmJogo == 0;
            } else {
                return false;
            }
        }

        /* O jogo termina se tiverem passados 3 dias e 3 noites */
        return nrTurno/2 >= nrMaxDiaENoite;
    }

    public List<String> getAuthors() {
        List<String> creditos = new ArrayList<>();
        creditos.add("Lodney Santos " + "|" + " nº: " + "21505293");
        creditos.add("Jolina Guvulo " + "|" + " nº: " + "21802650");
        return creditos;
    }

    public int getCurrentTeamId() {
        return idEquipaAtual;
    }

    public int getElementId(int x, int y) {
        for (Creature c: creatures){
            if (c.getXAtual() == x && c.getYAtual() == y && !c.isInSafeHaven()) {
                return c.getId();
            }
        }

        for (Equipamento e : equipamentos){
            if (e.getXAtual() == x && e.getYAtual() == y){
                //System.out.println(e.getId());
                return e.getId();
            }
        }

        return 0;
    }

    public List<String> getGameResults() {

        List<String> resultados = new ArrayList<>();

        resultados.add("Nr. de turnos terminados:");
        resultados.add(String.valueOf(nrTurno));
        resultados.add("");

        resultados.add("Ainda pelo bairo:");
        resultados.add("");

        resultados.add("OS VIVOS");
        for (Creature creatureVivos: creatures){
            if (creatureVivos.getIdEquipa() == 10) {
                if(!creatureVivos.isInSafeHaven() && !creatureVivos.isTransformado() && !creatureVivos.isEnvenenado()) {
                    resultados.add(creatureVivos.getId() + " " + creatureVivos.getNome());
                }
            }
        }

        resultados.add("");
        resultados.add("OS OUTROS");
        for (Creature creatureZombies : creatures){
            if (creatureZombies.getIdEquipa() == 20) {
                resultados.add(creatureZombies.getId() + " (antigamente conhecido como " + creatureZombies.getNome() + ")");
            }
        }

        resultados.add("");
        resultados.add("Num safe haven:");
        resultados.add("");

        resultados.add("OS VIVOS");
        for (Creature creatureVivos: safe ) {
            if (creatureVivos.getIdEquipa() == 10) {
                if (creatureVivos.isInSafeHaven()) {
                    resultados.add(creatureVivos.getId() + " " + creatureVivos.getNome());
                }
            }
        }

        resultados.add("");
        resultados.add("Envenenados / Destruidos");

        resultados.add("");
        resultados.add("OS VIVOS");
        for (Creature creatureVivos: criaturasEnvenenadas) {
            if (creatureVivos.getIdEquipa() == 10) {
                if(creatureVivos.isEnvenenado()) {
                    resultados.add(creatureVivos.getId() + " " + creatureVivos.getNome());
                }
            }
        }

        resultados.add("");
        resultados.add("OS OUTROS");
        for (Creature creatureZombies : zombiesDestruidos){
            if (creatureZombies.getIdEquipa() == 20) {
                resultados.add(creatureZombies.getId() + " (antigamente conhecido como " + creatureZombies.getNome() + ")");
            }
        }

        return resultados;
    }

    // TODO incompleto , isDay() está a devolver false erradamente - 2 erros no DropProjet
    public boolean isDay() {

        switch (nrTurno) {
            case 0:
            case 1:
            case 4:
            case 5:
            case 8:
            case 9:
            case 12:
            case 13:
            case 16:
            case 17:
            case 20:
            case 21:
                diurno = true;
                break;
            case 2:
            case 3:
            case 6:
            case 7:
            case 10:
            case 11:
            case 14:
            case 15:
            case 18:
            case 19:
            case 22:
                diurno = false;
                break;
        }

        /*if (nrTurno == 0 || nrTurno == 1 || nrTurno == 4 || nrTurno == 5 || nrTurno == 8 || nrTurno == 9) {
            diurno = true;
        } else if (nrTurno % 2 == 0) {
            if (!diurno) {
                diurno = true;
            } else {
                diurno = false;
            }
        }*/

        System.out.println(nrTurno);

        return diurno;
    }

    public int getEquipmentId(int creatureId) {
        /* verifica se o criatura tem o equipamento */
        for (Creature creature: creatures) {
            if (creature.getId() == creatureId) {
                if (creature.getEquipamentosVivos().size() != 0) {
                    return creature.getEquipamentosVivos().get(0).getId();
                }
            }
        }
        /* se nao tiver retorna 0 */
        return 0;
    }

    public List<Integer> getIdsInSafeHaven() {

        ArrayList<Integer> creaturesSafeHeavenID = new ArrayList<>();
        for (Creature creature : safe){
            creaturesSafeHeavenID.add(creature.getId());
        }
        return creaturesSafeHeavenID;
    }

    public boolean isDoorToSafeHaven(int x, int y) {
        /* Todas as portas guardadas na lista ...
        * se estiverem na posicao (x,y), retornamos true e mostramos as portas */
        for (Porta door : portasEmJogo) {
            if (door.getxAtual() == x && door.getyAtual() == y){
                return true;
            }
        }
        return false;
    }

    public int getEquipmentTypeId(int equipmentId){

        for (Equipamento equipamento: equipamentos){
            if (equipmentId == equipamento.getId()){
                return equipamento.getIdTipo();
            }
        }
        return 0;
    }

    public String getEquipmentInfo(int equipmentId) {

        for (Equipamento equipamento: equipamentos) {
            if (equipamento.getId() == equipmentId) {
                // Se os equipamentos forem escudo de madeira, pistola ou lixivia
                // <Nome Tipo> | <Info>
                if (equipamento.getIdTipo() == 0) {
                    return equipamento.getTitulo() + " | " + equipamento.getCountProtecaoDoEscudo();
                } else if (equipamento.getIdTipo() == 2) {
                    return equipamento.getTitulo() + " | " + equipamento.getCountUsos();
                } else if (equipamento.getIdTipo() == 7) {
                    return equipamento.getTitulo() + " | " + equipamento.getCountUsos();
                }
                // Caso nao for, retorna apenas <Nome Tipo>
                return equipamento.getTitulo();
            }
        }
        return null;
    }

    /* TODO falta implementar como gravar vivo com equipamento e dentro do safeHaven - 1 erro no DropProjet */
    public boolean saveGame(File fich) {

        /* retorna o separador de linha, ou seja será a quebra de linha quando chegar a final de uma linha lida */
        String nextLine = System.lineSeparator();

        try {
            FileWriter salvarFich = new FileWriter(fich.getAbsoluteFile());

            salvarFich.write(numLinha + " " + numColuna);
            salvarFich.write(nextLine);
            salvarFich.write(getCurrentTeamId() + "");
            salvarFich.write(nextLine);

            salvarFich.write(getCreatures().size() + "");
            salvarFich.write(nextLine);

            for(Creature criatura : getCreatures()) {
                    salvarFich.write(criatura.getId() + " : " + criatura.getIdTipo() + " : " + criatura.getNome() + " : "
                            + criatura.getXAtual() + " : " + criatura.getYAtual());

                    salvarFich.write(nextLine);
            }

            salvarFich.write(equipamentos.size() + "");
            salvarFich.write(nextLine);

            for(Equipamento objeto : equipamentos) {
                salvarFich.write(objeto.getId() + " : " + objeto.getIdTipo()+ " : " + objeto.getXAtual() + " : "
                        + objeto.getYAtual());

                salvarFich.write(nextLine);
            }

            salvarFich.write(portasEmJogo.size() + "");
            salvarFich.write(nextLine);

            for(Porta p : portasEmJogo) {
                salvarFich.write(p.getxAtual() + " : " + p.getyAtual());

                salvarFich.write(nextLine);
            }

            salvarFich.close();
            System.out.println("\n" + "Jogo guardado com sucesso ...");
            return true;

        } catch (IOException e) {
            System.out.println("Erro.: Não foi possível o guardar o jogo no ficheiro " + fich.getName() );
            return false;
        }
    }

    /* TODO falta implementar como carregar vivo com equipamento e dentro do safeHaven - 1 erro no DropProjet */
    public boolean loadGame(File fich) {

        try {
            startGame(fich);
            System.out.println("\n" + "Jogo carregado com sucesso ...");
            return true;

        } catch (IOException | InvalidTWDInitialFileException e) {
            System.out.println("Erro.: Não foi possível carregar o jogo guardado do ficheiro " + fich.getName());
            return false;
        }
    }

    public String[] popCultureExtravaganza() {

        String[] resposta = new String[14];

        resposta[0] = "Resident Evil";
        resposta[1] = "Evil Dead";
        resposta[2] = "I Am Legend";
        resposta[3] = "I Am Legend";
        resposta[4] = "Dragon Ball";
        resposta[5] = "World War Z";
        resposta[6] = "Mandalorians";
        resposta[7] = "1972";
        resposta[8] = "Kill Bill";
        resposta[9] = "1978";
        resposta[10] = "Bond, James Bond.";
        resposta[11] = "Lost";
        resposta[12] = "Chocho";
        resposta[13] = "Farrokh Bulsara";

        return resposta;
    }

    public Map<String, List<String>> getGameStatistics() {

        HashMap<String, List<String>> estatisticaDoJogo = new HashMap<>();

        estatisticaDoJogo.put("os3ZombiesMaisTramados", zombiesTramado());
        estatisticaDoJogo.put("os3VivosMaisDuros", vivosDuros());
        estatisticaDoJogo.put("tiposDeEquipamentoMaisUteis", equipamentosUteis());
        estatisticaDoJogo.put("tiposDeZombiesESeusEquipamentosDestruidos", tiposZombiesEEquipamentosDestruidos());
        estatisticaDoJogo.put("criaturasMaisEquipadas", criaturasMaisEquipamentosApanharam());

        return estatisticaDoJogo;
    }

    /* <IDCriatura>:<Nome>:<NrTransformações> */
    /* Quais os 3 zombies que mais vivos transformaram? */
    private List<String> zombiesTramado() {

        List<String> creturesZombies;

        creturesZombies = getCreatures().stream()
                   /* Filtrar apenas os zombies */
                   .filter((zombiesSemPiedade) -> zombiesSemPiedade.getIdEquipa() == 20)
                   /* Filtrar zombies que tiverem pelo menos 1 ou mais transformacoes */
                   .filter((t) -> t.getNumTransformacoesFeitasPorZombies() >= 1)
                   /* Ordena por ordem decrescente */
                   .sorted ((z1, z2) -> z2.getNumTransformacoesFeitasPorZombies() - z1.getNumTransformacoesFeitasPorZombies())
                   /* Top 3 zombies*/
                   .limit(3)
                   /* Transforma os elementos em string */
                   .map( (c) -> c.getId() + ":" + c.getNome() + ":" + c.getNumTransformacoesFeitasPorZombies())
                   /* Transforma o resultado final em lista */
                   .collect(toList());

        return creturesZombies;
    }

    /* <IDCriatura>:<Nome>:<NrDestruicoes> */
    /* Quais os 3 vivos que mais zombies destruiram */
    private List<String> vivosDuros() {

        List<String> creaturesVivos;

        creaturesVivos = getCreatures().stream()
                /* Filtrar apenas os vivos */
                .filter((vivosSemMedo) -> vivosSemMedo.getIdEquipa() == 10)
                /* Filtrar os vivos que têm pelo menos um zombie destruidos */
                .filter((destruidos) -> destruidos.getZombiesDestruidos() >= 1)
                /* Ordena por ordem descendente */
                .sorted ((v1, v2) -> v2.getZombiesDestruidos() - v1.getZombiesDestruidos())
                /* Top 3 */
                .limit(3)
                /* Transforma os elementos em string */
                .map( (c) -> c.getId() + ":" + c.getNome() + ":" + c.getZombiesDestruidos())
                /* Transforma o resultado final em lista */
                .collect(toList());

        return creaturesVivos;
    }

    /* <ID>:<NrSalvacoes> */
    /* Quais os equipamentos que mais safaram os vivos (of/def)? */
    private List<String> equipamentosUteis() {

        List<String> equipamentoQueSafaram;

        equipamentoQueSafaram = equipamentos.stream()
                /* filtrar apenas os equipamentos que salvaram */
                .filter((eq) -> eq.getNrSalvacoes() > 0)
                // ordena por ordem ascendente
                .sorted((eq2,eq1) -> eq1.getNrSalvacoes() - eq2.getNrSalvacoes())
                /* Transforma o resultado em strings */
                .map((eq)-> eq.getId() +":"+ eq.getNrSalvacoes())
                /* transforma o resultado final em formato de lista */
                .collect(toList());

        return equipamentoQueSafaram;
    }

    /* TODO falta implementar o desempate e o numero de equipamentos - 2 erros no DropProjet */
    /* <Nome do Tipo>:<ID_Zombies>: TODO <NrEquipamentos> */
    /* Qual o total de equipamentos destruidos por cada tipo de zombie? */
    private List<String> tiposZombiesEEquipamentosDestruidos() {

        List<String> zombieScore;

        zombieScore = getCreatures().stream()
                /* filtrar as criaturas zombies */
                .filter((zo) -> zo.getIdEquipa() == 20)
                /* filtrar os zombies que não foram destruidos */
                .filter((zomb) -> !zomb.zombieIsDestroyed())
                /* recolher os nomes do zombies e contar o numeros de zombies do mesmo tipo em jogo
                e converter num conjunto de dados */
                .collect(Collectors.groupingBy(Creature::getTipo, Collectors.counting()))
                /* O entrySet vai retornar o que está contido no collect, um set contido no mapa
                 e permitir obter chaves e valores contidos nesse map */
                .entrySet().stream()
                /* Ordenar do tipo que mais destruiu para o tipo que menos destruiu */
                .sorted((e1,e2)-> (int)(e2.getValue() - e1.getValue()))
                /* Transforma o resultado em strings */
                .map((z) -> z.getKey() +":"+ z.getValue())
                /* transforma o resultado final em formato de lista */
                .collect(toList());

    return zombieScore;

    }

    /* <IDCriatura>:<Nome>:<NrEquipamentos> */
    /* Quais as 5 criaturas que mais equipamentos apanharam/destruitam */
    private List<String> criaturasMaisEquipamentosApanharam() {

        List<String> osEquipados;

        osEquipados = getCreatures().stream()
                /* filtrar as criaturas que estão em jogo */
                .filter((criaturas) -> !criaturas.isInSafeHaven() && !criaturas.isEnvenenado()
                        && !criaturas.zombieIsDestroyed())
                /* filtrar zombies e vivos */
                .filter((vz) -> vz.getIdEquipa() == 10 || vz.getIdEquipa() == 20)
                /* Ordenar por ordem decrescente do numero de equipamentos */
                .sorted ((v1, v2) -> v2.getEquipamentosNoBolso() - v1.getEquipamentosNoBolso())
                /* Mostrar apenas 5 criaturas que mais equipamentos apanharam/destruiram */
                .limit(5)
                /* Transforma o resultado em strings */
                .map( (c) -> c.getId() + ":" + c.getNome() + ":" + c.getEquipamentosNoBolso())
                /* transforma o resultado final em formato de lista */
                .collect(toList());

        return osEquipados;
    }

    public void incrementarTurno() {
        nrTurno++;
        nrTurnoDoVeneno++;

        switch (idEquipaAtual) {
            case 10:
                idEquipaAtual = 20;
                break;
            case 20:
                idEquipaAtual = 10;
                break;
        }
    }

    public void incrementarReset() {

        creatures = new ArrayList<>(); // resent da lista de creatures.
        equipamentos = new ArrayList<>(); // reset da lista de equipamentos
        safe = new ArrayList<>(); // reset da lista safeHaven
        criaturasEnvenenadas = new ArrayList<>(); // reset da lista de criaturas envenedadas
        equipamentosEncontrados = new ArrayList<>(); // reset da lista de equipamentos encontrados
        zombiesDestruidos = new ArrayList<>(); // reset da lista de zombies destruidos
        portasEmJogo = new ArrayList<>(); // reset das portas em jogo

        numLinha = 0; // reset variavel numLinha.
        numColuna = 0; // reset variavel numColuna.
        xPortas = 0; // reset variavel xPortas safeHaven.
        yPortas = 0; // reset variavel yPortas safeHaven.
        nrTurno = 0; // reset variavel turnos do jogo.
        nrTurnoDoVeneno = 0; // reset variavel de turnos quando o vivo apanha o equipamento veneno
    }
}