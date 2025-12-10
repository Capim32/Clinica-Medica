package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Medico;
import models.Paciente;

public class GerenciadorDeArquivos {
    // se não der certo o .txt com a interface gráfica, mudar pra .csv
    private static final String PATH_MEDICOS = "medicos.txt";
    private static final String PATH_PACIENTES = "pacientes.txt";
    private static final String PATH_CONSULTAS = "consultas.txt";

    // append médico
    public static void salvarMedico(Medico medico) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_MEDICOS, true))) {
            // precisa fazer um medico.getAtendimentos ou pode deixar implicito ja que o medico continua existindo independente dos atendimentos?
            String linha = medico.getId() + ";" + medico.getNome() + ";" + 
                           medico.getEspecialidade() + ";" + medico.getPlanoDeSaude();
            writer.write(linha);
            writer.newLine();
        }
    }

    public static List<Medico> carregarMedicos() throws IOException{
        List<Medico> listaMedica = new ArrayList<>();
        File file = new File(PATH_MEDICOS);

        if (!file.exists()) {return listaMedica;} 

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                // criar objeto Médico usando os dados lidos
                Medico m = new Medico(Integer.parseInt(dados[0])); 
                m.setNome(dados[1]);
                m.setEspecialidade(dados[2]);
                m.setPlanoDeSaude(dados[3]); // o ideal seria que o plano de saúde fosse int, mas isso força um String...
                //m.setAtendimentos(dados[n]); caso precise dos atendimentos do médico
                listaMedica.add(m);
            }
        }
        return listaMedica;
    }

    // replicar a lógica para Pacientes e Consultas

    public static void salvarPaciente(Paciente paciente) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PACIENTES, true))) {
            String linha = paciente.getId() + ";" + paciente.getNome() + ";" + 
                        paciente.getIdade() + ";" + paciente.getPlanoDeSaude();
            writer.write(linha); 
            writer.newLine();
        }
    }

    public static List<Paciente> carregarPacientes() throws IOException {
        List<Paciente> listaPacientes = new ArrayList<>();
        File file = new File(PATH_PACIENTES);

        if (!file.exists()) return listaPacientes;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                // cria o objeto Paciente usando os dados lidos
                Paciente p = new Paciente(dados[1], Integer.parseInt(dados[2]));
                p.setId(Integer.parseInt(dados[0]));
                p.setPlanoDeSaude(dados[3]);
                listaPacientes.add(p);
            }
        }
        return listaPacientes;
    }
    
}
