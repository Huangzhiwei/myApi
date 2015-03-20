package validator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ValidatorServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static String codeChars = "%#@1234567890qwertyuioplkjhgfdsazxcvbnmMNBVCXZASDFGHJKLPOIUYTREWQ";
	private static final String VALIDATOR_CODE = "validator_code";
	
	private Color getRandomColor(int minColor,int maxColor){
		Random random = new Random();
		if(minColor > 255)
			minColor = 255;
		if(maxColor > 255)
			maxColor = 255;
		int red = minColor + random.nextInt(maxColor-minColor);
		int green = minColor + random.nextInt(maxColor-minColor);
		int blue = minColor + random.nextInt(maxColor-minColor);
		return new Color(red,green,blue);
	}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int charsLength = codeChars.length();
		//采取保守的三重结构支持不同浏览器
		resp.setHeader("ragma", "No-cache");
		resp.setHeader("Cache-control", "no-cache");
		resp.setDateHeader("Expires", 0);
		
		int width = 90;
		int height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandomColor(180, 255));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, height));
		
		StringBuilder validatorCode = new StringBuilder();
		String[] fontName = {"Times New Roman","Book antiqua","Arial"};
		for(int i = 0;i < 3+random.nextInt(3);++i){
			g.setFont(new Font(fontName[random.nextInt(3)], Font.ITALIC, height));
			char charCode = codeChars.charAt(random.nextInt(charsLength));
			validatorCode.append(charCode);
			g.setColor(getRandomColor(0, 100));
			g.drawString(String.valueOf(charCode), 16*i+random.nextInt(7), height-random.nextInt(6));
		}
		HttpSession session = req.getSession(true);
		session.setAttribute(VALIDATOR_CODE, validatorCode.toString());
		session.setMaxInactiveInterval(5*60);
		g.dispose();
		OutputStream os = resp.getOutputStream();
		ImageIO.write(image, "JPEG", os);
	}
}
