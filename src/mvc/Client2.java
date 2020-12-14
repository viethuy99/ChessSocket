package mvc;

import model.User;
import view.LoginView;
import view.ListView;

public class Client2 {

    public static void main(String[] args)
    {
        LoginView lview = new LoginView();

        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(lview.getIsClose());
            if (lview.getIsClose()) {
                break;
            }
        }
        ListView LView = new ListView(new User("Hien", "123"));
        LView.setVisible(true);
    }
}
