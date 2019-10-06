package natera.graph;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class Graph<V> {
    public static final double DEFAULT_WEIGHT = 1.0;
    private final ReadWriteLock lock;
    private final Map<V, Edges<V>> vertices;
    private final boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
        lock = new ReentrantReadWriteLock();
        vertices = new HashMap<>();
    }

    /**
     * addVertex - adds vertex to the graph
     */
    public void addVertex(V v) {
        lock.writeLock().lock();
        try {
            vertices.putIfAbsent(v, new Edges<>(directed));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * addEdge - adds edge to the graph
     */
    public void addEdge(V from, V to) {
        addEdge(from, to, DEFAULT_WEIGHT);
    }

    /**
     * Add weighted edges support in your lib
     */
    public void addEdge(V from, V to, double w) {
        if (to.equals(from)) throw new RuntimeException("Wrong edge!");
        lock.writeLock().lock();
        try {
            vertices.get(from).addEdgeTo(to, w);
            vertices.get(to).addEdgeFrom(from, w);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * getPath - returns a list of edges between 2 vertices (path doesn’t have to be optimal)
     */
    public Collection<V> getPath(V from, V to) {
        lock.readLock().lock();
        try {
            PathFinder<V> finder = new SimplePath<>(this);
            return finder.getPath(from, to);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Add traverse function that will take a user defined function and apply it on every vertex of the graph.
     */
    public void traverse(Consumer<? super V> action) {
        for (V v : vertices.keySet()) {
            action.accept(v);
        }
    }

    //-----------------------------------------------------------------------------------------------------

    Map<V, Double> getOutgoingEdges(V v) {
        return vertices.get(v).getOuts();
    }

    private static class Edges<V> {
        final Map<V, Double> edges;
        final Map<V, Double> ins;
        private final boolean directed;

        Edges(boolean directed) {
            this.directed = directed;
            edges = new HashMap<>();
            ins = directed ? new HashMap<>() : null;
        }

        void addEdgeTo(V to, double w) {
            if( edges.putIfAbsent(to, w) != null) throw new RuntimeException("Already exists!");
        }

        void addEdgeFrom(V from, double w) {
            if (directed) {
                if( ins.putIfAbsent(from, w) != null) throw new RuntimeException("Already exists!");
            } else {
                if( edges.putIfAbsent(from, w) != null) throw new RuntimeException("Already exists!");
            }
        }

        public Map<V, Double> getOuts() {
            return edges;
        }
    }
}