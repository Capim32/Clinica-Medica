package view;

import controller.ClinicaController;
import java.awt.*;
import javax.swing.*;

public class TelaLogin extends JFrame {
    private ClinicaController controller;
    private JPanel painelCampos;
    private CardLayout cardLayout;
    
    // Campos
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtDadoExtra; // Data de nascimento ou Especialidade
    private JLabel lblDadoExtra;

    public TelaLogin(ClinicaController controller) {
        this.controller = controller;
        setTitle("Login");
        setSize(350, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Acesso ao Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo);

        // Seletor de Tipo
        JPanel pnlTipo = new JPanel(new FlowLayout());
        pnlTipo.add(new JLabel("Eu sou:"));
        JComboBox<String> cbTipoUser = new JComboBox<>(new String[]{"Médico", "Paciente"});
        pnlTipo.add(cbTipoUser);
        add(pnlTipo);

        // Seletor de Modo
        JPanel pnlModo = new JPanel(new FlowLayout());
        pnlModo.add(new JLabel("Entrar com:"));
        JComboBox<String> cbModo = new JComboBox<>(new String[]{"ID", "Dados Pessoais"});
        pnlModo.add(cbModo);
        add(pnlModo);

        // painel alternável
        painelCampos = new JPanel();
        cardLayout = new CardLayout();
        painelCampos.setLayout(cardLayout);

        // MODO ID
        JPanel pnlId = new JPanel(new GridLayout(2, 1));
        pnlId.add(new JLabel("ID do Usuário:"));
        txtId = new JTextField();
        pnlId.add(txtId);
        painelCampos.add(pnlId, "ID");

        // MODO DADOS
        JPanel pnlDados = new JPanel(new GridLayout(4, 1));
        pnlDados.add(new JLabel("Nome Completo:"));
        txtNome = new JTextField();
        pnlDados.add(txtNome);
        
        lblDadoExtra = new JLabel("Especialidade:"); 
        pnlDados.add(lblDadoExtra);
        txtDadoExtra = new JTextField();
        pnlDados.add(txtDadoExtra);
        painelCampos.add(pnlDados, "DADOS");

        add(painelCampos);

        JPanel pnlBotoes = new JPanel();
        JButton btnEntrar = new JButton("Entrar");
        JButton btnVoltar = new JButton("Voltar");
        pnlBotoes.add(btnEntrar);
        pnlBotoes.add(btnVoltar);
        add(pnlBotoes);
        
        this.getRootPane().setDefaultButton(btnEntrar);

        cbModo.addActionListener(e -> {
            String modo = (String) cbModo.getSelectedItem();
            if (modo.equals("ID")) cardLayout.show(painelCampos, "ID");
            else cardLayout.show(painelCampos, "DADOS");
        });

        cbTipoUser.addActionListener(e -> {
            String tipo = (String) cbTipoUser.getSelectedItem();
            if (tipo.equals("Médico")) {
                lblDadoExtra.setText("Especialidade:");
            } else {
                lblDadoExtra.setText("Data de Nascimento (dd/mm/aaaa):");
            }
        });

        btnEntrar.addActionListener(e -> {
            String tipo = (String) cbTipoUser.getSelectedItem();
            String modo = (String) cbModo.getSelectedItem();
            boolean sucesso = false;

            try {
                if (modo.equals("ID")) {
                    if (txtId.getText().isEmpty()) throw new Exception("Digite o ID.");
                    int id = Integer.parseInt(txtId.getText());
                    sucesso = controller.realizarLogin(id, tipo);
                } else {
                    String nome = txtNome.getText();
                    String extra = txtDadoExtra.getText();
                    if(nome.isEmpty() || extra.isEmpty()) throw new Exception("Preencha todos os campos.");
                    sucesso = controller.realizarLoginPorDados(nome, extra, tipo);
                }

                if (sucesso) dispose();
                else JOptionPane.showMessageDialog(this, "Usuário não encontrado ou dados incorretos!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID deve ser numérico!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnVoltar.addActionListener(e -> { controller.iniciar(); dispose(); });

        setVisible(true);
    }
}