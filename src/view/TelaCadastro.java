package view;

import controller.ClinicaController;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import javax.swing.*;

public class TelaCadastro extends JFrame {
    
    private JComboBox<Integer> cbDia, cbMes, cbAno;
    private JTextField txtEspecialidade;
    private JPanel painelVariavel;
    private CardLayout cardLayout;

    public TelaCadastro(ClinicaController controller) {
        setTitle("Cadastro de Usuário");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        // 1. tipo
        add(new JLabel("Tipo de Usuário:"));
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Paciente", "Médico"});
        add(cbTipo);

        // 2. nome
        add(new JLabel("Nome:"));
        JTextField txtNome = new JTextField();
        add(txtNome);

        // 3. campo variável (Idade ou Especialidade)
        JLabel lblVariavel = new JLabel("Data de Nascimento:");
        add(lblVariavel);

        painelVariavel = new JPanel();
        cardLayout = new CardLayout();
        painelVariavel.setLayout(cardLayout);

        // data (Paciente)
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

        pnlData.add(cbDia);
        pnlData.add(new JLabel("/"));
        pnlData.add(cbMes);
        pnlData.add(new JLabel("/"));
        pnlData.add(cbAno);

        // especialidade (Médico)
        JPanel pnlTexto = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtEspecialidade = new JTextField(15);
        pnlTexto.add(txtEspecialidade);

        painelVariavel.add(pnlData, "DATA");
        painelVariavel.add(pnlTexto, "TEXTO");
        add(painelVariavel);

        // 4. plano de saúde
        add(new JLabel("Plano de Saúde:"));
        String[] planos = {"Nao tenho", "Hapvida", "Unimed", "Outro"};
        JComboBox<String> cbPlano = new JComboBox<>(planos);
        add(cbPlano);

        // botões
        JButton btnVoltar = new JButton("Voltar");
        JButton btnSalvar = new JButton("Salvar Cadastro");

        add(btnVoltar);
        add(btnSalvar);
        
        this.getRootPane().setDefaultButton(btnSalvar);

        cbTipo.addActionListener(e -> {
            String tipo = (String) cbTipo.getSelectedItem();
            if (tipo.equals("Médico")) {
                lblVariavel.setText("Especialidade:");
                cardLayout.show(painelVariavel, "TEXTO");
            } else {
                lblVariavel.setText("Data de Nascimento:");
                cardLayout.show(painelVariavel, "DATA");
            }
        });

        // AÇÃO DO BOTÃO VOLTAR
        btnVoltar.addActionListener(e -> {
            controller.fazerLogout(); // limpa a sessao e volta ao inicio
            dispose();
        });

        // AÇÃO DO BOTÃO SALVAR
        btnSalvar.addActionListener(e -> {
            try {
                // VALIDAÇÃO: NOME VAZIO
                String nome = txtNome.getText();
                if (nome == null || nome.trim().isEmpty()) {
                    throw new Exception("O nome do usuário não pode ser vazio!");
                }

                String plano = (String) cbPlano.getSelectedItem();
                String tipo = (String) cbTipo.getSelectedItem();
                String infoVariavel = "";

                if (tipo.equals("Médico")) {
                    infoVariavel = txtEspecialidade.getText();
                    if(infoVariavel.trim().isEmpty()) throw new Exception("Digite a especialidade.");
                } else {
                    int dia = (int) cbDia.getSelectedItem();
                    int mes = (int) cbMes.getSelectedItem();
                    int ano = (int) cbAno.getSelectedItem();
                    
                    LocalDate dataNasc = LocalDate.of(ano, mes, dia);
                    if (dataNasc.isAfter(LocalDate.now())) {
                        throw new Exception("Data de nascimento inválida (futuro).");
                    }

                    int idadeCalculada = Period.between(dataNasc, LocalDate.now()).getYears();
                    infoVariavel = String.valueOf(idadeCalculada);
                }

                controller.cadastrarUsuario(tipo, nome, plano, infoVariavel);
                controller.abrirTelaLogin(); 
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    private void atualizarDias() {
        if (cbMes.getSelectedItem() == null || cbAno.getSelectedItem() == null) return;
        int mes = (int) cbMes.getSelectedItem();
        int ano = (int) cbAno.getSelectedItem();
        Object diaAtual = cbDia.getSelectedItem();

        cbDia.removeAllItems();
        int maxDias = YearMonth.of(ano, mes).lengthOfMonth();
        
        for (int i = 1; i <= maxDias; i++) cbDia.addItem(i);
        
        if (diaAtual != null && (int)diaAtual <= maxDias) cbDia.setSelectedItem(diaAtual);
    }
}