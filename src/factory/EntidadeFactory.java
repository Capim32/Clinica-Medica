package factory;

import exception.TipoException;
import models.*;

public class EntidadeFactory {
    public static int tipo;
    public static Entidade criarEntidade(int id) {
        switch(tipo) {
            case 1: return new Medico(id);
            case 2: return new Paciente(id);
            default: return null; // provisório até tratar excessoes
        }
    }
    
}
