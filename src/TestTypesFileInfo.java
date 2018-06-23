

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import typesGrammar.TypesFileInfo;
import utils.Factor;
import utils.Type;

/**
 * <b>TestTypesFileInfo</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class TestTypesFileInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//".\\src\\tests\\typesGrammar\\TypesExample1.txt"
		System.out.println("Testing File " + args[0]);
		TypesFileInfo file = new TypesFileInfo(args[0]);
		System.out.println(file);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				createAndShowGUI(file.getTypesGraph());
			}
		});
	}

	private static void createAndShowGUI(Graph<Type, Factor> g)
	{
		JFrame f = new JFrame();
		f.setTitle("Types Graph Visualizer | TestTypesFileInfo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension size = new Dimension(800,800);
		VisualizationViewer<Type, Factor> vv = new VisualizationViewer<Type, Factor>(new FRLayout<Type, Factor>(g, size));
		DefaultModalGraphMouse<String, Double> graphMouse = new DefaultModalGraphMouse<String, Double>();
		vv.setGraphMouse(graphMouse); 
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Type>() {
			@Override
			public String transform(Type v) {

				return v.toString();
			}});

		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Factor>() {
			@Override
			public String transform(Factor v) {

				return v.toString();
			}});
		improvePerformance(vv);

		f.getContentPane().add(vv);
		f.setSize(size);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	// This method summarizes several options for improving the painting
	// performance. Enable or disable them depending on which visual features
	// you want to sacrifice for the higher performance.
	private static <V, E> void improvePerformance(VisualizationViewer<V, E> vv) {
		// Probably the most important step for the pure rendering performance:
		// Disable anti-aliasing
		vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);

		// Skip vertices that are not inside the visible area. 
		doNotPaintInvisibleVertices(vv);

		// May be helpful for performance in general, but not appropriate 
		// when there are multiple edges between a pair of nodes: Draw
		// the edges not as curves, but as straight lines:
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<V,E>());

		// May be helpful for painting performance: Omit the arrow heads
		// of directed edges
		Predicate<Context<Graph<V, E>, E>> edgeArrowPredicate = new Predicate<Context<Graph<V,E>,E>>() {
			@Override
			public boolean evaluate(Context<Graph<V, E>, E> arg0) {
				return false;
			}
		};
		vv.getRenderContext().setEdgeArrowPredicate(edgeArrowPredicate);

	}

	// Skip all vertices that are not in the visible area. 
	// NOTE: See notes at the end of this method!
	private static <V, E> void doNotPaintInvisibleVertices(VisualizationViewer<V, E> vv) {
		Predicate<Context<Graph<V, E>, V>> vertexIncludePredicate = new Predicate<Context<Graph<V,E>,V>>() {
			Dimension size = new Dimension();

			@Override
			public boolean evaluate(Context<Graph<V, E>, V> c) {
				vv.getSize(size);
				Point2D point = vv.getGraphLayout().transform(c.element);
				Point2D transformed = 
						vv.getRenderContext().getMultiLayerTransformer()
						.transform(point);
				if (transformed.getX() < 0 || transformed.getX() > size.width)
				{
					return false;
				}
				if (transformed.getY() < 0 || transformed.getY() > size.height)
				{
					return false;
				}
				return true;
			}
		};
		vv.getRenderContext().setVertexIncludePredicate(vertexIncludePredicate);

		// NOTE: By default, edges will NOT be included in the visualization
		// when ONE of their vertices is NOT included in the visualization.
		// This may look a bit odd when zooming and panning over the graph.
		// Calling the following method will cause the edges to be skipped
		// ONLY when BOTH their vertices are NOT included in the visualization,
		// which may look nicer and more intuitive
		doPaintEdgesAtLeastOneVertexIsVisible(vv);
	}

	// See note at end of "doNotPaintInvisibleVertices"
	private static <V, E> void doPaintEdgesAtLeastOneVertexIsVisible(
			VisualizationViewer<V, E> vv)
	{
		vv.getRenderer().setEdgeRenderer(new BasicEdgeRenderer<V, E>()
		{
			@Override
			public void paintEdge(RenderContext<V,E> rc, Layout<V, E> layout, E e) 
			{
				GraphicsDecorator g2d = rc.getGraphicsContext();
				Graph<V,E> graph = layout.getGraph();
				if (!rc.getEdgeIncludePredicate().evaluate(
						Context.<Graph<V,E>,E>getInstance(graph,e)))
					return;

				Pair<V> endpoints = graph.getEndpoints(e);
				V v1 = endpoints.getFirst();
				V v2 = endpoints.getSecond();
				if (!rc.getVertexIncludePredicate().evaluate(
						Context.<Graph<V,E>,V>getInstance(graph,v1)) && 
						!rc.getVertexIncludePredicate().evaluate(
								Context.<Graph<V,E>,V>getInstance(graph,v2)))
					return;

				Stroke new_stroke = rc.getEdgeStrokeTransformer().transform(e);
				Stroke old_stroke = g2d.getStroke();
				if (new_stroke != null)
					g2d.setStroke(new_stroke);

				drawSimpleEdge(rc, layout, e);

				// restore paint and stroke
				if (new_stroke != null)
					g2d.setStroke(old_stroke);
			}
		});
	}
}
