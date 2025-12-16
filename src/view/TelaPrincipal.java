package view;

import controller.ClinicaController;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Consulta;
import models.Medico;
import models.Paciente;

public class TelaPrincipal extends JFrame {

    private ClinicaController controller;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> cbDia, cbMes, cbAno;
    private JComboBox<String> cbFiltroEspecialidade, cbFiltroNome;
    private DefaultListModel<String> listModelMedicos;

    public TelaPrincipal(ClinicaController controller) {
        this.controller = controller;

        setTitle("Sistema Clínica Médica");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        String nomeUser = controller.getMedicoLogado() != null ? 
            "Dr(a). " + controller.getMedicoLogado().getNome() : 
            "Paciente " + controller.getPacienteLogado().getNome();
        
        JLabel lblSaudacao = new JLabel("Olá, " + nomeUser);
        lblSaudacao.setFont(new Font("Arial", Font.BOLD, 18));
        lblSaudacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> { controller.fazerLogout(); dispose(); });

        headerPanel.add(lblSaudacao, BorderLayout.WEST);
        headerPanel.add(btnSair, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        if (controller.getMedicoLogado() != null) construirInterfaceMedico();
        else construirInterfacePaciente();

        setVisible(true);
    }

    private void construirInterfaceMedico() {
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelSuperior.add(new JLabel("Consultas do dia: "));
        cbDia = new JComboBox<>(); cbMes = new JComboBox<>(); cbAno = new JComboBox<>();
        
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual - 1; i <= anoAtual + 5; i++) cbAno.addItem(i);
        for (int i = 1; i <= 12; i++) cbMes.addItem(i);
        for (int i=1; i<=31; i++) cbDia.addItem(i); 
        
        LocalDate hoje = LocalDate.now();
        cbDia.setSelectedItem(hoje.getDayOfMonth());
        cbMes.setSelectedItem(hoje.getMonthValue());
        cbAno.setSelectedItem(hoje.getYear());

        JButton btnAtualizar = new JButton("Carregar Agenda");
        btnAtualizar.addActionListener(e -> atualizarTabelaMedico());
        painelSuperior.add(cbDia); painelSuperior.add(new JLabel("/"));
        painelSuperior.add(cbMes); painelSuperior.add(new JLabel("/"));
        painelSuperior.add(cbAno); painelSuperior.add(btnAtualizar);

        JPanel container = new JPanel(new BorderLayout());
        container.add(painelSuperior, BorderLayout.NORTH);

        String[] colunas = {"Data", "Status", "Paciente ID", "Valor", "Objeto"};
        tableModel = new DefaultTableModel(colunas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabela = new JTable(tableModel);
        tabela.removeColumn(tabela.getColumnModel().getColumn(4));
        container.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        JPanel panelBotoes = new JPanel();
        JButton btnRealizar = new JButton("Marcar Realizada / Cobrar");
        JButton btnEditar = new JButton("Editar Perfil");
        JButton btnVoltar = new JButton("Voltar");

        btnRealizar.addActionListener(e -> realizarConsultaMedico());
        btnEditar.addActionListener(e -> abrirDialogoEdicaoMedico());
        btnVoltar.addActionListener(e -> { controller.fazerLogout(); dispose(); });

        panelBotoes.add(btnRealizar); panelBotoes.add(btnEditar); panelBotoes.add(btnVoltar);
        container.add(panelBotoes, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);
        atualizarTabelaMedico();
    }

    private void atualizarTabelaMedico() {
        tableModel.setRowCount(0);
        int d = (int) cbDia.getSelectedItem();
        int m = (int) cbMes.getSelectedItem();
        int a = (int) cbAno.getSelectedItem();
        String dataStr = String.format("%02d/%02d/%04d", d, m, a);
        List<Consulta> lista = controller.getConsultasDoMedicoPorData(controller.getMedicoLogado().getId(), dataStr);
        for (Consulta c : lista) tableModel.addRow(new Object[]{c.getData(), c.getStatus(), c.getIdPaciente(), "R$ " + c.getValorPago(), c});
    }

    private void realizarConsultaMedico() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            Consulta c = (Consulta) tableModel.getValueAt(linha, 4);
            if (c.getStatus().equals("REALIZADA")) { JOptionPane.showMessageDialog(this, "Já realizada."); return; }
            
            double valor = 0.0;
            Paciente p = controller.getPacientePorId(c.getIdPaciente());
            boolean semPlano = false;
            if (p != null) {
                String plano = p.getPlanoDeSaude();
                if (plano == null || plano.equalsIgnoreCase("Não tenho") || plano.equalsIgnoreCase("Nao tenho")) {
                    semPlano = true;
                }
            }

            if (semPlano) {
                String valorStr = JOptionPane.showInputDialog(this, "Paciente Particular.\nValor (R$):", "0");
                if (valorStr == null) return;
                try { valor = Double.parseDouble(valorStr.replace(",", ".")); } 
                catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Inválido!"); return; }
            } else {
                JOptionPane.showMessageDialog(this, "Paciente com plano (" + (p!=null?p.getPlanoDeSaude():"?") + "). Valor: R$ 0,00");
            }

