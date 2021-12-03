package com.mazenetsolutions.mzs119.skst_new.Utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Config {
//  static String loginurl = "http://kopuramonline.com/Testing/Mobile/";

    //http://www.mpvchitfunds.in/app/


 static String loginurl = "http://www.mpvchitfunds.in/app_test/Mobile/";
// static String loginurl = "http://www.mpvchitfunds.in/app/Mobile/";
   // static String loginurl = "http://www.mpvchitfunds.in/app/Mobile/";
    public static String preff = "MPVCHITS";

    public static String login = loginurl + "registerit.php?";
    public static String deviceapprove = loginurl + "deviceapprove.php?";
    public static String get_registration = loginurl + "get_registration.php?";
    public static String reteriveusers = loginurl + "reteriveusers.php?";
    public static String sendfeedback = loginurl + "sendfeedback.php?";
    public static String branchmaster = loginurl + "branchmaster.php?";
    public static String collectagent = loginurl + "enrollmaster.php?";
    public static String mobile_receipt_print = loginurl + "mobile_receipt_print.php?";
    public static String getcustinfo = loginurl + "getcustinfo.php?";
    public static String registeractivity = loginurl + "registerit.php?";
    public static String getallviewbydate = loginurl + "viewbyemp.php?";
    public static String retreivegroups = loginurl + "reterivegroups.php?";
    public static String getcashinhand = loginurl + "cashinhand.php?";
    public static String settleurl = loginurl + "save_cash_settlement.php?";
    public static String reterivefeedback = loginurl + "reterivefeedback.php?";
    public static String reteriveenroll = loginurl + "reteriveenroll.php?";
    public static String saverecepit = loginurl + "saverecepit.php?";
    public static String retrievealluser = loginurl + "reteriveallusers.php?";
    public static String getreceiptno = loginurl + "getreceiptno.php?";
    public static String Enroll_update_cust = loginurl + "Enroll_update_cust.php?";
    public static String reteriveindienroll = loginurl + "retrieveindividualenroll.php?";
    public static String reteriveindienroll_adv = loginurl + "retrieveindividualenroll_adv.php?";
    public static String sendlocalityoffline = loginurl + "retrieveindividualenroll.php?";
    public static String get_version = loginurl + "get_version.php";
    public static String dailyreceipt = loginurl + "dailyadvancedreceipt.php";
    public static String get_advances = loginurl + "get_advances.php";
    public static String get_location = loginurl + "get_location.php";
    public static String update_location = loginurl + "update_location.php";
    public static String availableadvance = loginurl + "getdailyadvanceavailable.php?";
    public static String getavailableadvance = loginurl + "advanceavailablewithcust.php?";
    public static String getalladvancebydate = loginurl + "advancerecdate.php?";
    public static String get_collfeedback = loginurl + "get_collfeedback.php?";
    public static String send_receipt_sms = loginurl + "send_receipt_sms.php?";
    public static String send_advance_sms = loginurl + "send_advance_sms.php?";
    public static String totalavailableadvance = loginurl + "getadvanceavailable.php?";
    public static String getstates = loginurl + "states.php?";
    public static String saveform = loginurl + "save_customer.php?";
    public static String getdistricts = loginurl + "district.php?";
    public static String getdcity = loginurl + "city.php?";
    public static String getdpincode = loginurl + "pincode.php?";
    public static String add_city = loginurl + "add_city.php?";
    public static boolean isconnected;
// ============================================================================

    public static String FONTPATHMAIN = "fonts/Rajdhani-SemiBold.ttf";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final String jsonarraybranch = "branch";

    // ============================================================================
    public static String Currenttime(){
       SimpleDateFormat HourMinuteSeconds =new  SimpleDateFormat("HH:mm:ss");
        Date c = Calendar.getInstance().getTime();
        return HourMinuteSeconds.format(c);
    }
}
