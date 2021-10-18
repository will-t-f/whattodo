import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public class ListItem {
	public String keyword;
	public Date creationDate, dueDate;
	public byte color, mode;
	public String description, when;

	ListItem (boolean novelty, String s) {
		// If it is new, the ListItem will be called with a keyword,
		// otherwise it is called with the full string of the item
		if (novelty) generate_new_li(s); // new is a reserved keyword, btw
		else load_li(s);
	}

	public void edit () {
		System.out.println("Editing " + keyword);
		switch (scanwd("What color?", get_color(color)).toLowerCase()) { // TODO: comments
			case "black": color = 0; break;
			case "red": color = 1; break;
			case "green": color = 2; break;
			case "yellow": color = 3; break;
			case "blue": color = 4; break;
			case "magenta": color = 5; break;
			case "cyan": color = 6; break;
			case "white": color = 7;
		} switch (scanwd("What mode?", get_color((byte) (mode + 10))).toLowerCase()) {
			case "gray": mode = 0; break;
			case "regular": mode = 1; break;
			case "green": mode = 2; break;
			case "red": mode = 3;
		}
		dueDate = get_date_with_default("New Due Date:", dueDate);
		description = scanwd("Description:", description).replace(",", "");
		when = scanwd("When:", when).replace(",", "");
	}

	public String export_string () {
		String return_string = "key" + keyword + ",";
		return_string += "cre" + creationDate.getTime() + ",";
		return_string += "due" + dueDate.getTime() + ",";
		return_string += "col" + String.valueOf(color) + ",";
		return_string += "mod" + String.valueOf(mode) + ",";
		return_string += "des" + description + ",";
		return_string += "whe" + when;
		return return_string;
	}

	private void generate_new_li(String key) {
		keyword = key;
		System.out.println(key);
		creationDate = new Date();
		if (scanwd("Due date 1 day from now?", "y").toLowerCase().equals("y")) {
			dueDate = new Date(creationDate.getTime() + 86400000); // There are 86 400 000 milliseconds in a day.
		} else {
			dueDate = get_date_with_default("Set Due Date:", new Date(creationDate.getTime() + 86400000)); // There are 86 400 000 milliseconds in a day.
		}
		switch (scanwd("What color?", "green").toLowerCase()) {
			case "black": color = 0; break;
			case "red": color = 1; break;
			case "green": color = 2; break;
			case "yellow": color = 3; break;
			case "blue": color = 4; break;
			case "magenta": color = 5; break;
			case "cyan": color = 6; break;
			case "white": color = 7;
		} switch (scanwd("What mode?", "regular").toLowerCase()) {
			case "gray": mode = 0; break;
			case "regular": mode = 1; break;
			case "green": mode = 2; break;
			case "red": mode = 3;
		}
		description = scanwd("Description:", "").replace(",", "");
		when = scanwd("When:", "").replace(",", "");
	}

	private void load_li(String src) {
		keyword = "";
		creationDate = new Date();
		dueDate = new Date();
		color = 2;
		mode = 1;
		description = "";
		when = "";
		String[] argv = src.split(",");
		int argc = argv.length;

		int argn = 0;
		while (argn < argc) {
			if (argv[argn].length() >= 4) {
				String field = argv[argn].substring(0, 3);
				String content = argv[argn].substring(3);
				if (field.equals("key")) keyword = content;
				else if (field.equals("cre")) creationDate = new Date(Long.parseLong(content));
				else if (field.equals("due")) dueDate = new Date(Long.parseLong(content));
				else if (field.equals("col")) color = Byte.parseByte(content);
				else if (field.equals("mod")) mode = Byte.parseByte(content);
				else if (field.equals("des")) description = content;
				else if (field.equals("whe")) when = content;
				else if (field.equals("com")) ; // designated for comments
			}
			argn++;
		}
	}

	public void render (int num, String style) {
		int i = 0;
		while (i < style.length()) {
			switch (style.charAt(i)) {
				case 'c': print_in_color(color, "   "); break;
				case 'C': print_in_color(color, "     "); break;
				case 'd': print_date(dueDate); break;
				case 'e': print_in_color((byte) (mode + 10), description); break;
				case 'E': print_in_color((byte) (mode + 10), "(" + description + ")"); break;
				case 'h': System.out.print("-"); break;
				case 'H': System.out.print(" - "); break;
				case 'i': System.out.print(":"); break;
				case 'I': System.out.print(" : "); break;
				case 'k': print_in_color((byte) (mode + 10), keyword); break;
				case 'K': print_in_color((byte) (mode + 10), "(" + keyword + ")"); break;
				case 'l': System.out.print("|"); break;
				case 'L': System.out.print(" | "); break;
				case 'm': print_date(creationDate); break;
				case 'n': System.out.print(num); break;
				case 'o': System.out.print(mode); break;
				case 's': System.out.print(""); break;
				case 'S': System.out.print(" "); break;
				case 'w': print_in_color((byte) (mode + 10), when); break;
				case 'W': print_in_color((byte) (mode + 10), "(" + when + ")"); break;
			}
			System.out.print(" ");
			i++;
		}
		System.out.println();
	}

	// ----------------------------------------------------------------------------------------------------
	// Get Info

	public String getKeyword () {
		return keyword;
	}

	public Date getCreationDate () {
		return creationDate;
	}

	public Date getDueDate () {
		return dueDate;
	}

	public String getColor () {
		return color + keyword;
	}

	// ----------------------------------------------------------------------------------------------------
	// Utilities

	public static String scanwd (String question, String def) { // scan with default
		Scanner scanner = new Scanner (System.in);
		System.out.print(question + " [" + def + "]: ");
		String return_string = scanner.nextLine();
		return (return_string.equals("")) ? def : return_string;
	}

	public static void print_date (Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.print(simpleDateFormat.format(date));
	}

	public static String string_date (Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	public void print_in_color (byte color, String output) {
		switch (color) {
			case 0: System.out.print(BLACK); break;
			case 1: System.out.print(RED); break;
			case 2: System.out.print(GREEN); break;
			case 3: System.out.print(YELLOW); break;
			case 4: System.out.print(BLUE); break;
			case 5: System.out.print(MAGENTA); break;
			case 6: System.out.print(CYAN); break;
			case 7: System.out.print(WHITE); break;
			case 10: System.out.print(GRAYtext); break;
			case 12: System.out.print(GREENtext); break;
			case 13: System.out.print(REDtext);
		}
		System.out.print(output);
		System.out.print(RESET);
	}

	public String get_color (byte color) {
		switch (color) {
			case 0: return "black";
			case 1: return "red";
			case 2: return "green";
			case 3: return "yellow";
			case 4: return "blue";
			case 5: return "magenta";
			case 6: return "cyan";
			case 7: return "white";
			case 10: return "gray";
			case 11: return "regular";
			case 12: return "green";
			case 13: return "red";
		} return null;
	}

	public static Date get_date_with_default (String question, Date def) {
		System.out.println(question);
		print_date(def);
		LocalDateTime ldt = LocalDateTime.parse(scanwd("\n" + question, string_date(def)).replace(" ", "T"));
		return java.util.Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	// ----------------------------------------------------------------------------------------------------
	// Resources

	public static String RESET = "\u001B[0m";

	public static String BLACK = "\u001B[40m";
	public static String RED = "\u001B[41m";
	public static String GREEN = "\u001B[102m";
	public static String YELLOW = "\u001B[103m";
	public static String BLUE = "\u001B[104m";
	public static String MAGENTA = "\u001B[105m";
	public static String CYAN = "\u001B[106m";
	public static String WHITE = "\u001B[107m";

	public static String REDtext = "\u001B[31m";
	public static String GREENtext = "\u001B[32m";
	public static String GRAYtext = "\u001B[90m";
}
