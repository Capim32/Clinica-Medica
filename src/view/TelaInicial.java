package view;

import controller.ClinicaController;
import java.awt.*;
import javax.swing.*;

public class TelaInicial extends JFrame {
    
    private ClinicaController controller;

    public TelaInicial(ClinicaController controller) {
        this.controller = controller;

        setTitle("Bem-vindo à Clínica");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));
        setLocationRelativeTo(null);

        JLabel label = new JLabel("O que deseja fazer?", SwingConstants.CENTER);
        JButton btnLogin = new JButton("Fazer Login");
        JButton btnCadastro = new JButton("Cadastrar Novo Usuário");
        JButton btnSair = new JButton("Sair");

        add(label);
        add(btnLogin);
        add(btnCadastro);
        add(btnSair);

        this.getRootPane().setDefaultButton(btnLogin);

        btnLogin.addActionListener(e -> {
            controller.abrirTelaLogin();
            dispose(); // fecha esta tela
        });

        btnCadastro.addActionListener(e -> {
            controller.abrirTelaCadastro();
            dispose(); // fecha esta tela
        });

        btnSair.addActionListener(e -> {
            System.exit(0);
        });

        setVisible(true);
    }
}