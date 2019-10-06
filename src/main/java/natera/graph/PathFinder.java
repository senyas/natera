package natera.graph;

import java.util.Collection;
import java.util.Set;

public interface PathFinder<V> {
    Collection<V> getPath(V from, V to);
}
