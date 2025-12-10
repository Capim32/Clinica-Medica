package models;

public class Paciente extends Entidade {

    private int idade;
    protected int gravidade; // 1 - leve, 2 - moderado, 3 - grave
    protected int estado; // aguardando atendimento, atendido, internado, liberado ... etc
    protected int planoDeSaude;
    
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

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getPlanoDeSaude() {return planoDeSaude;}
    public void setPlanoDeSaude(int planoDeSaude) {this.planoDeSaude = planoDeSaude;}

}