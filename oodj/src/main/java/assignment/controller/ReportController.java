package assignment.controller;

import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.IOException;
import java.util.zip.*;
import java.nio.file.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.statistics.HistogramDataset;

import assignment.model.Model;
import assignment.model.models.AppointmentModel;
import assignment.model.models.UserModel;
import assignment.model.types.*;
import assignment.view.View;
import assignment.view.admin.Report;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;

public class ReportController {
    private Model model;
    private View view;

    private ChartPanel genderBox;
    private ChartPanel ageHist;
    private ChartPanel vacStatusBox;
    private ChartPanel vcnVacBox;
    private ChartPanel vcnVccBox;

    public ReportController(Model m, View v){
      model = m;
      view = v;
      Report r = view.getAdminPanel().getReport();

      r.addButtonListener(zipFile());

      createCharts();
    }
    private void createCharts() {
      Report r = view.getAdminPanel().getReport();

      genderBox = createGenderPieChart();
      ageHist = createAgeHistogram();
      vacStatusBox = createVacStatusBarChart();
      vcnVacBox = createVcnVacBarChart();
      vcnVccBox = createVcnVccBarChart();

      r.getGenderBox().add(genderBox);
      r.getAgeHist().add(ageHist);
      r.getVacStatusBox().add(vacStatusBox);
      r.getVcnVacBox().add(vcnVacBox);
      r.getVcnVccBox().add(vcnVccBox);
    }

    public void regen() {
      Report r = view.getAdminPanel().getReport();
      r.getGenderBox().remove(genderBox);
      r.getAgeHist().remove(ageHist);
      r.getVacStatusBox().remove(vacStatusBox);
      r.getVcnVacBox().remove(vcnVacBox);
      r.getVcnVccBox().remove(vcnVccBox);
      createCharts();
    }

