package models;


public class Consulta {
    private int id;
    private int idMedico;
    private int idPaciente;
    private String data; 
    private String status; // "AGENDADA", "REALIZADA", "CANCELADA", "LISTA_ESPERA"
    private String prontuario; // feito pelo médico
    private double valorPago; // 0 se tiver plano, valor X se não tiver 
    private int avaliacaoEstrelas; // 1 a 5
    private String avaliacaoTexto; 
}