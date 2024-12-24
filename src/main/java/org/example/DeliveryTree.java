package org.example;

import java.io.*;
import java.util.*;
import com.google.gson.*;


// Cargo sınıfı
class Cargo implements Comparable<Cargoo> {
    private String cargoId;
    private int deliveryTime;
    private String status;

    public Cargo(String cargoId, int deliveryTime, String status) {
        this.cargoId = cargoId;
        this.deliveryTime = deliveryTime;
        this.status = status;
    }

    public String getCargoId() {
        return cargoId;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(Cargoo other) {
        return Integer.compare(this.deliveryTime, other.getDeliveryTime());
    }

    @Override
    public String toString() {
        return "KargoID: " + cargoId + ", Teslimat Süresi: " + deliveryTime + " gün, Durum: " + status;
    }
}

// PriorityQueue sınıfı
class PriorityQueueManager {
    private PriorityQueue<Cargoo> queue;

    public PriorityQueueManager() {
        this.queue = new PriorityQueue<>();
    }

    // Kargo ekleme
    public void addCargo(Cargoo cargoo) {
        queue.add(cargoo);
        System.out.println("Eklendi: " + cargoo);
    }

    // Sıradaki kargoyu işleme
    public void processNextCargo() {
        if (queue.isEmpty()) {
            System.out.println("İşlenecek kargo yok.");
            return;
        }
        Cargoo nextCargoo = queue.poll();
        nextCargoo.setStatus("Teslim edildi");
        System.out.println("İşleniyor: " + nextCargoo);
    }

    // Kargo durumunu güncelleme
    public void updateCargoStatus(String cargoId, String newStatus) {
        for (Cargoo cargoo : queue) {
            if (cargoo.getCargoId().equals(cargoId)) {
                cargoo.setStatus(newStatus);
                System.out.println("Güncellendi: " + cargoo);
                return;
            }
        }
        System.out.println("KargoID " + cargoId + " kuyrukta bulunamadı.");
    }

    // Kargo arama
    public void searchCargo(String cargoId) {
        for (Cargoo cargoo : queue) {
            if (cargoo.getCargoId().equals(cargoId)) {
                System.out.println("Bulundu: " + cargoo);
                return;
            }
        }
        System.out.println("KargoID " + cargoId + " bulunamadı.");
    }

    // Kuyruk istatistiklerini raporlama
    public void reportStatistics() {
        int totalCargos = queue.size();
        if (totalCargos == 0) {
            System.out.println("Kuyrukta kargo bulunmamaktadır.");
            return;
        }

        double averageDeliveryTime = queue.stream().mapToInt(Cargoo::getDeliveryTime).average().orElse(0);
        System.out.println("Toplam Kargo: " + totalCargos);
        System.out.printf("Ortalama teslimat süresi: %.2f gün%n", averageDeliveryTime);
    }

    // Kuyruğu JSON formatında dosyaya kaydetme
    public void saveToFile(String filename) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filename)) {
            List<Cargoo> cargooList = new ArrayList<>(queue);
            gson.toJson(cargooList, writer);
            System.out.println("Kuyruk " + filename + " dosyasına kaydedildi.");
        } catch (IOException e) {
            System.out.println("Dosyaya kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    // Dosyadan kuyruğu yükleme
    public void loadFromFile(String filename) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            Cargoo[] cargoos = gson.fromJson(reader, Cargoo[].class);
            queue.clear();
            queue.addAll(Arrays.asList(cargoos));
            System.out.println("Kuyruk " + filename + " dosyasından yüklendi.");
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadı: " + filename);
        } catch (IOException e) {
            System.out.println("Dosyadan yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    // Kuyruğu ekrana yazdırma
    public void printQueue() {
        if (queue.isEmpty()) {
            System.out.println("Kuyruk boş.");
        } else {
            System.out.println("Mevcut Kuyruk:");
            for (Cargoo cargoo : queue) {
                System.out.println(cargoo);
            }
        }
    }
}

// Ana sınıf (test)