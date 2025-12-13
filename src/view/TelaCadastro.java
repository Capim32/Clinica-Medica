package view;

import javax.swing.*;
import java.awt.*;
import controller.ClinicaController;

public class TelaCadastro extends JFrame {
    
    public TelaCadastro(ClinicaController controller) {
        setTitle("Cadastro de Usuário");
        setSize(400, 350); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5)); 

        // componentes
        add(new JLabel("Tipo de Usuário:"));
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Paciente", "Médico"});
        add(cbTipo);

        add(new JLabel("Nome:"));
        JTextField txtNome = new JTextField();
        add(txtNome);

        JLabel lblVariavel = new JLabel("Idade:");
        JTextField txtVariavel = new JTextField();
        add(lblVariavel);
        add(txtVariavel);

        add(new JLabel("Plano de Saúde:"));
        JTextField txtPlano = new JTextField();
        add(txtPlano);

        JButton btnSalvar = new JButton("Salvar Cadastro");
        JButton btnVoltar = new JButton("Voltar");

        add(btnVoltar);
        add(btnSalvar);

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
                String plano = txtPlano.getText();
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