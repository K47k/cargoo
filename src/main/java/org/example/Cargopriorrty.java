package org.example;

import java.util.ArrayList;
import java.util.List;

// Node sınıfı
class Node {
    private String cityName;
    private int cityId;
    private List<Node> children;

    public Node(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.children = new ArrayList<>();
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node childNode) {
        children.add(childNode);
    }
}

// DeliveryTree sınıfı
class DeliveryTree {
    private Node root;

    public DeliveryTree(Node root) {
        this.root = root;
    }

    // Belirli bir şehri ID'ye göre bulma
    public Node findCity(int cityId) {
        return findCityRecursive(cityId, root);
    }

    private Node findCityRecursive(int cityId, Node currentNode) {
        if (currentNode == null) {
            return null;
        }

        if (currentNode.getCityId() == cityId) {
            return currentNode;
        }

        for (Node child : currentNode.getChildren()) {
            Node result = findCityRecursive(cityId, child);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    // Ağacı görüntüleme (düzey tabanlı olarak)
    public void displayTree() {
        displayTreeRecursive(root, 0);
    }

    private void displayTreeRecursive(Node node, int level) {
        if (node == null) {
            return;
        }

        System.out.println(" ".repeat(level * 4) + node.getCityName() + " (ID: " + node.getCityId() + ")");

        for (Node child : node.getChildren()) {
            displayTreeRecursive(child, level + 1);
        }
    }

    // Ağaca şehir ekleme
    public void addCity(int parentId, String cityName, int cityId) {
        Node parentNode = findCity(parentId);
        if (parentNode == null) {
            throw new IllegalArgumentException("Parent city with ID " + parentId + " not found.");
        }

        Node newCity = new Node(cityName, cityId);
        parentNode.addChild(newCity);
    }

    // Tüm yolları hesaplama
    public List<PathInfo> calculatePaths() {
        List<PathInfo> allPaths = new ArrayList<>();
        calculatePathsRecursive(root, 0, new ArrayList<>(), allPaths);
        return allPaths;
    }

    private void calculatePathsRecursive(Node node, int currentDepth, List<Node> currentPath, List<PathInfo> allPaths) {
        if (node == null) {
            return;
        }

        currentPath.add(node);

        if (node.getChildren().isEmpty()) { // Yaprak düğüm ise
            allPaths.add(new PathInfo(new ArrayList<>(currentPath), currentDepth));
        } else {
            for (Node child : node.getChildren()) {
                calculatePathsRecursive(child, currentDepth + 1, new ArrayList<>(currentPath), allPaths);
            }
        }
    }
}

// Path bilgilerini tutmak için yardımcı sınıf
class PathInfo {
    private List<Node> path;
    private int depth;

    public PathInfo(List<Node> path, int depth) {
        this.path = path;
        this.depth = depth;
    }

    public List<Node> getPath() {
        return path;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node : path) {
            sb.append(node.getCityName()).append(" (ID: ").append(node.getCityId()).append(") -> ");
        }
        sb.append("Depth: ").append(depth);
        return sb.toString();
    }
}