    private ChartPanel createGenderPieChart() {
      JFreeChart chart = ChartFactory.createPieChart(
        "Comparison of User Gender Groups",
        createGenderPieChartDataSet(),
        true, false, false
      );
      PiePlot plot = (PiePlot) chart.getPlot();
      plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}"));
      chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
      ChartPanel panel = new ChartPanel(chart);
      panel.setPreferredSize(new java.awt.Dimension(400, 300));
      return panel;
    }

    private PieDataset<String> createGenderPieChartDataSet() {
      DefaultPieDataset<String> df = new DefaultPieDataset<>();
      ArrayList<User> users = UserModel.getUsers();

      int m = 0, f = 0;
      for (int i = 0; i < users.size(); i++) {
        User u = users.get(i);
        if (u.getGender().equals(User.M)) {
          m++;
        } else {
          f++;
        }
      }

      int pMale = Math.round(Float.valueOf(m)/(m+f)*100);
      int pFemale = Math.round(Float.valueOf(f)/(m+f)*100);
      df.setValue("Female", pFemale);
      df.setValue("Male", pMale);
      return df;
    }

    private ChartPanel createAgeHistogram() {
      JFreeChart chart = ChartFactory.createHistogram(
        "Age Distribution of People Vaccinated",
        "Age",
        "Number of People",
        createAgeHistogramDataSet()
      );
      chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
      ChartPanel panel = new ChartPanel(chart);
      panel.setPreferredSize(new java.awt.Dimension(400, 300));
      return panel;
    }

    private HistogramDataset createAgeHistogramDataSet() {
      HistogramDataset df = new HistogramDataset();
      ArrayList<User> users = UserModel.getUsers();
      ArrayList<User> vacUsers = new ArrayList<>();

      for (int i = 0; i < users.size(); i++) {
        User u = users.get(i);
        if(u.getVacStatus().equals(User.VAC_FULL) || u.getVacStatus().equals(User.VAC_PARTIAL)){
          vacUsers.add(u);
        }
      }

      double[] ages = new double[vacUsers.size()];

      for (int i = 0; i < vacUsers.size(); i++) {
        User u = vacUsers.get(i);
        long age = ChronoUnit.YEARS.between(u.getBirthdate(), LocalDateTime.now());
        ages[i] = (double)age;
      }

      df.addSeries("Age", ages, 10);
      return df;
    }

    private ChartPanel createVacStatusBarChart() {
      JFreeChart chart = ChartFactory.createBarChart(
        "Comparison of Vaccination Status",
        "Vaccination Status",
        "Number of People",
        createVacStatusBarChartDataSet(),
        PlotOrientation.HORIZONTAL,
        true, false, false
      );
      chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
      ChartPanel panel = new ChartPanel(chart);
      panel.setPreferredSize(new java.awt.Dimension(400, 300));
      return panel;
    }

    private CategoryDataset createVacStatusBarChartDataSet() {
      DefaultCategoryDataset df = new DefaultCategoryDataset();
      ArrayList<User> users = UserModel.getUsers();

      int unvac = 0, partial = 0, full = 0;
      for (int i = 0; i < users.size(); i++) {
        User u = users.get(i);
        switch (u.getVacStatus()) {
          case User.VAC_UNVAC: unvac++; break;
          case User.VAC_PARTIAL: partial++; break;
          case User.VAC_FULL: full++; break;
        }
      }

      df.addValue(unvac, "Unvaccinated", "");
      df.addValue(partial, "Partially Vaccinated", "");
      df.addValue(full, "Fully Vaccinated", "");
      return df;
    }

    private ChartPanel createVcnVacBarChart() {
      JFreeChart chart = ChartFactory.createBarChart(
        "Comparison of Vaccination Count by Vaccine",
        "Vaccine",
        "Number of Vaccinations",
        createVcnVacBarChartDataSet(),
        PlotOrientation.VERTICAL,
        true, false, false
      );
      chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
      ChartPanel panel = new ChartPanel(chart);
      panel.setPreferredSize(new java.awt.Dimension(400, 300));
      return panel;
    }

    private CategoryDataset createVcnVacBarChartDataSet() {
      DefaultCategoryDataset df = new DefaultCategoryDataset();
      ArrayList<Appointment> appts = AppointmentModel.getAppointments();

      Map<String, Integer> map = new HashMap<>();

      for (int i = 0; i < appts.size(); i++) {
        Appointment a = appts.get(i);
        if (a.getStatus().equals(Appointment.APPT_FINISHED)) {
          String name = a.getVaccination().getVaccine().getName();
          if (!map.containsKey(name)) {
            map.put(name, 1);
            continue;
          }
          map.put(name, map.get(name) + 1);
        }
      }

      for (Map.Entry<String, Integer> i: map.entrySet()) {
        df.addValue(i.getValue(), i.getKey(), "");
      }

      return df;
    }

    private ChartPanel createVcnVccBarChart() {
      JFreeChart chart = ChartFactory.createBarChart(
        "Comparison of Vaccination Count by Vaccine Centre",
        "Vaccine Centre",
        "Number of Vaccinations",
        createVcnVccBarChartDataSet(),
        PlotOrientation.VERTICAL,
        true, false, false
      );
      chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
      ChartPanel panel = new ChartPanel(chart);
      panel.setPreferredSize(new java.awt.Dimension(400, 300));
      return panel;
    }

    private CategoryDataset createVcnVccBarChartDataSet() {
      DefaultCategoryDataset df = new DefaultCategoryDataset();
      ArrayList<Appointment> appts = AppointmentModel.getAppointments();

      Map<String, Integer> map = new HashMap<>();

      for (int i = 0; i < appts.size(); i++) {
        Appointment a = appts.get(i);
        if (a.getStatus().equals(Appointment.APPT_FINISHED)) {
          String name = a.getVaccination().getVacCentre().getName();
          if (!map.containsKey(name)) {
            map.put(name, 1);
            continue;
          }
          map.put(name, map.get(name) + 1);
        }
      }

      for (Map.Entry<String, Integer> i: map.entrySet()) {
        df.addValue(i.getValue(), i.getKey(), "");
      }

      return df;
    }
    
    // https://stackoverflow.com/questions/15968883/how-to-zip-a-folder-itself-using-java
    public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
      File f = new File(zipFilePath);
      if (f.exists()) f.delete();
      Path p = Files.createFile(Paths.get(zipFilePath));
      try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
          Path pp = Paths.get(sourceDirPath);
          Files.walk(pp)
            .filter(path -> !Files.isDirectory(path))
            .forEach(path -> {
                ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                try {
                    zs.putNextEntry(zipEntry);
                    Files.copy(path, zs);
                    zs.closeEntry();
              } catch (IOException e) {
                  System.err.println(e);
              }
            });
      }
    }
    
    private ActionListener zipFile() {
      return new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          
          JFileChooser chooser = new JFileChooser(); 
          chooser.setCurrentDirectory(new java.io.File("~"));
          chooser.setDialogTitle("Select folder to save data to");
          chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          chooser.setAcceptAllFileFilterUsed(false);

          if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
            String saveDir = chooser.getSelectedFile().toString() + "/data.zip";
            try {
              pack("./src/main/resources/data", saveDir);
              JOptionPane.showMessageDialog(
                  null, "Data Exported");
            } catch(Exception e) {
              System.err.println(e.getMessage());
              throw new RuntimeException(e);
            }
          }
        }
      };
   }
}

