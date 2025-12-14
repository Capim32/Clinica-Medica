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
    
    private Medico medicoLogado;
    private Paciente pacienteLogado;

    public ClinicaController() {
        carregarDados();
        iniciar(); // inicia o fluxo do programa
    }

    public void iniciar() {
        new TelaInicial(this);
    }

    public void abrirTelaLogin() {
        new TelaLogin(this);
    }

    public void abrirTelaCadastro() {
        new TelaCadastro(this);
    }

    public void cadastrarUsuario(String tipo, String nome, String plano, String infoVariavel) throws IOException {
        
        if (tipo.equals("Médico")) {
            // gera o ID automaticamente em vez do usuário digitar o próprio ID 
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
            Paciente p = new Paciente(nome, idade);
            p.setId(novoId);
            p.setPlanoDeSaude(plano);

            pacientes.add(p);
            GerenciadorDeArquivos.salvarPaciente(p);
            
            javax.swing.JOptionPane.showMessageDialog(null, "Paciente cadastrado com ID: " + novoId);
        }
    }

    private void carregarDados() {
        try {
            medicos = GerenciadorDeArquivos.carregarMedicos();
            pacientes = GerenciadorDeArquivos.carregarPacientes();
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

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
    
    public Medico getMedicoLogado() { return medicoLogado; }
    public Paciente getPacienteLogado() { return pacienteLogado; }


    public void abrirTelaAgendamento() {
        if (pacienteLogado != null) {
            new TelaAgendamento(this, pacienteLogado);
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Apenas pacientes podem agendar!");
        }
    }

    // getter para preencher o combobox de médicos
    public List<Medico> getListaMedicos() {
        return medicos;
    }

    public boolean agendarConsulta(int idMedico, int idPaciente, String data) {
        try {
            GerenciadorDeArquivos.salvarConsulta(idMedico, idPaciente, data);
            javax.swing.JOptionPane.showMessageDialog(null, "Consulta agendada com sucesso para " + data + "!");
            return true;
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Erro ao salvar consulta: " + e.getMessage());
            return false;
        }
    }

    // o método resolve o bug que faz com que o médico continue logado caso logue  com o médico, saia, logue com paciente  (trabalho do estagiário não remunerado)
    public void fazerLogout() {
        this.medicoLogado = null;
        this.pacienteLogado = null;
        iniciar(); // volta para a tela inicial
    }
}