package controller;

import data.GerenciadorDeArquivos;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.*;
import view.*;

public class ClinicaController {
    
    // listas para manter os dados na memória enquanto o programa roda
    private List<Medico> medicos = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    
    // usuário logado atualmente (se ninguem logoou e null)
    private Medico medicoLogado;
    private Paciente pacienteLogado;

    public ClinicaController() {
        carregarDados();
        // abre a tela de login ao iniciar o controller
        new TelaLogin(this);  
    }

    private void carregarDados() {
        try {
            medicos = GerenciadorDeArquivos.carregarMedicos();
            pacientes = GerenciadorDeArquivos.carregarPacientes();
            System.out.println("Dados carregados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivos: " + e.getMessage());
        }
    }

    public boolean realizarLogin(int id, String tipo) {
        if (tipo.equals("Médico")) {
            for (Medico m : medicos) {
                if (m.getId() == id) {
                    this.medicoLogado = m;
                    abrirTelaPrincipal();
                    return true;
                }
            }
        } else if (tipo.equals("Paciente")) {
            for (Paciente p : pacientes) {
                if (p.getId() == id) {
                    this.pacienteLogado = p;
                    abrirTelaPrincipal();
                    return true;
                }
            }
        }
        return false;
    }

    private void abrirTelaPrincipal() {
        // Passamos o controller para a tela principal também
        new TelaPrincipal(this); 
    }
    
    // Getters para saber quem está logado
    public Medico getMedicoLogado() { return medicoLogado; }
    public Paciente getPacienteLogado() { return pacienteLogado; }
}