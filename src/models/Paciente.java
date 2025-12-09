package models;

public class Paciente {
    protected String nome;
    protected int id;
    protected int planoDeSaude;
    
    public Paciente(String nome) {
        this.nome = nome;
        this.id = id;
        this.planoDeSaude = planoDeSaude;
    }
    // talvez seja interessante conseguir puxar o médico pelo ID
    public Paciente (int id) {
        this.nome = nome;
        this.id = id;
        this.planoDeSaude = planoDeSaude;
    }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getPlanoDeSaude() {return planoDeSaude;}
    public void setPlanoDeSaude(int planoDeSaude) {this.planoDeSaude = planoDeSaude;}

}