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

    // auxiliar para ComboBox de Consultas (Avaliação e Cancelamento)
    private class ItemConsulta {
        Consulta c;
        String label;
        public ItemConsulta(Consulta c, String label) { this.c = c; this.label = label; }
        @Override public String toString() { return label; }
    }

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
        int aa = LocalDate.now().getYear(); for(int i=aa-1;i<=aa+5;i++)cbAno.addItem(i); for(int i=1;i<=12;i++)cbMes.addItem(i); for(int i=1;i<=31;i++)cbDia.addItem(i); 
        LocalDate h = LocalDate.now(); cbDia.setSelectedItem(h.getDayOfMonth()); cbMes.setSelectedItem(h.getMonthValue()); cbAno.setSelectedItem(h.getYear());

        JButton btnAtualizar = new JButton("Carregar Agenda");
        btnAtualizar.addActionListener(e -> atualizarTabelaMedico());
        painelSuperior.add(cbDia); painelSuperior.add(new JLabel("/")); painelSuperior.add(cbMes); painelSuperior.add(new JLabel("/")); painelSuperior.add(cbAno); painelSuperior.add(btnAtualizar);

        JPanel container = new JPanel(new BorderLayout()); container.add(painelSuperior, BorderLayout.NORTH);
        String[] col = {"Data", "Status", "Pac. ID", "Valor", "Obj"};
        tableModel = new DefaultTableModel(col, 0) { public boolean isCellEditable(int r,int c){return false;} };
        tabela = new JTable(tableModel); tabela.removeColumn(tabela.getColumnModel().getColumn(4));
        container.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        JPanel pBtns = new JPanel();
        JButton btnRealizar = new JButton("Marcar Realizada / Cobrar");
        JButton btnEditar = new JButton("Editar Perfil");
        JButton btnVoltar = new JButton("Voltar");
        btnRealizar.addActionListener(e -> realizarConsultaMedico());
        btnEditar.addActionListener(e -> abrirDialogoEdicaoMedico());
        btnVoltar.addActionListener(e -> { controller.fazerLogout(); dispose(); });
        pBtns.add(btnRealizar); pBtns.add(btnEditar); pBtns.add(btnVoltar);
        container.add(pBtns, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);
        atualizarTabelaMedico();
    }

    private void atualizarTabelaMedico() {
        tableModel.setRowCount(0);
        String dStr = String.format("%02d/%02d/%04d", cbDia.getSelectedItem(), cbMes.getSelectedItem(), cbAno.getSelectedItem());
        List<Consulta> l = controller.getConsultasDoMedicoPorData(controller.getMedicoLogado().getId(), dStr);
        for (Consulta c : l) tableModel.addRow(new Object[]{c.getData(), c.getStatus(), c.getIdPaciente(), "R$ "+c.getValorPago(), c});
    }

    private void realizarConsultaMedico() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            Consulta c = (Consulta) tableModel.getValueAt(linha, 4);
            
            if (c.getStatus().equals("REALIZADA")) {
                JOptionPane.showMessageDialog(this, "Esta consulta já foi realizada.\nProntuário: " + c.getProntuario());
                return;
            }

            // 1. prontuário
            JTextArea txtProntuario = new JTextArea(10, 30);
            txtProntuario.setLineWrap(true);
            txtProntuario.setWrapStyleWord(true);
            
            Object[] msgProntuario = {
                "Descreva o atendimento (Sintomas, Tratamento, Exames):", 
                new JScrollPane(txtProntuario)
            };

            int option = JOptionPane.showConfirmDialog(this, msgProntuario, "Realizar Consulta", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                String textoProntuario = txtProntuario.getText();
                if (textoProntuario.trim().isEmpty()) {
                    textoProntuario = "Sem observações.";
                }
                // evita quebra de linha no .txt (substitui Enter por espaço)
                textoProntuario = textoProntuario.replace("\n", " ").replace(";", ",");

                // 2. cobrança
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
                    // Se não tem plano, GERA CONTA (Médico decide o valor)
                    String valorStr = JOptionPane.showInputDialog(this, 
                        "Paciente Particular (Sem Plano).\n" +
                        "Gere a cobrança de acordo com sua especialidade (R$):", "0");
                    
                    if (valorStr == null) return; // Cancelou
                    
                    try {
                        valor = Double.parseDouble(valorStr.replace(",", "."));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Valor inválido! Operação cancelada.");
                        return;
                    }
                } else {
                    // Se tem plano, NÃO PAGA NADA
                    JOptionPane.showMessageDialog(this, 
                        "Paciente possui convênio (" + (p != null ? p.getPlanoDeSaude() : "?") + ").\n" +
                        "A conta gerada para o paciente é R$ 0,00.");
                    valor = 0.0;
                }

                // 3. salvar tudo
                controller.atualizarStatusConsulta(c, "REALIZADA", valor, textoProntuario);
                
                JOptionPane.showMessageDialog(this, "Consulta registrada com sucesso!");
                atualizarTabelaMedico();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta na tabela.");
        }
    }

    private void construirInterfacePaciente() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        JPanel panelFiltros = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtrar Médicos"));

        panelFiltros.add(new JLabel("Especialidade:"));
        cbFiltroEspecialidade = new JComboBox<>(); cbFiltroEspecialidade.addItem("Todas"); for(String s:ClinicaController.ESPECIALIDADES)cbFiltroEspecialidade.addItem(s);
        panelFiltros.add(new JLabel("Nome:")); cbFiltroNome = new JComboBox<>(); cbFiltroNome.setEditable(true);
        panelFiltros.add(cbFiltroEspecialidade); panelFiltros.add(cbFiltroNome);

        listModelMedicos = new DefaultListModel<>(); JList<String> listResultados = new JList<>(listModelMedicos);
        JButton btnBuscar = new JButton("Pesquisar");
        btnBuscar.addActionListener(e -> atualizarListaMedicosPaciente());
        cbFiltroEspecialidade.addActionListener(e -> atualizarListaMedicosPaciente());

        JPanel pBusca = new JPanel(new BorderLayout()); pBusca.add(panelFiltros, BorderLayout.CENTER); pBusca.add(btnBuscar, BorderLayout.SOUTH);

        JPanel pBtns = new JPanel();
        JButton btnAgendar = new JButton("Agendar");
        // ADICIONADO: Botão Cancelar
        JButton btnCancelar = new JButton("Cancelar Consulta");
        JButton btnAvaliar = new JButton("Avaliar Pendentes");
        JButton btnEditar = new JButton("Meu Perfil");
        JButton btnVoltar = new JButton("Voltar");

        btnAgendar.addActionListener(e -> controller.abrirTelaAgendamento());
        // Ação de Cancelar
        btnCancelar.addActionListener(e -> cancelarAgendamento());
        btnAvaliar.addActionListener(e -> avaliarConsultas());
        btnEditar.addActionListener(e -> abrirDialogoEdicaoPaciente());
        btnVoltar.addActionListener(e -> { controller.fazerLogout(); dispose(); });

        pBtns.add(btnAgendar); pBtns.add(btnCancelar); pBtns.add(btnAvaliar); pBtns.add(btnEditar); pBtns.add(btnVoltar);

        panelCentral.add(pBusca, BorderLayout.NORTH); panelCentral.add(new JScrollPane(listResultados), BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER); add(pBtns, BorderLayout.SOUTH);
        atualizarListaMedicosPaciente();
    }

    private void atualizarListaMedicosPaciente() {
        listModelMedicos.clear();
        String esp = (String) cbFiltroEspecialidade.getSelectedItem();
        String nome = (String) cbFiltroNome.getEditor().getItem();
        List<Medico> res = controller.buscarMedicosPorFiltro(esp, nome);
        for(Medico m:res) { String md=controller.getMediaAvaliacaoMedico(m.getId()); listModelMedicos.addElement(m.getId()+" - "+m.getNome()+" ("+m.getEspecialidade()+") | "+m.getPlanoDeSaude()+" | Nota: "+md); }
    }

    private void cancelarAgendamento() {
        List<Consulta> minhas = controller.getConsultasDoPaciente(controller.getPacienteLogado().getId());
        JComboBox<ItemConsulta> cbAtivas = new JComboBox<>();
        
        for (Consulta c : minhas) {
            // mostra apenas consultas futuras ou em espera
            if (c.getStatus().equals("AGENDADA") || c.getStatus().equals("EM_ESPERA")) {
                Medico m = controller.getMedicoPorId(c.getIdMedico());
                String nomeM = m != null ? m.getNome() : "ID "+c.getIdMedico();
                String txt = c.getData() + " - " + nomeM + " (" + c.getStatus() + ")";
                cbAtivas.addItem(new ItemConsulta(c, txt));
            }
        }
        
        if (cbAtivas.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Você não tem agendamentos ativos para cancelar.");
            return;
        }

        Object[] msg = {"Selecione para cancelar:", cbAtivas};
        if (JOptionPane.showConfirmDialog(this, msg, "Cancelar Agendamento", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            ItemConsulta item = (ItemConsulta) cbAtivas.getSelectedItem();
            controller.cancelarConsulta(item.c);
        }
    }

    private void avaliarConsultas() {
        List<Consulta> minhas = controller.getConsultasDoPaciente(controller.getPacienteLogado().getId());
        JComboBox<ItemConsulta> cbPendentes = new JComboBox<>();
        for(Consulta c:minhas) if("REALIZADA".equals(c.getStatus()) && c.getAvaliacaoEstrelas()==0) {
            Medico m=controller.getMedicoPorId(c.getIdMedico()); String nm=m!=null?m.getNome():"ID "+c.getIdMedico();
            cbPendentes.addItem(new ItemConsulta(c, c.getData()+" - "+nm));
        }
        if(cbPendentes.getItemCount()==0) { JOptionPane.showMessageDialog(this,"Nada para avaliar."); return; }
        JTextField tN=new JTextField(); JTextField tT=new JTextField();
        if(JOptionPane.showConfirmDialog(this,new Object[]{"Consulta:",cbPendentes,"Nota (1-5):",tN,"Texto:",tT},"Avaliar",2)==0){
            try{int n=Integer.parseInt(tN.getText()); controller.avaliarMedico(((ItemConsulta)cbPendentes.getSelectedItem()).c,n,tT.getText()); JOptionPane.showMessageDialog(this,"Enviado!");}
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