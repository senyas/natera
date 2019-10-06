package natera.graph;

import java.util.*;

public class SimplePath<V> implements PathFinder<V> {
    private final Graph<V> graph;

    public SimplePath(Graph<V> graph) {
        this.graph = graph;
    }

    @Override
    public Collection<V> getPath(V from, V to) {
        if (from.equals(to)) return new ArrayList<>();
        Map<V, List<V>> level = new HashMap<>();
        level.put(from, new ArrayList<>());
        Set<V> visited = new HashSet<>();
        visited.add(from);
        while(!level.isEmpty()) {
            Map<V, List<V>> next = new HashMap<>();
            for (Map.Entry<V, List<V>> entry : level.entrySet()) {
                Set<V> outs = graph.getOutgoingEdges(entry.getKey()).keySet();
                if (!outs.isEmpty()) {
                    List<V> path = new ArrayList<>(entry.getValue());
                    path.add(entry.getKey());
                    if (outs.contains(to)) {
                        path.add(to);
                        return path;
                    }
                    for (V out : outs) {
                        if (!visited.contains(out)) {
                            next.put(out, path);
                            visited.add(out);
                        }
                    }
                }
            }
            level = next;
        }
        return null;
    }

}
