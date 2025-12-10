package models;

public class Medico extends Entidade {
    
    private String especialidade;
    private String planoDeSaude;
    private int atendimentos;

    public Medico(String nome) {
        super(nome);

        this.id = id;
        this.especialidade = especialidade;
        this.planoDeSaude = planoDeSaude;
        this.atendimentos = atendimentos;
    }
    // talvez seja interessante conseguir puxar o médico pelo ID
    public Medico (int id) {
        super(id);

        this.nome = nome;
        this.especialidade = especialidade;
        this.planoDeSaude = planoDeSaude;
        this.atendimentos = atendimentos;
    }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getEspecialidade() {return especialidade;}
    public void setEspecialidade(String especcialidade) {this.especialidade = especcialidade;}

    public String getPlanoDeSaude() {return planoDeSaude;}
    public void setPlanoDeSaude(String planoDeSaude) {this.planoDeSaude = planoDeSaude;}

    public int getAtendimentos() {return atendimentos;}
    public void setAtendimentos(int atendimentos) {this.atendimentos = atendimentos;}

}
