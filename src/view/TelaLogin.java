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
    private JPanel painelCampos;
    private CardLayout cardLayoutMain; // alterna entre ID e DADOS

    // painel de Dado extra (Especialidade ou Data)
    private JPanel painelDadoExtra;
    private CardLayout cardLayoutExtra; // alterna entre Combo Especialidade e Combo Data
    
    private JTextField txtId;
    private JTextField txtNome;
    
    private JComboBox<String> cbEspecialidadeLogin;
    private JComboBox<Integer> cbDia, cbMes, cbAno;
    
    private JComboBox<String> cbTipoUser;
    private JComboBox<String> cbModo;
    
    private JLabel lblDadoExtra;

    public TelaLogin(ClinicaController controller) {
        this.controller = controller;
        setTitle("Login - Clínica Médica");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // BorderLayout para fixar botões embaixo e titulo em cima
        setLayout(new BorderLayout());

        // --- 1. CABEÇALHO ---
        JLabel lblTitulo = new JLabel("Acesso ao Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // --- 2. CONTEÚDO CENTRAL ---
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBorder(new EmptyBorder(10, 20, 10, 20)); // Margens laterais

        // tipo de usuário
        JPanel pnlTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTipo.add(new JLabel("Eu sou: "));
        cbTipoUser = new JComboBox<>(new String[]{"Médico", "Paciente"});
        cbTipoUser.setPreferredSize(new Dimension(150, 25));
        pnlTipo.add(cbTipoUser);
        painelCentral.add(pnlTipo);

        // modo de login
        JPanel pnlModo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlModo.add(new JLabel("Entrar com: "));
        cbModo = new JComboBox<>(new String[]{"ID", "Dados Pessoais"});
        cbModo.setPreferredSize(new Dimension(150, 25));
        pnlModo.add(cbModo);
        painelCentral.add(pnlModo);

        painelCentral.add(Box.createVerticalStrut(15)); 
        // área dinâmica
        painelCampos = new JPanel();
        cardLayoutMain = new CardLayout();
        painelCampos.setLayout(cardLayoutMain);
        painelCampos.setBorder(BorderFactory.createTitledBorder("Credenciais"));

        // login por ID
        JPanel pnlId = new JPanel(new GridBagLayout()); // Centraliza o campo
        JPanel pnlIdInterno = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlIdInterno.add(new JLabel("ID do Usuário: "));
        txtId = new JTextField(10);
        pnlIdInterno.add(txtId);
        pnlId.add(pnlIdInterno);
        painelCampos.add(pnlId, "ID");

        // login por Dados
        JPanel pnlDados = new JPanel();
        pnlDados.setLayout(new BoxLayout(pnlDados, BoxLayout.Y_AXIS));
        
        // nome
        JPanel pnlNome = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNome.add(new JLabel("Nome Completo:"));
        txtNome = new JTextField(20);
        pnlNome.add(txtNome);
        pnlDados.add(pnlNome);
        
        // Label variável (Especialidade ou Data)
        JPanel pnlLbl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblDadoExtra = new JLabel("Especialidade:");
        pnlLbl.add(lblDadoExtra);
        pnlDados.add(pnlLbl);

        painelDadoExtra = new JPanel();
        cardLayoutExtra = new CardLayout();
        painelDadoExtra.setLayout(cardLayoutExtra);

        // especialidade
        JPanel pnlEsp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbEspecialidadeLogin = new JComboBox<>(ClinicaController.ESPECIALIDADES);
        cbEspecialidadeLogin.setPreferredSize(new Dimension(230, 25));
        pnlEsp.add(cbEspecialidadeLogin);
        painelDadoExtra.add(pnlEsp, "ESPECIALIDADE");

        // data
        JPanel pnlData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbDia = new JComboBox<>();
        cbMes = new JComboBox<>();
        cbAno = new JComboBox<>();
        
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i >= 1900; i--) cbAno.addItem(i);
        for (int i = 1; i <= 12; i++) cbMes.addItem(i);
        
        cbMes.addActionListener(e -> atualizarDias());
        cbAno.addActionListener(e -> atualizarDias());
        atualizarDias();

        pnlData.add(cbDia);
        pnlData.add(new JLabel("/"));
        pnlData.add(cbMes);
        pnlData.add(new JLabel("/"));
        pnlData.add(cbAno);
        painelDadoExtra.add(pnlData, "DATA");

        pnlDados.add(painelDadoExtra);
        painelCampos.add(pnlDados, "DADOS");

        // centraliza a área dinamica
        painelCentral.add(painelCampos);
        
        // coloca tudo no Frame
        add(painelCentral, BorderLayout.CENTER);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setPreferredSize(new Dimension(100, 35));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(100, 35));
        
        pnlBotoes.add(btnVoltar);
        pnlBotoes.add(btnEntrar);
        add(pnlBotoes, BorderLayout.SOUTH);
        
        this.getRootPane().setDefaultButton(btnEntrar);

        // --- LOGICA DE INTERFACE ---

        // alterna entre ID e Dados
        cbModo.addActionListener(e -> {
            String modo = (String) cbModo.getSelectedItem();
            if (modo.equals("ID")) cardLayoutMain.show(painelCampos, "ID");
            else cardLayoutMain.show(painelCampos, "DADOS");
        });

        // alterna entre Médico e Paciente
        cbTipoUser.addActionListener(e -> {
            String tipo = (String) cbTipoUser.getSelectedItem();
            if (tipo.equals("Médico")) {
                lblDadoExtra.setText("Especialidade:");
                cardLayoutExtra.show(painelDadoExtra, "ESPECIALIDADE");
            } else {
                lblDadoExtra.setText("Data de Nascimento:");
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