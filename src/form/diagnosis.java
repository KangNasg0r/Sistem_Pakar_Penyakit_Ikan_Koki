/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import koneksi.koneksi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Ahmad Nur Latif P
 */
public class diagnosis extends javax.swing.JFrame {

    private Connection conn = new koneksi().connect();
    private DefaultTableModel model;
    private String kodePenyakitHasil = "";
    private ArrayList<KemungkinanDiagnosa> listKemungkinan = new ArrayList<>();

    /**
     * Creates new form data_penyakit
     */
    public diagnosis() {
        initComponents();
        aktif();
        kosong();
        tampilGejala();
        autonumber();
    }

    protected void aktif() {
        nama_ikan.requestFocus();
        id_diagnosa.setEditable(false);
        hasil.setEditable(false);
        solusi.setEditable(false);
    }

    protected void kosong() {
        id_diagnosa.setText("");
        nama_ikan.setText("");
        hasil.setText("");
        solusi.setText("");
        tanggal.setDate(null);
    }

    protected void autonumber() {
        try {
            String sql = "SELECT id_diagnosa FROM diagnosa ORDER BY id_diagnosa DESC LIMIT 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String kodeDiagnosa = rs.getString("id_diagnosa").substring(1);
                int nomor = Integer.parseInt(kodeDiagnosa) + 1;
                String nol = "";
                if (nomor < 10) {
                    nol = "00";
                } else if (nomor < 100) {
                    nol = "0";
                } else {
                    nol = "";
                }
                id_diagnosa.setText("D" + nol + nomor);
            } else {
                id_diagnosa.setText("D001");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nomor Otomatis Gagal: " + e);
        }
    }

