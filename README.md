# Sistema de Clínica Médica

## Durante o processo de criação do sistema, esse README será usado como TODO-list e para conversação entre os devs!

### MVC
Models (clínica,pacientes e médicos) View (recepção) e Controller (clinicaController)

### Factory 
pacienteFactory e medicoFactory

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
