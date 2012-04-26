package image;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class okkamimage
 */
@WebServlet("/okkamimage")
public class okkamimage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			response.setContentType("image/png");
			okkamimage okimg=new okkamimage();
			int s=Integer.parseInt(request.getParameter("size"));
			String tag=request.getParameter("tag");
			if(s<400){
			s=400;//just for test. would be modified later to match "powered by okkam" size
			}
			if(tag==null||tag==""){
				tag="test";
			}
		BufferedImage bufokimg=okimg.createTag(s,tag);

			OutputStream out = response.getOutputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufokimg, "png", baos);
			byte[] bytesOut = baos.toByteArray();
			out.write(bytesOut);
			out.close();
		 
	}
	public BufferedImage createTag(int size, String tag) throws IOException{ 
		Image image = null; 
		 if(size>547)//maximum size of QR code
			 size=547;
		 if (size==0) size=1; //minimum size
			URL url = new URL("http://chart.googleapis.com/chart?cht=qr&chl="+tag+"&chs="+size+"x"+size);
	        image = ImageIO.read(url);
	        Image logo=ImageIO.read(new File("C:\\Users\\yusra\\workspace\\OkkamImage\\WebContent\\WEB-INF\\image\\Poweredby.png"));
	        
			BufferedImage img =new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			 Graphics2D gi = img.createGraphics();
		    gi.drawImage(image, null, null);
		    
		    BufferedImage logimg =new BufferedImage(logo.getWidth(null), logo.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			 Graphics2D loggi = logimg.createGraphics();
		    loggi.drawImage(logo, null, null);
		    
		    int x=img.getWidth()-logimg.getWidth()-(int)Math.round(0.13*img.getWidth());
		    int y=img.getHeight()-logimg.getHeight()-(int)Math.round(0.04*img.getHeight());
		    int w=logimg.getWidth();
		    int h=logimg.getHeight();
		    
		    System.out.println("x="+x+"y="+y+"w="+w+"h="+h);
		    
		    BufferedImage replaceArea = img.getSubimage(x, y, w, h);
			Graphics g = replaceArea.getGraphics();
			g.drawImage(logimg, 0, 0, w, h, null);
			
		
		return img;	
	}

}