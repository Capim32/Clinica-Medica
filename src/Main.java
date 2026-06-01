import controller.ClinicaController;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {
    public static void main(String[] args) {
        // tenta aplicar o tema Nimbus
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // se falhar, usa o padrão
            System.out.println("Nimbus não disponível");
        }

        // Inicia o sistema
        new ClinicaController();
    }
}