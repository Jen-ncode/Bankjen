
package bankjen.app;

import java.util.Scanner;
import java.util.Vector;


public abstract class Menu {

    private Vector<String> luaChon = new Vector<String>(10, 5);

    public Menu() {

    }

    public Menu(String[] inLuaChon) {
        luaChon.clear();
        for (String lc : inLuaChon) {
            luaChon.add(lc);
        }
    }

    public void setMenu(String[] inLuaChon) {
        luaChon.clear();
        for (String lc : inLuaChon) {
            luaChon.add(lc);
        }
    }

    public void display() {
        System.out.println("\n\n\t\t\t" + luaChon.elementAt(1) + "\n");
        System.out.println("-------- " + luaChon.elementAt(0) + " --------");
        for (int i = 2; i < luaChon.size(); i++) {
            System.out.println(i - 1 + "." + luaChon.get(i));
        }
        System.out.println("---------- *** ----------");
    }

    public int getSelected() {
        display();
        Scanner sc = new Scanner(System.in);
        String lc;
        int numberChoose = 0;
        System.out.println("Moi chon muc: ");
        lc = sc.nextLine();
        try {
            numberChoose = Integer.parseInt(lc);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Phải nhập vào kiểu số nguyên !");
        }
        return numberChoose;
    }

    public abstract void execute(int n);

    public void run() {
        int lc = -1;
        do {
            lc = getSelected();
            if (lc > luaChon.size() - 2 || lc <= 0) {
                System.out.println("Lua chon khong dung !\n\t\tVietcombank hen gap lai quy khach !");
                System.exit(0);
            }
            execute(lc);
        } while (lc <= luaChon.size() - 2 && lc > 0);
    }

    public Vector<String> getLuaChon() {
        return luaChon;
    }

    public void setLuaChon(Vector<String> luaChon) {
        this.luaChon = luaChon;
    }
}
