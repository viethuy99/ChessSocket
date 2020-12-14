package mvc;

import view.ListView;
import model.User;

public class List1 {
    public static void main(String[] args) {
        ListView LView = new ListView(new User("Huy", "123"));
        LView.setVisible(true);
    }
}
