/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Murugappan
 */
/**
 * Servlet implementation to forecast energy data for selected country based on 
 * input parameter
 */
public class Servlet3 extends HttpServlet {

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			/* TODO output your page here. You may use following sample code. */
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet Servlet3</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet Servlet3 at " + request.getContextPath()
					+ "</h1>");
			out.println("</body>");
			out.println("</html>");
		}
	}

	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("inside servlet");
			String a = request.getParameter("countryf");
			String c = request.getParameter("submit");
			String b = request.getParameter("paramf");

			String CurentUID = request.getParameter("UIDvalue2f");
			String URLRequest = request.getRequestURL().append('?')
					.append(request.getQueryString()).toString();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			SimpleDateFormat format1 = new SimpleDateFormat(
					"EEE MMM dd hh:mm:ss yyyy");
			String date1 = cal.getTime().toString();

			System.out.println("inside servlet");

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:server");

			// To Insert data to UserActivity table for Recent Activities Tab
			Statement sthistoryinsert3 = con.createStatement();
			String insertstring = "Insert into UserActivity values('"
					+ CurentUID + "','" + date1 + "','Future Data Forecast','"
					+ a + "','" + b + "','" + URLRequest + "')";
			sthistoryinsert3.executeUpdate(insertstring);
			sthistoryinsert3.close();
			System.out.println("\n Step 1");
			Statement st = con.createStatement();
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series = new XYSeries(b);

			String query = "SELECT [2000],[2012] FROM country where CountryName='"
					+ a + "' AND SeriesName='" + b + "'";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if (rs == null)
				System.out.println("\n no rows ");
			else
				System.out.println("Rows present ");
			rs.next();

			Double start = Double.parseDouble(rs.getString(1));
			Double end = Double.parseDouble(rs.getString(2));
			Double period = 13.0;
			Double growth = Math.pow((end / start), (1 / period)) - 1;
			System.out.println("growth percentage =" + growth);
			rs.close();
			String query2 = "select [2011],[2012] from country where CountryName='"
					+ a + "' AND SeriesName='" + b + "'";
			rs = st.executeQuery(query2);
			rs.next();
			series.add(2011, Double.parseDouble(rs.getString(1)));
			Double second = Double.parseDouble(rs.getString(2));
			series.add(2012, second);

			Double growthvalue = second + (second * growth);

			series.add(2013, growthvalue);
			for (int i = 2014; i <= 2016; i++) {
				System.out.println("actual growth value = " + growthvalue);
				series.add((i++), (growthvalue + growthvalue * growth));
				growthvalue = growthvalue + growthvalue * growth;
			}
			rs.close();
			dataset.addSeries(series);
			DecimalFormat format_2Places = new DecimalFormat("0.00");
			growth = growth * 100;
			growth = Double.valueOf(format_2Places.format(growth));
			JFreeChart chart = ChartFactory.createXYLineChart(
					"Energy forecasting for " + a + " based on " + b
							+ " with growth value estimated at " + growth
							+ "% ", "Year", "Energy consumed in millions",
					dataset, PlotOrientation.VERTICAL, true, true, false);
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
			 // iText now	
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
				int width = 640; /* Width of our chart */
				int height = 480; /* Height of our chart */
				Document PieChart = new Document(
						new com.itextpdf.text.Rectangle(width, height));
				java.util.Date date = new java.util.Date();
				String chartname = "My_Colored_Chart" + date.getTime() + ".pdf";
				PdfWriter writer = PdfWriter.getInstance(PieChart,
						new FileOutputStream(chartname));
				PieChart.open();

				PieChart.addTitle("Pie-Chart");
				PieChart.addAuthor("MUurugappan");
			
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
		}

		catch (Exception i) {
			i.printStackTrace();
		}

	}

}