            controller.atualizarStatusConsulta(c, "REALIZADA", valor);
            atualizarTabelaMedico();
        } else { JOptionPane.showMessageDialog(this, "Selecione uma consulta."); }
    }

    private void construirInterfacePaciente() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        JPanel panelFiltros = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtrar Médicos"));

        panelFiltros.add(new JLabel("Especialidade:"));
        cbFiltroEspecialidade = new JComboBox<>();
        cbFiltroEspecialidade.addItem("Todas");
        for (String esp : ClinicaController.ESPECIALIDADES) cbFiltroEspecialidade.addItem(esp);
        
        panelFiltros.add(new JLabel("Nome (opcional):"));
        cbFiltroNome = new JComboBox<>();
        cbFiltroNome.setEditable(true);
        
        panelFiltros.add(cbFiltroEspecialidade);
        panelFiltros.add(cbFiltroNome);

        listModelMedicos = new DefaultListModel<>();
        JList<String> listResultados = new JList<>(listModelMedicos);
        
        JButton btnBuscar = new JButton("Pesquisar / Atualizar Lista");
        btnBuscar.addActionListener(e -> atualizarListaMedicosPaciente());
        cbFiltroEspecialidade.addActionListener(e -> atualizarListaMedicosPaciente());

        JPanel panelBuscaCompleto = new JPanel(new BorderLayout());
        panelBuscaCompleto.add(panelFiltros, BorderLayout.CENTER);
        panelBuscaCompleto.add(btnBuscar, BorderLayout.SOUTH);

        JPanel panelBotoes = new JPanel();
        JButton btnAgendar = new JButton("Agendar");
        JButton btnAvaliar = new JButton("Avaliar Pendentes");
        JButton btnEditar = new JButton("Meu Perfil");
        JButton btnVoltar = new JButton("Voltar");

        btnAgendar.addActionListener(e -> controller.abrirTelaAgendamento());
        btnEditar.addActionListener(e -> abrirDialogoEdicaoPaciente());
        btnAvaliar.addActionListener(e -> avaliarConsultas());
        btnVoltar.addActionListener(e -> { controller.fazerLogout(); dispose(); });

        panelBotoes.add(btnAgendar); panelBotoes.add(btnAvaliar);
        panelBotoes.add(btnEditar); panelBotoes.add(btnVoltar);

        panelCentral.add(panelBuscaCompleto, BorderLayout.NORTH);
        panelCentral.add(new JScrollPane(listResultados), BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
        atualizarListaMedicosPaciente();
    }

    private void atualizarListaMedicosPaciente() {
        listModelMedicos.clear();
        String esp = (String) cbFiltroEspecialidade.getSelectedItem();
        String nome = (String) cbFiltroNome.getEditor().getItem();
        List<Medico> res = controller.buscarMedicosPorFiltro(esp, nome);
        for (Medico m : res) {
            String media = controller.getMediaAvaliacaoMedico(m.getId());
            listModelMedicos.addElement(m.getId() + " - " + m.getNome() + " (" + m.getEspecialidade() + ") | " + m.getPlanoDeSaude() + " | Nota: " + media);
        }
    }

    // classe auxiliar para formatação
    private class ItemConsulta {
        Consulta c;
        String label;
        public ItemConsulta(Consulta c, String label) { this.c = c; this.label = label; }
        @Override public String toString() { return label; }
    }

    private void avaliarConsultas() {
        List<Consulta> minhas = controller.getConsultasDoPaciente(controller.getPacienteLogado().getId());
        JComboBox<ItemConsulta> cbPendentes = new JComboBox<>();
        
        for (Consulta c : minhas) {
            if ("REALIZADA".equals(c.getStatus()) && c.getAvaliacaoEstrelas() == 0) {
                Medico m = controller.getMedicoPorId(c.getIdMedico());
                String nomeMed = (m != null) ? m.getNome() : "ID " + c.getIdMedico();
                String txt = String.format("%s - %s", c.getData(), nomeMed);
                cbPendentes.addItem(new ItemConsulta(c, txt));
            }
        }
        
        if (cbPendentes.getItemCount() == 0) { JOptionPane.showMessageDialog(this, "Nada para avaliar."); return; }
        
        JTextField txtNota = new JTextField();
        JTextField txtTexto = new JTextField();
        Object[] msg = {"Consulta:", cbPendentes, "Nota (1-5):", txtNota, "Comentário:", txtTexto};
        if (JOptionPane.showConfirmDialog(this, msg, "Avaliar", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
             try {
                 int nota = Integer.parseInt(txtNota.getText());
                 if(nota < 1 || nota > 5) throw new Exception("Nota inválida");
                 ItemConsulta item = (ItemConsulta) cbPendentes.getSelectedItem();
                 controller.avaliarMedico(item.c, nota, txtTexto.getText());
                 JOptionPane.showMessageDialog(this, "Enviado!");
             } catch(Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
        }
    }

    private void abrirDialogoEdicaoMedico() {
        Medico m = controller.getMedicoLogado();
        JTextField txtNome = new JTextField(m.getNome());
        JComboBox<String> boxEsp = new JComboBox<>(ClinicaController.ESPECIALIDADES);
        boxEsp.setSelectedItem(m.getEspecialidade());
        JComboBox<String> boxPlano = new JComboBox<>(ClinicaController.PLANOS);
        boxPlano.setSelectedItem(m.getPlanoDeSaude());

        Object[] msg = {"Nome:", txtNome, "Especialidade:", boxEsp, "Plano:", boxPlano};
        if (JOptionPane.showConfirmDialog(this, msg, "Editar Perfil", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            controller.atualizarPerfilMedico(txtNome.getText(), (String)boxEsp.getSelectedItem(), (String)boxPlano.getSelectedItem());
        }
    }

    private void abrirDialogoEdicaoPaciente() {
        Paciente p = controller.getPacienteLogado();
        JTextField txtNome = new JTextField(p.getNome());
        JComboBox<String> boxPlano = new JComboBox<>(ClinicaController.PLANOS);
        boxPlano.setSelectedItem(p.getPlanoDeSaude());
        Object[] msg = {"Nome:", txtNome, "Plano:", boxPlano};
        if (JOptionPane.showConfirmDialog(this, msg, "Editar Perfil", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            controller.atualizarPerfilPaciente(txtNome.getText(), (String)boxPlano.getSelectedItem());
        }
    }
}