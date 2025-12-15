package view;

import controller.ClinicaController;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import javax.swing.*;

public class TelaCadastro extends JFrame {
    
    private JComboBox<Integer> cbDia, cbMes, cbAno;
    private JComboBox<String> cbEspecialidade; // especialidade agora é ComBox
    private JPanel painelVariavel;
    private CardLayout cardLayout;

    public TelaCadastro(ClinicaController controller) {
        setTitle("Cadastro de Usuário");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Tipo de Usuário:"));
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Paciente", "Médico"});
        add(cbTipo);

        add(new JLabel("Nome:"));
        JTextField txtNome = new JTextField();
        add(txtNome);

        // 3. campo variável (Data para Paciente e Especialidade para Médico)
        JLabel lblVariavel = new JLabel("Data de Nascimento:");
        add(lblVariavel);

        painelVariavel = new JPanel();
        cardLayout = new CardLayout();
        painelVariavel.setLayout(cardLayout);

        // -- DATA (Paciente)
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
        pnlData.add(cbDia); pnlData.add(new JLabel("/")); pnlData.add(cbMes); pnlData.add(new JLabel("/")); pnlData.add(cbAno);

        // -- ESPECIALIDADE (Médico)
        JPanel pnlEsp = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cbEspecialidade = new JComboBox<>(ClinicaController.ESPECIALIDADES); // lista global
        pnlEsp.add(cbEspecialidade);

        painelVariavel.add(pnlData, "DATA");
        painelVariavel.add(pnlEsp, "ESPECIALIDADE");
        add(painelVariavel);

        // 4. Plano (LISTA GLOBAL)
        add(new JLabel("Plano de Saúde:"));
        JComboBox<String> cbPlano = new JComboBox<>(ClinicaController.PLANOS);
        add(cbPlano);

        // Botões
        JButton btnVoltar = new JButton("Voltar");
        JButton btnSalvar = new JButton("Salvar Cadastro");
        add(btnVoltar); add(btnSalvar);
        
        this.getRootPane().setDefaultButton(btnSalvar);

        cbTipo.addActionListener(e -> {
            String tipo = (String) cbTipo.getSelectedItem();
            if (tipo.equals("Médico")) {
                lblVariavel.setText("Especialidade:");
                cardLayout.show(painelVariavel, "ESPECIALIDADE");
            } else {
                lblVariavel.setText("Data de Nascimento:");
                cardLayout.show(painelVariavel, "DATA");
            }
        });

        btnVoltar.addActionListener(e -> { controller.fazerLogout(); dispose(); });

        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome vazio!");

                String plano = (String) cbPlano.getSelectedItem();
                String tipo = (String) cbTipo.getSelectedItem();
                String infoVariavel = "";

                if (tipo.equals("Médico")) {
                    infoVariavel = (String) cbEspecialidade.getSelectedItem();
                } else {
                    int dia = (int) cbDia.getSelectedItem();
                    int mes = (int) cbMes.getSelectedItem();
                    int ano = (int) cbAno.getSelectedItem();
                    LocalDate dataNasc = LocalDate.of(ano, mes, dia);
                    if (dataNasc.isAfter(LocalDate.now())) throw new Exception("Data futura!");
                    int idade = Period.between(dataNasc, LocalDate.now()).getYears();
                    infoVariavel = String.valueOf(idade);
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
        cbDia.removeAllItems();
        int max = YearMonth.of(ano, mes).lengthOfMonth();
        for (int i = 1; i <= max; i++) cbDia.addItem(i);
    }
}