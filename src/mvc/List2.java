package mvc;

import view.ListView;
import model.User;

public class List2 {
    public static void main(String[] args) {
        ListView LView = new ListView(new User("Hien", "123"));
        LView.setVisible(true);
    }
}
