package models;

public class Consulta {
    private int idMedico; 
    private int idPaciente;
    private String data; 
    private String status; // "AGENDADA", "REALIZADA", "CANCELADA"
    private double valorPago;
    private int avaliacaoEstrelas; // 0 se não avaliada
    private String avaliacaoTexto; // "null" se não avaliada
    private String prontuario; // esqueci do prontuario yipiiie 

    public Consulta(int idMedico, int idPaciente, String data, String status, double valor, int estrelas, String texto, String prontuario) {
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
        this.data = data;
        this.status = status;
        this.valorPago = valor;
        this.avaliacaoEstrelas = estrelas;
        this.avaliacaoTexto = texto;
        this.prontuario = prontuario;
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

    public String getProntuario() { return prontuario; }
    public void setProntuario(String prontuario) { this.prontuario = prontuario; }

    // método auxiliar para salvar no TXT
    @Override
    public String toString() {
        // Formato: ID_MED;ID_PAC;DATA;STATUS;VALOR;ESTRELAS;TEXTO;PRONTUARIO
        // Tratamento para evitar erro se for null
        String rev = (avaliacaoTexto == null) ? "null" : avaliacaoTexto;
        String pront = (prontuario == null) ? "null" : prontuario;
        
        return idMedico + ";" + idPaciente + ";" + data + ";" + status + ";" + valorPago + ";" + avaliacaoEstrelas + ";" + rev + ";" + pront;
    }

}