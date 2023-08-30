package duke.parser;

import duke.DukeException;
import duke.task.*;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;

public class Parser {
    private static String emptyDeadlineDate = "Please specify deadline date using /by\n" +
        "e.g. deadline report /by 2023-12-31";
    private static String emptyEventDate = "Please specify event start and end dates using /from and /to\n" + 
        "e.g. event holiday /from 2023-06-01 /to 2023-06-30";
    private static String invalidDate = "Please provide date with the following format: YYYY-MM-DD";
    private static String invalidEndDate = "Your end date is before start date";

    public static String convertToDMY(String dateStr) throws DukeException {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        } catch (DateTimeException e) {
            throw new DukeException(invalidDate);
        }
    }

    public static List<String> convertToList(String s) {
        String[] queries = s.trim().split("\\s+");
        List<String> queryList = Arrays.asList(queries);
        return queryList;
    }

    public static Task parseFile(String s) throws DukeException {
        String[] q = s.trim().split(",>");
        try {
            switch (q[0]) {
            case "deadline":
                return new Deadline(q[1], q[2].equals("X"), LocalDate.parse(q[3]));
            case "event":
                return new Event(q[1], q[2].equals("X"), LocalDate.parse(q[3]), LocalDate.parse(q[4]));
            case "todo":
                return new ToDo(q[1], q[2].equals("X"));
            }
        } catch (DateTimeException e) {
            throw new DukeException("Error parsing date in file");
        }
        throw new DukeException("Error parsing file data");
    }
    
    public static Deadline parseUserDeadline(List<String> queryList) throws DukeException {
        if (queryList.size() < 2) {
            throw new DukeException("The description of a deadline cannot be empty.");
        }
        String name = "";
        String deadlineString = "";
        int byIndex = -1;
        for (int i = 1; i < queryList.size(); i++) {
            if (queryList.get(i).equals("/by")) {
                byIndex = i;
                continue;
            }
            if (byIndex == -1) {
                name += i > 1 ? " " : "";
                name += queryList.get(i);
            } else {
                deadlineString += i > byIndex + 1 ? " " : "";
                deadlineString += queryList.get(i);
            }
        }
        if (deadlineString.equals("")) {
            throw new DukeException(emptyDeadlineDate);
        }
        try {
            LocalDate deadline = LocalDate.parse(deadlineString);
            return new Deadline(name, deadline);
        } catch (DateTimeException e) {
            throw new DukeException(invalidDate);
        }
    }
    
    public static Event parseUserEvent(List<String> queryList) throws DukeException {
        if (queryList.size() < 2) {
            throw new DukeException("The description of a event cannot be empty.");
        }
        String name = "";
        String fromStr = "", toStr = "";
        int fromIndex = -1, toIndex = -1;
        for (int i = 1; i < queryList.size(); i++) {
            if (queryList.get(i).equals("/from")) {
                fromIndex = i;
                toIndex = -1;
                continue;
            }
            if (queryList.get(i).equals("/to")) {
                toIndex = i;
                fromIndex = -1;
                continue;
            }
            if (fromIndex == -1 && toIndex == -1) {
                name += i > 1 ? " " : "";
                name += queryList.get(i);
            } else if (fromIndex > -1) {
                fromStr += i > fromIndex + 1 ? " " : "";
                fromStr += queryList.get(i);
            } else {
                toStr += i > toIndex + 1 ? " " : "";
                toStr += queryList.get(i);
            }
        }
        if (fromStr.equals("") || toStr.equals("")) {
            throw new DukeException(emptyEventDate);
        }
        try {
            LocalDate from = LocalDate.parse(fromStr);
            LocalDate to = LocalDate.parse(toStr);
            if (from.compareTo(to) > 0) {
                throw new DukeException(invalidEndDate);
            }
            return new Event(name, from, to);
        } catch (DateTimeException e) {
            throw new DukeException(invalidDate);
        }
    }

    public static ToDo parseUserToDo(List<String> queryList) throws DukeException {
        if (queryList.size() < 2) {
            throw new DukeException("The description of a todo cannot be empty.");
        }
        String name = "";
        for (int i = 1; i < queryList.size(); i++) {
            name += i > 1 ? " " : "";
            name += queryList.get(i);
        }
        return new ToDo(name);
    }

    public static String parseUserFind(List<String> queryList) throws DukeException {
        if (queryList.size() < 2) {
            throw new DukeException("What are you trying to find?");
        }
        String keyword = "";
        for (int i = 1; i < queryList.size(); i++) {
            keyword += i > 1 ? " " : "";
            keyword += queryList.get(i);
        }
        return keyword;
    }
}
