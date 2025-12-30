# Sistema de Clínica Médica

## Projeto final de POO - UECE 2025.2

### Sistema completo de clínica médica feito em Java 17 para fixar os conteúdos vistos na cadeira de Programação Orientada a Objeto durante o semestre de 2025.2 na Universidade Estadual do Ceará 

#### No projeto foram usados:

##### Interface gráfica:
- Java Swing e AWT

##### Padrões de Projetos:
-  MVC: Model, View e Controller para que haja uma clara separação entre os objetos criados e da interface gráfica para o controller orquestrar o projeto.

- Factory: Criação de entidades para que por meio de polimorfismo e herança os objetos como médicos e pacientes sejam criados.

- Facade: No projeto o controller funciona como Facade, permitindo que a Main seja limpa e não exponha o código do projeto.