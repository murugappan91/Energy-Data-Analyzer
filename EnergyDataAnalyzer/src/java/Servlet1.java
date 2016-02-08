
import com.itextpdf.awt.DefaultFontMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


/**
 *
 * @author Murugappan
 */
/**
 * Servlet implementation to generate one country based visualization of data
 * based on input parameter
 */
@WebServlet("/Serv1")
public class Servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet1() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			System.out.println("\n Inside Servlet");
			Double d = Double.parseDouble("1262645000");
			System.out.println("Sample dobule" + d);
			String a = request.getParameter("countrya");
			System.out.println("\n Country" + a);
			String c = request.getParameter("submit");
			String[] b = request.getParameterValues("param");
			String CurentUID = request.getParameter("UIDvalue");
			String URLRequest = request.getRequestURL().append('?')
					.append(request.getQueryString()).toString();

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			SimpleDateFormat format1 = new SimpleDateFormat(
					"EEE MMM dd hh:mm:ss yyyy");
			String date1 = cal.getTime().toString();
			String newParam = "";
			// get all the paramerers selected by the user
			for (int i = 0; i < b.length; i++) {
				newParam += b[i];
				newParam += ",";
			}
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:server");
			System.out.println("\n Step 1");
			Statement st = con.createStatement();

			//
			// Code to achieve Recent Activity tab : storing the requested graph
			// parameters into DB Table : UserActivity
			//
			Statement sthistoryinsert = con.createStatement();
			String insertstring = "Insert into UserActivity values('"
					+ CurentUID + "','" + date1 + "','One Country Analysis','"
					+ a + "','" + newParam + "','" + URLRequest + "')";
			sthistoryinsert.executeUpdate(insertstring);
			sthistoryinsert.close();

			XYSeriesCollection dataset = new XYSeriesCollection();

			XYSeries[] series = new XYSeries[8];
			for (int i = 0; i < b.length; i++) {
				series[i] = new XYSeries(b[i]);

			}
			System.out.println("\n Step 2");
			for (int i = 0; i < b.length; i++) {

				System.out.println("\n iteration " + i + " " + b[i]);
				String query = "select [2000],[2001] ,[2002] ,[2003] ,[204] ,[2005],[2006],[2007],[2008],[2009],[2010],[2011],[2012] from country where CountryName='"
						+ a + "' AND SeriesName='" + b[i] + "'";
				ResultSet rs = st.executeQuery(query);
				System.out.println("\n " + query);
				if (rs != null)
					System.out.println("\n result has some rows ");
				else
					System.out.println("\n Result has no rows ");

				System.out.println("\n Step 3");

				ResultSetMetaData rsmd = rs.getMetaData();
				String name = rsmd.getColumnName(1);

				System.out.println("\n Value =" + name);
				// Generate series based on historical data for required country 
				while (rs.next()) {
					System.out.println("\n 1 i=" + i);

					series[i].add(2000, Double.parseDouble(rs.getString(1)));
					series[i].add(2001, Double.parseDouble(rs.getString(2)));
					series[i].add(2002, Double.parseDouble(rs.getString(3)));
					series[i].add(2003, Double.parseDouble(rs.getString(4)));
					series[i].add(2004, Double.parseDouble(rs.getString(5)));
					series[i].add(2005, Double.parseDouble(rs.getString(6)));
					series[i].add(2006, Double.parseDouble(rs.getString(7)));
					series[i].add(2007, Double.parseDouble(rs.getString(8)));
					series[i].add(2008, Double.parseDouble(rs.getString(9)));
					series[i].add(2009, Double.parseDouble(rs.getString(10)));
					series[i].add(2010, Double.parseDouble(rs.getString(11)));
					series[i].add(2011, Double.parseDouble(rs.getString(12)));
					series[i].add(2012, Double.parseDouble(rs.getString(13)));

				}
				rs.close();
				// st.close();
			}

			for (int i = 0; i < b.length; i++)
				dataset.addSeries(series[i]);
			System.out.println("\n Step 4");
			String temp = "Energy consumption for " + a;
			System.out.println("\n" + temp);
			JFreeChart chart = ChartFactory.createXYLineChart(temp, "Year",
					"Energy consumed in millions", dataset,
					PlotOrientation.VERTICAL, true, true, false);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			chart.setBackgroundPaint(Color.white);
			final XYPlot plot = chart.getXYPlot();
			plot.setBackgroundPaint(Color.white);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);

			final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setSeriesLinesVisible(b.length, false);
			renderer.setSeriesShapesVisible(b.length, false);
			plot.setRenderer(renderer);

			 // To insert colored Pie Chart into the PDF file using
			 // iText now			 

			System.out.println("step 5");
			java.util.Date date = new java.util.Date();
			String chartname = "My_Colored_Chart" + date.getTime() + ".pdf";

			if (c.equals("View Graph in Browser")) {

				ChartUtilities.writeChartAsPNG(bos, chart, 700, 500);
				response.setContentType("image/png");
				OutputStream out = new BufferedOutputStream(
						response.getOutputStream());
				out.write(bos.toByteArray());
				out.flush();
				out.close();
			}

			else {

				int width = 640; //Width of our chart 
				int height = 480; // Height of our chart
				Document PieChart = new Document(
						new com.itextpdf.text.Rectangle(width, height));

				PdfWriter writer = PdfWriter.getInstance(PieChart,
						new FileOutputStream(chartname));
				PieChart.open();

				PieChart.addTitle("Energy consumption for" + a);
				PieChart.addAuthor("Thinktibits");
				PieChart.addKeywords("iText,Color PieChart,JFreeChart,PDF,Example Tutorial");
				PdfContentByte Add_Chart_Content = writer.getDirectContent();
				PdfTemplate template_Chart_Holder = Add_Chart_Content
						.createTemplate(width, height);
				Graphics2D Graphics_Chart = template_Chart_Holder
						.createGraphics(width, height, new DefaultFontMapper());
				Rectangle2D Chart_Region = new Rectangle2D.Double(0, 0, 540,
						380);
				chart.draw(Graphics_Chart, Chart_Region);
				Graphics_Chart.dispose();
				Add_Chart_Content.addTemplate(template_Chart_Holder, 0, 0);
				PieChart.close();
				PdfReader reader = new PdfReader(chartname);
				PdfStamper stamper = null;

				try {
					stamper = new PdfStamper(reader, bos);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
				try {
					stamper.close();
				} catch (DocumentException e) {
					System.out.println("Why is it making me do this?");
					e.printStackTrace();
				}

				// set response headers to view PDF
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");
				response.setContentLength(bos.size());

				OutputStream os = response.getOutputStream();
				bos.writeTo(os);
				os.flush();
				os.close();
			}
			System.out.println("Step 6 ");
		} catch (Exception i) {
			System.out.println(i);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