    private void tampilGejala() {
        model = new DefaultTableModel(
                new Object[]{"Pilih", "Kode Gejala", "Nama Gejala"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        try {
            Connection conn = new koneksi().connect();
            String cari = txtcari.getText();
            String sql = "SELECT kode_gejala, nama_gejala FROM gejala "
                    + "WHERE kode_gejala LIKE ? "
                    + "OR nama_gejala LIKE ? "
                    + "ORDER BY kode_gejala ASC";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + cari + "%");
            pst.setString(2, "%" + cari + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    false,
                    rs.getString("kode_gejala"),
                    rs.getString("nama_gejala")
                });
            }

            tabel_pilih_gejala.setModel(model);
            tabel_pilih_gejala.setRowHeight(35);
            tabel_pilih_gejala.getTableHeader().setFont(
                    new java.awt.Font("Tahoma", java.awt.Font.BOLD, 18)
            );
            tabel_pilih_gejala.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabel_pilih_gejala.getColumnModel().getColumn(0).setMaxWidth(60);
            tabel_pilih_gejala.getColumnModel().getColumn(1).setPreferredWidth(60);
            tabel_pilih_gejala.getColumnModel().getColumn(2).setPreferredWidth(650);
            TextAreaRenderer renderer = new TextAreaRenderer();
            tabel_pilih_gejala.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tabel_pilih_gejala.getColumnModel().getColumn(2).setCellRenderer(renderer);
            tabel_pilih_gejala.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan gejala: " + e.getMessage());
        }
    }

    public javax.swing.JPanel getMainPanel() {
        return panel_diagnosis;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_diagnosis = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        id_diagnosa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        diagnosis = new javax.swing.JButton();
        bsimpan = new javax.swing.JButton();
        bbatal = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        panel_gejala_rule = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabel_pilih_gejala = new javax.swing.JTable();
        txtcari = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nama_ikan = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        bkembali = new javax.swing.JButton();
        tanggal = new org.jdesktop.swingx.JXDatePicker();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        hasil = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        solusi = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel_diagnosis.setBackground(new java.awt.Color(69, 46, 90));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DIAGNOSIS PENYAKIT");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 0, 3, 0, new java.awt.Color(255, 255, 255)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("ID Diagnosa");

        id_diagnosa.setBackground(java.awt.SystemColor.controlHighlight);
        id_diagnosa.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Nama Ikan");

        diagnosis.setBackground(new java.awt.Color(51, 153, 0));
        diagnosis.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        diagnosis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Update_1.png"))); // NOI18N
        diagnosis.setText("MULAI DIAGNOSIS");
        diagnosis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagnosisActionPerformed(evt);
            }
        });

        bsimpan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bsimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/simpan.png"))); // NOI18N
        bsimpan.setText("SIMPAN");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
            }
        });

        bbatal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bbatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/cancel.png"))); // NOI18N
        bbatal.setText("BATAL");
        bbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbatalActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        panel_gejala_rule.setBackground(new java.awt.Color(255, 255, 255));
        panel_gejala_rule.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3), "Pilih Gejala Yang Dialami Ikan", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        tabel_pilih_gejala.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabel_pilih_gejala.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tabel_pilih_gejala);

        txtcari.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcariKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcariKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("CARI :");

        javax.swing.GroupLayout panel_gejala_ruleLayout = new javax.swing.GroupLayout(panel_gejala_rule);
        panel_gejala_rule.setLayout(panel_gejala_ruleLayout);
        panel_gejala_ruleLayout.setHorizontalGroup(
            panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_gejala_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(panel_gejala_ruleLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_gejala_ruleLayout.setVerticalGroup(
            panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_gejala_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText(":");

        nama_ikan.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setText(":");

        bkembali.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bkembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/arrow_undo.png"))); // NOI18N
        bkembali.setText("KEMBALI");
        bkembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkembaliActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("Tanggal");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText(":");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_gejala_rule, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(diagnosis)
                        .addGap(357, 357, 357)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(id_diagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bkembali))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(nama_ikan, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(id_diagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(bkembali))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nama_ikan, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_gejala_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(diagnosis)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bbatal)
                        .addComponent(bsimpan)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Hasil Diagnosis");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText(":");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Solusi");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText(":");

        hasil.setBackground(java.awt.SystemColor.controlHighlight);
        hasil.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        solusi.setBackground(java.awt.SystemColor.controlHighlight);
        solusi.setColumns(20);
        solusi.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        solusi.setRows(5);
        jScrollPane1.setViewportView(solusi);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Penyakit");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hasil)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel6)
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel13))
                .addGap(14, 14, 14)
                .addComponent(hasil, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_diagnosisLayout = new javax.swing.GroupLayout(panel_diagnosis);
        panel_diagnosis.setLayout(panel_diagnosisLayout);
        panel_diagnosisLayout.setHorizontalGroup(
            panel_diagnosisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_diagnosisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_diagnosisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_diagnosisLayout.setVerticalGroup(
            panel_diagnosisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_diagnosisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_diagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_diagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void diagnosisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diagnosisActionPerformed
        try {
            String gejalaDipilih = "";
            int jumlahDipilih = 0;
            kodePenyakitHasil = "";
            listKemungkinan.clear();
            for (int i = 0; i < tabel_pilih_gejala.getRowCount(); i++) {
                Boolean cek = (Boolean) tabel_pilih_gejala.getValueAt(i, 0);

                if (cek != null && cek == true) {
                    String kodeGejala = tabel_pilih_gejala.getValueAt(i, 1).toString();
                    gejalaDipilih += "'" + kodeGejala + "',";
                    jumlahDipilih++;
                }
            }
            if (jumlahDipilih == 0) {
                JOptionPane.showMessageDialog(this, "Silakan pilih minimal satu gejala.");
                return;
            }
            gejalaDipilih = gejalaDipilih.substring(0, gejalaDipilih.length() - 1);

            Connection conn = new koneksi().connect();

            String sql
                    = "SELECT r.kode_rule, p.kode_penyakit, p.nama_penyakit, p.solusi, "
                    + "COUNT(rd.kode_gejala) AS jumlah_cocok, "
                    + "(SELECT COUNT(*) FROM rule_detail WHERE kode_rule = r.kode_rule) AS jumlah_rule "
                    + "FROM rule r "
                    + "JOIN penyakit p ON r.kode_penyakit = p.kode_penyakit "
                    + "JOIN rule_detail rd ON r.kode_rule = rd.kode_rule "
                    + "WHERE rd.kode_gejala IN (" + gejalaDipilih + ") "
                    + "GROUP BY r.kode_rule, p.kode_penyakit, p.nama_penyakit, p.solusi "
                    + "HAVING jumlah_cocok = jumlah_rule "
                    + "ORDER BY jumlah_rule DESC, r.kode_rule ASC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int jumlahRuleTertinggi = 0;
            int jumlahKemungkinan = 0;

            String daftarPenyakit = "";
            String daftarSolusitunggal = "";
            String daftarSolusi = "";
            String kodePenyakitTunggal = "";

            kodePenyakitHasil = "";

            while (rs.next()) {
                int jumlahRule = rs.getInt("jumlah_rule");

                // Rule pertama adalah rule dengan jumlah gejala terbanyak
                if (jumlahRuleTertinggi == 0) {
                    jumlahRuleTertinggi = jumlahRule;
                }

                // Ambil hanya rule yang jumlah gejalanya sama dengan rule tertinggi
                if (jumlahRule == jumlahRuleTertinggi) {
                    jumlahKemungkinan++;

                    String kodePenyakit = rs.getString("kode_penyakit");
                    String namaPenyakit = rs.getString("nama_penyakit");
                    String solusiPenyakit = rs.getString("solusi");

                    listKemungkinan.add(
                            new KemungkinanDiagnosa(kodePenyakit, namaPenyakit, solusiPenyakit)
                    );

                    if (jumlahKemungkinan == 1) {
                        kodePenyakitTunggal = kodePenyakit;
                    }

                    daftarPenyakit += jumlahKemungkinan + ". "
                            + namaPenyakit + "\n";

                    daftarSolusitunggal += jumlahKemungkinan + ". "
                            + solusiPenyakit + "\n\n";
                    
                    daftarSolusi += jumlahKemungkinan + ". "
                            + namaPenyakit + "\n"
                            + solusiPenyakit + "\n\n";
                }
            }

            if (jumlahKemungkinan == 0) {
                kodePenyakitHasil = "";
                hasil.setText("Tidak ditemukan");
                solusi.setText("Gejala yang dipilih belum memenuhi rule penyakit yang tersedia.");
            } else if (jumlahKemungkinan == 1) {
                kodePenyakitHasil = kodePenyakitTunggal;
                hasil.setText(daftarPenyakit.replace("1. ", "").trim());
                solusi.setText(daftarSolusitunggal.replace("1. ", "").trim());
            } else {
                kodePenyakitHasil = "";
                hasil.setText("Lebih dari satu kemungkinan penyakit");
                solusi.setText(daftarSolusi);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal melakukan diagnosis: " + e.getMessage());
        }
    }//GEN-LAST:event_diagnosisActionPerformed

    private void cetakDiagnosisSetelahSimpan(String idDiagnosa) {
        try {
            String loginId = UserID.getidadmin();
            String loginadmin = "Tidak Diketahui";

            try (PreparedStatement nama = conn.prepareStatement(
                    "SELECT nama_lengkap FROM admin WHERE id_admin = ?"
            )) {
                nama.setString(1, loginId);

                try (ResultSet rsNama = nama.executeQuery()) {
                    if (rsNama.next()) {
                        loginadmin = rsNama.getString("nama_lengkap");
                    }
                }
            }

            String reportPath = "./src/report/rep_diagnosis.jasper";
            HashMap parameter = new HashMap();
            parameter.put("ADMIN", loginadmin);
            parameter.put("ID_DIAGNOSA", idDiagnosa);
            parameter.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID"));
            Locale.setDefault(new Locale("id", "ID"));
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameter, conn);
            form.menu_utama menuUtama = form.menu_utama.getInstance();
            if (menuUtama != null) {
                javax.swing.JPanel reportPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
                JRViewer viewer = new JRViewer(print);
                reportPanel.add(viewer, java.awt.BorderLayout.CENTER);
                menuUtama.loadPanel(reportPanel);
            } else {
                JasperViewer.viewReport(print, false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan, tetapi gagal mencetak laporan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void bsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimpanActionPerformed
        if (id_diagnosa.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "ID diagnosa masih kosong.");
            return;
        }
        if (nama_ikan.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Nama ikan belum diisi.");
            nama_ikan.requestFocus();
            return;
        }
        if (tanggal.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Tanggal diagnosa belum dipilih.");
            tanggal.requestFocus();
            return;
        }
        if (hasil.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Silakan lakukan diagnosis terlebih dahulu.");
            return;
        }
        if (listKemungkinan.isEmpty()
                && !hasil.getText().trim().equalsIgnoreCase("Tidak ditemukan")) {
            JOptionPane.showMessageDialog(this, "Data kemungkinan penyakit masih kosong. Silakan lakukan diagnosis ulang.");
            return;
        }
        int jumlahGejalaDipilih = 0;
        for (int i = 0; i < tabel_pilih_gejala.getRowCount(); i++) {
            Boolean cek = (Boolean) tabel_pilih_gejala.getValueAt(i, 0);

            if (cek != null && cek == true) {
                jumlahGejalaDipilih++;
            }
        }
        if (jumlahGejalaDipilih == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada gejala yang dipilih.");
            return;
        }
        try {
            Connection conn = new koneksi().connect();

            String sqlDiagnosa = "INSERT INTO diagnosa "
                    + "(id_diagnosa, nama_ikan, tanggal_diagnosa, kode_penyakit, hasil_diagnosa, solusi) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstDiagnosa = conn.prepareStatement(sqlDiagnosa);
            pstDiagnosa.setString(1, id_diagnosa.getText());
            pstDiagnosa.setString(2, nama_ikan.getText());
            java.sql.Date tglDiagnosa = new java.sql.Date(tanggal.getDate().getTime());
            pstDiagnosa.setDate(3, tglDiagnosa);

            if (kodePenyakitHasil.equals("")) {
                pstDiagnosa.setNull(4, java.sql.Types.VARCHAR);
            } else {
                pstDiagnosa.setString(4, kodePenyakitHasil);
            }
            pstDiagnosa.setString(5, hasil.getText());
            pstDiagnosa.setString(6, solusi.getText());

            pstDiagnosa.executeUpdate();

            String sqlDetail = "INSERT INTO diagnosa_detail "
                    + "(id_diagnosa, kode_gejala) "
                    + "VALUES (?, ?)";

            PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);

            for (int i = 0; i < tabel_pilih_gejala.getRowCount(); i++) {
                Boolean cek = (Boolean) tabel_pilih_gejala.getValueAt(i, 0);

                if (cek != null && cek == true) {
                    String kodeGejala = tabel_pilih_gejala.getValueAt(i, 1).toString();

                    pstDetail.setString(1, id_diagnosa.getText());
                    pstDetail.setString(2, kodeGejala);
                    pstDetail.executeUpdate();
                }
            }

            String sqlKemungkinan = "INSERT INTO hasil_kemungkinan_diagnosa "
                    + "(id_diagnosa, kode_penyakit, nama_penyakit, solusi) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement pstKemungkinan = conn.prepareStatement(sqlKemungkinan);

            for (KemungkinanDiagnosa kd : listKemungkinan) {
                pstKemungkinan.setString(1, id_diagnosa.getText());
                pstKemungkinan.setString(2, kd.kodePenyakit);
                pstKemungkinan.setString(3, kd.namaPenyakit);
                pstKemungkinan.setString(4, kd.solusiPenyakit);
                pstKemungkinan.executeUpdate();
            }

            String idCetak = id_diagnosa.getText();

            JOptionPane.showMessageDialog(this, "Data diagnosis berhasil disimpan.");

            cetakDiagnosisSetelahSimpan(idCetak);

            kosong();
            autonumber();

            kodePenyakitHasil = "";
            listKemungkinan.clear();

            for (int i = 0; i < tabel_pilih_gejala.getRowCount(); i++) {
                tabel_pilih_gejala.setValueAt(false, i, 0);
            }

            nama_ikan.requestFocus();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data diagnosis gagal disimpan: " + e.getMessage());
        }
    }//GEN-LAST:event_bsimpanActionPerformed

    private void bbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbatalActionPerformed
        aktif();
        kosong();
        autonumber();

        kodePenyakitHasil = "";
        listKemungkinan.clear();

        for (int i = 0; i < tabel_pilih_gejala.getRowCount(); i++) {
            tabel_pilih_gejala.setValueAt(false, i, 0);
        }
    }//GEN-LAST:event_bbatalActionPerformed

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tampilGejala();
        }
    }//GEN-LAST:event_txtcariKeyPressed

    private void txtcariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyTyped
        tampilGejala();
    }//GEN-LAST:event_txtcariKeyTyped

    private void txtcariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyReleased
        tampilGejala();
    }//GEN-LAST:event_txtcariKeyReleased

    private void bkembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkembaliActionPerformed
        form.menu_utama menuUtama = form.menu_utama.getInstance();
        if (menuUtama != null) {
            menuUtama.kembaliPanel();
        }
    }//GEN-LAST:event_bkembaliActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new diagnosis().setVisible(true);
            }
        });
    }

    class KemungkinanDiagnosa {

        String kodePenyakit;
        String namaPenyakit;
        String solusiPenyakit;

        KemungkinanDiagnosa(String kodePenyakit, String namaPenyakit, String solusiPenyakit) {
            this.kodePenyakit = kodePenyakit;
            this.namaPenyakit = namaPenyakit;
            this.solusiPenyakit = solusiPenyakit;
        }
    }

    class TextAreaRenderer extends JTextArea implements TableCellRenderer {

        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(new java.awt.Font("Tahoma", 1, 16));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            setText(value == null ? "" : value.toString());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);

            int tinggiBaris = getPreferredSize().height;
            if (table.getRowHeight(row) < tinggiBaris) {
                table.setRowHeight(row, tinggiBaris);
            }
            return this;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bbatal;
    private javax.swing.JButton bkembali;
    private javax.swing.JButton bsimpan;
    private javax.swing.JButton diagnosis;
    private javax.swing.JTextField hasil;
    private javax.swing.JTextField id_diagnosa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nama_ikan;
    private javax.swing.JPanel panel_diagnosis;
    private javax.swing.JPanel panel_gejala_rule;
    private javax.swing.JTextArea solusi;
    private javax.swing.JTable tabel_pilih_gejala;
    private org.jdesktop.swingx.JXDatePicker tanggal;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
}
