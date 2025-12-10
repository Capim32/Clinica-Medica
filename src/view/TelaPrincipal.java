package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.ClinicaController;

public class TelaPrincipal extends JFrame {

    private ClinicaController controller;

    public TelaPrincipal() {
        controller = new ClinicaController();
        setTitle("Sistema Clínica Médica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

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