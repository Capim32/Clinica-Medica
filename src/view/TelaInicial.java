package view;

import javax.swing.*;
import java.awt.*;
import controller.ClinicaController;

public class TelaInicial extends JFrame {
    
    private ClinicaController controller;

    public TelaInicial(ClinicaController controller) {
        this.controller = controller;

        setTitle("Bem-vindo à Clínica");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));
        setLocationRelativeTo(null);

        JLabel label = new JLabel("O que deseja fazer?", SwingConstants.CENTER);
        JButton btnLogin = new JButton("Fazer Login");
        JButton btnCadastro = new JButton("Cadastrar Novo Usuário");

        add(label);
        add(btnLogin);
        add(btnCadastro);

        btnLogin.addActionListener(e -> {
            controller.abrirTelaLogin();
            dispose(); // fecha esta tela
        });

        btnCadastro.addActionListener(e -> {
            controller.abrirTelaCadastro();
            dispose(); // fecha esta tela
        });

        setVisible(true);
    }
}