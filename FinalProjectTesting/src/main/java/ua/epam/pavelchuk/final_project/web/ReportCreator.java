package ua.epam.pavelchuk.final_project.web;

import java.io.DataOutputStream; 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.ResultDAO;
import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.Result;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * Creates reports in PDF format
 * 
 * @author O.Pavelchuk
 */
public class ReportCreator extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -918638611392803447L;

	private static final Logger LOG = Logger.getLogger(ReportCreator.class);

	private Font bigFont = null;
	private Font mediumFont = null;
	private Font mBold = null;

	public ReportCreator() {
		super();
	}

	@Override
	public void init() throws ServletException {
		LOG.debug("ReportCreator #init");
		try {
			BaseFont baseFont = BaseFont.createFont(System.getProperty("catalina.home") + "\\logs\\fonts\\times.ttf",
					"cp1251", BaseFont.EMBEDDED);
			bigFont = new Font(baseFont, 18);
			mediumFont = new Font(baseFont, 14);
			mBold = new Font(baseFont, 16, Font.BOLD);
		} catch (DocumentException | IOException e) {
			LOG.error("cannot init fonts for ReportCreator");
			throw new ServletException("cannot init fonts for ReportCreator", e);
		}
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute(AttributeNames.USER) == null) {
				LOG.error("U don't have permision to do this!");
				response.sendRedirect(Path.COMMAND_VIEW_ERROR_PAGE + "&errorMessage=ACHTUNG!");
				return;
			}
			createReport(request);

			getReport(response);

		} catch (AppException ex) {
			LOG.error("Err in ReportCreator" + ex.getMessage());
			response.sendRedirect(
					ex.getMessage() != null ? Path.COMMAND_VIEW_ERROR_PAGE + "&errorMessage=" + ex.getMessage()
							: Path.COMMAND_VIEW_ERROR_PAGE);
		}
	}

	public void createReport(HttpServletRequest request) throws AppException {
		Document document = new Document();
		try {
			File file = new File(System.getProperty("catalina.home") + "\\logs\\temp\\iTextHelloWorld.pdf");
			FileOutputStream pdfFileout = new FileOutputStream(file);
			PdfWriter.getInstance(document, pdfFileout);
			document.open();

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(AttributeNames.USER);
			Locale locale;
			if ("ru".equals(user.getLanguage())) {
				locale = new Locale("ru", "RU");
			} else {
				locale = new Locale("en", "EN");
			}
			ResourceBundle rb = ResourceBundle.getBundle("resource", locale);

			createTitle(rb, document, request);

			createTable(rb, document, request);

			document.close();
		} catch (DocumentException | IOException ex) {
			LOG.error(ex.getMessage());
			throw new AppException();
		}
	}

	public void getReport(HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment;filename=" + "testPDF.pdf");
		FileInputStream fis = null;
		try {
			File f = new File(System.getProperty("catalina.home") + "\\logs\\temp\\iTextHelloWorld.pdf");
			fis = new FileInputStream(f);
			DataOutputStream os = new DataOutputStream(response.getOutputStream());
			response.setHeader("Content-Length", String.valueOf(f.length()));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException ex) {
					LOG.error("Error!");
				}
		}
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private void createTitle(ResourceBundle rb, Document document, HttpServletRequest request)
			throws DocumentException, AppException {
		int userId = Integer.parseInt(request.getParameter(ParameterNames.USER_ID));
		User user = null;
		try {
			UserDAO userDAO = UserDAO.getInstance();
			user = userDAO.findById(userId);
		} catch (DBException ex) {
			LOG.error(ex);
			throw new AppException("Cannot get user by id", ex);
		}

		Chunk chunk = new Chunk(rb.getString("report.title"), bigFont);

		Chunk chunk1 = new Chunk(rb.getString("report.student") + " " + user.getLastName() + " " + user.getFirstName(),
				mediumFont);
		String pattern = "EEEEE, d MMMMM yyyy HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, rb.getLocale());
		String date = simpleDateFormat.format(new Date());
		Chunk chunk2 = new Chunk(rb.getString("report.date") + " " + date, mediumFont);

		Paragraph parargaph = new Paragraph(chunk);
		parargaph.setAlignment(Element.ALIGN_CENTER);
		document.add(parargaph);

		parargaph = new Paragraph(chunk1);
		parargaph.setAlignment(Element.ALIGN_CENTER);
		document.add(parargaph);

		parargaph = new Paragraph(chunk2);
		parargaph.setAlignment(Element.ALIGN_CENTER);
		addEmptyLine(parargaph, 2);

		document.add(parargaph);
	}

	private void createTable(ResourceBundle rb, Document document, HttpServletRequest request)
			throws DocumentException, AppException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);

		PdfPCell c1 = new PdfPCell(new Phrase(rb.getString("profile_jsp.subject_name"), bigFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase(rb.getString("profile_jsp.test_name"), bigFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase(rb.getString("profile_jsp.mark"), bigFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase(rb.getString("profile_jsp.test_date"), bigFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		table.setHeaderRows(1);

		try {
			ResultDAO resultDAO = ResultDAO.getInstance();
			int userId = Integer.parseInt(request.getParameter(ParameterNames.USER_ID));
			List<Result> results = resultDAO.findResultsByUserId(userId);
			if (!results.isEmpty()) {
				if (rb.getLocale().getLanguage().equals("ru")) {
					for (Result res : results) {
						table.addCell(new Phrase(res.getSubjectNameRu(), mediumFont));
						table.addCell(new Phrase(res.getTestNameRu(), mediumFont));
						table.addCell(new Phrase(res.getMark().toString(), mediumFont));
						table.addCell(new Phrase(res.getTestDate(), mediumFont));
						table.completeRow();
					}
				} else {
					for (Result res : results) {
						table.addCell(new Phrase(res.getSubjectNameEn(), mediumFont));
						table.addCell(new Phrase(res.getTestNameEn(), mediumFont));
						table.addCell(new Phrase(res.getMark().toString(), mediumFont));
						table.addCell(new Phrase(res.getTestDate(), mediumFont));
						table.completeRow();
					}
				}
			} else {
				document.add(new Paragraph(rb.getString("report.no_results"), mBold));
			}
		} catch (DBException e) {
			LOG.error("cannot generate a table for report");
			throw new AppException("cannot generate a table for report", e);
		}
		document.add(table);
	}
}
