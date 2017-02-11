package cs455.overlay.dijkstra;

import java.util.ArrayList;
import java.util.Random;

public class Graph {
	private ArrayList<Vertex> vertices, possibleVertices;
	private ArrayList<Edge> edges;
	private boolean overlayStatus;

	public Graph() {
		this.edges = new ArrayList<Edge>();
		setOverlayStatus(false);
	}

	public Graph(ArrayList<Edge> edges) {
		this.edges = edges;
		this.vertices = new ArrayList<Vertex>();
		setOverlayStatus(true);
		createVertices();
	}

	private void createVertices() {
		for (Edge edge : edges) {
			if (!vertices.contains(edge.getSource())) {
				vertices.add(new Vertex(edge.getSource().getID(), 4));

			}
			if (!vertices.contains(edge.getDestination())) {
				vertices.add(new Vertex(edge.getDestination().getID(), 4));
			}
			vertices.get(vertices.indexOf(edge.getSource())).addConnection(edge.getDestination());
			vertices.get(vertices.indexOf(edge.getDestination())).addConnection(edge.getSource());
		}
	}

	public void assignWeights() {
		for (Edge e : edges) {
			e.setWeight((int) Math.floor(Math.random() * 10) + 1);
			System.out.println("Assigned " + e.toString());
		}
	}

	public void setupOverlay(ArrayList<Vertex> vertices, int connectionsRequired) {
		this.vertices = vertices;
		possibleVertices = new ArrayList<Vertex>();
		possibleVertices.addAll(vertices);

		setupFirstRoundConnections();
		System.out.println("Number Edges: " + edges.size());

		int count = 0;
		while (!possibleVertices.isEmpty()) {
			count++;
			System.out.println("[Iteration " + count + "] Size: " + possibleVertices.size());
			Vertex first, second;

			first = possibleVertices.get(new Random().nextInt(possibleVertices.size()));

			do {
				second = possibleVertices.get(new Random().nextInt(possibleVertices.size()));
			} while (first.equals(second));

			if (first.addConnection(second)) {
				second.addConnection(first);
				System.out.println("Connected " + first.toString() + " " + first.getNumberConnection() + " and "
						+ second.toString() + " " + second.getNumberConnection());
				edges.add(new Edge(first, second));
			}

			if (first.getNumberConnection() == connectionsRequired) {
				System.out.println(
						"Removing " + first.toString() + " due to max connections: " + first.getNumberConnection());
				possibleVertices.remove(first);
			}
			if (second.getNumberConnection() == connectionsRequired) {
				System.out.println(
						"Removing " + second.toString() + " due to max connections: " + second.getNumberConnection());
				possibleVertices.remove(second);
			}
			System.out.println("");
			if (possibleVertices.size() == 1) {
				count = 0;
				possibleVertices.clear();
				for (int i = 0; i < vertices.size(); i++) {
					vertices.get(i).clearConnections();
				}
				possibleVertices.addAll(vertices);
			}
			if (count > 100)
				break;
		}
		System.out.println("Number Edges: " + edges.size());
		setOverlayStatus(true);
	}

	private void setupFirstRoundConnections() {
		for (int index = 0; index < possibleVertices.size() - 1; index++) {
			edges.add(new Edge(possibleVertices.get(index), possibleVertices.get(index + 1)));
			possibleVertices.get(index).addConnection(possibleVertices.get(index + 1));
			possibleVertices.get(index + 1).addConnection(possibleVertices.get(index));

		}
		edges.add(new Edge(possibleVertices.get(0), possibleVertices.get(possibleVertices.size() - 1)));
	}

	private void setOverlayStatus(boolean status) {
		this.overlayStatus = status;
	}

	public ArrayList<Vertex> getVertices() {
		return this.vertices;
	}

	public ArrayList<Edge> getEdges() {
		return this.edges;
	}

	public String marshallEdgeString() {
		String ret = "";

		for (Edge edge : edges) {
			ret += edge.toString() + "\n";
		}

		return ret;
	}

	public boolean getOverlayStatus() {
		return overlayStatus;
	}

	public int getEdgeWeight(Vertex source, Vertex step) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(source) && edge.getDestination().equals(step)) {
				return edge.getWeight();
			}
		}
		return -1;
	}

}
