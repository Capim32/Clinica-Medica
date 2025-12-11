package view;

import javax.swing.*;
import java.awt.*;
import controller.ClinicaController;

public class TelaLogin extends JFrame {
    private ClinicaController controller;

    public TelaLogin(ClinicaController controller) {
        this.controller = controller;
        
        setTitle("Login - Clínica Médica");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 5, 5)); // Layout de grade simples
        setLocationRelativeTo(null); // Centralizar na tela

        JLabel lblId = new JLabel("ID do Usuário:");
        JTextField txtId = new JTextField();
        
        JLabel lblTipo = new JLabel("Sou:");
        String[] tipos = {"Médico", "Paciente"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);

        JButton btnEntrar = new JButton("Entrar");
        JButton btnCadastrar = new JButton("Cadastrar"); // Para depois

        add(lblId);
        add(txtId);
        add(lblTipo);
        add(cbTipo);
        add(new JLabel("")); // Espaço vazio
        add(btnEntrar);
        
        // Ação do botão Entrar
        btnEntrar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String tipoSelecionado = (String) cbTipo.getSelectedItem();
                boolean sucesso = controller.realizarLogin(id, tipoSelecionado);
                
                if (sucesso) {
                    dispose(); // Fecha a tela de login
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário não encontrado!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O ID deve ser um número!");
            }
        });

        setVisible(true);
    }
}