package controller;

import data.GerenciadorDeArquivos;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.*;
import view.*;

public class ClinicaController {
    
    private List<Medico> medicos = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Consulta> consultas = new ArrayList<>();
    
    private Medico medicoLogado;
    private Paciente pacienteLogado;

    public static final String[] ESPECIALIDADES = {
        "Geral", "Obstetrícia", "Neurologia", "Cardiologia", "Dermatologia", "Pediatria", "Ortopedia"
    };
    
    public static final String[] PLANOS = {
        "Não tenho", "Hapvida", "Unimed"
    };

    public ClinicaController() {
        carregarDados();
        iniciar();
    }

    public void iniciar() { new TelaInicial(this); }
    public void abrirTelaLogin() { new TelaLogin(this); }
    public void abrirTelaCadastro() { new TelaCadastro(this); }
    
    public void abrirTelaAgendamento() {
        if (pacienteLogado != null) {
            new TelaAgendamento(this, pacienteLogado);
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Apenas pacientes podem agendar!");
        }
    }

    private void carregarDados() {
        try {
            medicos = GerenciadorDeArquivos.carregarMedicos();
            pacientes = GerenciadorDeArquivos.carregarPacientes();
            consultas = GerenciadorDeArquivos.carregarConsultas();
        } catch (IOException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }
    }

    // login por ID
    public boolean realizarLogin(int id, String tipo) {
        if (tipo.equals("Médico")) {
            for (Medico m : medicos) {
                if (m.getId() == id) {
                    this.medicoLogado = m;
                    new TelaPrincipal(this);
                    return true;
                }
            }
        } else if (tipo.equals("Paciente")) {
            for (Paciente p : pacientes) {
                if (p.getId() == id) {
                    this.pacienteLogado = p;
                    new TelaPrincipal(this);
                    return true;
                }
            }
        }
        return false;
    }

    // login por Dados (Nome + Extra)
    public boolean realizarLoginPorDados(String nome, String dadoExtra, String tipo) {
        if (tipo.equals("Médico")) {
            for (Medico m : medicos) {
                if (m.getNome().equalsIgnoreCase(nome) && m.getEspecialidade().equalsIgnoreCase(dadoExtra)) {
                    this.medicoLogado = m;
                    new TelaPrincipal(this);
                    return true;
                }
            }
        } else {
            for (Paciente p : pacientes) {
                // compara nome e data de nascimento
                if (p.getNome().equalsIgnoreCase(nome) && p.getDataNascimento().equals(dadoExtra)) {
                    this.pacienteLogado = p;
                    new TelaPrincipal(this);
                    return true;
                }
            }
        }
        return false;
    }

    public void fazerLogout() {
        this.medicoLogado = null;
        this.pacienteLogado = null;
        iniciar();
    }

    public void cadastrarUsuario(String tipo, String nome, String plano, String infoVariavel, String dadoExtra) throws IOException {
        if (tipo.equals("Médico")) {
            int novoId = GerenciadorDeArquivos.getProximoIdMedico();
            Medico m = new Medico(nome);
            m.setId(novoId);
            m.setPlanoDeSaude(plano);
            m.setEspecialidade(infoVariavel);
            medicos.add(m);
            GerenciadorDeArquivos.salvarMedico(m);
            javax.swing.JOptionPane.showMessageDialog(null, "Médico cadastrado com ID: " + novoId);
        } else {
            int novoId = GerenciadorDeArquivos.getProximoIdPaciente();
            int idade = Integer.parseInt(infoVariavel);
            // usa o construtor com data de nascimento (dadoExtra)
            Paciente p = new Paciente(nome, idade, dadoExtra);
            p.setId(novoId);
            p.setPlanoDeSaude(plano);
            pacientes.add(p);
            GerenciadorDeArquivos.salvarPaciente(p);
            javax.swing.JOptionPane.showMessageDialog(null, "Paciente cadastrado com ID: " + novoId);
        }
    }

    // métodos auxiliares
    public Paciente getPacientePorId(int id) {
        for (Paciente p : pacientes) if (p.getId() == id) return p;
        return null;
    }
    
    public Medico getMedicoPorId(int id) {
        for(Medico m : medicos) if(m.getId() == id) return m;
        return null;
    }

    public List<Medico> getMedicosCompativeis(Paciente p) {
        List<Medico> lista = new ArrayList<>();
        String planoP = p.getPlanoDeSaude();
        boolean semPlano = planoP == null || planoP.equalsIgnoreCase("Não tenho") || planoP.equalsIgnoreCase("Nao tenho");

        for (Medico m : medicos) {
            if (semPlano) lista.add(m);
            else if (m.getPlanoDeSaude().equalsIgnoreCase(planoP)) lista.add(m);
        }
        return lista;
    }

