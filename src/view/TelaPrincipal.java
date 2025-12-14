package view;

import controller.ClinicaController;
import java.awt.*;
import javax.swing.*;

public class TelaPrincipal extends JFrame {

    private ClinicaController controller;

    public TelaPrincipal(ClinicaController controller) {
        this.controller = controller;

        setTitle("Sistema Clínica Médica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null); // Centralizar na tela


        if (controller.getMedicoLogado() != null) {
            add(new JLabel("Olá, Dr(a). " + controller.getMedicoLogado().getNome()));
            /* botões:
                - realizar consulta
                - gerar 
             */
        } else {
            add(new JLabel("Olá, Paciente " + controller.getPacienteLogado().getNome()));
            /* botões:
                - verificar médicos (outra janela para o usuário inserir nome e especialidade do médico)
                - agendar consulta
                - cancelar agendamento
                - # APÓS A CONSULTA FINALIZADA o paciente deve poder avaliar o médico (opicional), com um texto e estrelas, isso deve entrar no BD para ser puxado por usuarios
                que desejam ver as avaliações do médico 
            */
        }

        JButton btnAgendar = new JButton("Agendar Consulta");
        JButton btnSair = new JButton("Sair");

        btnAgendar.addActionListener(e -> controller.abrirTelaAgendamento());

        
        add(new JLabel("Bem-vindo à Clínica"));
        add(btnAgendar);
        add(btnSair);
        
        setVisible(true);
    }

    private void abrirTelaAgendamento() {
        // lógica para abrir outra janela
        JOptionPane.showMessageDialog(this, "Abrindo agendamento...");
    }
}