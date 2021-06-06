package ua.epam.pavelchuk.final_project.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;


public class PaginationTag extends SimpleTagSupport {

	private String command;
	private String page;
	private String orderBy;
	private String direction;
	private String lines;

	public void setLines(String lines) {
		this.lines = lines;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setPage(String page) {
		this.page = page;
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.println("<div id=\"pageLeftFooter\">");
		out.println("<a href=\"controller?command=");
		out.print(command);
		out.print("&page=");
		out.print(page);
		out.print("&lines=");
		out.print(5);
		out.print("&orderBy=");
		out.print(orderBy);
		out.print("&direction=");
		out.print(direction);
		out.print("\" class=\"bot1g\">x5</a> ");
		
		out.println("<a href=\"controller?command=");
		out.print(command);
		out.print("&page=");
		out.print(page);
		out.print("&lines=");
		out.print(10);
		out.print("&orderBy=");
		out.print(orderBy);
		out.print("&direction=");
		out.print(direction);
		out.print("\" class=\"bot1g\">x10</a> ");
		
		out.println("<a href=\"controller?command=");
		out.print(command);
		out.print("&page=");
		out.print(page);
		out.print("&lines=");
		out.print(30);
		out.print("&orderBy=");
		out.print(orderBy);
		out.print("&direction=");
		out.print(direction);
		out.print("\" class=\"bot1g\">x30</a> ");
		
		out.println("</div>");
		out.println("<div id=\"pageRightFooter\">");
		
		out.println("<a href=\"controller?command=");
		out.print(command);
		out.print("&page=");
		out.print(page.equals("") ? 1 :(Integer.parseInt(page)-1));
		out.print("&lines=");
		out.print(lines);
		out.print("&orderBy=");
		out.print(orderBy);
		out.print("&direction=");
		out.print(direction);
		out.println("\" class=\"bot1g\">&lt;</a>");
		
		out.println("&nbsp;");
		out.println(page);
		out.println("&nbsp;");
		
		out.println("<a href=\"controller?command=");
		out.print(command);
		out.print("&page=");
		out.print(page.equals("") ? 1 :(Integer.parseInt(page)+1));
		out.print("&lines=");
		out.print(lines);
		out.print("&orderBy=");
		out.print(orderBy);
		out.print("&direction=");
		out.print(direction);
		out.println("\" class=\"bot1g\">&gt;</a>");
		
		out.println("</div>");
	}
}