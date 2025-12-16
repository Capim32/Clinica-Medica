# Sistema de Clínica Médica

## Durante o processo de criação do sistema, esse README será usado como TODO-list e para conversação entre os devs!

## TODO
#### 1. Criar um sistema de cadastro de usuários para integrar com o sistema de login já existente (tá chato colocar os testes na mão no .txt) (done)
#### 2. Mexer na parte do médico para que seja possível realizar consultas (apagar o botão de realizar consulta) (done)
#### 3. Adicionar os limitadores de visibilidade de médicos por plano de saúde (feito em partes, tá estranho ainda...)
#### 4. Adicionar feedback do paciente pós consulta (review de texto + estrelas (atire logo)) (done)
#### 5. 

### MVC
Models (clínica, consultas, entidades, pacientes e médicos) View (telas do JFrame) e Controller (clinicaController)

#### View
TelaInicial -> TelaLogin, TelaCadastro -> TelaPrincipal -> TelaAgendamento

### Factory 
EntidadeFactory (abstrato para que pacientes e medicos herdem)

### Facade 
Main

### Interface gráfica
vou começar a tentar implementar a interface gráfica usando Swing

### Persistência de dados
o pacote data cuida da persistencia de dados em txt (é um banco de dados fake da silva, eu admito)

## Esquema de Organização
### Main -> ClinicaController -> Models, View e Data


