package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Medico;
import models.Paciente;

// -> essa parte de gerenciamento de arquivos teve como base o trabalho de gerenciamento de arquivos

public class GerenciadorDeArquivos {

    private static final String DIRETORIO_DADOS = "banco_de_dados";

    // se não der certo o .txt com a interface gráfica, mudar pra .csv
    private static final String PATH_MEDICOS =  DIRETORIO_DADOS + File.separator + "medicos.txt";
    private static final String PATH_PACIENTES =  DIRETORIO_DADOS + File.separator + "pacientes.txt";
    private static final String PATH_CONSULTAS =  DIRETORIO_DADOS + File.separator + "consultas.txt";

    // Bloco estático: Executa automaticamente na primeira vez que esta classe for chamada (ideia do gepeto)
    static {
        verificarEInicializarArquivos();
    }

    private static void verificarEInicializarArquivos() {
        try {
            // 1. verificar e criar o diretório
            File diretorio = new File(DIRETORIO_DADOS);
            if (!diretorio.exists()) {
                if (diretorio.mkdirs()) {
                    System.out.println("Pasta de dados criada: " + DIRETORIO_DADOS);
                }
            }

            // 2. Verificar e criar cada arquivo individualmente
            criarArquivoSeNaoExistir(new File(PATH_MEDICOS));
            criarArquivoSeNaoExistir(new File(PATH_PACIENTES));
            criarArquivoSeNaoExistir(new File(PATH_CONSULTAS));

        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO: Não foi possível criar arquivos de banco de dados.");
            e.printStackTrace();
        }
    }

    private static void criarArquivoSeNaoExistir(File arquivo) throws IOException {
        if (!arquivo.exists()) {
            if (arquivo.createNewFile()) {
                System.out.println("Arquivo criado: " + arquivo.getName());
            }
        }
    }

    // metodos de leitura e escrita se manteram qyase enguais a ultima versao (que eu nao subi pro github entao vai so spawnar tudo isso de uma vez)

    // talvez depois seja jogo mudar o ; como separador, mas meeeeeh
    public static void salvarMedico(Medico medico) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_MEDICOS, true))) {
            String linha = medico.getId() + ";" + medico.getNome() + ";" + 
                           medico.getEspecialidade() + ";" + medico.getPlanoDeSaude();
            writer.write(linha);
            writer.newLine();
        }
    }

    public static List<Medico> carregarMedicos() throws IOException {
        List<Medico> lista = new ArrayList<>();
        File file = new File(PATH_MEDICOS);
        
        // se o arquivo estiver vazio ou não existir (embora o static crie, não sei em que mundo chegaria nesse erro), retorna lista vazia
        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue; // pula linhas em branco
                
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    Medico m = new Medico(Integer.parseInt(dados[0]));
                    m.setNome(dados[1]);
                    m.setEspecialidade(dados[2]);
                    m.setPlanoDeSaude(dados[3]);
                    lista.add(m);
                }
            }
        }
        return lista;
    }

    public static void salvarPaciente(Paciente paciente) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES, true))) {
            String linha = paciente.getId() + ";" + paciente.getNome() + ";" + 
                           paciente.getIdade() + ";" + paciente.getPlanoDeSaude();
            writer.write(linha);
            writer.newLine();
        }
    }

    public static List<Paciente> carregarPacientes() throws IOException {
        List<Paciente> lista = new ArrayList<>();
        File file = new File(PATH_PACIENTES);

        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    Paciente p = new Paciente(dados[1], Integer.parseInt(dados[2]));
                    p.setId(Integer.parseInt(dados[0]));
                    p.setPlanoDeSaude(dados[3]);
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}