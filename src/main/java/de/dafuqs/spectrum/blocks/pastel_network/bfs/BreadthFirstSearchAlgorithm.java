package de.dafuqs.spectrum.blocks.pastel_network.bfs;

import java.util.*;

// checks id a node is connected to node b
public class BreadthFirstSearchAlgorithm {

    public static class Node<T> {

        private final T value;
        private final Set<Node<T>> neighbors;

        public Node(T value) {
            this.value = value;
            this.neighbors = new HashSet<>();
        }

        public T getValue() {
            return value;
        }

        public Set<Node<T>> getNeighbors() {
            return Collections.unmodifiableSet(neighbors);
        }

        public void connect(Node<T> node) {
            if (this == node) throw new IllegalArgumentException("Can't connect node to itself");
            this.neighbors.add(node);
            node.neighbors.add(this);
        }
    }

    public static <T> Optional<Node<T>> isConnected(T value, Node<T> start) {
        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(start);

        Node<T> currentNode;
        Set<Node<T>> alreadyVisited = new HashSet<>();

        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            if (currentNode.getValue().equals(value)) {
                return Optional.of(currentNode);
            } else {
                alreadyVisited.add(currentNode);
                queue.addAll(currentNode.getNeighbors());
                queue.removeAll(alreadyVisited);
            }
        }

        return Optional.empty();
    }

}