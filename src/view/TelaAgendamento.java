package view;

import controller.ClinicaController;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(500, 600); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // BoxLayout para empilhar melhor os componentes de tamanhos variados
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. título
        JLabel lblTitulo = new JLabel("Novo Agendamento");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblTitulo);
        add(Box.createVerticalStrut(20));

        // 2. seleção de médico
        JPanel painelMedico = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelMedico.setMaximumSize(new Dimension(500, 40));
        painelMedico.add(new JLabel("Selecione o Médico:"));
        
        List<Medico> listaFiltrada = controller.getMedicosCompativeis(pacienteLogado);
        cbMedicos = new JComboBox<>();
        
        if (listaFiltrada.isEmpty()) {
            cbMedicos.addItem("Nenhum médico compatível.");
            cbMedicos.setEnabled(false);
        } else {
            for (Medico m : listaFiltrada) {
                // Formatação simples no dropdown
                cbMedicos.addItem(m.getId() + " - " + m.getNome() + " (" + m.getEspecialidade() + ")");
            }
        }
        
        cbMedicos.addActionListener(e -> atualizarReviews());
        painelMedico.add(cbMedicos);
        add(painelMedico);

        // 3. área de reviews (maior para melhor visualização)
        JLabel lblRev = new JLabel("Reputação e Avaliações:");
        lblRev.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lblRev);
        
        txtReviews = new JTextArea(10, 30); 
        txtReviews.setEditable(false);
        txtReviews.setLineWrap(true);
        txtReviews.setWrapStyleWord(true);
        txtReviews.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JScrollPane scrollReviews = new JScrollPane(txtReviews);
        scrollReviews.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        // força um tamanho maior visualmente
        scrollReviews.setPreferredSize(new Dimension(450, 200)); 
        
        add(scrollReviews);
        add(Box.createVerticalStrut(15));

        // 4. data
        JPanel painelData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelData.setMaximumSize(new Dimension(500, 40));
        painelData.add(new JLabel("Data Desejada:"));
        
        cbDia = new JComboBox<>(); 
        cbMes = new JComboBox<>(); 
        cbAno = new JComboBox<>();
        
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i <= anoAtual + 5; i++) cbAno.addItem(i);
        for (int i = 1; i <= 12; i++) cbMes.addItem(i);
        
        cbMes.addActionListener(e -> atualizarDias());
        cbAno.addActionListener(e -> atualizarDias());
        atualizarDias();
        
        painelData.add(cbDia); 
        painelData.add(new JLabel("/"));
        painelData.add(cbMes); 
        painelData.add(new JLabel("/"));
        painelData.add(cbAno);
        add(painelData);
        add(Box.createVerticalStrut(20));

        // 5. Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgendar = new JButton("Confirmar Agendamento");
        JButton btnVoltar = new JButton("Cancelar");
        
        btnAgendar.setBackground(new Color(0, 102, 204));
        btnAgendar.setForeground(Color.WHITE);
        btnAgendar.setPreferredSize(new Dimension(180, 35));
        
        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnAgendar);
        add(painelBotoes);

        this.getRootPane().setDefaultButton(btnAgendar);

        // Ações
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
                
                String planoP = pacienteLogado.getPlanoDeSaude();
                boolean semPlano = (planoP == null || planoP.equalsIgnoreCase("Não tenho") || planoP.equalsIgnoreCase("Nao tenho"));
                
                if (semPlano) {
                    int opt = JOptionPane.showConfirmDialog(this, 
                        "Este médico atenderá como CONSULTA PARTICULAR.\nO valor será definido no momento do atendimento.\nDeseja confirmar?", 
                        "Consulta Particular", JOptionPane.YES_NO_OPTION);
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
        
        StringBuilder sb = new StringBuilder();
        sb.append("NOTA MÉDIA: ").append(media).append("\n\n");
        sb.append("Últimos Comentários:\n--------------------------------\n");
        
        if(revs.isEmpty()) sb.append("(Nenhuma avaliação escrita ainda)");
        else for(String r : revs) sb.append("• ").append(r).append("\n\n");
        
        txtReviews.setText(sb.toString());
        txtReviews.setCaretPosition(0);
    }
}