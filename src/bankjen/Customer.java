package bankjen.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;


public class Customer extends Menu implements Comparable<Customer> {

    protected String maKH, ten, matKhau;
    protected int cmnd;
    protected Date ngaySinh;
    protected static int count = 1000;
    static String[] mc = {"Menu 2", "Vietcombank Internet Banking welcome !", "Dang ky them tai khoan ngan hang", "Xem danh sach tai khoan dang so huu", "Đang nhap vao tai khoan ngan hang", "Quay lai Menu 1", "Thoat"};

    public Customer() {
        super();
    }

    public Customer(String ht, int cmnd, String ngS) {
        if (ht == null || cmnd < 0 || ngS == null) {
            throw new RuntimeException("Lỗi dữ liệu");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);//validate of date
        try {
            ngaySinh = dateFormat.parse(ngS);
            ten = ht;
            maKH = "C" + count++;
            matKhau = Integer.toString(cmnd);
            this.cmnd = cmnd;
        } catch (ParseException e) {
            throw new RuntimeException("Ngay sinh khong hop le !");
        }
    }

    public Customer(Customer c) {
        if (c == null) {
            throw new RuntimeException("Loi du lieu !");
        }
        this.maKH = c.maKH;
        this.cmnd = c.cmnd;
        this.matKhau = c.matKhau;
        this.ten = c.ten;
        this.ngaySinh = c.ngaySinh;
    }

    public Customer(String maKH, String ht, String matKhau, int cmnd, String ngS) {
        if (maKH == null || ht == null || matKhau == null || cmnd < 0 || ngS == null) {
            throw new RuntimeException("Loi du lieu ");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);//validate of date
        try {
            ngaySinh = dateFormat.parse(ngS);
            this.maKH = maKH;
            this.ten = ht;
            this.matKhau = matKhau;
            this.cmnd = cmnd;
        } catch (ParseException e) {
            throw new RuntimeException("Ngay sinh khong hop le!");
        }
    }

    public void setMenu() {
        super.setMenu(mc);
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                doCreateAcountOfCustomer();
                break;
            case 2:
                showAccountOfCustomer(this);
                break;
            case 3:
                try {
                    Account a = loginAccount();
                    a.setMenu();
                    a.run();
                } catch (Exception e) {
                    System.out.println("Loi dang nhap tai khoan: " + e.getMessage());
                }
                break;
            case 4:
                Bank b = new Bank(Bank.mc);
                b.run();
                break;
            case 5:
                System.out.println("\t\tVietcombank Internet Banking hen gap lai quy khach !");
                System.exit(0);
        }
    }

    public void doCreateAcountOfCustomer() {
        double soDu = 0;
        int pin = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------- Them tai khoan ngan hang -------");
        System.out.println("Khai bao so du: ");
        try {
            soDu = Double.parseDouble(sc.nextLine());
            Random rd = new Random();
            pin = 100000 + rd.nextInt((900000 - 100000) + 1);
            Account a = CreateAcountOfCustomer(this, pin, soDu);
            System.out.println("\n>>> Them tai khoan thanh cong !");
            System.out.println("So tai khoan: " + a.getSoTK() + "\nPin mac dinh: " + a.getPin());
            System.out.println("\t------- *** -------");
        } catch (NumberFormatException e) {
            System.out.println("Loi: so du phai kieu so !");
        }
    }

    public Account CreateAcountOfCustomer(Customer c, int pin, double soDu) {
        Account a = new Account(c, soDu, pin);
        Bank.addAcountInaList(a);
        Bank.saveData("account.txt");
        return a;
    }

    public static Account loginAccount() {
        int soTK = 0;
        int pin = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------- Dang nhap vao so tai khoan ngan hang -------");

        try {
            System.out.println("So tai khoan: ");
            soTK = Integer.parseInt(sc.nextLine());
            System.out.println("So Pin: ");
            pin = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Loi: nhap sai kieu so !");
        }

        for (Account a : Bank.getaList()) {
            if (a.getSoTK() == soTK && a.getPin() == pin) {
                System.out.println(">>> Dang nhap thanh cong !");
                return a;
            }
        }
        throw new RuntimeException("Tai khoan va mat khau khong dung");
    }

    public void showAccountOfCustomer(Customer c) {
        boolean check = false;
        System.out.println("\n------- Cac so tai khoan cua \"" + c.ten + "\" -------");
        System.out.printf("%-7s|  %-16s| %-13s| %-10s", "Ma KH", "Ho ten", "So tai khoan", "Pin");
        for (Account a : Bank.getaList()) {
            if (a.maKH.equals(c.maKH)) {
                System.out.printf("\n%-7s|  %-16s| %-13d| %-10d", a.maKH, a.ten, a.getSoTK(),a.getPin());
                check = true;
            }
        }
        if (check == false) {
            System.out.println("Danh sach rong !");
        }
        System.out.println("\n\t --------- *** ---------");
    }

    @Override
    public String toString() {
        return String.format("\n%-7s| %-16s| %-15s", getMaKH(), getTen(), getMatKhau());
    }

    @Override
    public int compareTo(Customer o) {
        return this.ten.compareTo(o.ten);
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getCmnd() {
        return cmnd;
    }

    public void setCmnd(int cmnd) {
        this.cmnd = cmnd;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Customer.count = count;
    }

}