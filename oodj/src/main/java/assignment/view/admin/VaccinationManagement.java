package assignment.view.admin;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

/**
 *
 * @author PAVILION
 */
public class VaccinationManagement extends javax.swing.JPanel {

    /**
     * Creates new form contentPane
     */
    public VaccinationManagement() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jScrollPane = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        inputForm = new javax.swing.JPanel();
        txtVcnId = new javax.swing.JLabel();
        txtVacName = new javax.swing.JLabel();
        inputVacId = new javax.swing.JTextField();
        txtVacDose = new javax.swing.JLabel();
        inputCentreId = new javax.swing.JTextField();
        txtVacDoseInterval = new javax.swing.JLabel();
        inputQty = new javax.swing.JTextField();
        inputId = new javax.swing.JTextField();
        txtVacDoseInterval1 = new javax.swing.JLabel();
        txtVacDoseInterval2 = new javax.swing.JLabel();
        startDatePicker = new com.github.lgooddatepicker.components.DatePicker();
        startTimePicker = new com.github.lgooddatepicker.components.TimePicker();
        endDatePicker = new com.github.lgooddatepicker.components.DatePicker();
        endTimePicker = new com.github.lgooddatepicker.components.TimePicker();
        btnAddVcn = new javax.swing.JButton();
        btnEditVcn = new javax.swing.JButton();
        btnRemoveVcn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        txtSearch = new javax.swing.JLabel();
        inputSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        title = new javax.swing.JPanel();
        jSeparator = new javax.swing.JSeparator();
        lblTitle = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(960, 720));

        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setBorder(null);
        jScrollPane.setPreferredSize(new java.awt.Dimension(880, 720));
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(960, 1000));

        inputForm.setBackground(new java.awt.Color(255, 255, 255));
        inputForm.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(60, 63, 65), 1, true));
        inputForm.setPreferredSize(new java.awt.Dimension(250, 540));

        txtVcnId.setText("Vaccination ID");
        txtVcnId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVcnId.setForeground(new java.awt.Color(60, 63, 65));

        txtVacName.setText("Vaccine ID");
        txtVacName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVacName.setForeground(new java.awt.Color(60, 63, 65));

        inputVacId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtVacDose.setText("Centre ID");
        txtVacDose.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVacDose.setForeground(new java.awt.Color(60, 63, 65));

        inputCentreId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtVacDoseInterval.setText("Quantity");
        txtVacDoseInterval.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVacDoseInterval.setForeground(new java.awt.Color(60, 63, 65));

        inputQty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        inputId.setEditable(false);
        inputId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtVacDoseInterval1.setText("Start Date");
        txtVacDoseInterval1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVacDoseInterval1.setForeground(new java.awt.Color(60, 63, 65));

        txtVacDoseInterval2.setText("End Date");
        txtVacDoseInterval2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVacDoseInterval2.setForeground(new java.awt.Color(60, 63, 65));

        javax.swing.GroupLayout inputFormLayout = new javax.swing.GroupLayout(inputForm);
        inputForm.setLayout(inputFormLayout);
        inputFormLayout.setHorizontalGroup(
            inputFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputFormLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(inputFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtVcnId)
                        .addComponent(txtVacName)
                        .addComponent(inputVacId)
                        .addComponent(txtVacDose)
                        .addComponent(inputCentreId)
                        .addComponent(txtVacDoseInterval)
                        .addComponent(inputQty)
                        .addComponent(inputId)
                        .addComponent(txtVacDoseInterval1)
                        .addComponent(txtVacDoseInterval2)
                        .addComponent(startDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(endDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(endTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(startTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );
        inputFormLayout.setVerticalGroup(
            inputFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputFormLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(txtVcnId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVacName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputVacId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVacDose)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputCentreId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVacDoseInterval)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVacDoseInterval1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtVacDoseInterval2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(endTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        btnAddVcn.setText("Add Vaccination");
        btnAddVcn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnEditVcn.setText("Edit Vaccination");
        btnEditVcn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnRemoveVcn.setText("Remove Vaccination");
        btnRemoveVcn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        inventoryTable.setModel(new javax.swing.table.DefaultTableModel() {
        });
        inventoryTable.setFont(new java.awt.Font("Cantarell", 0, 18)); // NOI18N
        inventoryTable.setRowHeight(24);
        inventoryTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(inventoryTable);

        txtSearch.setText("Search Vaccinations:");
        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(60, 63, 65));

        inputSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnSearch.setText("Search");
        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearch)
                            .addComponent(inputSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnEditVcn, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddVcn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnRemoveVcn, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(inputForm, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddVcn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditVcn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRemoveVcn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(241, Short.MAX_VALUE))
        );

        jScrollPane.setViewportView(jPanel2);

        title.setBackground(new java.awt.Color(255, 255, 255));
        title.setPreferredSize(new java.awt.Dimension(960, 70));

        jSeparator.setPreferredSize(new java.awt.Dimension(960, 3));

        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTitle.setText("Manage Vaccination");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(79, 79, 79));
        lblTitle.setInheritsPopupMenu(false);
        lblTitle.setPreferredSize(new java.awt.Dimension(960, 22));

        javax.swing.GroupLayout titleLayout = new javax.swing.GroupLayout(title);
        title.setLayout(titleLayout);
        titleLayout.setHorizontalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titleLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 827, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        titleLayout.setVerticalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        title.getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    public void setTable(javax.swing.table.DefaultTableModel m) {
        inventoryTable.setModel(m);
    }

    public javax.swing.table.TableModel getTableModel() {
        return inventoryTable.getModel();
    }

    public javax.swing.JTable getTable() {
        return inventoryTable;
    }

    public Object[] getFields() {
      Object[] fields = {inputId, inputVacId, inputCentreId, inputQty,
                         startDatePicker, startTimePicker, endDatePicker, endTimePicker};
      return fields;
    }

    public javax.swing.JTextField[] getTextField() {
      javax.swing.JTextField[] textFields = { inputId, inputVacId, inputCentreId, inputQty };
      return textFields;
    }

    public DatePicker[] getDatePicker() {
      DatePicker[] d = { startDatePicker, endDatePicker };
      return d;
    }

    public TimePicker[] getTimePicker() {
      TimePicker[] t = { startTimePicker, endTimePicker };
      return t;
    }

    public String getSearchField() {
      return inputSearch.getText();
    }

    //controller
    public void addInventoryTableMouseListener(java.awt.event.MouseListener l) {
        inventoryTable.addMouseListener(l);
    }

    public void addTableAddListener(java.awt.event.ActionListener l) {
        btnAddVcn.addActionListener(l);
    }

    public void addTableEditListener(java.awt.event.ActionListener l) {
        btnEditVcn.addActionListener(l);
    }
    
    public void addTableDeleteListener(java.awt.event.ActionListener l) {
        btnRemoveVcn.addActionListener(l);
    }

    public void addSearchListener(java.awt.event.ActionListener l) {
      btnSearch.addActionListener(l);
    }

    public void addSearchDocumentListener(javax.swing.event.DocumentListener l) {
      inputSearch.getDocument().addDocumentListener(l);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddVcn;
    private javax.swing.JButton btnEditVcn;
    private javax.swing.JButton btnRemoveVcn;
    private javax.swing.JButton btnSearch;
    private com.github.lgooddatepicker.components.DatePicker endDatePicker;
    private com.github.lgooddatepicker.components.TimePicker endTimePicker;
    private javax.swing.JTextField inputCentreId;
    private javax.swing.JPanel inputForm;
    private javax.swing.JTextField inputId;
    private javax.swing.JTextField inputQty;
    private javax.swing.JTextField inputSearch;
    private javax.swing.JTextField inputVacId;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JLabel lblTitle;
    private com.github.lgooddatepicker.components.DatePicker startDatePicker;
    private com.github.lgooddatepicker.components.TimePicker startTimePicker;
    private javax.swing.JPanel title;
    private javax.swing.JLabel txtSearch;
    private javax.swing.JLabel txtVacDose;
    private javax.swing.JLabel txtVacDoseInterval;
    private javax.swing.JLabel txtVacDoseInterval1;
    private javax.swing.JLabel txtVacDoseInterval2;
    private javax.swing.JLabel txtVacName;
    private javax.swing.JLabel txtVcnId;
    // End of variables declaration//GEN-END:variables
}
