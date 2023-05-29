package bankjen.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Bank extends Menu {

    private static Vector<Account> aList = new Vector<>(20, 10);
    private static Vector<Customer> cList = new Vector<>(20, 10);
    static String[] mc = {"Menu 1", "Vietcombank banking counters welcome !", "Dang ky thanh vien", "Dang nhap thanh vien", "Nop tien vao tai khoan", "Xem danh sach thanh vien", "Ket thuc"};
    Customer curenrCustomer;
    Account currentAccount;

    public Bank() {
        super(mc);
        loadData("account.txt");
    }

    public Bank(String[] mang) {
        super(mc);
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                customerRegistration();
                saveData("account.txt");
                break;
            case 2:
                try {
                    curenrCustomer = customerLogIn();
                    System.out.println(">>> Dang nhap thanh cang, xin chao " + curenrCustomer.getTen() + " !");
                } catch (Exception e) {
                    System.out.println("Loi dang nhap: " + e.getMessage());
                }
                try {
                    curenrCustomer.setMenu();
                    curenrCustomer.run();
                } catch (Exception e) {
                    System.out.println("Loi menu: " + e.toString());
                }
                break;
            case 3:
                doDepositCash();
                saveData("account.txt");
                break;
            case 4:
                viewCustomerList();
                break;
            case 5:
//                saveData("account.txt");
                System.out.println("\t\tVietcombank hen gap lai quy khach !");
                System.exit(0);
        }
    }

    public void customerRegistration() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------- Dang ky thanh vien -------");

        String ten;
        int cmnd = 0;
        String ngaySinh = null;
        try {
            System.out.println("Nhap ten");
            ten = sc.nextLine();
            System.out.println("Nhap cmnd");
            cmnd = Integer.parseInt(sc.nextLine());
            System.out.println("Nhap ngay sinh");
            ngaySinh = sc.nextLine();
            Customer c = new Customer(ten, cmnd, ngaySinh);
            cList.add(c);

            //Random rand = new Random();
            //int randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
            Random rd = new Random();
            int pin = 100000 + rd.nextInt((900000 - 100000) + 1);//pin = [100000, 900000]
            double soDu = 100;
            Account a = new Account(c, soDu, pin);
            aList.add(a);
            System.out.println("\n>>> Dang ky tai khoan thanh cong, xin chao " + ten + " !");
            System.out.println("\t------- *** -------");
            System.out.println("Ma KH cua ban: " + c.getMaKH() + "\nMat khau mac dinh: " + c.getMatKhau());
            System.out.println("Ban duoc cap so tai khoan: " + a.getSoTK() + "\nPin mac dinh: " + a.getPin() + "\nSo du Mac dinh: " + a.getSoDu());
        } catch (Exception e) {
            System.out.println("Loi nhap sai du lieu: " + e.getMessage());
        }
    }

    public Customer customerLogIn() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------- Dang nhap thanh vien -------");
        System.out.println("Ma khach hang: ");
        String maKH = sc.nextLine();
        System.out.println("Mat khau: ");
        String matKhau = sc.nextLine();
        for (Customer c : cList) {
            if (maKH.equals(c.maKH) && matKhau.equals(c.matKhau)) {
                return c;
            }
        }
        throw new RuntimeException("Tai khoan va mat khau khong đung !");
    }

    public void doDepositCash() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------- Nop tien vao tai khoan -------");
        System.out.println("Nhap so tai khoan: ");
        int stk = sc.nextInt();
        Account a = getAccount(stk);
        double tienNop = 0;
        try {
            tienNop = depositCash(a);
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
            return;
        }
        double soDuCu = a.getSoDu();
        a.setSoDu(tienNop + soDuCu);
        System.out.println(">>> Nop tien vao tai khoan " + a.getSoTK() + " thanh cong !");
    }

    public double depositCash(Account a) {
        if (a == null) {
            throw new RuntimeException("So tai khoan khong ton tai !");
        }
        Scanner sc = new Scanner(System.in);
        double tienNop = 0;
        System.out.println("So tien gui vao tai khoan: ");
        try {
            tienNop = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Tien phai la kieu so !");
        }
        String mota = "Nop tien vao tai khoan";
        if (tienNop <= 0) {
            throw new RuntimeException("Tien vao tai khoan phai > 0");
        }
        a.transactionDiary.add(new Transaction(a, tienNop, "Nop tien", mota));
        return tienNop;
    }

    public void viewCustomerList() {
        System.out.println("\n------- Danh sach thanh vien -------");
        if (cList.size() == 0) {
            System.out.println("Danh sach rong !");
        } else {
            Collections.sort(cList);
            System.out.printf("%-7s| %-16s| %-15s", "Ma KH", "Ho ten", "Mat khau");
            for (int i = 0; i < cList.size(); i++) {
                if (i < cList.size() - 1) {
                    if ((cList.get(i).getMaKH()).equals(cList.get(i + 1).getMaKH())) {
                        continue;
                    }
                }
                System.out.print(cList.get(i));
            }
        }
        System.out.println("\n\t ------- *** -------");
    }

    public static void saveData(String path) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        File fileName = new File(path);
        try {
            fileName.createNewFile();
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            Collections.sort(aList);
            for (Account a : aList) {
                String dateOfBirth = dateFormat.format(a.getNgaySinh());
                bw.write(a.getMaKH() + "::" + a.getTen() + "::" + a.getMatKhau() + "::" + a.getCmnd() + "::" + dateOfBirth
                        + "::" + a.getSoTK() + "::" + a.getPin() + "::" + a.getSoDu() + "\n");
            }
            bw.close();
            fw.close();
        } catch (IOException ex) {
            System.out.println("Loi luu file !" + ex.toString());
        }
    }

    public void loadData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] array = line.trim().split("::");
                Customer c = createCustomer(array);
                cList.add(c);
                Account a = createAccount(c, array);
                aList.add(a);
            }
            br.close();
            fr.close();
        } catch (Exception ex) {
            System.out.println("Loi load file: " + ex.toString());
        }
    }

    public Customer createCustomer(String[] array) {
        String maKH = array[0];
        String ht = array[1];
        String matKhau = array[2];
        int cmnd = Integer.parseInt(array[3]);
        String ngaySinh = array[4];
        int count = Integer.parseInt(array[0].trim().substring(1, 5));
        Customer.setCount(++count);
        return new Customer(maKH, ht, matKhau, cmnd, ngaySinh);
    }

    public Account createAccount(Customer c, String[] array) {
        int soTK = Integer.parseInt(array[5]);
        int pin = Integer.parseInt(array[6]);
        double soDu = Double.parseDouble(array[7]);
        int count = soTK;
        //Đảm bảo CountSoTK cuối cùng khi load xong file là lớn nhất để không bị trùng, vì file sắp xếp theo tên Khách hàng
        if (count >= Account.getCountSoTK()) {
            Account.setCountSoTK(++count);
        }
        return new Account(c, soTK, pin, soDu);
    }

    public static Account getAccount(int soTK) {
        for (Account a : aList) {
            if (soTK == a.getSoTK()) {
                return a;
            }
        }
        return null;
    }

    public static void addAcountInaList(Account a) {
        aList.add(a);
    }

    public static Vector<Account> getaList() {
        return aList;
    }

    public static void setaList(Vector<Account> aList) {
        Bank.aList = aList;
    }

    public static Vector<Customer> getcList() {
        return cList;
    }

    public static void setcList(Vector<Customer> cList) {
        Bank.cList = cList;
    }

}

