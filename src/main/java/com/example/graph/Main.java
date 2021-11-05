package com.example.graph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.gml.GmlExporter;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader dataFile = new FileReader("karate_club.txt");
        Scanner dataScanner = new Scanner(dataFile);
        ArrayList<int[]> edges = new ArrayList<>();
        int count = 0;
        while (dataScanner.hasNextLine()) {
            StringBuilder edge = new StringBuilder();
            for (char symbol: dataScanner.nextLine().toCharArray()) {
                if (symbol == ']') {
                    count = 0;
                    String[] nodes = edge.toString().split(" ");
                    int[] nodeNumbers = new int[2];
                    nodeNumbers[0] = Integer.parseInt(nodes[0]) - 1;
                    nodeNumbers[1] = Integer.parseInt(nodes[1]) - 1;
                    edges.add(nodeNumbers);
                    edge = new StringBuilder();
                }
                if (count == 1) edge.append(symbol);
                if (symbol == '[') count = 1;
            }
        }
        dataFile.close();

        Graph<String, Edge> g = new DefaultDirectedGraph<>(Edge.class);
        int n = 34;
        String[] v = new String[n];
        for (int i = 0; i < n; i++) g.addVertex(v[i] = "Person №" + (i + 1));
        for (int[] edge: edges) g.addEdge(v[edge[0]], v[edge[1]]);

        JGraphXAdapter<String, Edge> graphAdapter = new JGraphXAdapter<>(g);

        mxGraphComponent component = new mxGraphComponent(graphAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);

        mxCircleLayout layout = new mxCircleLayout(graphAdapter, 200);
        layout.execute(graphAdapter.getDefaultParent());

        JFrame window = new JFrame("Graph");
        window.add(component);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        GmlExporter<String, Edge> gmlExporter = new GmlExporter<>();
        gmlExporter.setParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS, true);
        gmlExporter.exportGraph(g, new File("graph_data.gml"));
    }

    private static class Edge extends DefaultEdge {
        @Override
        public String toString() {
            return "";
        }
    }
}