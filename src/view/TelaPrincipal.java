package view;

import controller.ClinicaController;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private final Color COR_HEADER = new Color(0, 102, 204);

    // auxiliar para ComboBox de Consultas (Avaliação e Cancelamento)
    private class ItemConsulta {
        Consulta c; String label;
        public ItemConsulta(Consulta c, String l) { this.c = c; this.label = l; }
        @Override public String toString() { return label; }
    }

    public TelaPrincipal(ClinicaController controller) {
        this.controller = controller;
        setTitle("Sistema Clínica Médica - Painel Principal");
        setSize(950, 700); // Um pouco mais largo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COR_HEADER);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        String nomeUser = controller.getMedicoLogado() != null ? 
            "Dr(a). " + controller.getMedicoLogado().getNome() : 
            "Paciente " + controller.getPacienteLogado().getNome();
        
        JLabel lblSaudacao = new JLabel("Bem-vindo(a), " + nomeUser);
        lblSaudacao.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSaudacao.setForeground(Color.WHITE);
        
        JButton btnSair = new JButton("Sair");
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> { controller.fazerLogout(); dispose(); });

        headerPanel.add(lblSaudacao, BorderLayout.WEST);
        headerPanel.add(btnSair, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBorder(new EmptyBorder(20, 20, 20, 20));

        if (controller.getMedicoLogado() != null) construirInterfaceMedico(conteudo);
        else construirInterfacePaciente(conteudo);

        add(conteudo, BorderLayout.CENTER);
        setVisible(true);
    }

    // --- INTERFACES ---
    private void construirInterfaceMedico(JPanel container) {
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.setBorder(BorderFactory.createTitledBorder("Minha Agenda"));
        
        cbDia = new JComboBox<>(); cbMes = new JComboBox<>(); cbAno = new JComboBox<>();
        int aa = LocalDate.now().getYear(); for(int i=aa-1;i<=aa+5;i++)cbAno.addItem(i); for(int i=1;i<=12;i++)cbMes.addItem(i); for(int i=1;i<=31;i++)cbDia.addItem(i); 
        LocalDate h = LocalDate.now(); cbDia.setSelectedItem(h.getDayOfMonth()); cbMes.setSelectedItem(h.getMonthValue()); cbAno.setSelectedItem(h.getYear());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarTabelaMedico());
        
        painelFiltro.add(new JLabel("Data:"));
        painelFiltro.add(cbDia); painelFiltro.add(new JLabel("/")); painelFiltro.add(cbMes); painelFiltro.add(new JLabel("/")); painelFiltro.add(cbAno);
        painelFiltro.add(Box.createHorizontalStrut(20)); painelFiltro.add(btnAtualizar);

        container.add(painelFiltro, BorderLayout.NORTH);

        String[] col = {"Data", "Status", "Paciente ID", "Valor", "Obj"};
        tableModel = new DefaultTableModel(col, 0) { public boolean isCellEditable(int r,int c){return false;} };
        tabela = new JTable(tableModel);
        tabela.removeColumn(tabela.getColumnModel().getColumn(4));
        tabela.setRowHeight(30); 
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        container.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRealizar = new JButton("Realizar Consulta");
        btnRealizar.setBackground(new Color(0, 153, 76)); btnRealizar.setForeground(Color.WHITE);
        btnRealizar.setPreferredSize(new Dimension(150, 40));
        JButton btnEditar = new JButton("Editar Perfil");
        
        btnRealizar.addActionListener(e -> realizarConsultaMedico());
        btnEditar.addActionListener(e -> abrirDialogoEdicaoMedico());
        
        pBtns.add(btnEditar); pBtns.add(btnRealizar);
        container.add(pBtns, BorderLayout.SOUTH);
        atualizarTabelaMedico();
    }

    private void construirInterfacePaciente(JPanel container) {
        JPanel panelBusca = new JPanel(new GridLayout(1, 4, 10, 10));
        panelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Especialistas"));

        cbFiltroEspecialidade = new JComboBox<>(); cbFiltroEspecialidade.addItem("Todas"); for(String s:ClinicaController.ESPECIALIDADES)cbFiltroEspecialidade.addItem(s);
        cbFiltroNome = new JComboBox<>(); cbFiltroNome.setEditable(true);

        panelBusca.add(new JLabel("Especialidade:", SwingConstants.RIGHT)); panelBusca.add(cbFiltroEspecialidade);
        panelBusca.add(new JLabel("Nome:", SwingConstants.RIGHT)); panelBusca.add(cbFiltroNome);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panelBusca, BorderLayout.CENTER);
        JButton btnBuscar = new JButton("Pesquisar");
        btnBuscar.addActionListener(e -> atualizarListaMedicosPaciente());
        cbFiltroEspecialidade.addActionListener(e -> atualizarListaMedicosPaciente());
        JPanel pB = new JPanel(new FlowLayout(FlowLayout.RIGHT)); pB.add(btnBuscar); wrapper.add(pB, BorderLayout.SOUTH);

        container.add(wrapper, BorderLayout.NORTH);

        listModelMedicos = new DefaultListModel<>();
        JList<String> listResultados = new JList<>(listModelMedicos);
        listResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Aumenta a altura de cada item da lista para caber o HTML
        listResultados.setFixedCellHeight(60); 
        
        JScrollPane scrollList = new JScrollPane(listResultados);
        scrollList.setBorder(BorderFactory.createTitledBorder("Resultados da Busca"));
        container.add(scrollList, BorderLayout.CENTER);

        JPanel pBtns = new JPanel(new GridLayout(1, 4, 10, 0));
        pBtns.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton b1 = new JButton("Agendar"); b1.setBackground(new Color(0, 102, 204)); b1.setForeground(Color.WHITE);
        JButton b2 = new JButton("Cancelar"); b2.setBackground(new Color(204, 0, 0)); b2.setForeground(Color.WHITE);
        JButton b3 = new JButton("Avaliar");
        JButton b4 = new JButton("Perfil");

        b1.addActionListener(e -> controller.abrirTelaAgendamento());
        b2.addActionListener(e -> cancelarAgendamento());
        b3.addActionListener(e -> avaliarConsultas());
        b4.addActionListener(e -> abrirDialogoEdicaoPaciente());

        pBtns.add(b1); pBtns.add(b2); pBtns.add(b3); pBtns.add(b4);
        container.add(pBtns, BorderLayout.SOUTH);
        
        atualizarListaMedicosPaciente();
    }

    // --- MÉTODOS DE ATUALIZAÇÃO ---

    private void atualizarListaMedicosPaciente() {
        listModelMedicos.clear();
        String esp = (String) cbFiltroEspecialidade.getSelectedItem();
        String nome = (String) cbFiltroNome.getEditor().getItem();
        List<Medico> res = controller.buscarMedicosPorFiltro(esp, nome);
        
        for(Medico m : res) { 
            String media = controller.getMediaAvaliacaoMedico(m.getId());
            String plano = m.getPlanoDeSaude();
            String planoExibicao;
            String corPlano;

            // lógica para aparecer "Consulta Particular", pq "Não tenho" tava feio 
            if (plano == null || plano.equalsIgnoreCase("Não tenho") || plano.equalsIgnoreCase("Nao tenho")) {
                planoExibicao = "Consulta Particular";
                corPlano = "black"; 
            } else {
                planoExibicao = plano;
                corPlano = "black";
            }

            // formatação com HTML básico para clareza 
            String html = String.format("<html>" +
                "<div style='padding:5px;'>" +
                "<b style='font-size:14px; color:#333;'>%s</b> <span style='color:gray; font-size:11px;'>(%s)</span><br/>" +
                "Atendimento: <b style='color:%s;'>%s</b>  |  Avaliação: <b>%s</b>" +
                "</div></html>", 
                m.getNome(), m.getEspecialidade(), corPlano, planoExibicao, media);

            listModelMedicos.addElement(html);
        }
    }

    private void atualizarTabelaMedico() {
        tableModel.setRowCount(0);
        String dStr = String.format("%02d/%02d/%04d", cbDia.getSelectedItem(), cbMes.getSelectedItem(), cbAno.getSelectedItem());
        List<Consulta> l = controller.getConsultasDoMedicoPorData(controller.getMedicoLogado().getId(), dStr);
        for (Consulta c : l) tableModel.addRow(new Object[]{c.getData(), c.getStatus(), c.getIdPaciente(), "R$ "+c.getValorPago(), c});
    }

    private void realizarConsultaMedico() {
        int l = tabela.getSelectedRow();
        if (l < 0) { JOptionPane.showMessageDialog(this, "Selecione uma consulta."); return; }
        Consulta c = (Consulta) tableModel.getValueAt(l, 4);
        if(c.getStatus().equals("REALIZADA")){JOptionPane.showMessageDialog(this,"Já realizada.\nObs: "+c.getProntuario());return;}

        JTextArea tP = new JTextArea(8, 30); tP.setLineWrap(true); tP.setWrapStyleWord(true);
        if(JOptionPane.showConfirmDialog(this, new Object[]{"Descrição do Atendimento:", new JScrollPane(tP)}, "Realizar Consulta", 2) == 0) {
            String txt = tP.getText().trim().isEmpty() ? "Sem obs" : tP.getText().replace("\n", " ").replace(";", ",");
            double v = 0.0; Paciente p = controller.getPacientePorId(c.getIdPaciente());
            boolean sp = p!=null && (p.getPlanoDeSaude()==null || p.getPlanoDeSaude().toLowerCase().contains("não"));
            if(sp){ String s=JOptionPane.showInputDialog(this,"Paciente Particular. Valor (R$):","0"); if(s!=null)try{v=Double.parseDouble(s.replace(",","."));}catch(Exception e){return;}else return; }
            else JOptionPane.showMessageDialog(this,"Convênio: "+(p!=null?p.getPlanoDeSaude():"?")+"\nValor: R$ 0,00");
            controller.atualizarStatusConsulta(c,"REALIZADA",v, txt); atualizarTabelaMedico();
        }
    }

    private void cancelarAgendamento() {
        List<Consulta> minhas = controller.getConsultasDoPaciente(controller.getPacienteLogado().getId());
        JComboBox<ItemConsulta> cb = new JComboBox<>();
        for (Consulta c : minhas) if (c.getStatus().equals("AGENDADA") || c.getStatus().equals("EM_ESPERA")) {
            Medico m = controller.getMedicoPorId(c.getIdMedico());
            cb.addItem(new ItemConsulta(c, c.getData() + " - " + (m != null ? m.getNome() : "Médico ID " + c.getIdMedico()) + " (" + c.getStatus() + ")"));
        }
        if (cb.getItemCount() == 0) { JOptionPane.showMessageDialog(this, "Sem agendamentos ativos."); return; }
        if (JOptionPane.showConfirmDialog(this, new Object[]{"Cancelar:", cb}, "Cancelar", 2) == 0) controller.cancelarConsulta(((ItemConsulta)cb.getSelectedItem()).c);
    }

    private void avaliarConsultas() {
        List<Consulta> minhas = controller.getConsultasDoPaciente(controller.getPacienteLogado().getId());
        JComboBox<ItemConsulta> cb = new JComboBox<>();
        for(Consulta c:minhas) if("REALIZADA".equals(c.getStatus()) && c.getAvaliacaoEstrelas()==0) {
            Medico m=controller.getMedicoPorId(c.getIdMedico());
            cb.addItem(new ItemConsulta(c, c.getData()+" - "+(m!=null?m.getNome():"ID "+c.getIdMedico())));
        }
        if(cb.getItemCount()==0) { JOptionPane.showMessageDialog(this,"Nada para avaliar."); return; }
        JTextField tN=new JTextField(); JTextField tT=new JTextField();
        if(JOptionPane.showConfirmDialog(this,new Object[]{"Consulta:",cb,"Nota (1-5):",tN,"Opinião:",tT},"Avaliar",2)==0){
            try{int n=Integer.parseInt(tN.getText()); controller.avaliarMedico(((ItemConsulta)cb.getSelectedItem()).c,n,tT.getText()); JOptionPane.showMessageDialog(this,"Obrigado!");}
            catch(Exception e){JOptionPane.showMessageDialog(this,"Nota inválida.");}
        }
    }
    
    private void abrirDialogoEdicaoMedico() {
        Medico m=controller.getMedicoLogado(); JTextField tN=new JTextField(m.getNome());
        JComboBox<String> bE=new JComboBox<>(ClinicaController.ESPECIALIDADES); bE.setSelectedItem(m.getEspecialidade());
        JComboBox<String> bP=new JComboBox<>(ClinicaController.PLANOS); bP.setSelectedItem(m.getPlanoDeSaude());
        if(JOptionPane.showConfirmDialog(this,new Object[]{"Nome:",tN,"Esp:",bE,"Plano:",bP},"Editar",2)==0) controller.atualizarPerfilMedico(tN.getText(),(String)bE.getSelectedItem(),(String)bP.getSelectedItem());
    }
    private void abrirDialogoEdicaoPaciente() {
        Paciente p=controller.getPacienteLogado(); JTextField tN=new JTextField(p.getNome());
        JComboBox<String> bP=new JComboBox<>(ClinicaController.PLANOS); bP.setSelectedItem(p.getPlanoDeSaude());
        if(JOptionPane.showConfirmDialog(this,new Object[]{"Nome:",tN,"Plano:",bP},"Editar",2)==0) controller.atualizarPerfilPaciente(tN.getText(),(String)bP.getSelectedItem());
    }
}