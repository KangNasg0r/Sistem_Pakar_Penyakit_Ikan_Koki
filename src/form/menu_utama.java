/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import koneksi.koneksi;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.util.Stack;

/**
 *
 * @author Ahmad Nur Latif P
 */
public class menu_utama extends javax.swing.JFrame {

    private Connection conn = new koneksi().connect();
    private static menu_utama instance;

    private Stack<javax.swing.JPanel> riwayatPanel = new Stack<>();
    private javax.swing.JPanel panelAktif;

    /**
     * Creates new form menu_utama
     */
    public menu_utama() {
        initComponents();

        instance = this;

        panelAktif = panel_menu_utama;

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setupResizeListener();

        String idKasir = UserID.getidadmin();
        label_id.setText(idKasir);
        String namaKasir = UserID.getnamaadmin();
        label_nama.setText(namaKasir);

        tampilkanTanggal_hariini();
        tampilalamatService();
        tampilkanWaktuSekarang();
        waktuBerjalan();

        tampilkanTotalData();
    }

    public static menu_utama getInstance() {
        return instance;
    }

    private void setupResizeListener() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                refreshLayeredPane();
            }
        });
    }

    private void refreshLayeredPane() {
        if (jLayeredPane1.getComponentCount() > 0) {
            java.awt.Component comp = jLayeredPane1.getComponent(0);
            comp.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
            jLayeredPane1.revalidate();
            jLayeredPane1.repaint();
        }
    }

    public void loadPanel(javax.swing.JPanel panel) {
        if (panelAktif != null) {
            riwayatPanel.push(panelAktif);
        }

        panelAktif = panel;
        tampilkanPanel(panel);
    }

    private void tampilkanPanel(javax.swing.JPanel panel) {
        jLayeredPane1.removeAll();

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        jLayeredPane1.add(scrollPane, java.awt.BorderLayout.CENTER);

        jLayeredPane1.revalidate();
        jLayeredPane1.repaint();
    }

    public void kembaliPanel() {
        if (!riwayatPanel.empty()) {
            javax.swing.JPanel panelSebelumnya = riwayatPanel.pop();
            panelAktif = panelSebelumnya;

            if (panelSebelumnya == panel_menu_utama) {
                tampilHalamanAwal();
            } else {
                tampilkanPanel(panelSebelumnya);
            }

        } else {
            tampilHalamanAwal();
        }
    }

    public void tampilHalamanAwal() {
        jLayeredPane1.removeAll();

        panelAktif = panel_menu_utama;
        tampilkanTotalData();

        jLayeredPane1.add(panel_menu_utama, java.awt.BorderLayout.CENTER);

        jLayeredPane1.revalidate();
        jLayeredPane1.repaint();
    }

    private void tampilkanTanggal_hariini() {
        Date tanggalSaatIni = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "EEEE, dd-MM-yyyy",
                new Locale("id", "ID")
        );
        String tanggalFormatted = formatter.format(tanggalSaatIni);
        tanggal_label.setText(tanggalFormatted);
    }

    private void tampilkanWaktuSekarang() {
        Date waktuSaatIni = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String waktuFormatted = formatter.format(waktuSaatIni);
        jam_label.setText("Jam " + waktuFormatted);
    }

    private void waktuBerjalan() {
        int delay = 1000;
        Timer waktu = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilkanWaktuSekarang();
            }
        });
        waktu.start();
    }

    private void tampilalamatService() {
        String teksAlamatPanjang = "Jl. Jatipadang Raya, gang menara 3 RT 004/04 no.58-B, Pasar Minggu, Jakarta Selatan Telp./WA. +62 813-8597-0988";
        alamat_label.setText("<html><p style=' text-align: center;'>" + teksAlamatPanjang + "</p></html>");
    }

    public void refreshNamaAdmin() {
        try {

            String sql = "SELECT nama_lengkap FROM admin WHERE id_admin = ?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, UserID.getidadmin());

            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                label_nama.setText(rs.getString("nama_lengkap"));
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Gagal memperbarui nama admin di menu utama: " + e
            );
        }
    }

    public void tampilkanTotalData() {
        try {

            String sql
                    = "SELECT "
                    + "(SELECT COUNT(*) FROM penyakit) AS total_penyakit, "
                    + "(SELECT COUNT(*) FROM gejala) AS total_gejala, "
                    + "(SELECT COUNT(*) FROM `rule`) AS total_rule, "
                    + "(SELECT COUNT(*) FROM diagnosa) AS total_diagnosis";

            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                angka_penyakit.setText(rs.getString("total_penyakit"));
                angka_gejala.setText(rs.getString("total_gejala"));
                angka_rule.setText(rs.getString("total_rule"));
                angka_diagnosis.setText(rs.getString("total_diagnosis"));
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Gagal menampilkan total data: " + e
            );
        }
    }

    private void isiComboDiagnosa(JComboBox<ItemDiagnosa> combo) {
        combo.removeAllItems();

        try {

            String sql = "SELECT id_diagnosa, nama_ikan "
                    + "FROM diagnosa "
                    + "ORDER BY id_diagnosa DESC";

            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                combo.addItem(new ItemDiagnosa(
                        rs.getString("id_diagnosa"),
                        rs.getString("nama_ikan")
                ));
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Gagal mengambil data diagnosa: " + e.getMessage()
            );
        }
    }

    private void tampilDialogCetakDiagnosa() {
        javax.swing.JDialog dialog = new javax.swing.JDialog(
                this,
                "Cetak Laporan Diagnosa",
                true
        );

        dialog.setSize(430, 190);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new java.awt.BorderLayout(10, 10));

        javax.swing.JPanel panelIsi = new javax.swing.JPanel(
                new java.awt.GridLayout(2, 1, 5, 5)
        );

        javax.swing.JLabel label = new javax.swing.JLabel(
                "Pilih Data Ikan yang Akan Dicetak:"
        );

        javax.swing.JComboBox<ItemDiagnosa> comboDiagnosa
                = new javax.swing.JComboBox<>();

        isiComboDiagnosa(comboDiagnosa);

        panelIsi.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 5, 15));
        panelIsi.add(label);
        panelIsi.add(comboDiagnosa);

        javax.swing.JPanel panelTombol = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT)
        );

        javax.swing.JButton btnCetak = new javax.swing.JButton("Cetak");
        javax.swing.JButton btnBatal = new javax.swing.JButton("Batal");

        panelTombol.add(btnCetak);
        panelTombol.add(btnBatal);

        dialog.add(panelIsi, java.awt.BorderLayout.CENTER);
        dialog.add(panelTombol, java.awt.BorderLayout.SOUTH);

        btnCetak.addActionListener(e -> {
            if (comboDiagnosa.getSelectedItem() == null) {
                javax.swing.JOptionPane.showMessageDialog(
                        dialog,
                        "Belum ada data diagnosa yang dapat dicetak."
                );
                return;
            }

            ItemDiagnosa item = (ItemDiagnosa) comboDiagnosa.getSelectedItem();

            cetakLaporanDiagnosa(item.getIdDiagnosa());

            dialog.dispose();
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void cetakLaporanDiagnosa(String idDiagnosa) {
        try {
            String loginId = UserID.getidadmin();
            String loginadmin = "Tidak Diketahui";

            try (java.sql.PreparedStatement nama = conn.prepareStatement(
                    "SELECT nama_lengkap FROM admin WHERE id_admin = ?")) {

                nama.setString(1, loginId);

                try (java.sql.ResultSet rsNama = nama.executeQuery()) {
                    if (rsNama.next()) {
                        loginadmin = rsNama.getString("nama_lengkap");
                    }
                }
            }

            String reportPath = "./src/report/rep_diagnosis.jasper";

            java.util.HashMap parameter = new java.util.HashMap();

            parameter.put("ID_DIAGNOSA", idDiagnosa);
            parameter.put("ADMIN", loginadmin);

            parameter.put(
                    net.sf.jasperreports.engine.JRParameter.REPORT_LOCALE,
                    new java.util.Locale("id", "ID")
            );

            java.util.Locale.setDefault(new java.util.Locale("id", "ID"));

            net.sf.jasperreports.engine.JasperPrint print
                    = net.sf.jasperreports.engine.JasperFillManager.fillReport(
                            reportPath,
                            parameter,
                            conn
                    );

            form.menu_utama menuUtama = form.menu_utama.getInstance();

            if (menuUtama != null) {
                javax.swing.JPanel reportPanel
                        = new javax.swing.JPanel(new java.awt.BorderLayout());

                net.sf.jasperreports.swing.JRViewer viewer
                        = new net.sf.jasperreports.swing.JRViewer(print);

                reportPanel.add(viewer, java.awt.BorderLayout.CENTER);

                menuUtama.loadPanel(reportPanel);
            } else {
                net.sf.jasperreports.view.JasperViewer.viewReport(print, false);
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Gagal mencetak laporan diagnosa: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panel_kiri = new javax.swing.JPanel();
        alamat_label = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        keluar_aplikasi = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        labeling4 = new javax.swing.JLabel();
        labeling5 = new javax.swing.JLabel();
        labeling6 = new javax.swing.JLabel();
        labeling7 = new javax.swing.JLabel();
        label_nama = new javax.swing.JLabel();
        label_id = new javax.swing.JLabel();
        jam_label = new javax.swing.JLabel();
        tanggal_label = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        menu_utama = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panel_menu_utama = new bg_menu_utama();
        panel_pen = new panel_keterangan();
        jLabel2 = new javax.swing.JLabel();
        angka_penyakit = new javax.swing.JLabel();
        panel_rule = new panel_keterangan();
        jLabel3 = new javax.swing.JLabel();
        angka_rule = new javax.swing.JLabel();
        panel_diag = new panel_keterangan();
        jLabel4 = new javax.swing.JLabel();
        angka_diagnosis = new javax.swing.JLabel();
        panel_gej = new panel_keterangan();
        jLabel10 = new javax.swing.JLabel();
        angka_gejala = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        menu_bar = new javax.swing.JMenuBar();
        menu_master = new javax.swing.JMenu();
        menu_penyakit = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menu_gejala = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menu_rule = new javax.swing.JMenuItem();
        menu_diagnosis = new javax.swing.JMenu();
        menu_riwayat_diagnosis = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        lap_penyakit = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        lap_gejala = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        lap_rule = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        lap_diagnosa = new javax.swing.JMenuItem();
        menu_informasi = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        panel_kiri.setBackground(new java.awt.Color(30, 16, 78));
        panel_kiri.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 3, new java.awt.Color(0, 0, 0)));

        alamat_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        alamat_label.setForeground(new java.awt.Color(255, 255, 255));
        alamat_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alamat_label.setText("Teks Alamat JP Farm Jakarta");
        alamat_label.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/profil.png"))); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(30, 16, 78), 3));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        keluar_aplikasi.setBackground(new java.awt.Color(255, 255, 255));
        keluar_aplikasi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        keluar_aplikasi.setText("KELUAR APLIKASI");
        keluar_aplikasi.setToolTipText("Keluar dari aplikasi kasir");
        keluar_aplikasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluar_aplikasiActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(30, 16, 78));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        labeling4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labeling4.setForeground(new java.awt.Color(255, 255, 255));
        labeling4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labeling4.setText("Nama");
        labeling4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        labeling5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labeling5.setForeground(new java.awt.Color(255, 255, 255));
        labeling5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeling5.setText(":");
        labeling5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        labeling6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labeling6.setForeground(new java.awt.Color(255, 255, 255));
        labeling6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labeling6.setText("ID");
        labeling6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        labeling7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labeling7.setForeground(new java.awt.Color(255, 255, 255));
        labeling7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeling7.setText(":");
        labeling7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        label_nama.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_nama.setForeground(new java.awt.Color(255, 255, 255));
        label_nama.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_nama.setText("Nama Admin");
        label_nama.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        label_id.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_id.setForeground(new java.awt.Color(255, 255, 255));
        label_id.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_id.setText("Id Admin");
        label_id.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jam_label.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jam_label.setForeground(new java.awt.Color(255, 255, 255));
        jam_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jam_label.setText("Jam Hari Ini");

        tanggal_label.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tanggal_label.setForeground(new java.awt.Color(255, 255, 255));
        tanggal_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tanggal_label.setText("Tanggal Hari Ini");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(labeling6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labeling7))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(labeling4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labeling5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_nama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(tanggal_label, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jam_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jSeparator13)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labeling6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeling7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labeling4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeling5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tanggal_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jam_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/admin.png"))); // NOI18N
        jLabel9.setText("ADMIN");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/cog_edit.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        menu_utama.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        menu_utama.setText("MENU UTAMA");
        menu_utama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_utamaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_kiriLayout = new javax.swing.GroupLayout(panel_kiri);
        panel_kiri.setLayout(panel_kiriLayout);
        panel_kiriLayout.setHorizontalGroup(
            panel_kiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_kiriLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_kiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_kiriLayout.createSequentialGroup()
                        .addGroup(panel_kiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(keluar_aplikasi, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alamat_label, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel_kiriLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panel_kiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(menu_utama, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_kiriLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71)))))
                .addContainerGap())
        );
        panel_kiriLayout.setVerticalGroup(
            panel_kiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_kiriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(panel_kiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menu_utama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(keluar_aplikasi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alamat_label, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.setBackground(new java.awt.Color(102, 102, 255));
        jLayeredPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jLayeredPane1.setLayout(new java.awt.BorderLayout());

        panel_menu_utama.setBackground(new java.awt.Color(69, 46, 90));

        panel_pen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("TOTAL PENYAKIT");

        angka_penyakit.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        angka_penyakit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        angka_penyakit.setText("0");

        javax.swing.GroupLayout panel_penLayout = new javax.swing.GroupLayout(panel_pen);
        panel_pen.setLayout(panel_penLayout);
        panel_penLayout.setHorizontalGroup(
            panel_penLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(angka_penyakit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_penLayout.setVerticalGroup(
            panel_penLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_penLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(angka_penyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );

        panel_rule.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("TOTAL RULE");

        angka_rule.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        angka_rule.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        angka_rule.setText("0");

        javax.swing.GroupLayout panel_ruleLayout = new javax.swing.GroupLayout(panel_rule);
        panel_rule.setLayout(panel_ruleLayout);
        panel_ruleLayout.setHorizontalGroup(
            panel_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(angka_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_ruleLayout.setVerticalGroup(
            panel_ruleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_ruleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(angka_rule, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );

        panel_diag.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("TOTAL DIAGNOSIS");

        angka_diagnosis.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        angka_diagnosis.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        angka_diagnosis.setText("0");

        javax.swing.GroupLayout panel_diagLayout = new javax.swing.GroupLayout(panel_diag);
        panel_diag.setLayout(panel_diagLayout);
        panel_diagLayout.setHorizontalGroup(
            panel_diagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(angka_diagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_diagLayout.setVerticalGroup(
            panel_diagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_diagLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(angka_diagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );

        panel_gej.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("TOTAL GEJALA");

        angka_gejala.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        angka_gejala.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        angka_gejala.setText("0");

        javax.swing.GroupLayout panel_gejLayout = new javax.swing.GroupLayout(panel_gej);
        panel_gej.setLayout(panel_gejLayout);
        panel_gejLayout.setHorizontalGroup(
            panel_gejLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(angka_gejala, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_gejLayout.setVerticalGroup(
            panel_gejLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_gejLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(angka_gejala, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Aplikasi dibuat oleh Ahmad Nur Latif Prayoga - 202243501659 - ahmadyoga2812@gmail.com");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 38)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Aplikasi Sistem Pakar Diagnosis Penyakit Ikan Mas Koki");

        javax.swing.GroupLayout panel_menu_utamaLayout = new javax.swing.GroupLayout(panel_menu_utama);
        panel_menu_utama.setLayout(panel_menu_utamaLayout);
        panel_menu_utamaLayout.setHorizontalGroup(
            panel_menu_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menu_utamaLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(panel_menu_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panel_diag, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_menu_utamaLayout.createSequentialGroup()
                        .addComponent(panel_pen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_gej, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(66, 66, 66))
        );
        panel_menu_utamaLayout.setVerticalGroup(
            panel_menu_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menu_utamaLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(29, 29, 29)
                .addGroup(panel_menu_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_pen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_rule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_gej, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(panel_diag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(362, 362, 362)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        jLayeredPane1.add(panel_menu_utama, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(panel_kiri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_kiri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLayeredPane1)
        );

        menu_bar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menu_bar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        menu_bar.setName(""); // NOI18N
        menu_bar.setPreferredSize(new java.awt.Dimension(344, 35));

        menu_master.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/DaftarPeminjaman.png"))); // NOI18N
        menu_master.setText("Data Master |");
        menu_master.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        menu_penyakit.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        menu_penyakit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/book_open.png"))); // NOI18N
        menu_penyakit.setText("Data Penyakit");
        menu_penyakit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_penyakitActionPerformed(evt);
            }
        });
        menu_master.add(menu_penyakit);
        menu_master.add(jSeparator1);

        menu_gejala.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        menu_gejala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/book_open.png"))); // NOI18N
        menu_gejala.setText("Data Gejala");
        menu_gejala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_gejalaActionPerformed(evt);
            }
        });
        menu_master.add(menu_gejala);
        menu_master.add(jSeparator2);

        menu_rule.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        menu_rule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/book_open.png"))); // NOI18N
        menu_rule.setText("Data Rule");
        menu_rule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_ruleActionPerformed(evt);
            }
        });
        menu_master.add(menu_rule);

        menu_bar.add(menu_master);
        menu_master.getAccessibleContext().setAccessibleDescription("");

        menu_diagnosis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/book_edit.png"))); // NOI18N
        menu_diagnosis.setText("Diagnosis |");
        menu_diagnosis.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu_diagnosis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_diagnosisMouseClicked(evt);
            }
        });
        menu_bar.add(menu_diagnosis);
        menu_diagnosis.getAccessibleContext().setAccessibleDescription("");

        menu_riwayat_diagnosis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/book.png"))); // NOI18N
        menu_riwayat_diagnosis.setText("Riwayat Diagnosis |");
        menu_riwayat_diagnosis.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu_riwayat_diagnosis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_riwayat_diagnosisMouseClicked(evt);
            }
        });
        menu_bar.add(menu_riwayat_diagnosis);
        menu_riwayat_diagnosis.getAccessibleContext().setAccessibleDescription("");

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/folder_page.png"))); // NOI18N
        jMenu4.setText("Laporan |");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        lap_penyakit.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lap_penyakit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/page_white_acrobat.png"))); // NOI18N
        lap_penyakit.setText("Laporan Data Penyakit");
        lap_penyakit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lap_penyakitActionPerformed(evt);
            }
        });
        jMenu4.add(lap_penyakit);
        jMenu4.add(jSeparator3);

        lap_gejala.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lap_gejala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/page_white_acrobat.png"))); // NOI18N
        lap_gejala.setText("Laporan Data Gejala");
        jMenu4.add(lap_gejala);
        jMenu4.add(jSeparator4);

        lap_rule.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lap_rule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/page_white_acrobat.png"))); // NOI18N
        lap_rule.setText("Laporan Data Rule");
        jMenu4.add(lap_rule);
        jMenu4.add(jSeparator5);

        lap_diagnosa.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lap_diagnosa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/page_white_acrobat.png"))); // NOI18N
        lap_diagnosa.setText("Laporan Diagnosis");
        lap_diagnosa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lap_diagnosaActionPerformed(evt);
            }
        });
        jMenu4.add(lap_diagnosa);

        menu_bar.add(jMenu4);
        jMenu4.getAccessibleContext().setAccessibleDescription("");

        menu_informasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/information.png"))); // NOI18N
        menu_informasi.setText("Informasi |");
        menu_informasi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu_informasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_informasiMouseClicked(evt);
            }
        });
        menu_bar.add(menu_informasi);

        setJMenuBar(menu_bar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void keluar_aplikasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluar_aplikasiActionPerformed
        int konfirmasi_keluarapp = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        if (konfirmasi_keluarapp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_keluar_aplikasiActionPerformed

    private void menu_penyakitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_penyakitActionPerformed
        loadPanel(new master_penyakit().getMainPanel());
    }//GEN-LAST:event_menu_penyakitActionPerformed

    private void menu_gejalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_gejalaActionPerformed
        loadPanel(new master_gejala().getMainPanel());
    }//GEN-LAST:event_menu_gejalaActionPerformed

    private void menu_ruleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_ruleActionPerformed
        loadPanel(new master_rule().getMainPanel());
    }//GEN-LAST:event_menu_ruleActionPerformed

    private void menu_informasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_informasiMouseClicked
        javax.swing.JLabel pesan = new javax.swing.JLabel(
                "<html>"
                + "<div style='text-align: center; font-family: Tahoma; font-size: 14px; width: 520px;'>"
                + "<b style='font-size: 18px;'>Aplikasi Sistem Pakar Diagnosis Penyakit Ikan Mas Koki</b><br><br>"
                + "Aplikasi ini dibuat untuk membantu proses diagnosis awal penyakit<br>"
                + "pada ikan mas koki berdasarkan gejala-gejala yang dipilih oleh pengguna.<br>"
                + "Sistem ini menggunakan metode Forward Chaining sebagai proses penalaran<br>"
                + "untuk menentukan kemungkinan penyakit yang dialami oleh ikan.<br><br>"
                + "Aplikasi ini dibuat untuk memenuhi kebutuhan penyusunan Tugas Akhir<br>"
                + "pada Program Studi Teknik Informatika.<br><br>"
                + "<b>Judul Tugas Akhir:</b><br>"
                + "Sistem Pakar Diagnosis Penyakit Ikan Mas Koki Menggunakan Metode<br>"
                + "Forward Chaining pada JP Farm Jakarta<br><br>"
                + "<b>Identitas Pengembang:</b><br>"
                + "Nama&nbsp;&nbsp;: Ahmad Nur Latif Prayoga<br>"
                + "NPM&nbsp;&nbsp;&nbsp;: 202243501659<br>"
                + "Prodi&nbsp;: Teknik Informatika<br>"
                + "E-Mail: ahmadyoga2812@gmail.com<br><br>"
                + "<b>Tempat Penelitian:</b><br>"
                + "JP Farm Jakarta"
                + "</div>"
                + "</html>"
        );

        pesan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.JOptionPane.showMessageDialog(
                this,
                pesan,
                "Informasi Aplikasi",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_menu_informasiMouseClicked

    private void menu_diagnosisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_diagnosisMouseClicked
        loadPanel(new diagnosis().getMainPanel());
    }//GEN-LAST:event_menu_diagnosisMouseClicked

    private void menu_riwayat_diagnosisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_riwayat_diagnosisMouseClicked
        loadPanel(new riwayat_diagnosis().getMainPanel());
    }//GEN-LAST:event_menu_riwayat_diagnosisMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadPanel(new master_admin().getMainPanel());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void lap_penyakitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lap_penyakitActionPerformed
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

            String reportPath = "./src/report/rep_penyakit.jasper";
            HashMap parameter = new HashMap();
            parameter.put("ADMIN", loginadmin);

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
    }//GEN-LAST:event_lap_penyakitActionPerformed

    private void lap_diagnosaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lap_diagnosaActionPerformed
        tampilDialogCetakDiagnosa();
    }//GEN-LAST:event_lap_diagnosaActionPerformed

    private void menu_utamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_utamaActionPerformed
        form.menu_utama menuUtama = form.menu_utama.getInstance();
        if (menuUtama != null) {
            menuUtama.tampilHalamanAwal();
        }
    }//GEN-LAST:event_menu_utamaActionPerformed

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
            java.util.logging.Logger.getLogger(menu_utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu_utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu_utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu_utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menu_utama().setVisible(true);
            }
        });
    }

    private class ItemDiagnosa {

        private String idDiagnosa;
        private String namaIkan;

        public ItemDiagnosa(String idDiagnosa, String namaIkan) {
            this.idDiagnosa = idDiagnosa;
            this.namaIkan = namaIkan;
        }

        public String getIdDiagnosa() {
            return idDiagnosa;
        }

        @Override
        public String toString() {
            return namaIkan;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alamat_label;
    private javax.swing.JLabel angka_diagnosis;
    private javax.swing.JLabel angka_gejala;
    private javax.swing.JLabel angka_penyakit;
    private javax.swing.JLabel angka_rule;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JLabel jam_label;
    private javax.swing.JButton keluar_aplikasi;
    private javax.swing.JLabel label_id;
    private javax.swing.JLabel label_nama;
    private javax.swing.JLabel labeling4;
    private javax.swing.JLabel labeling5;
    private javax.swing.JLabel labeling6;
    private javax.swing.JLabel labeling7;
    private javax.swing.JMenuItem lap_diagnosa;
    private javax.swing.JMenuItem lap_gejala;
    private javax.swing.JMenuItem lap_penyakit;
    private javax.swing.JMenuItem lap_rule;
    private javax.swing.JMenuBar menu_bar;
    private javax.swing.JMenu menu_diagnosis;
    private javax.swing.JMenuItem menu_gejala;
    private javax.swing.JMenu menu_informasi;
    private javax.swing.JMenu menu_master;
    private javax.swing.JMenuItem menu_penyakit;
    private javax.swing.JMenu menu_riwayat_diagnosis;
    private javax.swing.JMenuItem menu_rule;
    private javax.swing.JButton menu_utama;
    private javax.swing.JPanel panel_diag;
    private javax.swing.JPanel panel_gej;
    private javax.swing.JPanel panel_kiri;
    private javax.swing.JPanel panel_menu_utama;
    private javax.swing.JPanel panel_pen;
    private javax.swing.JPanel panel_rule;
    private javax.swing.JLabel tanggal_label;
    // End of variables declaration//GEN-END:variables
}

class bg_menu_utama extends JPanel {

    private Image menuUtama_bg;

    public bg_menu_utama() {
        menuUtama_bg = new ImageIcon(getClass().getResource("/gambar/bg_menu_utama.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(menuUtama_bg, 0, 0, getWidth(), getHeight(), this);
    }
}

class panel_keterangan extends JPanel {

    public panel_keterangan() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(240, 240, 240, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
