package bankjen.app;

import java.util.Scanner;
import java.util.Vector;

public class Account extends Customer {

    private int soTK, pin;
    private Double soDu = 100D;
    private static int countSoTK = 10000;
    static Vector<Transaction> transactionDiary = new Vector<Transaction>(10, 5);
    String menu[] = {"Menu 3", "Giao dich tai ATM Vietcombank", "Rut tien", "Chuyen tien", "Doi pin", "Xem so du", "Xem nhat ky giao dich", "Thoat"};

    public Account() {
        super();
    }

    public Account(String ht, int cmnd, String ngS, double sd, int pin) {
        super(ht, cmnd, ngS);
        this.pin = pin;
        this.soDu = sd;
        this.soTK = countSoTK++;
    }

    public Account(Customer c, double sd, int pin) {
        super(c);
        this.soDu = sd;
        this.pin = pin;
        this.soTK = countSoTK++;
    }

    public Account(Customer c, int soTK, int pin, double sd) {
        super(c);
        this.soTK = soTK;
        this.soDu = sd;
        this.pin = pin;
    }

    public Account accountLogIn(int stk, int pin) {
        if (this.soTK == stk && this.pin == pin) {
            return this;
        }
        return null;
    }

    @Override
    public void setMenu() {
        super.setMenu(menu);
    }

    public void doWithdraw() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\t\t------- Rut tien -------");
        System.out.println("So tien can rut: ");
        double st = 0D;
        try {
            st = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Loi : Nhap sai kieu so !");
            return;//call menu
        }
        String mt = "Rut tien mat tai ATM";
        try {
            withdraw(st, mt);
            System.out.println(">>> Rut tien thanh cong !");
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    public double withdraw(double sotien, String mota) {
        if (sotien > this.soDu) {
            throw new RuntimeException("So du tai khoan khong du de rut");
        }
        transactionDiary.add(new Transaction(this, sotien, "Rut tien", mota));
        return this.soDu -= sotien;
    }

    public void doTransferMoney() {
        int stk = 0;
        double st = 0D;
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\t\t------- Chuyen tien -------");
        try {
            System.out.println("So tai khoan nhan tien: ");
            stk = Integer.parseInt(sc.nextLine());
            System.out.println("So tien can chuyen: ");
            st = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Loi : Nhap sai kieu so !");
            return;//call menu
        }
        System.out.println("Noi dung chuyen tien: ");
        String mt = sc.nextLine();
        Account a = Bank.getAccount(stk);
        try {
            transferMoney(a, st, this.getSoTK()+ " chuyen tien cho " + a.getSoTK()+ ", noi dung: " + mt);
            System.out.println(">>> Chuyen khoan thanh cong !");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void transferMoney(Account a, double sotien, String mota) {
        if (a == null) {
            throw new RuntimeException("Khong tim thay tai khoan nhan tien !");
        }
        if (a == this) {
            throw new RuntimeException("Tai khoan nhan tien phai khac tai khoan chuyen tien !");
        }
        if (sotien > soDu) {
            throw new RuntimeException("So du khong du de chuyen tien !");
        }

        transactionDiary.add(new Transaction(this, sotien, "Chuyen Khoan", mota));
        transactionDiary.add(new Transaction(a, sotien, "Nhận Chuyen Khoan", mota));
        this.soDu -= sotien;
        a.soDu += sotien;
    }

    public void doChangePin() {
        int pin = 0;
        int pin2 = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\t\t------- Đoi Pin -------");
        try {
            System.out.println("Nhap pin moi: ");
            pin = Integer.parseInt(sc.nextLine());
            System.out.println("Nhap lai pin moi: ");
            pin2 = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Loi : Nhap sai kieu so !");
            return;//call menu
        }
        String mota = "Doi pin tai ATM";
        if (pin < 100000) {
            System.out.println("Pin phai co 6 chu so !");
            return;
        }
        if (pin != pin2) {
            System.out.println("Pin nhap lai khong chinh xac !");
            return;
        }
        try {
            changePin(pin, mota);
            System.out.println(">>> Doi pin thanh cong !");
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
            return;
        }

    }

    public void changePin(int newpin, String mota) {
        if (newpin == this.pin) {
            throw new RuntimeException("Pin moi phai khac pin cu !");
        }
        transactionDiary.add(new Transaction(this, 0, "Doi Pin", mota));
        this.pin = newpin;
    }

    public void checkBalance() {
        System.out.println("\n\n\t\t------- Kiem tra so du -------");
         System.out.printf("%-10s| %-10s","Tai khoan","So du");
        System.out.printf("\n%-10d| %-10f", this.getSoTK(), this.getSoDu());
        transactionDiary.add(new Transaction(this, 0, "KTSD", "Kiem tra so du tai ATM"));
    }

    public void viewTransactionDiary() {
        System.out.println("\n------- Nhat ky GD cua tai khoan \"" + this.soTK + "\" -------");
        System.out.printf("\n%-5s| %-28s| %-8s| %-15s| %-15s| %-25s","ID", "Thoi gian", "So TK", "So tien", "Loai GD", "Mo ta");
        for (Transaction t : transactionDiary) {
            if (t.getAcc().getSoTK() == this.getSoTK()) {
                System.out.print(t.toString());
            }
        }
        if(transactionDiary.size() == 0 ){
            System.out.println("\nBạn chua co giao dich nao tren he thong ATM !");
        }
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                doWithdraw();
                break;
            case 2:
                doTransferMoney();
                break;
            case 3:
                doChangePin();
                break;
            case 4:
                checkBalance();
                break;
            case 5:
                viewTransactionDiary();
                break;
            case 6:
                System.out.println("\t\tVietcombank ATM hẹn gặp lại quý khách !");
                System.exit(0);
        }
    }

    public int getSoTK() {
        return soTK;
    }

    public void setSoTK(int soTK) {
        this.soTK = soTK;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public Double getSoDu() {
        return soDu;
    }

    public void setSoDu(Double soDu) {
        this.soDu = soDu;
    }

    public static int getCountSoTK() {
        return countSoTK;
    }

    public static void setCountSoTK(int countSoTK) {
        Account.countSoTK = countSoTK;
    }

    public static Vector<Transaction> getTransactionDiary() {
        return transactionDiary;
    }

    public static void setTransactionDiary(Vector<Transaction> transactionDiary) {
        Account.transactionDiary = transactionDiary;
    }

    @Override
    public String toString() {
        return "Account{" + "soTK=" + soTK + ", pin=" + pin + ", soDu=" + soDu + '}';
    }

}