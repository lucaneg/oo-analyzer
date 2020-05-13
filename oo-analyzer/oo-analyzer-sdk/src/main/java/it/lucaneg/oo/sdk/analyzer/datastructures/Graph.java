package it.lucaneg.oo.sdk.analyzer.datastructures;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

/**
 * A graph, represented as an adjacency matrix.
 * 
 * @author Luca Negrini
 */
public abstract class Graph<T> {

	protected final Map<T, List<T>> edges = new IdentityHashMap<>();
	
	private final List<T> EMPTY_LIST = new ArrayList<>();

	/**
	 * Adds the given vertex to the set of vertexes
	 * 
	 * @param vertex the vertex to add
	 */
	protected void addVertex(T vertex) {
		edges.put(vertex, new LinkedList<T>());
	}

	/**
	 * Adds an edge from source to destination.
	 * 
	 * @param source      the source vertex
	 * @param destination the destination vertex
	 */
	protected void addEdge(T source, T destination) {
		if (!edges.containsKey(source))
			throw new UnsupportedOperationException("The source node is not in the graph");

		if (!edges.containsKey(destination))
			throw new UnsupportedOperationException("The destination node is not in the graph");

		edges.get(source).add(destination);
	}

	/**
	 * Yields the total number of vertexes of this graph.
	 * 
	 * @return the number of vertexes
	 */
	protected int getVertexCount() {
		return edges.keySet().size();
	}

	/**
	 * Yields the total number of edges of this graph.
	 * 
	 * @return the number of edges
	 */
	protected int getEdgesCount() {
		int count = 0;

		for (T v : edges.keySet())
			count += edges.get(v).size();

		return count;
	}

	/**
	 * Yields true if and only if the given vertex is part of this graph.
	 * 
	 * @param vertex the vertex
	 * @return true only if that condition holds
	 */
	protected boolean hasVertex(T vertex) {
		return edges.containsKey(vertex);
	}

	/**
	 * Yields true if and only if this graph contains an edge going from the given
	 * source vertex to the given destination vertex.
	 * 
	 * @param source      the source vertex
	 * @param destination the destination vertex
	 * @return true only if that condition holds
	 */
	protected boolean hasEdge(T source, T destination) {
		if (!edges.containsKey(source))
			return false;

		return edges.get(source).contains(destination);
	}

	/**
	 * Yields an unmodifiable view of all the vertexes of this graph.
	 * 
	 * @return the collection of vertexes
	 */
	public Collection<T> nodes() {
		return Collections.unmodifiableCollection(edges.keySet());
	}

	/**
	 * Yields an unmodifiable view of the vertexes that are followers of the given
	 * vertex, that is, all vertexes such that there exist an edge in this graph
	 * going from the given vertex to such vertex.
	 * 
	 * @param vertex the vertex
	 * @return the collection of followers
	 */
	public Collection<T> followersOf(T vertex) {
		return Collections.unmodifiableCollection(edges.getOrDefault(vertex, EMPTY_LIST));
	}

	/**
	 * Yields an unmodifiable view of the vertexes that are predecessors of the
	 * given vertex, that is, all vertexes such that there exist an edge in this
	 * graph going from such vertex to the given one.
	 * 
	 * @param vertex the vertex
	 * @return the collection of predecessors
	 */
	public Collection<T> predecessorsOf(T vertex) {
		return Collections
				.unmodifiableCollection(edges.entrySet().stream().filter(pair -> pair.getValue().contains(vertex))
						.map(pair -> pair.getKey()).collect(Collectors.toList()));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		TreeSet<T> orderedKeys = new TreeSet<>((t, u) -> t.toString().compareTo(u.toString()));
		orderedKeys.addAll(edges.keySet());
		for (T v : orderedKeys) {
			builder.append(v.toString() + ": [");
			for (T w : edges.get(v))
				builder.append(w.toString() + ", ");

			builder.append("]\n");
		}

		return builder.toString();
	}

	/**
	 * Dumps the content of this graph in the given writer, formatted as a dot file.
	 * 
	 * @param writer the writer where the content will be written
	 * @param name   the name of the dot diagraph
	 * @throws IOException if an exception happens while writing something to the
	 *                     given writer
	 */
	public void dump(Writer writer, String name) throws IOException {
		dump(writer, name, st -> "");
	}

	/**
	 * Dumps the content of this graph in the given writer, formatted as a dot file.
	 * The content of each vertex will be enriched by invoking labelGenerator on the
	 * vertex itself, to obtain an extra description to be concatenated with the
	 * standard call to the vertex's {@link #toString()}
	 * 
	 * @param writer         the writer where the content will be written
	 * @param name           the name of the dot diagraph
	 * @param labelGenerator the function used to generate extra labels
	 * @throws IOException if an exception happens while writing something to the
	 *                     given writer
	 */
	public void dump(Writer writer, String name, Function<T, String> labelGenerator) throws IOException {
		writer.write("digraph " + name + " {\n");

		Map<T, Integer> codes = new IdentityHashMap<>();
		int code = 0;

		Stack<T> ws = new Stack<>();
		HashSet<T> seen = new HashSet<>();
		ws.push(getRootNode());
		seen.add(getRootNode());

		while (!ws.isEmpty()) {
			T current = ws.pop();

			if (!codes.containsKey(current))
				codes.put(current, code++);

			int id = codes.get(current);
			String extraLabel = labelGenerator.apply(current);
			if (!extraLabel.isEmpty())
				extraLabel = "\\n" + dotEscape(extraLabel);

			writer.write("node" + id + " [");
			writer.write(provideVertexShapeIfNeeded(current));
			writer.write("label = \"" + dotEscape(current.toString()) + extraLabel + "\"];\n");

			for (T follower : followersOf(current)) {
				if (!codes.containsKey(follower))
					codes.put(follower, code++);

				int id1 = codes.get(follower);
				String label = provideEdgeLabelIfNeeded(current, follower);
				if (!label.isEmpty())
					writer.write("node" + id + " -> node" + id1 + " [label=\"" + label + "\"]\n");
				else
					writer.write("node" + id + " -> node" + id1 + "\n");

				if (seen.add(follower))
					ws.push(follower);
			}
		}

		writer.write("}");
	}

	private String dotEscape(String extraLabel) {
		return StringEscapeUtils.escapeHtml4(extraLabel).replace("\\", "\\\\");
	}

	/**
	 * Yields the root vertex of this graph
	 * 
	 * @return the root vertex
	 */
	protected abstract T getRootNode();

	/**
	 * Provides shape information, in dot format, for the given vertex. Such string
	 * should be comma-terminated. If no shape information is needed, this method
	 * should return the empty string.
	 * 
	 * @param vertex the vertex
	 * @return the shape information of the given vertex, if needed
	 */
	protected abstract String provideVertexShapeIfNeeded(T vertex);

	/**
	 * Provides a textual label to be placed on the edge going from source to
	 * destination. If no label is needed, this method should return the empty
	 * string.
	 * 
	 * @param source      the source of the edge
	 * @param destination the destination of the edge
	 * @return the label of the edge, if needed
	 */
	protected abstract String provideEdgeLabelIfNeeded(T source, T destination);
}