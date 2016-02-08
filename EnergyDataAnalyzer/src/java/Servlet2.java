
import com.itextpdf.awt.DefaultFontMapper;
import java.io.BufferedOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
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
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


/**
 *
 * @author Murugappan
 */
/**
 * Servlet implementation class to generate two country comparison
 */
@WebServlet("/Serv2")
public class Servlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet2() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String a = request.getParameter("country1");
			String b = request.getParameter("country2");
			System.out.println("\n Country" + a);
			System.out.println("\n Country" + b);
			String c = request.getParameter("param2");
			String d = request.getParameter("submit");
			System.out.println("\n Parameter" + c);
			String CurentUID = request.getParameter("UIDvalue2");
			String URLRequest = request.getRequestURL().append('?')
					.append(request.getQueryString()).toString();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			SimpleDateFormat format1 = new SimpleDateFormat(
					"EEE MMM dd hh:mm:ss yyyy");
			String date1 = cal.getTime().toString();
			String comma = ", ";
			String countries = a + comma + b;
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:server");
			Statement sthistoryinsert2 = con.createStatement();
			String insertstring = "Insert into UserActivity values('"
					+ CurentUID + "','" + date1
					+ "','Two Countries Comparison','" + countries + "','" + c
					+ "','" + URLRequest + "')";
			sthistoryinsert2.executeUpdate(insertstring);
			sthistoryinsert2.close();

			System.out.println("\n Step 1");
			Statement st = con.createStatement();
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			//retrieve historical energy data and store in series
			XYSeries[] series = new XYSeries[2];
			String query = "select [2000],[2001] ,[2002] ,[2003] ,[204] ,[2005],[2006],[2007],[2008],[2009],[2010],[2011],[2012] from country where CountryName='"
					+ a + "' AND SeriesName='" + c + "'";
			String query2 = "select [2000],[2001] ,[2002] ,[2003] ,[204] ,[2005],[2006],[2007],[2008],[2009],[2010],[2011],[2012] from country where CountryName='"
					+ b + "' AND SeriesName='" + c + "'";
			ResultSet rs = st.executeQuery(query);
			System.out.println("\n " + query);
			if (rs != null)
				System.out.println("\n result has some rows ");
			else
				System.out.println("\n Result has no rows ");
		
			System.out.println("\n Step 3");
			// Generate series based on historical data for country 1
			series[0] = new XYSeries(a);
			while (rs.next()) {
				series[0].add(2000, Double.parseDouble(rs.getString("2000")));
				series[0].add(2001, Double.parseDouble(rs.getString("2001")));
				series[0].add(2002, Double.parseDouble(rs.getString("2002")));
				series[0].add(2003, Double.parseDouble(rs.getString("2003")));
				series[0].add(2004, Double.parseDouble(rs.getString("204")));
				series[0].add(2005, Double.parseDouble(rs.getString("2005")));
				series[0].add(2006, Double.parseDouble(rs.getString("2006")));
				series[0].add(2007, Double.parseDouble(rs.getString("2007")));
				series[0].add(2008, Double.parseDouble(rs.getString("2008")));
				series[0].add(2009, Double.parseDouble(rs.getString("2009")));
				series[0].add(2010, Double.parseDouble(rs.getString("2010")));
				series[0].add(2011, Double.parseDouble(rs.getString("2011")));
				series[0].add(2012, Double.parseDouble(rs.getString("2012")));
			}

			rs.close();
			st.close();
			st = con.createStatement();
			ResultSet rs2 = st.executeQuery(query2);
			// Generate series based on historical data for country 2
			series[1] = new XYSeries(b);
			while (rs2.next()) {
				System.out.println("inside result set 2");
				series[1].add(2000, Double.parseDouble(rs2.getString("2000")));
				series[1].add(2001, Double.parseDouble(rs2.getString("2001")));
				series[1].add(2002, Double.parseDouble(rs2.getString("2002")));
				series[1].add(2003, Double.parseDouble(rs2.getString("2003")));
				series[1].add(2004, Double.parseDouble(rs2.getString("204")));
				series[1].add(2005, Double.parseDouble(rs2.getString("2005")));
				series[1].add(2006, Double.parseDouble(rs2.getString("2006")));
				series[1].add(2007, Double.parseDouble(rs2.getString("2007")));
				series[1].add(2008, Double.parseDouble(rs2.getString("2008")));
				series[1].add(2009, Double.parseDouble(rs2.getString("2009")));
				series[1].add(2010, Double.parseDouble(rs2.getString("2010")));
				series[1].add(2011, Double.parseDouble(rs2.getString("2011")));
				series[1].add(2012, Double.parseDouble(rs2.getString("2012")));

			}
			rs2.close();

			dataset.addSeries(series[0]);
			dataset.addSeries(series[1]);
			System.out.println("\n Step 4");
			//
			//intitialize JFree chart to plot required graph in a new tab
			//
			JFreeChart chart = ChartFactory.createXYLineChart(
					"World Power Consumption between " + a + " and " + b
							+ " based on " + c, "Year",
					"Energy consumed in millions", dataset,
					PlotOrientation.VERTICAL, true, true, false);
		
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			chart.setBackgroundPaint(Color.white);
			final XYPlot plot = chart.getXYPlot();
			plot.setBackgroundPaint(Color.white);
			plot.setDomainGridlinesVisible(true);
			plot.setRangeGridlinesVisible(true);
			plot.setDomainGridlinePaint(Color.black);
			plot.setRangeGridlinePaint(Color.black);

			final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setSeriesLinesVisible(2, false);
			renderer.setSeriesShapesVisible(2, false);
			plot.setRenderer(renderer);
			 // To insert colored Pie Chart into the PDF file using
			 //iText now
			if (d.equals("View Graph in Browser")) {
				ChartUtilities.writeChartAsPNG(bos, chart, 700, 500);
				response.setContentType("image/png");
				OutputStream out = new BufferedOutputStream(
						response.getOutputStream());
				out.write(bos.toByteArray());
				out.flush();
				out.close();
			}

			else {
				int width = 640; /* Width of  chart */
				int height = 480; /* Height of chart */
				Document PieChart = new Document(
						new com.itextpdf.text.Rectangle(width, height));
				java.util.Date date = new java.util.Date();
				String chartname = "My_Colored_Chart" + date.getTime() + ".pdf";
				PdfWriter writer = PdfWriter.getInstance(PieChart,
						new FileOutputStream(chartname));
				PieChart.open();
				
				PieChart.addTitle("How to color your Pie Chart and embed in a PDF file using iText");
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
				// to Import data generated into Pdf Document
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

					e.printStackTrace();
				}

				// set response headers for PDF Document
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
		}

		catch (Exception i) {
			i.printStackTrace();
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
