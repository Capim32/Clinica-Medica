package view;

import controller.ClinicaController;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaLogin extends JFrame {
    private ClinicaController controller;
    
    // painéis principais 
    private JPanel painelCamposDinamicos;
    private CardLayout cardLayoutMain; // altera entre ID e Dados
    
    private JPanel painelDadoExtra;
    private CardLayout cardLayoutExtra;

    // campos de entrada
    private JTextField txtId;
    private JTextField txtNome;
    
    private JComboBox<String> cbTipoUser;
    private JComboBox<String> cbModo;
    
    // seletores específicos
    private JComboBox<String> cbEspecialidadeLogin;
    private JComboBox<Integer> cbDia, cbMes, cbAno;
    
    private JLabel lblDadoExtra;

    public TelaLogin(ClinicaController controller) {
        this.controller = controller;
        setTitle("Acesso ao Sistema");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. cabeçalho
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(245, 245, 245));
        pnlHeader.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel lblTitulo = new JLabel("CliniCafé Login");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 204));
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. formulário
        JPanel pnlCentral = new JPanel(new GridBagLayout());
        pnlCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // margem entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // --- Linha 0: tipo de usuário ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlCentral.add(new JLabel("Eu sou:", SwingConstants.RIGHT), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        cbTipoUser = new JComboBox<>(new String[]{"Médico", "Paciente"});
        pnlCentral.add(cbTipoUser, gbc);

        // --- linha 1: modo de acesso ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        pnlCentral.add(new JLabel("Entrar com:", SwingConstants.RIGHT), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        cbModo = new JComboBox<>(new String[]{"ID", "Dados Pessoais"});
        pnlCentral.add(cbModo, gbc);

        // --- linha 2: separador ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        pnlCentral.add(new JSeparator(), gbc);

        // --- Linha 3:ID ou Dados ---
        gbc.gridy = 3; 
        
        painelCamposDinamicos = new JPanel();
        cardLayoutMain = new CardLayout();
        painelCamposDinamicos.setLayout(cardLayoutMain);
        
        // login por ID
        JPanel pnlID = new JPanel(new GridBagLayout());
        GridBagConstraints gbcId = new GridBagConstraints();
        gbcId.insets = new Insets(5, 5, 5, 5);
        gbcId.gridx = 0; gbcId.gridy = 0;
        pnlID.add(new JLabel("ID do Usuário:"), gbcId);
        gbcId.gridx = 1; gbcId.weightx = 1.0; gbcId.fill = GridBagConstraints.HORIZONTAL;
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(150, 30));
        pnlID.add(txtId, gbcId);
        
        // login por Dados
        JPanel pnlDados = new JPanel(new GridBagLayout());
        GridBagConstraints gbcDados = new GridBagConstraints();
        gbcDados.insets = new Insets(5, 5, 5, 5);
        gbcDados.fill = GridBagConstraints.HORIZONTAL;
        
        // nome
        gbcDados.gridx = 0; gbcDados.gridy = 0; gbcDados.weightx = 0.3;
        pnlDados.add(new JLabel("Nome Completo:", SwingConstants.RIGHT), gbcDados);
        
        gbcDados.gridx = 1; gbcDados.weightx = 0.7;
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(150, 30));
        pnlDados.add(txtNome, gbcDados);
        
        gbcDados.gridx = 0; gbcDados.gridy = 1; gbcDados.weightx = 0.3;
        lblDadoExtra = new JLabel("Especialidade:", SwingConstants.RIGHT);
        pnlDados.add(lblDadoExtra, gbcDados);
        
        // Label variável (Especialidade ou Data)
        gbcDados.gridx = 1; gbcDados.weightx = 0.7;
        painelDadoExtra = new JPanel();
        cardLayoutExtra = new CardLayout();
        painelDadoExtra.setLayout(cardLayoutExtra);
        
        // especialidade
        cbEspecialidadeLogin = new JComboBox<>(ClinicaController.ESPECIALIDADES);
        painelDadoExtra.add(cbEspecialidadeLogin, "ESPECIALIDADE");
        
        // data
        JPanel pnlData = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cbDia = new JComboBox<>();
        cbMes = new JComboBox<>();
        cbAno = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i >= 1900; i--) cbAno.addItem(i);
        for (int i = 1; i <= 12; i++) cbMes.addItem(i);
        cbMes.addActionListener(e -> atualizarDias());
        cbAno.addActionListener(e -> atualizarDias());
        atualizarDias();
        
        pnlData.add(cbDia); pnlData.add(new JLabel("/"));
        pnlData.add(cbMes); pnlData.add(new JLabel("/"));
        pnlData.add(cbAno);
        painelDadoExtra.add(pnlData, "DATA");
        
        pnlDados.add(painelDadoExtra, gbcDados);

        // add cards ao painel principal
        painelCamposDinamicos.add(pnlID, "ID");
        painelCamposDinamicos.add(pnlDados, "DADOS");
        
        // centraliza a área dinamica
        pnlCentral.add(painelCamposDinamicos, gbc);
        
        // coloca tudo no frame
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(pnlCentral, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnVoltar = new JButton("Voltar");
        JButton btnEntrar = new JButton("Entrar");
        
        btnEntrar.setBackground(new Color(0, 102, 204));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setPreferredSize(new Dimension(120, 40));
        btnVoltar.setPreferredSize(new Dimension(100, 40));
        
        pnlBotoes.add(btnVoltar);
        pnlBotoes.add(btnEntrar);
        add(pnlBotoes, BorderLayout.SOUTH);
        
        this.getRootPane().setDefaultButton(btnEntrar);

        // --- LOGICA DE INTERFACE ---

        // alterna entre ID e Dados
        cbModo.addActionListener(e -> {
            String modo = (String) cbModo.getSelectedItem();
            if (modo.equals("ID")) cardLayoutMain.show(painelCamposDinamicos, "ID");
            else cardLayoutMain.show(painelCamposDinamicos, "DADOS");
        });

        // alterna entre Médico e Paciente
        cbTipoUser.addActionListener(e -> {
            String tipo = (String) cbTipoUser.getSelectedItem();
            if (tipo.equals("Médico")) {
                lblDadoExtra.setText("Especialidade:");
                cardLayoutExtra.show(painelDadoExtra, "ESPECIALIDADE");
            } else {
                lblDadoExtra.setText("Data Nasc.:");
                cardLayoutExtra.show(painelDadoExtra, "DATA");
            }
        });

        btnEntrar.addActionListener(e -> {
            String tipo = (String) cbTipoUser.getSelectedItem();
            String modo = (String) cbModo.getSelectedItem();
            boolean sucesso = false;

            try {
                if (modo.equals("ID")) {
                    if (txtId.getText().trim().isEmpty()) throw new Exception("Digite o ID.");
                    int id = Integer.parseInt(txtId.getText().trim());
                    sucesso = controller.realizarLogin(id, tipo);
                } else {
                    String nome = txtNome.getText().trim();
                    if(nome.isEmpty()) throw new Exception("Digite seu nome.");

                    String dadoExtra = "";
                    if (tipo.equals("Médico")) {
                        dadoExtra = (String) cbEspecialidadeLogin.getSelectedItem();
                    } else {
                        int d = (int) cbDia.getSelectedItem();
                        int m = (int) cbMes.getSelectedItem();
                        int a = (int) cbAno.getSelectedItem();
                        dadoExtra = String.format("%02d/%02d/%04d", d, m, a);
                    }
                    
                    sucesso = controller.realizarLoginPorDados(nome, dadoExtra, tipo);
                }

                if (sucesso) dispose();
                else JOptionPane.showMessageDialog(this, "Login falhou! Verifique seus dados.");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O ID deve ser numérico!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnVoltar.addActionListener(e -> { controller.iniciar(); dispose(); });

        // Estado Inicial
        cardLayoutExtra.show(painelDadoExtra, "ESPECIALIDADE");
        setVisible(true);
    }
    
    private void atualizarDias() {
        if (cbMes.getSelectedItem() == null || cbAno.getSelectedItem() == null) return;
        int m = (int) cbMes.getSelectedItem();
        int a = (int) cbAno.getSelectedItem();
        Object diaAtual = cbDia.getSelectedItem();

        cbDia.removeAllItems();
        int max = YearMonth.of(a, m).lengthOfMonth();
        for (int i = 1; i <= max; i++) cbDia.addItem(i);
        
        if (diaAtual != null && (int)diaAtual <= max) {
            cbDia.setSelectedItem(diaAtual);
        }
    }
}