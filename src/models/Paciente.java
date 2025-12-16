package models;

public class Paciente extends Entidade {

    private int idade;
    private String dataNascimento; 
    protected String planoDeSaude;
    
    // Construtor completo
    public Paciente(String nome, int idade, String dataNascimento) {
        super(nome);
        this.idade = idade;
        this.dataNascimento = dataNascimento;
        this.planoDeSaude = planoDeSaude;
    }

    public Paciente (int id) {
        super(id);
    }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public int getIdade() {return idade;}
    public void setIdade(int idade) {this.idade = idade;}
    
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getPlanoDeSaude() {return planoDeSaude;}
    public void setPlanoDeSaude(String planoDeSaude) {this.planoDeSaude = planoDeSaude;}
}