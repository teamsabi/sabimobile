package com.example.ngikngik;

public class DbContract {
    static final String IP = "192.168.18.231";
//    static final String IP = "10.10.181.181";
    public static final String SERVER_LOGIN_URL = "http://" + IP + "/db_sabiproject/checklogin.php";
    public static final String SERVER_REGISTER_URL = "http://" + IP + "/db_sabiproject/createData.php";
    public static final String SERVER_LUPA_PASSWORD_URL = "http://" + IP + "/db_sabiproject/resetpassword.php";
    public static final String SERVER_VERIF_OTP_URL = "http://" + IP + "/db_sabiproject/verifotp.php";
    public static final String SERVER_NEWPASSWORD_URL= "http://" + IP + "/db_sabiproject/newpassword.php";
    public static final String SERVER_RESEND_OTP_URL = "http://" + IP + "/db_sabiproject/resend_otp";
    public static final String SERVER_DAFTAR_URL = "http://" + IP + "/db_sabiproject/daftaruser.php";

}
