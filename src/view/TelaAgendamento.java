package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controller.ClinicaController;
import models.Medico;
import models.Paciente;

public class TelaAgendamento extends JFrame {

    public TelaAgendamento(ClinicaController controller, Paciente pacienteLogado) {
        setTitle("Agendar Consulta");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // fecha só a janela, não o app
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Agendamento para: " + pacienteLogado.getNome());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo);

        // 1. selecionar médico
        JPanel painelMedico = new JPanel(new FlowLayout());
        painelMedico.add(new JLabel("Escolha o Médico:"));
        
        // carrega médicos do controller para o combo box
        List<Medico> listaMedicos = controller.getListaMedicos();
        JComboBox<String> cbMedicos = new JComboBox<>();
        for (Medico m : listaMedicos) {
            // add no formato "ID - Nome (Especialidade)"
            cbMedicos.addItem(m.getId() + " - " + m.getNome() + " (" + m.getEspecialidade() + ")");
        }
        painelMedico.add(cbMedicos);
        add(painelMedico);

        // 2. data
        JPanel painelData = new JPanel(new FlowLayout());
        painelData.add(new JLabel("Data (DD/MM/AAAA):"));
        JTextField txtData = new JTextField(10);
        painelData.add(txtData);
        add(painelData);

        JButton btnAgendar = new JButton("Confirmar Agendamento");
        add(btnAgendar);
        
        JButton btnVoltar = new JButton("Cancelar");
        add(btnVoltar);

        // Enter funciona para agendar
        this.getRootPane().setDefaultButton(btnAgendar);

        btnAgendar.addActionListener(e -> {
            String itemSelecionado = (String) cbMedicos.getSelectedItem();
            if (itemSelecionado != null && !txtData.getText().isEmpty()) {
                // extrai o ID do médico da string "1 - Dr House..."
                int idMedico = Integer.parseInt(itemSelecionado.split(" - ")[0]);
                String data = txtData.getText();
                
                // chama o controller
                boolean sucesso = controller.agendarConsulta(idMedico, pacienteLogado.getId(), data);
                if (sucesso) {
                    dispose(); // fecha tela
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um médico e informe a data!");
            }
        });

        btnVoltar.addActionListener(e -> dispose());

        setVisible(true);
    }
}