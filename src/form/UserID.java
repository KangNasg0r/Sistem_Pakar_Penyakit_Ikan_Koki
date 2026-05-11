package form;

public class UserID {

    private static String id_admin;
    private static String nama;

    static void setidadmin(String id) {
        UserID.id_admin = id;
    }

    public static String getidadmin() {
        return id_admin;
    }

    public static void setnamaadmin(String namaadmin) {
        UserID.nama = namaadmin;
    }

    public static String getnamaadmin() {
        return nama;
    }
}
