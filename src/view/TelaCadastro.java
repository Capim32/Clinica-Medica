package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import controller.ClinicaController;

public class TelaCadastro extends JFrame {
    
    public TelaCadastro(ClinicaController controller) {
        setTitle("Cadastro de Usuário");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        // 1. Tipo de Usuário
        add(new JLabel("Tipo de Usuário:"));
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Paciente", "Médico"});
        add(cbTipo);

        // 2. Nome
        add(new JLabel("Nome:"));
        JTextField txtNome = new JTextField();
        add(txtNome);

        // 3. Variável (Idade ou Especialidade)
        JLabel lblVariavel = new JLabel("Idade:");
        JTextField txtVariavel = new JTextField();
        add(lblVariavel);
        add(txtVariavel);

        // 4. Plano de Saúde (fica mais fácil assim do que o usuário digitar)
        add(new JLabel("Plano de Saúde:"));
        String[] planos = {"Não tenho", "Hapvida", "Unimed", "Outro"};
        JComboBox<String> cbPlano = new JComboBox<>(planos);
        add(cbPlano);

        // Botões
        JButton btnVoltar = new JButton("Voltar");
        JButton btnSalvar = new JButton("Salvar Cadastro");

        add(btnVoltar);
        add(btnSalvar);

        // define o botão "Salvar" como padrão da janela (pressionar Enter ativa ele)
        this.getRootPane().setDefaultButton(btnSalvar);

        // Lógica visual 
        cbTipo.addActionListener(e -> {
            if (cbTipo.getSelectedItem().equals("Médico")) {
                lblVariavel.setText("Especialidade:");
            } else {
                lblVariavel.setText("Idade:");
            }
        });

        btnVoltar.addActionListener(e -> {
            controller.iniciar(); 
            dispose();
        });

        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                String plano = (String) cbPlano.getSelectedItem(); // Pega do Dropdown
                String tipo = (String) cbTipo.getSelectedItem();
                String infoVariavel = txtVariavel.getText();

                controller.cadastrarUsuario(tipo, nome, plano, infoVariavel);
                
                controller.abrirTelaLogin(); 
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: Idade deve ser número.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}