    // Cada médico só pode ter até 3 consultas por dia (que vida mansa...)
    public boolean agendarConsulta(int idMedico, int idPaciente, String data) {
        // 1. contar quantas consultas ativas (AGENDADA ou REALIZADA) existem para este médico neste dia 
        int agendadasNoDia = 0;
        for (Consulta c : consultas) {
            if (c.getIdMedico() == idMedico && c.getData().equals(data)) {
                if (c.getStatus().equals("AGENDADA") || c.getStatus().equals("REALIZADA")) {
                    agendadasNoDia++;
                }
            }
        }

        String statusInicial = "AGENDADA";
        String msgAviso = "Consulta agendada com sucesso!";

        if (agendadasNoDia >= 3) {
            statusInicial = "EM_ESPERA";
            msgAviso = "Atenção: A agenda deste dia está cheia.\nVocê foi colocado na LISTA DE ESPERA.";
        }

        try {
            // salva na memória
            Consulta nova = new Consulta(idMedico, idPaciente, data, statusInicial, 0.0, 0, "null");
            consultas.add(nova);
            
            // salva no arquivo usando sobrescreverConsultas
            GerenciadorDeArquivos.sobrescreverConsultas(consultas);
            
            javax.swing.JOptionPane.showMessageDialog(null, msgAviso);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void cancelarConsulta(Consulta c) {
        if (!c.getStatus().equals("AGENDADA") && !c.getStatus().equals("EM_ESPERA")) {
             javax.swing.JOptionPane.showMessageDialog(null, "Apenas consultas agendadas ou em espera podem ser canceladas.");
             return;
        }

        String statusAntigo = c.getStatus();
        c.setStatus("CANCELADA"); 

        // 2. se a consulta estiver agendada, alguem na fila de espera é buscado para ocupar a vaga
        if (statusAntigo.equals("AGENDADA")) {
            for (Consulta fila : consultas) {
                // busca: mesmo médico, mesma data, status EM_ESPERA
                if (fila.getIdMedico() == c.getIdMedico() && 
                    fila.getData().equals(c.getData()) && 
                    fila.getStatus().equals("EM_ESPERA")) {
                    
                    fila.setStatus("AGENDADA"); // promove o primeiro que achar (mais antigo)
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Consulta cancelada.\nUm paciente da lista de espera (ID " + fila.getIdPaciente() + ") assumiu a vaga.");
                    break;
                }
            }
        } else {
             javax.swing.JOptionPane.showMessageDialog(null, "Você saiu da lista de espera.");
        }

        // escreve as alteracoes feitas
        try { GerenciadorDeArquivos.sobrescreverConsultas(consultas); } catch (Exception e) {}
    }

    public List<Consulta> getConsultasDoMedicoPorData(int idMedico, String data) {
        List<Consulta> lista = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getIdMedico() == idMedico && c.getData().equals(data)) lista.add(c);
        }
        return lista;
    }

    public void atualizarStatusConsulta(Consulta c, String status, double valor) {
        c.setStatus(status);
        c.setValorPago(valor);
        try { GerenciadorDeArquivos.sobrescreverConsultas(consultas); } catch (Exception e) {}
    }

    public List<Medico> buscarMedicosPorFiltro(String esp, String nome) {
        List<Medico> res = new ArrayList<>();
        String nLower = nome != null ? nome.toLowerCase() : "";
        for (Medico m : medicos) {
            if ((esp.equals("Todas") || m.getEspecialidade().equalsIgnoreCase(esp)) &&
                (nome == null || nome.isEmpty() || m.getNome().toLowerCase().contains(nLower))) {
                res.add(m);
            }
        }
        return res;
    }
    
    public String getMediaAvaliacaoMedico(int id) {
        double soma = 0; int count = 0;
        for (Consulta c : consultas) if (c.getIdMedico() == id && c.getAvaliacaoEstrelas() > 0) { soma += c.getAvaliacaoEstrelas(); count++; }
        return count == 0 ? "Sem avaliações" : String.format("%.1f/5 (%d)", soma/count, count);
    }
    
    public List<String> getUltimasAvaliacoes(int id) {
        List<String> r = new ArrayList<>();
        for (int i = consultas.size() - 1; i >= 0; i--) {
            Consulta c = consultas.get(i);
            if (c.getIdMedico() == id && c.getAvaliacaoEstrelas() > 0) {
                r.add("★" + c.getAvaliacaoEstrelas() + ": " + c.getAvaliacaoTexto());
                if (r.size() >= 5) break;
            }
        }
        return r;
    }

    public List<Consulta> getConsultasDoPaciente(int idPaciente) {
        List<Consulta> lista = new ArrayList<>();
        for (Consulta c : consultas) if (c.getIdPaciente() == idPaciente) lista.add(c);
        return lista;
    }

    public void avaliarMedico(Consulta c, int estrelas, String texto) {
        c.setAvaliacaoEstrelas(estrelas);
        c.setAvaliacaoTexto(texto);
        try { GerenciadorDeArquivos.sobrescreverConsultas(consultas); } catch (Exception e) {}
    }
    
    public void atualizarPerfilMedico(String n, String e, String p) {
        if (medicoLogado != null) {
            medicoLogado.setNome(n); medicoLogado.setEspecialidade(e); medicoLogado.setPlanoDeSaude(p);
            try { GerenciadorDeArquivos.sobrescreverMedicos(medicos); } catch (Exception ex) {}
        }
    }

    public void atualizarPerfilPaciente(String n, String p) {
        if (pacienteLogado != null) {
            pacienteLogado.setNome(n); pacienteLogado.setPlanoDeSaude(p);
            try { GerenciadorDeArquivos.sobrescreverPacientes(pacientes); } catch (Exception ex) {}
        }
    }

    public List<Medico> getListaMedicos() { return medicos; }
    public Medico getMedicoLogado() { return medicoLogado; }
    public Paciente getPacienteLogado() { return pacienteLogado; }
}