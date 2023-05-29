/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atm.app;

import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author An Nguyen
 */
public class Account extends Customer {

    private int soTK, pin;
    private Double soDu = 100D;
    private static int countSoTK = 10000;
    static Vector<Transaction> transactionDiary = new Vector<Transaction>(10, 5);
    String menu[] = {"Menu 3", "Giao dịch tại ATM Vietcombank", "Rút tiền", "Chuyển tiền", "Đổi pin", "Xem số dư", "Xem nhật ký giao dịch", "Thoát"};

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
        System.out.println("\n\t\t------- Rút tiền -------");
        System.out.println("Số tiền cần rút: ");
        double st = 0D;
        try {
            st = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: nhập sai kiểu số !");
            return;//call menu
        }
        String mt = "Rút tiền mặt tại ATM";
        try {
            withdraw(st, mt);
            System.out.println(">>> Rút tiền thành công !");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public double withdraw(double sotien, String mota) {
        if (sotien > this.soDu) {
            throw new RuntimeException("Số dư không đủ để rút tiền");
        }
        transactionDiary.add(new Transaction(this, sotien, "Rút Tiền", mota));
        return this.soDu -= sotien;
    }

    public void doTransferMoney() {
        int stk = 0;
        double st = 0D;
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\t\t------- Chuyển tiền -------");
        try {
            System.out.println("Số tài khoản nhận tiền: ");
            stk = Integer.parseInt(sc.nextLine());
            System.out.println("Số tiền cần chuyển: ");
            st = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: nhập sai kiểu số !");
            return;//call menu
        }
        System.out.println("Nội dung chuyển tiền: ");
        String mt = sc.nextLine();
        Account a = Bank.getAccount(stk);
        try {
            transferMoney(a, st, this.getSoTK()+ " chuyển tiền cho " + a.getSoTK()+ ", nội dung: " + mt);
            System.out.println(">>> Chuyển khoản thành công !");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void transferMoney(Account a, double sotien, String mota) {
        if (a == null) {
            throw new RuntimeException("Không tìm thấy tài khoản nhận tiền !");
        }
        if (a == this) {
            throw new RuntimeException("Tài khoản nhận tiền phải khác tài khoản chuyển tiền !");
        }
        if (sotien > soDu) {
            throw new RuntimeException("Số dư không đủ để chuyển tiền !");
        }

        transactionDiary.add(new Transaction(this, sotien, "Chuyển Khoản", mota));
        transactionDiary.add(new Transaction(a, sotien, "Nhận Chuyển Khoản", mota));
        this.soDu -= sotien;
        a.soDu += sotien;
    }

    public void doChangePin() {
        int pin = 0;
        int pin2 = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\t\t------- Đổi Pin -------");
        try {
            System.out.println("Nhập pin mới: ");
            pin = Integer.parseInt(sc.nextLine());
            System.out.println("Nhập lại pin mới: ");
            pin2 = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Nhập sai kiểu số !");
            return;//call menu
        }
        String mota = "Đổi pin tại ATM";
        if (pin < 100000) {
            System.out.println("Pin phải có 6 chữ số !");
            return;
        }
        if (pin != pin2) {
            System.out.println("Pin nhập lại không chính xác !");
            return;
        }
        try {
            changePin(pin, mota);
            System.out.println(">>> Đổi pin thành công !");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return;
        }

    }

    public void changePin(int newpin, String mota) {
        if (newpin == this.pin) {
            throw new RuntimeException("Pin mới phải khác Pin cũ !");
        }
        transactionDiary.add(new Transaction(this, 0, "Đổi Pin", mota));
        this.pin = newpin;
    }

    public void checkBalance() {
        System.out.println("\n\n\t\t------- Kiểm tra số dư -------");
         System.out.printf("%-10s| %-10s","Tài khoản","Số dư");
        System.out.printf("\n%-10d| %-10f", this.getSoTK(), this.getSoDu());
        transactionDiary.add(new Transaction(this, 0, "KTSD", "Kiểm tra số dư tại ATM"));
    }

    public void viewTransactionDiary() {
        System.out.println("\n------- Nhật ký GD của tài khoản \"" + this.soTK + "\" -------");
        System.out.printf("\n%-5s| %-28s| %-8s| %-15s| %-15s| %-25s","ID", "Thời gian", "Số TK", "Số tiền", "Loại GD", "Mô tả");
        for (Transaction t : transactionDiary) {
            if (t.getAcc().getSoTK() == this.getSoTK()) {
                System.out.print(t.toString());
            }
        }
        if(transactionDiary.size() == 0 ){
            System.out.println("\nBạn chưa có giao dịch nào trên hệ thống ATM !");
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