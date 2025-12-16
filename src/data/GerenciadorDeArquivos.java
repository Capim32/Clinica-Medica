package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Consulta;
import models.Medico;
import models.Paciente;

public class GerenciadorDeArquivos {

    private static final String DIRETORIO_DADOS = "banco_de_dados";

    private static final String PATH_MEDICOS =  DIRETORIO_DADOS + File.separator + "medicos.txt";
    private static final String PATH_PACIENTES =  DIRETORIO_DADOS + File.separator + "pacientes.txt";
    private static final String PATH_CONSULTAS =  DIRETORIO_DADOS + File.separator + "consultas.txt";

    static {
        verificarEInicializarArquivos();
    }

    private static void verificarEInicializarArquivos() {
        try {
            File diretorio = new File(DIRETORIO_DADOS);
            if (!diretorio.exists()) diretorio.mkdirs();

            criarArquivoSeNaoExistir(new File(PATH_MEDICOS));
            criarArquivoSeNaoExistir(new File(PATH_PACIENTES));
            criarArquivoSeNaoExistir(new File(PATH_CONSULTAS));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void criarArquivoSeNaoExistir(File arquivo) throws IOException {
        if (!arquivo.exists()) arquivo.createNewFile();
    }

    // --- ID'S ---
    public static int getProximoIdMedico() {
        return lerMaxId(PATH_MEDICOS) + 1;
    }

    public static int getProximoIdPaciente() {
        return lerMaxId(PATH_PACIENTES) + 1;
    }

    private static int lerMaxId(String path) {
        int maxId = 0;
        File file = new File(path);
        if (!file.exists()) return 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(";");
                if (dados.length > 0) {
                    try {
                        int id = Integer.parseInt(dados[0]);
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException e) {}
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return maxId;
    }

    // --- MÉDICOS ---
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
        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
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
    
    public static void sobrescreverMedicos(List<Medico> medicos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_MEDICOS, false))) {
            for (Medico m : medicos) {
                String linha = m.getId() + ";" + m.getNome() + ";" + 
                               m.getEspecialidade() + ";" + m.getPlanoDeSaude();
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    // --- PACIENTES ---
    public static void salvarPaciente(Paciente paciente) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES, true))) {
            // ID;NOME;IDADE;DATA_NASC;PLANO
            String linha = paciente.getId() + ";" + paciente.getNome() + ";" + 
                           paciente.getIdade() + ";" + paciente.getDataNascimento() + ";" + 
                           paciente.getPlanoDeSaude();
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
                if (dados.length >= 5) {
                    // ID;NOME;IDADE;DATA_NASC;PLANO
                    Paciente p = new Paciente(dados[1], Integer.parseInt(dados[2]), dados[3]); 
                    p.setId(Integer.parseInt(dados[0]));
                    p.setPlanoDeSaude(dados[4]);
                    lista.add(p);
                }
            }
        }
        return lista;
    }
    
    public static void sobrescreverPacientes(List<Paciente> pacientes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES, false))) {
            for (Paciente p : pacientes) {
                String linha = p.getId() + ";" + p.getNome() + ";" + 
                               p.getIdade() + ";" + p.getDataNascimento() + ";" + 
                               p.getPlanoDeSaude();
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    // --- CONSULTAS ---
    public static void salvarConsulta(int idMedico, int idPaciente, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS, true))) {
            // adicionado ";null" no final para o prontuário vazio
            // ID_MED;ID_PAC;DATA;STATUS;VALOR;ESTRELAS;TEXTO;PRONTUARIO
            String linha = idMedico + ";" + idPaciente + ";" + data + ";AGENDADA;0.0;0;null;null";
            writer.write(linha);
            writer.newLine();
        }
    }

    public static List<Consulta> carregarConsultas() throws IOException {
        List<Consulta> lista = new ArrayList<>();
        File file = new File(PATH_CONSULTAS);
        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(";");
                // Agora esperamos 8 colunas
                if (dados.length >= 8) {
                    Consulta c = new Consulta(
                        Integer.parseInt(dados[0]),
                        Integer.parseInt(dados[1]),
                        dados[2],
                        dados[3],
                        Double.parseDouble(dados[4]),
                        Integer.parseInt(dados[5].equals("null") ? "0" : dados[5]),
                        dados[6],
                        dados[7] 
                    );
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public static void sobrescreverConsultas(List<Consulta> consultas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_CONSULTAS, false))) {
            for (Consulta c : consultas) {
                writer.write(c.toString());
                writer.newLine();
            }
        }
    }

}