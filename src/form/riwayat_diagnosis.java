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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import java.util.Calendar;
import java.sql.Date;
import java.util.Locale;
import net.sf.jasperreports.engine.JRParameter;

/**
 *
 * @author Ahmad Nur Latif P
 */
public class riwayat_diagnosis extends javax.swing.JFrame {

    private Connection conn = new koneksi().connect();
    private DefaultTableModel model;

    /**
     * Creates new form data_penyakit
     */
    public riwayat_diagnosis() {
        initComponents();
        aktif();
        kosong();
        tampilRiwayat();

    }

    protected void aktif() {
        nama_ikan.setEditable(false);
        id_diagnosa.setEditable(false);
        solusi.setEditable(false);
        jXDatePicker1.setEditable(false);
    }

    protected void kosong() {
        id_diagnosa.setText("");
        nama_ikan.setText("");
        solusi.setText("");
        jXDatePicker1.setDate(null);
    }

    private void tampilRiwayat() {
        Object[] kolom = {
            "ID Diagnosa",
            "Nama Ikan",
            "Tanggal Diagnosa",
            "Kode Penyakit",
            "Hasil Diagnosa"
        };
        model = new DefaultTableModel(null, kolom);
        String cari = txtcari.getText();
        try {
            String sql = "SELECT id_diagnosa, nama_ikan, tanggal_diagnosa, "
                    + "IFNULL(kode_penyakit, '-') AS kode_penyakit, hasil_diagnosa "
                    + "FROM diagnosa "
                    + "WHERE id_diagnosa LIKE ? "
                    + "OR nama_ikan LIKE ? "
                    + "OR tanggal_diagnosa LIKE ? "
                    + "OR kode_penyakit LIKE ? "
                    + "OR hasil_diagnosa LIKE ? "
                    + "ORDER BY id_diagnosa DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + cari + "%");
            pst.setString(2, "%" + cari + "%");
            pst.setString(3, "%" + cari + "%");
            pst.setString(4, "%" + cari + "%");
            pst.setString(5, "%" + cari + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_diagnosa"),
                    rs.getString("nama_ikan"),
                    rs.getString("tanggal_diagnosa"),
                    rs.getString("kode_penyakit"),
                    rs.getString("hasil_diagnosa")
                });
            }
            tabel_riwayat.setModel(model);
            aturTabelRiwayat();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan riwayat diagnosis: " + e.getMessage());
        }
    }

    private void aturTabelRiwayat() {
        tabel_riwayat.setRowHeight(30);

        tabel_riwayat.getTableHeader().setFont(
                new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14)
        );
        tabel_riwayat.setFont(
                new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14)
        );
        tabel_riwayat.getColumnModel().getColumn(0).setPreferredWidth(100);  // ID Diagnosa
        tabel_riwayat.getColumnModel().getColumn(1).setPreferredWidth(180);  // Nama Ikan
        tabel_riwayat.getColumnModel().getColumn(2).setPreferredWidth(150);  // Tanggal
        tabel_riwayat.getColumnModel().getColumn(3).setPreferredWidth(120);  // Kode Penyakit
        tabel_riwayat.getColumnModel().getColumn(4).setPreferredWidth(300);  // Hasil Diagnosa

        tabel_riwayat.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void tampilDetailDiagnosa(String id) {
        try {
            String sql = "SELECT id_diagnosa, nama_ikan, tanggal_diagnosa, solusi "
                    + "FROM diagnosa "
                    + "WHERE id_diagnosa = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id_diagnosa.setText(rs.getString("id_diagnosa"));
                nama_ikan.setText(rs.getString("nama_ikan"));
                jXDatePicker1.setDate(rs.getDate("tanggal_diagnosa"));
                solusi.setText(rs.getString("solusi"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan detail diagnosa: " + e.getMessage());
        }
    }

    private void tampilDetailGejala(String id) {
        Object[] kolom = {
            "Kode Gejala",
            "Nama Gejala"
        };
        DefaultTableModel tabmode = new DefaultTableModel(null, kolom);
        try {
            String sql = "SELECT dg.kode_gejala, g.nama_gejala "
                    + "FROM diagnosa_detail dg "
                    + "JOIN gejala g ON dg.kode_gejala = g.kode_gejala "
                    + "WHERE dg.id_diagnosa = ? "
                    + "ORDER BY dg.kode_gejala ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                tabmode.addRow(new Object[]{
                    rs.getString("kode_gejala"),
                    rs.getString("nama_gejala")
                });
            }
            tabel_detail_gejala.setModel(tabmode);
            aturTabelDetailGejala();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan detail gejala: " + e.getMessage());
        }
    }

    private void tampilKemungkinanPenyakit(String id) {
        Object[] kolom = {
            "Kode Penyakit",
            "Nama Penyakit"
        };
        DefaultTableModel tabmode = new DefaultTableModel(null, kolom);
        try {
            String sql = "SELECT kode_penyakit, nama_penyakit "
                    + "FROM hasil_kemungkinan_diagnosa "
                    + "WHERE id_diagnosa = ? "
                    + "ORDER BY kode_penyakit ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                tabmode.addRow(new Object[]{
                    rs.getString("kode_penyakit"),
                    rs.getString("nama_penyakit")
                });
            }
            tabel_kemungkinan.setModel(tabmode);
            aturTabelKemungkinan();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan kemungkinan penyakit: " + e.getMessage());
        }
    }

    private void aturTabelDetailGejala() {
        tabel_detail_gejala.setRowHeight(30);
        tabel_detail_gejala.getTableHeader().setFont(
                new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14)
        );
        tabel_detail_gejala.setFont(
                new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14)
        );
        tabel_detail_gejala.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabel_detail_gejala.getColumnModel().getColumn(1).setPreferredWidth(400);
        tabel_detail_gejala.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void aturTabelKemungkinan() {
        tabel_kemungkinan.setRowHeight(30);
        tabel_kemungkinan.getTableHeader().setFont(
                new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14)
        );
        tabel_kemungkinan.setFont(
                new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14)
        );
        tabel_kemungkinan.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabel_kemungkinan.getColumnModel().getColumn(1).setPreferredWidth(400);
        tabel_kemungkinan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
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
        bbatal = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        panel_gejala_rule = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabel_riwayat = new javax.swing.JTable();
        bkembali = new javax.swing.JButton();
        txtcari = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        bprint_diagnosis_semua = new javax.swing.JButton();
        bhapus = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        id_diagnosa = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        nama_ikan = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        bprint_diagnosis_ini = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabel_detail_gejala = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabel_kemungkinan = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        solusi = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel_diagnosis.setBackground(new java.awt.Color(69, 46, 90));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("RIWAYAT DIAGNOSIS PENYAKIT");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 0, 3, 0, new java.awt.Color(255, 255, 255)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

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
        panel_gejala_rule.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3), "Tabel Riwayat Diagnosis", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 24))); // NOI18N

        tabel_riwayat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabel_riwayat.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel_riwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_riwayatMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabel_riwayat);

        javax.swing.GroupLayout panel_gejala_ruleLayout = new javax.swing.GroupLayout(panel_gejala_rule);
        panel_gejala_rule.setLayout(panel_gejala_ruleLayout);
        panel_gejala_ruleLayout.setHorizontalGroup(
            panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_gejala_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        panel_gejala_ruleLayout.setVerticalGroup(
            panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_gejala_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        bkembali.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bkembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/arrow_undo.png"))); // NOI18N
        bkembali.setText("KEMBALI");
        bkembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkembaliActionPerformed(evt);
            }
        });

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

        bprint_diagnosis_semua.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bprint_diagnosis_semua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/printer.png"))); // NOI18N
        bprint_diagnosis_semua.setText("CETAK SEMUA");
        bprint_diagnosis_semua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bprint_diagnosis_semuaActionPerformed(evt);
            }
        });

        bhapus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bhapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/hapus.png"))); // NOI18N
        bhapus.setText("HAPUS");
        bhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel_gejala_rule, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(554, 554, 554)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(184, 184, 184)
                                .addComponent(bprint_diagnosis_semua, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bkembali)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bkembali)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_gejala_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bbatal)
                        .addComponent(bprint_diagnosis_semua)
                        .addComponent(bhapus)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Hasil Diagnosis");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText("ID Diagnosa :");

        id_diagnosa.setBackground(java.awt.SystemColor.controlHighlight);
        id_diagnosa.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("Nama Ikan :");

        nama_ikan.setBackground(java.awt.SystemColor.controlHighlight);
        nama_ikan.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setText("Tanggal :");

        jXDatePicker1.setBackground(java.awt.SystemColor.controlHighlight);

        bprint_diagnosis_ini.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bprint_diagnosis_ini.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/printer.png"))); // NOI18N
        bprint_diagnosis_ini.setText("CETAK");
        bprint_diagnosis_ini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bprint_diagnosis_iniActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true), "Fakta Gejala Ikan", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        tabel_detail_gejala.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tabel_detail_gejala);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true), "Hasil Kemungkinan Penyakit", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        tabel_kemungkinan.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tabel_kemungkinan);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        solusi.setBackground(java.awt.SystemColor.controlHighlight);
        solusi.setColumns(20);
        solusi.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        solusi.setRows(5);
        jScrollPane1.setViewportView(solusi);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Solusi");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText(":");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(id_diagnosa)
                                    .addComponent(nama_ikan, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bprint_diagnosis_ini, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(id_diagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bprint_diagnosis_ini, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nama_ikan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
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

    private void bkembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkembaliActionPerformed
        form.menu_utama menuUtama = form.menu_utama.getInstance();
        if (menuUtama != null) {
            menuUtama.kembaliPanel();
        }
    }//GEN-LAST:event_bkembaliActionPerformed

    private void txtcariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyTyped
        tampilRiwayat();
    }//GEN-LAST:event_txtcariKeyTyped

    private void txtcariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyReleased
        tampilRiwayat();
    }//GEN-LAST:event_txtcariKeyReleased

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        tampilRiwayat();
    }//GEN-LAST:event_txtcariKeyPressed

    private void bbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbatalActionPerformed
        kosong();
        txtcari.setText("");
        tampilRiwayat();

        tabel_detail_gejala.setModel(new DefaultTableModel(
                null,
                new Object[]{"Kode Gejala", "Nama Gejala"}
        ));

        tabel_kemungkinan.setModel(new DefaultTableModel(
                null,
                new Object[]{"Kode Penyakit", "Nama Penyakit"}
        ));
    }//GEN-LAST:event_bbatalActionPerformed

    private void bprint_diagnosis_iniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bprint_diagnosis_iniActionPerformed
        try {
            if (id_diagnosa.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Pilih data diagnosis yang ingin dicetak terlebih dahulu.");
                return;
            }

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
            parameter.put("ID_DIAGNOSA", id_diagnosa.getText());

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
            JOptionPane.showMessageDialog(this, "Gagal mencetak riwayat diagnosis: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_bprint_diagnosis_iniActionPerformed

    private void bprint_diagnosis_semuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bprint_diagnosis_semuaActionPerformed
        try {
            String[] pilihan = {
                "7 Hari Terakhir",
                "1 Bulan Terakhir",
                "1 Tahun Terakhir"
            };
            String pilih = (String) JOptionPane.showInputDialog(
                    this,
                    "Pilih periode laporan diagnosis:",
                    "Cetak Riwayat Diagnosis",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    pilihan,
                    pilihan[0]
            );

            // Jika user menekan Cancel
            if (pilih == null) {
                return;
            }
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

            // Menghitung tanggal awal dan tanggal akhir
            Calendar cal = Calendar.getInstance();
            java.sql.Date tanggalAkhir = new java.sql.Date(System.currentTimeMillis());
            if (pilih.equals("7 Hari Terakhir")) {
                cal.add(Calendar.DAY_OF_MONTH, -6);
            } else if (pilih.equals("1 Bulan Terakhir")) {
                cal.add(Calendar.MONTH, -1);
            } else if (pilih.equals("1 Tahun Terakhir")) {
                cal.add(Calendar.YEAR, -1);
            }
            java.sql.Date tanggalAwal = new java.sql.Date(cal.getTimeInMillis());
            String reportPath = "./src/report/rep_diagnosis_semua.jasper";
            HashMap parameter = new HashMap();
            parameter.put("ADMIN", loginadmin);
            parameter.put("TGL_AWAL", tanggalAwal);
            parameter.put("TGL_AKHIR", tanggalAkhir);
            parameter.put("PERIODE", pilih);
            parameter.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID"));
            Locale.setDefault(new Locale("id", "ID"));
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameter, conn);
            form.menu_utama menuUtama = form.menu_utama.getInstance();
            if (menuUtama != null) {
                javax.swing.JPanel reportPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
                net.sf.jasperreports.swing.JRViewer viewer = new net.sf.jasperreports.swing.JRViewer(print);
                reportPanel.add(viewer, java.awt.BorderLayout.CENTER);
                menuUtama.loadPanel(reportPanel);
            } else {
                JasperViewer.viewReport(print, false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak report: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_bprint_diagnosis_semuaActionPerformed

    private void tabel_riwayatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_riwayatMouseClicked
        int baris = tabel_riwayat.getSelectedRow();
        if (baris != -1) {
            String id = tabel_riwayat.getValueAt(baris, 0).toString();

            tampilDetailDiagnosa(id);
            tampilDetailGejala(id);
            tampilKemungkinanPenyakit(id);
        }
    }//GEN-LAST:event_tabel_riwayatMouseClicked

    private void bhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusActionPerformed
        if (id_diagnosa.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Pilih data riwayat diagnosis yang ingin dihapus terlebih dahulu.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data diagnosis dengan ID "
                + id_diagnosa.getText() + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                Connection conn = new koneksi().connect();

                String id = id_diagnosa.getText();

                // Hapus data detail gejala
                String sqlDetail = "DELETE FROM diagnosa_detail WHERE id_diagnosa = ?";
                PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
                pstDetail.setString(1, id);
                pstDetail.executeUpdate();

                // Hapus data kemungkinan penyakit
                String sqlKemungkinan = "DELETE FROM hasil_kemungkinan_diagnosa WHERE id_diagnosa = ?";
                PreparedStatement pstKemungkinan = conn.prepareStatement(sqlKemungkinan);
                pstKemungkinan.setString(1, id);
                pstKemungkinan.executeUpdate();

                // Hapus data utama diagnosis
                String sqlDiagnosa = "DELETE FROM diagnosa WHERE id_diagnosa = ?";
                PreparedStatement pstDiagnosa = conn.prepareStatement(sqlDiagnosa);
                pstDiagnosa.setString(1, id);
                pstDiagnosa.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data riwayat diagnosis berhasil dihapus.");

                kosong();
                txtcari.setText("");
                tampilRiwayat();

                tabel_detail_gejala.setModel(new DefaultTableModel(
                        null,
                        new Object[]{"Kode Gejala", "Nama Gejala"}
                ));

                tabel_kemungkinan.setModel(new DefaultTableModel(
                        null,
                        new Object[]{"Kode Penyakit", "Nama Penyakit"}
                ));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Data riwayat diagnosis gagal dihapus: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_bhapusActionPerformed

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
            java.util.logging.Logger.getLogger(riwayat_diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(riwayat_diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(riwayat_diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(riwayat_diagnosis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new riwayat_diagnosis().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bbatal;
    private javax.swing.JButton bhapus;
    private javax.swing.JButton bkembali;
    private javax.swing.JButton bprint_diagnosis_ini;
    private javax.swing.JButton bprint_diagnosis_semua;
    private javax.swing.JTextField id_diagnosa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JTextField nama_ikan;
    private javax.swing.JPanel panel_diagnosis;
    private javax.swing.JPanel panel_gejala_rule;
    private javax.swing.JTextArea solusi;
    private javax.swing.JTable tabel_detail_gejala;
    private javax.swing.JTable tabel_kemungkinan;
    private javax.swing.JTable tabel_riwayat;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
}
