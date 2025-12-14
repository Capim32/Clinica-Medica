package view;

import controller.ClinicaController;
import java.awt.*;
import javax.swing.*;

public class TelaLogin extends JFrame {
    private ClinicaController controller;

    public TelaLogin(ClinicaController controller) {
        this.controller = controller;
        
        setTitle("Login - Clínica Médica");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10)); 

        JLabel lblMensagem = new JLabel("Bem-vindo! Insira seu ID:", SwingConstants.CENTER);
        JTextField txtId = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Médico", "Paciente"});
        
        JButton btnEntrar = new JButton("Entrar");
        JButton btnVoltar = new JButton("Voltar"); 

        add(lblMensagem);
        add(txtId);
        add(cbTipo);
        add(new JLabel("")); // espaço vazio
        add(btnEntrar);
        add(btnVoltar);

        // define o Enter para acionar o botão Entrar (aplicar nos outros depois pq isso é interessante no quesito funcionalidade)
        this.getRootPane().setDefaultButton(btnEntrar);

        btnEntrar.addActionListener(e -> {
            try {
                if (txtId.getText().trim().isEmpty()) {
                    throw new Exception("Digite um ID.");
                }
                int id = Integer.parseInt(txtId.getText());
                String tipo = (String) cbTipo.getSelectedItem();
                
                boolean sucesso = controller.realizarLogin(id, tipo);
                
                if (sucesso) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário não encontrado!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O ID deve ser numérico.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // lógica do botão voltar
        btnVoltar.addActionListener(e -> {
            controller.fazerLogout(); // limpa a sessao e volta ao inicio
            dispose(); // fecha essa janela
        });

        setVisible(true);
    }
}