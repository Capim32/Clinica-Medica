# Sistema de Clínica Médica

## Durante o processo de criação do sistema, esse README será usado como TODO-list e para conversação entre os devs!

## TODO
#### 1. Criar um sistema de cadastro de usuários para integrar com o sistema de login já existente (tá chato colocar os testes na mão no .txt)

### MVC
Models (clínica,pacientes e médicos) View (telas do JFrame) e Controller (clinicaController)

#### View
TelaLogin -> TelaPrincipal

### Factory 
EntidadeFactory (abstrato para que pacientes e medicos herdem)
### Facade 
Main

### Interface gráfica
vou começar a tentar implementar a interface gráfica usando Swing

### Persistência de dados
no pacote data terá o gerenciador de arquivos, provavelmente para .txt (vou ver se funciona com a interface gráfica)

### Exception
as excessões serão tratadas no pacote exception para maior clareza

## Esquema de Organização
### Main -> ClinicaController -> (Clinica(instancia medicos e pacientes) e Recepção)
