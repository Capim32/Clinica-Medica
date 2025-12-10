package models;

public class Paciente extends Entidade {

    private int idade;
    protected int gravidade; // 1 - leve, 2 - moderado, 3 - grave
    protected int estado; // aguardando atendimento, atendido, internado, liberado ... etc
    protected String planoDeSaude;
    
    public Paciente(String nome, int idade) {
        super(nome);
        this.idade = idade;
        this.planoDeSaude = planoDeSaude;
    }
    // talvez seja interessante conseguir puxar o médico pelo ID
    public Paciente (int id) {
        super(id);
        this.nome = nome;
        this.idade = idade;
        this.planoDeSaude = planoDeSaude;
    }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public int getIdade() {return idade;}
    public void setIdade(int idade) {this.idade = idade;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getPlanoDeSaude() {return planoDeSaude;}
    public void setPlanoDeSaude(String planoDeSaude) {this.planoDeSaude = planoDeSaude;}

}