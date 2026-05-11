/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.awt.Component;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import koneksi.koneksi;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Ahmad Nur Latif P
 */
public class master_rule extends javax.swing.JFrame {

    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    private DefaultTableModel modelGejalaTerpilih;

    /**
     * Creates new form data_penyakit
     */
    public master_rule() {
        initComponents();
        autonumber();
        tampilPenyakit();
        tampilDaftarGejala();
        tampilGejalaTerpilihKosong();
        tampilDataRule();
        kosong();
    }

    protected void autonumber() {
        try {
            String sql = "SELECT kode_rule FROM rule ORDER BY kode_rule DESC LIMIT 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                String kode = rs.getString("kode_rule").substring(1);
                int nomor = Integer.parseInt(kode) + 1;

                String nol = "";
                if (nomor < 10) {
                    nol = "0";
                }

                kode_rule.setText("R" + nol + nomor);
            } else {
                kode_rule.setText("R01");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Kode Rule Otomatis Gagal: " + e);
        }
    }

    protected void tampilPenyakit() {
        try {
            combobox_penyakit.removeAllItems();
            combobox_penyakit.addItem("-- Pilih Penyakit --");

            String sql = "SELECT p.kode_penyakit, p.nama_penyakit "
                    + "FROM penyakit p "
                    + "WHERE p.kode_penyakit NOT IN ("
                    + "SELECT r.kode_penyakit FROM `rule` r"
                    + ") "
                    + "ORDER BY p.kode_penyakit ASC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String kode = rs.getString("kode_penyakit");
                String nama = rs.getString("nama_penyakit");

                combobox_penyakit.addItem(kode + " - " + nama);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data penyakit gagal ditampilkan: " + e);
        }
    }

