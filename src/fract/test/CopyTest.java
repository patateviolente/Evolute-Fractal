package fract.test;
import java.io.IOException;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.datatransfer.*;
 
public class CopyTest extends JPanel{
	public static void main(String args[]) throws Exception{
		final CopyTest copyTest = new CopyTest();
		
		JFrame f = new JFrame();
		f.addWindowListener(new WindowListener(){
			public void windowClosed(WindowEvent e){}
			public void windowClosing(WindowEvent e){ System.exit(0); }
			public void windowIconified(WindowEvent e){}
			public void windowDeiconified(WindowEvent e){}
			public void windowActivated(WindowEvent e){}
			public void windowDeactivated(WindowEvent e){}
			public void windowOpened(WindowEvent e){}
		});
		f.setLayout(new BorderLayout());
		
		JButton btnCopy = new JButton("Copy");
		JButton btnPaste = new JButton("Paste");
		
		JPanel southPanel = new JPanel();
		southPanel.add(btnCopy);
		southPanel.add(btnPaste);
		f.add(copyTest, BorderLayout.CENTER);
		f.add(southPanel, BorderLayout.SOUTH);
		
		btnCopy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ copyTest.copy(); }
		});
		btnPaste.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ copyTest.paste(); }
		});
		
		f.setSize(320, 240);
		f.setVisible(true);
	}
	
	private static final int SQUARE_SIZE=6;
	private BufferedImage image;
	
	// Constructor
	public CopyTest() throws Exception{
		this.image = ImageIO.read(new URL("http://forums.sun.com/im/duke.gif"));
	}
	
	// Copy
	public void copy(){
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new Transferable(){
					public DataFlavor[] getTransferDataFlavors() { return new DataFlavor[]{DataFlavor.imageFlavor}; }
					public boolean isDataFlavorSupported(DataFlavor flavor) { return DataFlavor.imageFlavor.equals(DataFlavor.imageFlavor); }
					public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			            if(!DataFlavor.imageFlavor.equals(flavor)){ throw new UnsupportedFlavorException(flavor); }
						return image;
			        }
				}
				, null);
	}
	
	// Paste
	public void paste(){
		// Get the contents
		BufferedImage clipboard = null;
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
            if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            	clipboard = (BufferedImage)t.getTransferData(DataFlavor.imageFlavor);
            }
        } catch (Exception e) { e.printStackTrace(); }
 
        // Use the contents
        if(clipboard != null){
			image = clipboard;
			repaint();
		}
	}
	
	
	// Paint
	public void paint(Graphics g){
		// Paint squares in the background to accent transparency
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.DARK_GRAY);
		for(int x=0; x<(getWidth()/SQUARE_SIZE)+1; x=x+1){
			for(int y=0; y<(getHeight()/SQUARE_SIZE)+1; y=y+1){
				if(x%2 == y%2){ g.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE); }
			}
		}
		// Paint the image
		g.drawImage(image, 0, 0, this);
	}
}