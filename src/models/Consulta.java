package models;

public class Consulta {
    private int idMedico; 
    private int idPaciente;
    private String data; 
    private String status; // "AGENDADA", "REALIZADA", "CANCELADA"
    private double valorPago;
    private int avaliacaoEstrelas; // 0 se não avaliada
    private String avaliacaoTexto; // "null" se não avaliada

    public Consulta(int idMedico, int idPaciente, String data, String status, double valor, int estrelas, String texto) {
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
        this.data = data;
        this.status = status;
        this.valorPago = valor;
        this.avaliacaoEstrelas = estrelas;
        this.avaliacaoTexto = texto;
    }

    public int getIdMedico() { return idMedico; }
    public int getIdPaciente() { return idPaciente; }
    public String getData() { return data; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorPago() { return valorPago; }
    public void setValorPago(double valorPago) {this.valorPago = valorPago;}
    
    public int getAvaliacaoEstrelas() { return avaliacaoEstrelas; }
    public void setAvaliacaoEstrelas(int avaliacaoEstrelas) { this.avaliacaoEstrelas = avaliacaoEstrelas; }

    public String getAvaliacaoTexto() { return avaliacaoTexto; }
    public void setAvaliacaoTexto(String avaliacaoTexto) { this.avaliacaoTexto = avaliacaoTexto; }

    // método auxiliar para salvar no TXT
    @Override
    public String toString() {
        // Formato: ID_MED;ID_PAC;DATA;STATUS;VALOR;ESTRELAS;TEXTO
        return idMedico + ";" + idPaciente + ";" + data + ";" + status + ";" + valorPago + ";" + avaliacaoEstrelas + ";" + avaliacaoTexto;
    }
}