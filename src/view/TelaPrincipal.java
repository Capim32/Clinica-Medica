package view;

import controller.ClinicaController;
import java.awt.*;
import javax.swing.*;

public class TelaPrincipal extends JFrame {

    private ClinicaController controller;

    public TelaPrincipal(ClinicaController controller) {
        this.controller = controller;

        setTitle("Sistema Clínica Médica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        if (controller.getMedicoLogado() != null) {
            add(new JLabel("Olá, Dr(a). " + controller.getMedicoLogado().getNome()));
            // Adicione botões específicos de médico aqui
        } else {
            add(new JLabel("Olá, Paciente " + controller.getPacienteLogado().getNome()));
             // Adicione botões específicos de paciente aqui
        }

        JButton btnAgendar = new JButton("Agendar Consulta");
        JButton btnSair = new JButton("Sair");

        btnAgendar.addActionListener(e -> abrirTelaAgendamento());
        
        add(new JLabel("Bem-vindo à Clínica"));
        add(btnAgendar);
        add(btnSair);
        
        setVisible(true);
    }

    private void abrirTelaAgendamento() {
        // lógica para abrir outra janela
        JOptionPane.showMessageDialog(this, "Abrindo agendamento...");
    }
}