    protected void tampilDaftarGejala() {
        Object[] kolom = {"Kode Gejala", "Nama Gejala"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);

        try {
            String sql = "SELECT kode_gejala, nama_gejala FROM gejala ORDER BY kode_gejala ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kode_gejala"),
                    rs.getString("nama_gejala")
                });
            }

            tabel_daftar_gejala.setModel(model);
            aturTabelDaftarGejala();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data gejala gagal ditampilkan: " + e);
        }
    }

    protected void aturTabelDaftarGejala() {
        tabel_daftar_gejala.setRowHeight(35);
        tabel_daftar_gejala.getTableHeader().setFont(
                new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16)
        );
        tabel_daftar_gejala.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabel_daftar_gejala.getColumnModel().getColumn(1).setPreferredWidth(350);
    }

    protected void tampilGejalaTerpilihKosong() {
        Object[] kolom = {"Kode Gejala", "Nama Gejala"};
        modelGejalaTerpilih = new DefaultTableModel(null, kolom);
        tabel_gejala_terpilih.setModel(modelGejalaTerpilih);
        aturTabelGejalaTerpilih();
    }

    protected void aturTabelGejalaTerpilih() {
        tabel_gejala_terpilih.setRowHeight(35);
        tabel_gejala_terpilih.getTableHeader().setFont(
                new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16)
        );
        tabel_gejala_terpilih.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabel_gejala_terpilih.getColumnModel().getColumn(1).setPreferredWidth(350);
    }

    protected void tampilDataRule() {
        Object[] kolom = {
            "Kode Rule",
            "Kode Penyakit",
            "Nama Penyakit",
            "Kode Gejala",
            "Nama Gejala"
        };
        tabmode = new DefaultTableModel(null, kolom);
        String cariitem = txtcari.getText();
        try {
            String sql = "SELECT r.kode_rule, p.kode_penyakit, p.nama_penyakit, "
                    + "GROUP_CONCAT(g.kode_gejala ORDER BY g.kode_gejala SEPARATOR ', ') AS kode_gejala, "
                    + "GROUP_CONCAT(g.nama_gejala ORDER BY g.kode_gejala SEPARATOR ', ') AS nama_gejala "
                    + "FROM `rule` r "
                    + "JOIN penyakit p ON r.kode_penyakit = p.kode_penyakit "
                    + "JOIN rule_detail rd ON r.kode_rule = rd.kode_rule "
                    + "JOIN gejala g ON rd.kode_gejala = g.kode_gejala "
                    + "WHERE r.kode_rule LIKE '%" + cariitem + "%' "
                    + "OR p.kode_penyakit LIKE '%" + cariitem + "%' "
                    + "OR p.nama_penyakit LIKE '%" + cariitem + "%' "
                    + "OR g.kode_gejala LIKE '%" + cariitem + "%' "
                    + "OR g.nama_gejala LIKE '%" + cariitem + "%' "
                    + "GROUP BY r.kode_rule, p.kode_penyakit, p.nama_penyakit "
                    + "ORDER BY r.kode_rule ASC";

            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                tabmode.addRow(new Object[]{
                    hasil.getString("kode_rule"),
                    hasil.getString("kode_penyakit"),
                    hasil.getString("nama_penyakit"),
                    hasil.getString("kode_gejala"),
                    hasil.getString("nama_gejala")
                });
            }
            tabel_rule.setModel(tabmode);
            aturTabelRule();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data rule gagal ditampilkan: " + e);
        }
    }

    protected void aturTabelRule() {
        tabel_rule.setRowHeight(45);

        tabel_rule.getTableHeader().setFont(
                new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14)
        );
        tabel_rule.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabel_rule.getColumnModel().getColumn(0).setPreferredWidth(80);   // Kode Rule
        tabel_rule.getColumnModel().getColumn(1).setPreferredWidth(100);  // Kode Penyakit
        tabel_rule.getColumnModel().getColumn(2).setPreferredWidth(150);  // Nama Penyakit
        tabel_rule.getColumnModel().getColumn(3).setPreferredWidth(180);  // Kode Gejala
        tabel_rule.getColumnModel().getColumn(4).setPreferredWidth(350);  // Nama Gejala 
        TextAreaRenderer renderer = new TextAreaRenderer();
        tabel_rule.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tabel_rule.getColumnModel().getColumn(1).setCellRenderer(renderer);
        tabel_rule.getColumnModel().getColumn(2).setCellRenderer(renderer);
        tabel_rule.getColumnModel().getColumn(3).setCellRenderer(renderer);
        tabel_rule.getColumnModel().getColumn(4).setCellRenderer(renderer);
    }

    protected void tampilDetailRule(String kodeRule) {
        Object[] kolom = {"Kode Gejala", "Nama Gejala"};
        modelGejalaTerpilih = new DefaultTableModel(null, kolom);

        try {
            String sql = "SELECT g.kode_gejala, g.nama_gejala "
                    + "FROM rule_detail rd "
                    + "JOIN gejala g ON rd.kode_gejala = g.kode_gejala "
                    + "WHERE rd.kode_rule = ? "
                    + "ORDER BY g.kode_gejala ASC";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kodeRule);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                modelGejalaTerpilih.addRow(new Object[]{
                    rs.getString("kode_gejala"),
                    rs.getString("nama_gejala")
                });
            }
            tabel_gejala_terpilih.setModel(modelGejalaTerpilih);
            aturTabelGejalaTerpilih();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Detail rule gagal ditampilkan: " + e);
        }
    }

    protected void tampilPenyakitEdit(String kodePenyakitAktif) {
        try {
            combobox_penyakit.removeAllItems();
            combobox_penyakit.addItem("-- Pilih Penyakit --");
            String sql = "SELECT p.kode_penyakit, p.nama_penyakit "
                    + "FROM penyakit p "
                    + "WHERE p.kode_penyakit NOT IN ("
                    + "SELECT r.kode_penyakit FROM `rule` r "
                    + "WHERE r.kode_penyakit <> ?"
                    + ") "
                    + "ORDER BY p.kode_penyakit ASC";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kodePenyakitAktif);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String kode = rs.getString("kode_penyakit");
                String nama = rs.getString("nama_penyakit");

                combobox_penyakit.addItem(kode + " - " + nama);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data penyakit edit gagal ditampilkan: " + e);
        }
    }

    protected void kosong() {
        autonumber();

        tampilPenyakit();
        combobox_penyakit.setSelectedIndex(0);

        tampilGejalaTerpilihKosong();

        txtcari.setText("");
        tampilDataRule();

        tabel_daftar_gejala.clearSelection();
        tabel_gejala_terpilih.clearSelection();
        tabel_rule.clearSelection();
        kode_rule.setEditable(false);
    }

    public javax.swing.JPanel getMainPanel() {
        return panel_rule;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_rule = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        kode_rule = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        bsimpan = new javax.swing.JButton();
        bhapus = new javax.swing.JButton();
        bbatal = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        combobox_penyakit = new javax.swing.JComboBox<>();
        panel_gejala_rule = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabel_gejala_terpilih = new javax.swing.JTable();
        panel_daftar_gejala = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabel_daftar_gejala = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btambahrule = new javax.swing.JToggleButton();
        bhapusrule = new javax.swing.JToggleButton();
        bubah = new javax.swing.JButton();
        bkembali = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_rule = new javax.swing.JTable();
        txtcari = new javax.swing.JTextField();
        cari_gejal = new javax.swing.JButton();
        bprint_gejala = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel_rule.setBackground(new java.awt.Color(69, 46, 90));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATA RULE");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 0, 3, 0, new java.awt.Color(255, 255, 255)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Kode Rule");

        kode_rule.setBackground(java.awt.SystemColor.controlHighlight);
        kode_rule.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Penyakit");

        bsimpan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bsimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/simpan.png"))); // NOI18N
        bsimpan.setText("SIMPAN");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
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
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        combobox_penyakit.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        panel_gejala_rule.setBackground(new java.awt.Color(255, 255, 255));
        panel_gejala_rule.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3), "Daftar Gejala", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        tabel_gejala_terpilih.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tabel_gejala_terpilih.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabel_gejala_terpilih);

        javax.swing.GroupLayout panel_gejala_ruleLayout = new javax.swing.GroupLayout(panel_gejala_rule);
        panel_gejala_rule.setLayout(panel_gejala_ruleLayout);
        panel_gejala_ruleLayout.setHorizontalGroup(
            panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_gejala_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_gejala_ruleLayout.setVerticalGroup(
            panel_gejala_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_gejala_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_daftar_gejala.setBackground(new java.awt.Color(255, 255, 255));
        panel_daftar_gejala.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3), "Daftar Gejala", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        tabel_daftar_gejala.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tabel_daftar_gejala.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tabel_daftar_gejala);

        javax.swing.GroupLayout panel_daftar_gejalaLayout = new javax.swing.GroupLayout(panel_daftar_gejala);
        panel_daftar_gejala.setLayout(panel_daftar_gejalaLayout);
        panel_daftar_gejalaLayout.setHorizontalGroup(
            panel_daftar_gejalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_daftar_gejalaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_daftar_gejalaLayout.setVerticalGroup(
            panel_daftar_gejalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_daftar_gejalaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText(":");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText(":");

        btambahrule.setBackground(new java.awt.Color(0, 153, 51));
        btambahrule.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btambahrule.setForeground(new java.awt.Color(255, 255, 255));
        btambahrule.setSelected(true);
        btambahrule.setText(">>");
        btambahrule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btambahruleActionPerformed(evt);
            }
        });

        bhapusrule.setBackground(new java.awt.Color(204, 0, 0));
        bhapusrule.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bhapusrule.setForeground(new java.awt.Color(255, 255, 255));
        bhapusrule.setSelected(true);
        bhapusrule.setText("<<");
        bhapusrule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusruleActionPerformed(evt);
            }
        });

        bubah.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bubah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/cog_edit.png"))); // NOI18N
        bubah.setText("UBAH");
        bubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bubahActionPerformed(evt);
            }
        });

        bkembali.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bkembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/arrow_undo.png"))); // NOI18N
        bkembali.setText("KEMBALI");
        bkembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(bsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bubah, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panel_daftar_gejala, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btambahrule)
                            .addComponent(bhapusrule))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_gejala_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kode_rule))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combobox_penyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bkembali)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(kode_rule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(bkembali))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(combobox_penyakit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(btambahrule, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(bhapusrule, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 96, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panel_gejala_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel_daftar_gejala, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(bsimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bubah, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bhapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bbatal))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

        tabel_rule.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabel_rule.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel_rule.setRowHeight(25);
        tabel_rule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_ruleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_rule);

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

        cari_gejal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cari_gejal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/cari.png"))); // NOI18N
        cari_gejal.setText("CARI");
        cari_gejal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari_gejalActionPerformed(evt);
            }
        });

        bprint_gejala.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bprint_gejala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/printer.png"))); // NOI18N
        bprint_gejala.setText("CETAK");
        bprint_gejala.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bprint_gejala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bprint_gejalaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cari_gejal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bprint_gejala))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtcari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(bprint_gejala, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cari_gejal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_ruleLayout = new javax.swing.GroupLayout(panel_rule);
        panel_rule.setLayout(panel_ruleLayout);
        panel_ruleLayout.setHorizontalGroup(
            panel_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_ruleLayout.setVerticalGroup(
            panel_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_ruleLayout.createSequentialGroup()
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
            .addComponent(panel_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabel_ruleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_ruleMouseClicked
        int baris = tabel_rule.getSelectedRow();

        if (baris == -1) {
            return;
        }

        String kodeRule = tabel_rule.getValueAt(baris, 0).toString();
        String kodePenyakit = tabel_rule.getValueAt(baris, 1).toString();

        kode_rule.setText(kodeRule);

        tampilPenyakitEdit(kodePenyakit);

        for (int i = 0; i < combobox_penyakit.getItemCount(); i++) {
            String item = combobox_penyakit.getItemAt(i);

            if (item.startsWith(kodePenyakit + " - ")) {
                combobox_penyakit.setSelectedIndex(i);
                break;
            }
        }

        tampilDetailRule(kodeRule);
    }//GEN-LAST:event_tabel_ruleMouseClicked

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        tampilDataRule();
    }//GEN-LAST:event_txtcariKeyPressed

    private void txtcariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyTyped
        tampilDataRule();
    }//GEN-LAST:event_txtcariKeyTyped

    private void cari_gejalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari_gejalActionPerformed
        tampilDataRule();
    }//GEN-LAST:event_cari_gejalActionPerformed

    private void bprint_gejalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bprint_gejalaActionPerformed
        try {
            String loginId = UserID.getidadmin();
            String loginadmin = "Tidak Diketahui";

            try (PreparedStatement nama = conn.prepareStatement("SELECT nama_lengkap FROM admin WHERE id_admin = ?")) {
                nama.setString(1, loginId);
                try (ResultSet rsNama = nama.executeQuery()) {
                    if (rsNama.next()) {
                        loginadmin = rsNama.getString("nama_lengkap");
                    }
                }
            }

            String reportPath = "./src/report/rep_rule.jasper";
            HashMap parameter = new HashMap();
            parameter.put("ADMIN", loginadmin);

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
    }//GEN-LAST:event_bprint_gejalaActionPerformed

    private void bubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bubahActionPerformed
        if (kode_rule.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Pilih data rule yang ingin diubah terlebih dahulu!");
            return;
        }
        if (combobox_penyakit.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Pilih penyakit terlebih dahulu!");
            return;
        }
        if (modelGejalaTerpilih.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Pilih minimal satu gejala untuk rule!");
            return;
        }
        String kodeRule = kode_rule.getText().trim();
        String penyakitDipilih = combobox_penyakit.getSelectedItem().toString();
        String kodePenyakit = penyakitDipilih.substring(0, penyakitDipilih.indexOf(" - "));
        int konfirmasi = JOptionPane.showConfirmDialog(
                null,
                "Yakin ingin mengubah data rule " + kodeRule + "?",
                "Konfirmasi Ubah",
                JOptionPane.YES_NO_OPTION
        );
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                conn.setAutoCommit(false);
                String sqlRule = "UPDATE `rule` SET kode_penyakit = ? WHERE kode_rule = ?";
                PreparedStatement pstRule = conn.prepareStatement(sqlRule);
                pstRule.setString(1, kodePenyakit);
                pstRule.setString(2, kodeRule);
                pstRule.executeUpdate();

                // Hapus detail gejala lama
                String sqlHapusDetail = "DELETE FROM rule_detail WHERE kode_rule = ?";
                PreparedStatement pstHapusDetail = conn.prepareStatement(sqlHapusDetail);
                pstHapusDetail.setString(1, kodeRule);
                pstHapusDetail.executeUpdate();

                // Simpan ulang detail gejala yang baru
                String sqlDetail = "INSERT INTO rule_detail (kode_rule, kode_gejala) VALUES (?, ?)";
                PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);

                for (int i = 0; i < modelGejalaTerpilih.getRowCount(); i++) {
                    String kodeGejala = modelGejalaTerpilih.getValueAt(i, 0).toString();
                    pstDetail.setString(1, kodeRule);
                    pstDetail.setString(2, kodeGejala);
                    pstDetail.executeUpdate();
                }

                conn.commit();

                JOptionPane.showMessageDialog(null, "Data rule berhasil diubah!");
                kosong();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Rollback gagal: " + ex);
                }
                JOptionPane.showMessageDialog(null, "Data rule gagal diubah: " + e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Auto commit gagal dikembalikan: " + e);
                }
            }
        }
    }//GEN-LAST:event_bubahActionPerformed

    private void bhapusruleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusruleActionPerformed
        int baris = tabel_gejala_terpilih.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(null, "Pilih gejala yang ingin dihapus dari tabel gejala terpilih!");
            return;
        }
        modelGejalaTerpilih.removeRow(baris);
    }//GEN-LAST:event_bhapusruleActionPerformed

    private void btambahruleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btambahruleActionPerformed
        int baris = tabel_daftar_gejala.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(null, "Pilih gejala terlebih dahulu dari tabel daftar gejala!");
            return;
        }
        String kodeGejala = tabel_daftar_gejala.getValueAt(baris, 0).toString();
        String namaGejala = tabel_daftar_gejala.getValueAt(baris, 1).toString();

        for (int i = 0; i < modelGejalaTerpilih.getRowCount(); i++) {
            String kodeTerpilih = modelGejalaTerpilih.getValueAt(i, 0).toString();

            if (kodeTerpilih.equals(kodeGejala)) {
                JOptionPane.showMessageDialog(null, "Gejala tersebut sudah ditambahkan!");
                return;
            }
        }
        modelGejalaTerpilih.addRow(new Object[]{
            kodeGejala,
            namaGejala
        });
    }//GEN-LAST:event_btambahruleActionPerformed

    private void bbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbatalActionPerformed
        kosong();
    }//GEN-LAST:event_bbatalActionPerformed

    private void bhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusActionPerformed
        int baris = tabel_rule.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data rule yang ingin dihapus terlebih dahulu!");
            return;
        }
        String kodeRule = tabel_rule.getValueAt(baris, 0).toString();
        int konfirmasi = JOptionPane.showConfirmDialog(
                null,
                "Yakin ingin menghapus data rule " + kodeRule + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                conn.setAutoCommit(false);
                // Hapus data detail rule terlebih dahulu
                String sqlDetail = "DELETE FROM rule_detail WHERE kode_rule = ?";
                PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
                pstDetail.setString(1, kodeRule);
                pstDetail.executeUpdate();
                // Hapus data rule utama
                String sqlRule = "DELETE FROM `rule` WHERE kode_rule = ?";
                PreparedStatement pstRule = conn.prepareStatement(sqlRule);
                pstRule.setString(1, kodeRule);
                pstRule.executeUpdate();
                conn.commit();
                JOptionPane.showMessageDialog(null, "Data rule berhasil dihapus!");
                kosong();

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Rollback gagal: " + ex);
                }
                JOptionPane.showMessageDialog(null, "Data rule gagal dihapus: " + e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Auto commit gagal dikembalikan: " + e);
                }
            }
        }
    }//GEN-LAST:event_bhapusActionPerformed

    private void bsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimpanActionPerformed
        if (kode_rule.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Kode rule tidak boleh kosong!");
            return;
        }
        if (combobox_penyakit.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Pilih penyakit terlebih dahulu!");
            return;
        }
        if (modelGejalaTerpilih.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Pilih minimal satu gejala untuk rule!");
            return;
        }
        String kodeRule = kode_rule.getText().trim();

        String penyakitDipilih = combobox_penyakit.getSelectedItem().toString();
        String kodePenyakit = penyakitDipilih.substring(0, penyakitDipilih.indexOf(" - "));

        try {
            String cekSql = "SELECT kode_rule FROM `rule` WHERE kode_penyakit = ?";
            PreparedStatement cekPst = conn.prepareStatement(cekSql);
            cekPst.setString(1, kodePenyakit);
            ResultSet cekRs = cekPst.executeQuery();

            if (cekRs.next()) {
                JOptionPane.showMessageDialog(null, "Penyakit ini sudah memiliki rule!");
                tampilPenyakit();
                combobox_penyakit.setSelectedIndex(0);
                return;
            }

            conn.setAutoCommit(false);
            String sqlRule = "INSERT INTO `rule` (kode_rule, kode_penyakit) VALUES (?, ?)";
            PreparedStatement pstRule = conn.prepareStatement(sqlRule);
            pstRule.setString(1, kodeRule);
            pstRule.setString(2, kodePenyakit);
            pstRule.executeUpdate();

            // Simpan ke tabel detail_rule
            String sqlDetail = "INSERT INTO rule_detail (kode_rule, kode_gejala) VALUES (?, ?)";
            PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);

            for (int i = 0; i < modelGejalaTerpilih.getRowCount(); i++) {
                String kodeGejala = modelGejalaTerpilih.getValueAt(i, 0).toString();
                pstDetail.setString(1, kodeRule);
                pstDetail.setString(2, kodeGejala);
                pstDetail.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Data rule berhasil disimpan!");
            kosong();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Rollback gagal: " + ex);
            }
            JOptionPane.showMessageDialog(null, "Data rule gagal disimpan: " + e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Auto commit gagal dikembalikan: " + e);
            }
        }
    }//GEN-LAST:event_bsimpanActionPerformed

    private void txtcariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyReleased
        tampilDataRule();
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
            java.util.logging.Logger.getLogger(master_rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(master_rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(master_rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(master_rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new master_rule().setVisible(true);
            }
        });
    }

    class TextAreaRenderer extends JTextArea implements TableCellRenderer {

        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(new java.awt.Font("Tahoma", 1, 14));
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
    private javax.swing.JButton bhapus;
    private javax.swing.JToggleButton bhapusrule;
    private javax.swing.JButton bkembali;
    private javax.swing.JButton bprint_gejala;
    private javax.swing.JButton bsimpan;
    private javax.swing.JToggleButton btambahrule;
    private javax.swing.JButton bubah;
    private javax.swing.JButton cari_gejal;
    private javax.swing.JComboBox<String> combobox_penyakit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField kode_rule;
    private javax.swing.JPanel panel_daftar_gejala;
    private javax.swing.JPanel panel_gejala_rule;
    private javax.swing.JPanel panel_rule;
    private javax.swing.JTable tabel_daftar_gejala;
    private javax.swing.JTable tabel_gejala_terpilih;
    private javax.swing.JTable tabel_rule;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
}
