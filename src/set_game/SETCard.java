package set_game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SETCard {
	private int item_size = 7;
	private int item_number = 2;
	private int color_number = 2;
	private int shape_number = 2;
	private int fill_number = 2;
	private boolean is_selected=false;
	Color DefaultBackground;

	final CardPanel view_panel;
	CardPanel backing_panel;
	private int ID;

	public void SetSelected(boolean value)
	{
		is_selected=value;
        refresh_background();

	}
	
	public void SetItemNumber(int value)
	{
		item_number=value+1;
	}
	public void SetColorNumber(int value)
	{
		color_number=value+1;
	}
	public void SetShapeNumber(int value)
	{
		shape_number=value+1;
	}
	public void SetFillNumber(int value)
	{
		fill_number=value+1;
	}

	public int GetItemNumber()
	{
		return item_number+2;
	}
	
	public int GetColorNumber()
	{
		return color_number+2;
	}
	
	public int GetShapeNumber()
	{
		return shape_number+2;
	}
	
	public int GetFillNumber()
	{
		return fill_number+2;
	}
	
	public SETCard() {
		view_panel = new CardPanel();
    	DefaultBackground=view_panel.getBackground();
    	
	}



	private void refresh_background()
	{
        if( view_panel.getBackground()==DefaultBackground)
        	view_panel.setBackground(Color.GRAY);
        else
        	view_panel.setBackground(DefaultBackground);		
	}

    
	public boolean isSelected()
	{
		return is_selected;
	}
	
	public CardPanel GetCardView() {
	return view_panel;
	}


	

	public class CardPanel extends JPanel {


		
	    public CardPanel() {

	        addMouseListener(new MouseAdapter() {

	            @Override
	            public void mouseClicked(MouseEvent e) {
//	                System.out.println("Inner was clicked at : " + e.getPoint());
	                MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(e.getComponent(), e, getParent());
	                is_selected= !is_selected;
	                refresh_background();
	                getParent().dispatchEvent(convertMouseEvent);
	            }

	        });
	    }
	    
	    protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			Polygon p[] = new Polygon[item_number];
			int Polygon_Angle[] = { 3, 4, 20 };
			GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 2, 2, Color.WHITE);
			g2.setPaint(gp);
			g2.fillRoundRect(item_size, item_size, item_size * 16, item_size * 6, item_size, item_size);
			Color Card_Color[] = { Color.RED, Color.GREEN, Color.BLUE };
			boolean Card_Fill_1[] = { true, true, false };
			boolean Card_Fill_2[] = { true, false, false };
			Graphics2D garr2d[] = new Graphics2D[item_number];
			GradientPaint gparr[] = new GradientPaint[item_number];
			for (int k = 0; k < item_number; k++) {
				int m = Polygon_Angle[shape_number - 1];
				p[k] = new Polygon();
				garr2d[k] = (Graphics2D) g.create();
				gparr[k] = new GradientPaint(0, 0,
						Card_Fill_1[fill_number - 1] ? Card_Color[color_number - 1] : Color.WHITE, 1, 1,
						Card_Fill_2[fill_number - 1] ? Card_Color[color_number - 1] : Color.WHITE, true);
				for (int i = 0; i < m; i++)
					p[k].addPoint(
							(int) ((3 - item_number) * 2.5 * item_size + item_size * 4 + item_size * 5 * k
									+ 2 * item_size * Math.cos(i * 2 * Math.PI / m)),
							(int) (item_size * 4 + 2 * item_size * Math.sin(i * 2 * Math.PI / m)));
				g2.setColor(Card_Color[color_number - 1]);
				garr2d[k].setPaint(gparr[k]);
				garr2d[k].fillPolygon(p[k]);
				g.drawPolygon(p[k]);
				g2.drawPolygon(p[k]);
				garr2d[k].dispose();
			}
	    }


	    public Dimension getPreferredSize() {
			return new Dimension(item_size * 18, item_size * 8);
		}
	}


	public void SetId(int value) {
		// TODO Auto-generated method stub
		ID=value;
	}
	
	public int GetId() {
		// TODO Auto-generated method stub
		return ID;
	}
	
}