package natera.graph;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.*;
import org.junit.rules.TestRule;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GraphTest {
    Graph<String> graph;

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    @Test
    public void test1()
    {
        Graph<Integer> graph = new Graph<>(true);
        for(int i = 0; i <= 10000; i++) {
            graph.addVertex(i);
        }

        for(int i = 0; i < 9999; i++) {
            graph.addEdge(i, i + 1 );
        }
        for(int i = 0; i < 9998; i+=2) {
            graph.addEdge(i, i + 2 );
        }
        for(int i = 0; i < 9997; i+=3) {
            graph.addEdge(i, i + 3 );
        }
        Collection<Integer> path = graph.getPath(0, 9999);
        assertEquals(3334, path.size());

        path = graph.getPath(0, 10000);
        assertNull(path);
    }

    int s = 0;
    @Test
    public void test2()
    {
        Graph<Integer> graph = new Graph<>(true);
        for(int i = 0; i < 100; i++) {
            graph.addVertex(i);
        }
        s = 0;
        graph.traverse(v -> s++);
        assertEquals(100, s);
    }

}
