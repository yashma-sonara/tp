package seedu.teachstack.logic.commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import seedu.teachstack.logic.commands.exceptions.CommandException;
import seedu.teachstack.model.Model;
import seedu.teachstack.model.person.Grade;
import seedu.teachstack.model.person.Person;
import java.util.DoubleSummaryStatistics;



public class SummaryCommand extends Command{
    public static final String COMMAND_WORD = "summary";
    public static final String MESSAGE_SUCCESS = "Total Students: %d \nMean Grade: %s \nGrade "
            + "Standard Deviation: %.2f";

    public SummaryCommand() {

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        ObservableList<Person> students = model.getFilteredPersonList();
        int totalStudents = students.size();

        DoubleSummaryStatistics stats = students.stream()
                .mapToDouble(student -> student.getGrade().gradeToInt())
                .summaryStatistics();

        int meanGrade = (int) stats.getAverage();
        String mean = Grade.intToGrade(meanGrade);
        double standardDeviation = calculateStandardDeviation(students, meanGrade);

        String summaryMessage = String.format(MESSAGE_SUCCESS, totalStudents, mean, standardDeviation);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        extracted(students, pieChartData);

        // Create the pie chart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Grade Distribution");

        // Display the pie chart in a layout pane
        VBox chartLayout = new VBox(pieChart);
        // Return CommandResult with summary message
        return new CommandResult(summaryMessage, false, false, true, chartLayout);

    }

    private void extracted(ObservableList<Person> students, ObservableList<PieChart.Data> pieChartData) {
        pieChartData.add(new PieChart.Data("A+", countStudentsWithGrade(students, "A+")));
        pieChartData.add(new PieChart.Data("A", countStudentsWithGrade(students, "A")));
        pieChartData.add(new PieChart.Data("A-", countStudentsWithGrade(students, "A-")));
        pieChartData.add(new PieChart.Data("B+", countStudentsWithGrade(students, "B+")));
        pieChartData.add(new PieChart.Data("B", countStudentsWithGrade(students, "B")));
        pieChartData.add(new PieChart.Data("B-", countStudentsWithGrade(students, "B-")));
        pieChartData.add(new PieChart.Data("C+", countStudentsWithGrade(students, "C+")));
        pieChartData.add(new PieChart.Data("C", countStudentsWithGrade(students, "C")));
        pieChartData.add(new PieChart.Data("C-", countStudentsWithGrade(students, "C-")));
        pieChartData.add(new PieChart.Data("D+", countStudentsWithGrade(students, "D+")));
        pieChartData.add(new PieChart.Data("D", countStudentsWithGrade(students, "D")));
        pieChartData.add(new PieChart.Data("D-", countStudentsWithGrade(students, "D-")));
        pieChartData.add(new PieChart.Data("F", countStudentsWithGrade(students, "F")));
    }

    private double calculateStandardDeviation(ObservableList<Person> students, double meanGrade) {
        double variance = students.stream()
                .mapToDouble(student -> {
                    double diff = student.getGrade().gradeToInt() - meanGrade;
                    return diff * diff;
                })
                .average()
                .orElse(0);

        return Math.sqrt(variance);
    }

    private int countStudentsWithGrade(ObservableList<Person> students, String grade) {
        return (int) students.stream()
                .filter(student -> student.getGrade().toString().equals(grade))
                .count();
    }
}