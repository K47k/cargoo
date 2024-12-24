package org.example;

import java.util.*;
import java.time.LocalDate;

// Cargo sınıfı
class Cargoo {
    private String cargoId;
    private LocalDate sendDate;
    private String deliveryStatus;
    private int deliveryTime;

    public Cargoo(String cargoId, LocalDate sendDate, String deliveryStatus, int deliveryTime) {
        this.cargoId = cargoId;
        this.sendDate = sendDate;
        this.deliveryStatus = deliveryStatus;
        this.deliveryTime = deliveryTime;
    }

    public String getCargoId() {
        return cargoId;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    @Override
    //kargo bilgisini döndürür
    public String toString() {
        return "[ID: " + cargoId + ", Tarih: " + sendDate + ", Durum: " + deliveryStatus + ", Süre: " + deliveryTime + " gün]";
    }

    public String setStatus(String teslimEdildi) {
        return teslimEdildi;
    }
}

// Customer sınıfı
class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private List<Cargoo> cargooHistory = new ArrayList<>();
    private Deque<Cargoo> cargooStack = new ArrayDeque<>(); // Son 5 gönderim için yığın (Stack)
    //
    public Customer(String customerId, String firstName, String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    //
    public void addCargo(Cargoo cargoo) {
        cargooHistory.add(cargoo);
        cargooHistory.sort(Comparator.comparing(Cargoo::getSendDate)); // Tarihe göre sıralama

        cargooStack.addLast(cargoo);
        if (cargooStack.size() > 5) {
            cargooStack.removeFirst();
        }
    }
    //display last 5 methodu
    public void displayLast5Cargos() {
        if (cargooStack.isEmpty()) {
            System.out.println("Bu müşteri için gönderim geçmişi bulunmamaktadır.");
        } else {
            System.out.println(firstName + " " + lastName + " için son 5 gönderim:");
            cargooStack.descendingIterator().forEachRemaining(System.out::println);
        }
    }

    public Cargoo searchDeliveredCargo(String cargoId) {
        List<Cargoo> deliveredCargoos = new ArrayList<>();
        for (Cargoo cargoo : cargooHistory) {
            if ("Teslim Edildi".equals(cargoo.getDeliveryStatus())) {
                deliveredCargoos.add(cargoo);
            }
        }

        deliveredCargoos.sort(Comparator.comparing(Cargoo::getCargoId)); // ID'ye göre sıralama

        // Binary Search
        int left = 0, right = deliveredCargoos.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            Cargoo midCargoo = deliveredCargoos.get(mid);
            if (midCargoo.getCargoId().equals(cargoId)) {
                return midCargoo;
            } else if (midCargoo.getCargoId().compareTo(cargoId) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }

    public void displayCargoHistory() {
        if (cargooHistory.isEmpty()) {
            System.out.println("Gönderim geçmişi bulunmamaktadır.");
        } else {
            cargooHistory.forEach(System.out::println);
        }
    }

    public List<Cargoo> sortUndeliveredCargos() {
        List<Cargoo> undeliveredCargoos = new ArrayList<>();
        for (Cargoo cargoo : cargooHistory) {
            if ("Teslim Edilmedi".equals(cargoo.getDeliveryStatus())) {
                undeliveredCargoos.add(cargoo);
            }
        }

        // Teslim süresine göre QuickSort
        undeliveredCargoos.sort(Comparator.comparingInt(Cargoo::getDeliveryTime));
        return undeliveredCargoos;
    }

    @Override
    public String toString() {
        return "[ID: " + customerId + ", Ad: " + firstName + ", Soyad: " + lastName + "]";
    }
}

// CustomerManagement sınıfı
class CustomerManagement {
    private Map<String, Customer> customers = new HashMap<>();

    public void addCustomer(String customerId, String firstName, String lastName) {
        if (customers.containsKey(customerId)) {
            System.out.println("Bu ID'ye sahip bir müşteri zaten mevcut.");
        } else {
            customers.put(customerId, new Customer(customerId, firstName, lastName));
            System.out.println("Müşteri eklendi: " + firstName + " " + lastName);
        }
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public void displayAllCustomers() {
        customers.values().forEach(System.out::println);
    }
}

// Ana Menü
public class CustomerManagementt {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerManagement cm = new CustomerManagement();

        while (true) {
            System.out.println("\n1. Yeni müşteri ekle");
            System.out.println("2. Müşteri bilgisi al ve kargo gönderimi ekle");
            System.out.println("3. Müşteri gönderim geçmişini görüntüle");
            System.out.println("4. Müşteri son 5 gönderimi görüntüle");
            System.out.println("5. Teslim edilmiş kargoyu ID ile ara");
            System.out.println("6. Teslim edilmemiş kargoları sırala");
            System.out.println("7. Tüm müşterileri görüntüle");
            System.out.println("8. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Buffer temizleme

            if (choice == 1) {
                // Yeni müşteri ekle
                System.out.print("Müşteri ID: ");
                String id = scanner.nextLine();
                System.out.print("İsim: ");
                String firstName = scanner.nextLine();
                System.out.print("Soyisim: ");
                String lastName = scanner.nextLine();
                cm.addCustomer(id, firstName, lastName);

            }
            else if (choice == 2) {
                System.out.print("Müşteri ID: ");
                String id = scanner.nextLine();
                Customer customer = cm.getCustomer(id);
                if (customer != null) {
                    System.out.print("Kargo ID: ");
                    String cargoId = scanner.nextLine();
                    System.out.print("Gönderi Tarihi (YYYY-MM-DD): ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    System.out.print("Teslim Durumu: ");
                    String status = scanner.nextLine();
                    System.out.print("Teslim Süresi (gün): ");
                    int time = scanner.nextInt();
                    customer.addCargo(new Cargoo(cargoId, date, status, time));
                } else {
                    System.out.println("Müşteri bulunamadı.");
                }
            }
            else if (choice == 3) {
                // Müşteri gönderim geçmişini görüntüle
                System.out.print("Müşteri ID: ");
                String id = scanner.nextLine();
                Customer customer = cm.getCustomer(id);
                if (customer != null) {
                    customer.displayCargoHistory();
                } else {
                    System.out.println("Müşteri bulunamadı.");
                }

            } else if (choice == 4) {
                // Müşteri son 5 gönderimi görüntüle
                System.out.print("Müşteri ID: ");
                String id = scanner.nextLine();
                Customer customer = cm.getCustomer(id);
                if (customer != null) {
                    customer.displayLast5Cargos();
                } else {
                    System.out.println("Müşteri bulunamadı.");
                }

            } else if (choice == 5) {
                // Teslim edilmiş kargoyu ID ile ara
                System.out.print("Müşteri ID: ");
                String id = scanner.nextLine();
                Customer customer = cm.getCustomer(id);
                if (customer != null) {
                    System.out.print("Aramak istediğiniz kargo ID: ");
                    String cargoId = scanner.nextLine();
                    Cargoo cargo = customer.searchDeliveredCargo(cargoId);
                    if (cargo != null) {
                        System.out.println("Teslim edilen kargo bulundu: " + cargo);
                    } else {
                        System.out.println("Bu ID ile teslim edilmiş kargo bulunamadı.");
                    }
                } else {
                    System.out.println("Müşteri bulunamadı.");
                }

            } else if (choice == 6) {
                // Teslim edilmemiş kargoları sırala
                System.out.print("Müşteri ID: ");
                String id = scanner.nextLine();
                Customer customer = cm.getCustomer(id);
                if (customer != null) {
                    List<Cargoo> undeliveredCargos = customer.sortUndeliveredCargos();
                    if (undeliveredCargos.isEmpty()) {
                        System.out.println("Teslim edilmemiş kargo bulunmamaktadır.");
                    } else {
                        System.out.println("Teslim edilmemiş kargolar (teslim süresine göre sıralı):");
                        undeliveredCargos.forEach(System.out::println);
                    }
                } else {
                    System.out.println("Müşteri bulunamadı.");
                }

            } else if (choice == 7) {
                // Tüm müşterileri görüntüle
                System.out.println("Tüm müşteriler:");
                cm.displayAllCustomers();

            } else if (choice == 8) {
                // Çıkış
                System.out.println("Programdan çıkılıyor...");
                break;

            } else {
                // Geçersiz seçim
                System.out.println("Geçersiz seçim! Lütfen tekrar deneyin.");
            }
        }
        scanner.close();
    }
}