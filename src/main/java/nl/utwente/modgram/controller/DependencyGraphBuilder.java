package nl.utwente.modgram.controller;

import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;
import org.antlr.v4.misc.Graph;

import java.util.HashSet;

public class DependencyGraphBuilder {

    /**
     * Builds a dependency graph for the given modular grammar. The nodes of the graph are the modules of the grammar.
     * An edge is added from a module to another module if the module uses the other module.
     * @param grammar   The modular grammar of which the dependency graph should be made.
     * @return          The dependency graph.
     */
    public static Graph<String> buildDependencyGraph(ModularGrammar grammar) {
        Graph<String> graph = new Graph<>();
        for (Module module : grammar.getModules()) {
            for (String dependency : module.getDependencies()) {
                graph.addEdge(module.getName(), dependency);
            }
        }
        return graph;
    }

    /**
     * Checks if the graph from the given start node is cyclic.
     * @param startNode The start node of the graph.
     * @return          if the graph is cyclic {@code true}, if not {@code false}.
     */
    public static boolean graphIsCyclic(Graph.Node<String> startNode) {
        for (Graph.Node<String> node : startNode.edges) {
            HashSet<Graph.Node<String>> encounteredNodes = new HashSet<>();
            encounteredNodes.add(startNode);
            if (nodeIsPartOfCycle(node, encounteredNodes))
                return true;
        }
        return false;
    }

    /**
     * Helper function of {@link DependencyGraphBuilder#graphIsCyclic(Graph.Node)}. Checks if the given node is part of
     * a cycle.
     * @param node              The node that is checked.
     * @param encounteredNodes  The nodes that already have been encountered.
     * @return                  if the node is part of a cycle {@code true}, if not {@code false}.
     */
    private static boolean nodeIsPartOfCycle(Graph.Node<String> node, HashSet<Graph.Node<String>> encounteredNodes) {
        for (Graph.Node<String> child : node.edges) {
            HashSet<Graph.Node<String>> newEncounteredNodes = new HashSet<>(encounteredNodes);
            newEncounteredNodes.add(child);
            if (newEncounteredNodes.size() == encounteredNodes.size())
                return true;
            if (nodeIsPartOfCycle(child, newEncounteredNodes))
                return true;
        }
        return false;
    }
}
