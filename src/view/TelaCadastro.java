package view;

import javax.swing.*;
import java.awt.*;
import controller.ClinicaController;

public class TelaCadastro extends JFrame {
    private ClinicaController controller;

    public TelaCadastro(ClinicaController controller) {
        this.controller = controller;

        setTitle("Cadastro - Clínica Médica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 5, 5)); 
        setLocationRelativeTo(null); 
    }


    
}
