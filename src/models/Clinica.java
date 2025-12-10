package models;

import java.util.ArrayList;
import java.util.List;

public class Clinica {
    private final String nome;
    private final List<Medico> medicos;
    private final List<Paciente> pacientes;

    public Clinica(String nome) {
        this.nome = nome;
        this.medicos = new ArrayList<>();
        this.pacientes = new ArrayList<>();

    }
}
