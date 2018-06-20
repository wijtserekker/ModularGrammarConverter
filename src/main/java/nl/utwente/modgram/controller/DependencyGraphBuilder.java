package nl.utwente.modgram.controller;

import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;
import org.antlr.v4.misc.Graph;

import java.util.HashSet;

public class DependencyGraphBuilder {

    public static Graph<String> buildDependencyGraph(ModularGrammar grammar) {
        Graph<String> graph = new Graph<>();
        for (Module module : grammar.getModules()) {
            for (String dependency : module.getDependencies()) {
                graph.addEdge(module.getName(), dependency);
            }
        }
        return graph;
    }

    public static boolean graphIsCyclic(Graph<String> graph, Graph.Node<String> startNode) {
        for (Graph.Node<String> node : startNode.edges) {
            HashSet<Graph.Node<String>> encounteredNodes = new HashSet<>();
            encounteredNodes.add(startNode);
            if (nodeIsPartOfCycle(node, encounteredNodes))
                return true;
        }
        return false;
    }

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
