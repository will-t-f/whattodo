import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Main {
	public static String app_name, default_list, input, file_prefix, output, style, version;
	public static ArrayList<String> items = new ArrayList<String>();
	public static ArrayList<ListItem> todo_list = new ArrayList<ListItem>();
	public static char sortMode;
	public static boolean verbose;

	public static void exit (String message) {
		System.out.println("!!! " + message + " !!!");
		System.exit(1);
	}

	public static void message (String message) {
		if (verbose) System.out.println(message);
	}

	public static int keyword_used (String key) {
		message("Comparing keywords...");
		for (int i = 0; i < todo_list.size(); i++) if (todo_list.get(i).keyword.equals(key)) return i;
		return -1;
	}

	public static void main (String[] args) {
		app_name = "whattodo";
		file_prefix = System.getProperty("user.home") + "/.config/" + app_name + "/";
		default_list = "main.todo";
		input = file_prefix + default_list;
		output = file_prefix + default_list;
		version = "0.0.1";
		sortMode = 'K'; // This means sort by keywords ascending
		style = "kie";
		verbose = false;

		parse_args(args);

		String[] file_content = open_file(input);
		for (int i = 0; i < file_content.length; i++) todo_list.add(new ListItem(false, file_content[i]));

		if (sortMode == 'c') todo_list.sort(Comparator.comparing(ListItem::getColor).reversed());
		else if (sortMode == 'C') todo_list.sort(Comparator.comparing(ListItem::getColor));
		else if (sortMode == 'd') todo_list.sort(Comparator.comparing(ListItem::getDueDate).reversed());
		else if (sortMode == 'D') todo_list.sort(Comparator.comparing(ListItem::getDueDate));
		else if (sortMode == 'k') todo_list.sort(Comparator.comparing(ListItem::getKeyword).reversed());
		else if (sortMode == 'K') todo_list.sort(Comparator.comparing(ListItem::getKeyword));
		else if (sortMode == 'm') todo_list.sort(Comparator.comparing(ListItem::getCreationDate).reversed());
		else if (sortMode == 'M') todo_list.sort(Comparator.comparing(ListItem::getCreationDate));

		if (items.size() == 0) {
			for (int i = 0; i < todo_list.size(); i++) todo_list.get(i).render(i + 1, style);
		} else {
			for (int i = 0; i < items.size(); i++) {
				int loc = keyword_used(items.get(i));
				if (loc != -1) {
					if (!ListItem.scanwd("Delete or edit " + items.get(i) + "?", "edit").toLowerCase().equals("delete")) {
						todo_list.get(loc).edit();
					} else todo_list.remove(loc);
				} else todo_list.add(new ListItem(true, items.get(i)));
			}

			String[] export_strings = new String[todo_list.size()];
			for (int i = 0; i < todo_list.size(); i++) export_strings[i] = todo_list.get(i).export_string();
			write_file(output, export_strings);
		}
	}

	public static void parse_args(String[] args) {
		message("Parsing Arguments.");
		if (args.length == 0) return;
		else {
			int current_argument = 0;
			while (current_argument < args.length) {
				if (args[current_argument].equals("--help")) {
					System.out.println("whattodo Help Menu:");
					System.out.println("--help    : show this help menu");
					System.out.println("--input   : specify list input");
					System.out.println("--item    : explicity state next argument is an item (most arguments are items unless otherwise stated)");
					System.out.println("--output  : specify list output");
					System.out.println("--verbose : turn on verbose mode");

					System.out.println("Style Options:");
					System.out.println("-c color three columns wide");
					System.out.println("-C color five columns wide");
					System.out.println("-d print due date field");
					System.out.println("-e print description field");
					System.out.println("-E print description field in parenthesis");
					System.out.println("-h print \"-\"");
					System.out.println("-H print \" - \"");
					System.out.println("-i print \":\"");
					System.out.println("-I print \" : \"");
					System.out.println("-k print keyword field");
					System.out.println("-K print keyword field in parenthesis");
					System.out.println("-l print \"|\"");
					System.out.println("-L print \" | \"");
					System.out.println("-m print creation date (for make)");
					System.out.println("-n print number on the list");
					System.out.println("-o print mode field");
					System.out.println("-s print \"\"");
					System.out.println("-S print \" \"");
					System.out.println("-w print when field");
					System.out.println("-W print when field in parenthesis");
					System.exit(0);
				} else if (args[current_argument].equals("--input")) {
					if (++current_argument >= args.length) exit("Input flag was called, but no input was given.");
					else input = args[current_argument];
				} else if (args[current_argument].equals("--item")) {
					if (++current_argument >= args.length) exit("Item flag was called, but no item was given.");
					else items.add(args[current_argument]);
				} else if (args[current_argument].equals("--output")) {
					if (++current_argument >= args.length) exit("Output flag was called, but no output was given.");
					else output = args[current_argument];
				} else if (args[current_argument].equals("--sort")) {
					if (++current_argument >= args.length) exit("Sort flag was called, but no sort mode was given.");
					else sortMode = args[current_argument].charAt(0);
				} else if (args[current_argument].equals("--verbose")) {
					verbose = true;
				} else if (args[current_argument].charAt(0) == '-') {
					style = args[current_argument].substring(1);
				} else items.add(args[current_argument]);
				current_argument++;
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Utilities

	public static String[] open_file(String file_location) {
		message("Opening File " + file_location);
		File file;
		Scanner scanner;
		ArrayList<String> return_array = new ArrayList<String>();

		try {
			file = new File(file_location);
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) return_array.add(scanner.nextLine());
		} catch (Exception e) {
			exit(file_location + " not found");
		}

		String[] return_strings = new String[return_array.size()];
		for (int i = 0; i < return_strings.length; i++) return_strings[i] = return_array.get(i);

		return return_strings;
	}

	public static void write_file(String file_location, String[] content) {
		message("Writing to " + file_location);
		try {
			FileWriter file = new FileWriter(file_location);
			for (int i = 0; i < content.length; i++) file.write(content[i] + "\n");
			file.close();
		} catch (Exception e) {
			exit("An error ocurred writing to " + file_location);
		}
	}
}
