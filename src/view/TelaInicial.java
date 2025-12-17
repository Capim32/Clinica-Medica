package view;

import controller.ClinicaController;
import java.awt.*;
import javax.swing.*;

public class TelaInicial extends JFrame {
    
    private ClinicaController controller;

    public TelaInicial(ClinicaController controller) {
        this.controller = controller;

        setTitle("Sistema Clínica Médica");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // fundo branco e layout centralizado
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(Color.WHITE);
        setContentPane(painelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margem entre componentes
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // título abaitolado
        JLabel labelTitulo = new JLabel("CliniCafé", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        labelTitulo.setForeground(new Color(0, 102, 204)); // Azul Escuro
        gbc.gridy = 0;
        painelPrincipal.add(labelTitulo, gbc);
        
        JLabel labelSub = new JLabel("Clínica para Programadores Cansados", SwingConstants.CENTER);
        labelSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 30, 10); // espaçamento abaixo do subtítulo
        painelPrincipal.add(labelSub, gbc);

        // botões maiores e mais bunitus
        JButton btnLogin = criarBotaoEstilizado("Acessar Sistema (Login)");
        JButton btnCadastro = criarBotaoEstilizado("Criar Nova Conta");
        JButton btnSair = criarBotaoEstilizado("Sair");

        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 2; painelPrincipal.add(btnLogin, gbc);
        gbc.gridy = 3; painelPrincipal.add(btnCadastro, gbc);
        gbc.gridy = 4; painelPrincipal.add(btnSair, gbc);

        // ações dos botões
        btnLogin.addActionListener(e -> { controller.abrirTelaLogin(); dispose(); });
        btnCadastro.addActionListener(e -> { controller.abrirTelaCadastro(); dispose(); });
        btnSair.addActionListener(e -> System.exit(0));

        this.getRootPane().setDefaultButton(btnLogin);

        setVisible(true);
    }
    
    // a mágica acontece aqui 
    private JButton criarBotaoEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setPreferredSize(new Dimension(250, 40));
        return btn;
    }
}