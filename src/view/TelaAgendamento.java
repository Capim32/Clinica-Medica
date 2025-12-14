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

    private JComboBox<Integer> cbDia;
    private JComboBox<Integer> cbMes;
    private JComboBox<Integer> cbAno;

    public TelaAgendamento(ClinicaController controller, Paciente pacienteLogado) {
        setTitle("Agendar Consulta");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Agendamento para: " + pacienteLogado.getNome());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo);

        // 1. Selecionar Médico
        JPanel painelMedico = new JPanel(new FlowLayout());
        painelMedico.add(new JLabel("Escolha o Médico:"));
        
        List<Medico> listaMedicos = controller.getListaMedicos();
        JComboBox<String> cbMedicos = new JComboBox<>();
        for (Medico m : listaMedicos) {
            cbMedicos.addItem(m.getId() + " - " + m.getNome() + " (" + m.getEspecialidade() + ")");
        }
        painelMedico.add(cbMedicos);
        add(painelMedico);

        // 2. Data (3 caixas dd/mm/aaaa) -> usar LocalDate foi ideia do estagiário, juntamente com a montagem da data
        JPanel painelData = new JPanel(new FlowLayout());
        painelData.add(new JLabel("Data da Consulta:"));

        // config dos combos
        cbDia = new JComboBox<>();
        cbMes = new JComboBox<>();
        cbAno = new JComboBox<>();

        // preencher ano (ano atual + 5 anos para frente)
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual; i <= anoAtual + 5; i++) {
            cbAno.addItem(i);
        }

        // preencher o mês (1 a 12)
        for (int i = 1; i <= 12; i++) {
            cbMes.addItem(i);
        }

        // adicionar Listeners para atualizar os dias quando mês/ano mudar
        cbMes.addActionListener(e -> atualizarDias());
        cbAno.addActionListener(e -> atualizarDias());

        atualizarDias();

        painelData.add(new JLabel("Dia:"));
        painelData.add(cbDia);
        painelData.add(new JLabel("Mês:"));
        painelData.add(cbMes);
        painelData.add(new JLabel("Ano:"));
        painelData.add(cbAno);

        add(painelData);

        // botões
        JButton btnAgendar = new JButton("Confirmar Agendamento");
        add(btnAgendar);
        
        JButton btnVoltar = new JButton("Cancelar");
        add(btnVoltar);

        this.getRootPane().setDefaultButton(btnAgendar);

        // lógica do botão Agendar
        btnAgendar.addActionListener(e -> {
            String itemSelecionado = (String) cbMedicos.getSelectedItem();
            
            if (itemSelecionado != null) {
                int idMedico = Integer.parseInt(itemSelecionado.split(" - ")[0]);
                
                // montar a data selecionada
                int dia = (int) cbDia.getSelectedItem();
                int mes = (int) cbMes.getSelectedItem();
                int ano = (int) cbAno.getSelectedItem();
                
                LocalDate dataConsulta = LocalDate.of(ano, mes, dia);
                
                // VALIDAÇÃO: não permitir data passada
                if (dataConsulta.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "Erro: Não é possível agendar uma consulta no passado, viajante no tempo!");
                } else {
                    // formata para String DD/MM/AAAA para salvar no arquivo
                    String dataFormatada = String.format("%02d/%02d/%04d", dia, mes, ano);
                    
                    boolean sucesso = controller.agendarConsulta(idMedico, pacienteLogado.getId(), dataFormatada);
                    if (sucesso) {
                        dispose();
                    }
                }
            } 
            else {
                JOptionPane.showMessageDialog(this, "Selecione um médico!");
            }
        });

        btnVoltar.addActionListener(e -> {
            controller.fazerLogout(); // limpa a sessao e volta ao inicio 
            dispose();
        });

        setVisible(true);
    }

    // Método para calcular quantos dias tem o mês selecionado (nosso funcionario não remunerado cuidou da parte de data!)
    private void atualizarDias() {
        if (cbMes.getSelectedItem() == null || cbAno.getSelectedItem() == null) return;

        int mes = (int) cbMes.getSelectedItem();
        int ano = (int) cbAno.getSelectedItem();
        
        // guarda o dia selecionado atualmente para tentar manter depois
        Object diaSelecionado = cbDia.getSelectedItem();

        cbDia.removeAllItems();
        
        // classe YearMonth do Java já sabe se é bissexto ou quantos dias tem o mês
        int diasNoMes = YearMonth.of(ano, mes).lengthOfMonth();

        for (int i = 1; i <= diasNoMes; i++) {
            cbDia.addItem(i);
        }

        // tenta selecionar o dia que estava antes (se ele ainda existir no novo mês)
        if (diaSelecionado != null && (int)diaSelecionado <= diasNoMes) {
            cbDia.setSelectedItem(diaSelecionado);
        }
    }
}