import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import utils.Factor;
import utils.Type;

public class VisualizeGraph2 extends Container
{
	static final long serialVersionUID = 420001L;
	DirectedSparseGraph<Type, Factor> graph = null;
	VisualizationViewer<Type, Factor> vv = null;
	PickedState<Number> pickedState = null;

	public VisualizeGraph2()
	{
		try
		{
			graph = new DirectedSparseGraph<Type, Factor>();
			construct_graph();

			vv = new VisualizationViewer<Type, Factor>
			(new CircleLayout<Type, Factor>(graph), new Dimension(400, 400));
			vv.getRenderer().setTypeRenderer(new MyRenderer());

		}
		catch (Exception e)
		{
			System.err.println("Failed to construct graph!\n");
			System.err.println("Caught Exception: " + e.getMessage());
		}
	}

	public void attach_to_frame(JFrame frame)
	{
		frame.setContentPane(vv);
	}

	static class MyRenderer extends JPanel implements Renderer.Type<Type, Factor>
	{
		static final long serialVersionUID = 420000L;
		@Override
		public void paintType(RenderContext<Type, Factor> rc,
				Layout<Type, Factor> layout, Type vertex)
		{
			try
			{
				GraphicsDecorator graphicsContext = rc.getGraphicsContext();
				Point2D center = layout.transform(vertex);
				Dimension size = new Dimension(100, 80);

				System.out.printf("Type[%s] X = %d Y = %d: Running paintType()\n", vertex, (int)center.getX(), (int)center.getY());

				JPanel sv = new JPanel();
				sv.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				sv.setBackground(Color.GREEN);
				//sv.setPreferredSize(size);
				JLabel label = new JLabel("<html><div>"+vertex.toString()+"</div></html>");
				label.setSize(100, 50);
				sv.add(label);
				//OK
				//final Dimension size = label.getPreferredSize();
				label.setMinimumSize(size);
				label.setPreferredSize(size);
				label.setSize(size);
				label.setSize(100, 50);
				//label.setFont(new Font(label.getName(), Font.PLAIN, 6));
				label.setText("<html><div>"+vertex.toString()+"</p></html>");

				//sv.setPreferredSize(size);

				///*
				Font labelFont = label.getFont();
				String labelText = vertex.toString();

				int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
				int componentWidth = label.getWidth();

				double widthRatio = (double)componentWidth / (double)stringWidth;

				int newFontSize = (int)(labelFont.getSize() * widthRatio);
				int componentHeight = label.getHeight();

				int fontSizeToUse = Math.min(newFontSize, componentHeight);

				label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
				label.setText(vertex.toString());
				//*/

				graphicsContext.draw(sv, rc.getRendererPane(), (int)center.getX(), 
						(int)center.getY(), size.width, size.height, true);
			}
			catch (Exception e)
			{
				System.err.println("Failed to render images!\n");
				System.err.println("Caught Exception: " + e.getMessage());
			}
		}
	}

	public static void main(String[] args)
	{
		/*Create the window*/
		JFrame frame = new JFrame("BLABLA");
		//Number[][] list = {{0, 1, 3}, {1, 3, 1}, {2, 2, 3}, {3, 2, 0}};
		VisualizeGraph2 g = new VisualizeGraph2();
		g.attach_to_frame(frame);
		frame.getContentPane().setPreferredSize(new Dimension(640, 480));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}/*2*/
