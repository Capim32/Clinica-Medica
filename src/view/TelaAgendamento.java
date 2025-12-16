package view;

import controller.ClinicaController;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.*;
import models.Medico;
import models.Paciente;

public class TelaAgendamento extends JFrame {

    private JComboBox<Integer> cbDia, cbMes, cbAno;
    private JComboBox<String> cbMedicos;
    private JTextArea txtReviews;
    private ClinicaController controller;

    public TelaAgendamento(ClinicaController controller, Paciente pacienteLogado) {
        this.controller = controller;
        
        setTitle("Agendar Consulta");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Agendamento para: " + pacienteLogado.getNome());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTitulo);

        // SELEÇÃO DE MÉDICO COM FILTRO
        JPanel painelMedico = new JPanel(new FlowLayout());
        painelMedico.add(new JLabel("Médico:"));
        
        // MUDANÇA NESSA BOMBA: Usa getMedicosCompativeis em vez de getListaMedicos
        List<Medico> listaFiltrada = controller.getMedicosCompativeis(pacienteLogado);
        
        cbMedicos = new JComboBox<>();
        if (listaFiltrada.isEmpty()) {
            cbMedicos.addItem("Nenhum médico aceita seu plano :(");
            cbMedicos.setEnabled(false);
        } else {
            for (Medico m : listaFiltrada) {
                cbMedicos.addItem(m.getId() + " - " + m.getNome() + " (" + m.getEspecialidade() + ")");
            }
        }
        
        cbMedicos.addActionListener(e -> atualizarReviews());
        painelMedico.add(cbMedicos);
        add(painelMedico);

        // Reviews
        txtReviews = new JTextArea(5, 30);
        txtReviews.setEditable(false);
        txtReviews.setLineWrap(true);
        txtReviews.setWrapStyleWord(true);
        JScrollPane scrollReviews = new JScrollPane(txtReviews);
        scrollReviews.setBorder(BorderFactory.createTitledBorder("Avaliações"));
        add(scrollReviews);

        // Data
        JPanel painelData = new JPanel(new FlowLayout());
        painelData.add(new JLabel("Data:"));
        cbDia = new JComboBox<>(); cbMes = new JComboBox<>(); cbAno = new JComboBox<>();
        
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i <= anoAtual + 5; i++) cbAno.addItem(i);
        for (int i = 1; i <= 12; i++) cbMes.addItem(i);
        cbMes.addActionListener(e -> atualizarDias());
        cbAno.addActionListener(e -> atualizarDias());
        atualizarDias();
        
        painelData.add(cbDia); painelData.add(new JLabel("/"));
        painelData.add(cbMes); painelData.add(new JLabel("/"));
        painelData.add(cbAno);
        add(painelData);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAgendar = new JButton("Confirmar");
        JButton btnVoltar = new JButton("Cancelar");
        painelBotoes.add(btnAgendar); painelBotoes.add(btnVoltar);
        add(painelBotoes);

        this.getRootPane().setDefaultButton(btnAgendar);

        btnAgendar.addActionListener(e -> {
            if (!cbMedicos.isEnabled() || cbMedicos.getSelectedItem() == null) return;
            
            String item = (String) cbMedicos.getSelectedItem();
            int idMedico = Integer.parseInt(item.split(" - ")[0]);
            int dia = (int) cbDia.getSelectedItem();
            int mes = (int) cbMes.getSelectedItem();
            int ano = (int) cbAno.getSelectedItem();
            LocalDate data = LocalDate.of(ano, mes, dia);
            
            if (data.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Data inválida (viajante no tempo)!");
            } else {
                String dataFmt = String.format("%02d/%02d/%04d", dia, mes, ano);
                // verifica plano 'Não tenho' para avisar cobrança
                String planoP = pacienteLogado.getPlanoDeSaude();
                if (planoP.equalsIgnoreCase("Não tenho") || planoP.equalsIgnoreCase("Nao tenho")) {
                    int opt = JOptionPane.showConfirmDialog(this, "Consulta Particular (Sem Plano).\nO médico definirá o valor. Continuar?", "Aviso", JOptionPane.YES_NO_OPTION);
                    if (opt != JOptionPane.YES_OPTION) return;
                }
                
                if (controller.agendarConsulta(idMedico, pacienteLogado.getId(), dataFmt)) dispose();
            }
        });

        btnVoltar.addActionListener(e -> dispose());
        atualizarReviews();
        setVisible(true);
    }

    private void atualizarDias() {
        if (cbMes.getSelectedItem() == null || cbAno.getSelectedItem() == null) return;
        int m = (int) cbMes.getSelectedItem();
        int a = (int) cbAno.getSelectedItem();
        cbDia.removeAllItems();
        int max = YearMonth.of(a, m).lengthOfMonth();
        for (int i=1; i<=max; i++) cbDia.addItem(i);
    }

    private void atualizarReviews() {
        if (!cbMedicos.isEnabled() || cbMedicos.getSelectedItem() == null) {
            txtReviews.setText(""); return;
        }
        String item = (String) cbMedicos.getSelectedItem();
        int id = Integer.parseInt(item.split(" - ")[0]);
        String media = controller.getMediaAvaliacaoMedico(id);
        List<String> revs = controller.getUltimasAvaliacoes(id);
        StringBuilder sb = new StringBuilder("Nota: " + media + "\n\n");
        if(revs.isEmpty()) sb.append("Sem comentários.");
        else for(String r : revs) sb.append("• ").append(r).append("\n");
        txtReviews.setText(sb.toString());
        txtReviews.setCaretPosition(0);
    }
}