package com.mazenetsolutions.mzs119.skst_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.EnglishNumberToWords;


import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.N)
public class PrintActivityAdvance extends AppCompatActivity {
    // Debugging
    private static final String TAG = "BloothPrinterActivity";
    private static final boolean D = true;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView txt_name, txt_receive, txt_pending, txt_advance;
    String date = "", party = "", address = "", prty_gstin = "", phone = "", grand_total = "", cmpny_gst = "4587GH8VF875K", cmpny_phone = "",
            owner_nme = "", cmpny_street = "",
            cmpny_city = "", grnd = "", sg = "", cg = "", igs = "", tx_incld = "", customer_state = "", tx_incld1 = "", igst1 = "", bill_no = "0001";
    String[] item_array;
    Locale curLocale;
    String Cusname = "", Amount = "", Groupname = "", ReceiptAmount = "", pendingnow = "", ticketno = "", paymode = "", Receiptno = "", pendingamnt = "", penaltyamnt = "", instlmntamnt = "", bonusamnt = "", username = "", due = "", cheno = "", chedate = "", chebank = "", totalamnt = "", recid = "", Branch_Name = "";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    TextView crumbs;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static int revBytes = 0;
    public static boolean isHex = false;
    String PAGENAME = "Print Advance Receipt";
    public static final int REFRESH = 8;


    private Button mBtnConnetBluetoothDevice = null;
    private Button mBtnPrint = null;

    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;

    public PrintActivityAdvance() throws ParseException {
    }

    @Override
    public void onBackPressed() {
//        String crumb = pref.getString("Crumbs", "");
//        System.out.println("crumb " + crumb);
//        crumb = crumb.replace(PAGENAME, "");
//        crumb = crumb.trim();
//        System.out.println("crumb bwf " + crumb+"5");
//        if (crumb.endsWith(">")) {
//            crumb = crumb.substring(0, crumb.length() - 1);
//            System.out.println("crumb aft " + crumb);
////            editor.putString("Crumbs", crumb);
////            editor.commit();
//        }
//        if (crumb.endsWith(">")) {
//            crumb = crumb.substring(0, crumb.length() - 1);
//            System.out.println("crumb after  " + crumb);
//            editor.putString("Crumbs", crumb);
//            editor.commit();
//        }

        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Date date1 = new Date();
        date = dateFormat.format(date1);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        editor.putString("Crumbs", pref.getString("Crumbs", "") + " >> " + PAGENAME);
        editor.commit();

        username = pref.getString("username", "");
        username = username.replaceAll(" ", "");
        if (D) Log.e(TAG, "+++ ON CREATE +++");

        curLocale = new Locale("en", "IN");

        setContentView(R.layout.main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // �����ʼ��
        InitUIControl();
        //fininvoice = "vjv-8";
        try {
            Intent it = getIntent();
            Cusname = it.getStringExtra("Cusname");
            System.out.println("cusname " + Cusname);
            Amount = it.getStringExtra("ReceivedAmount");
            System.out.println("Amount " + Amount);
            ticketno = it.getStringExtra("ticketno");
            System.out.println("ticketno " + ticketno);
            paymode = it.getStringExtra("paymode");
            System.out.println("paymode " + paymode);
            cheno = it.getStringExtra("cheno");
            System.out.println("cheno " + cheno);
            chebank = it.getStringExtra("chebank");
            System.out.println("chebank " + chebank);
            chedate = it.getStringExtra("chedate");
            System.out.println("chedate " + chedate);
//            Branch_Name = it.getStringExtra("Branch_Name");
            totalamnt = it.getStringExtra("TotalAmount");
            System.out.println("totalamnt " + totalamnt);
            Receiptno = it.getStringExtra("Receiptno");
            System.out.println("Receiptno " + Receiptno);
            Groupname = it.getStringExtra("Groupname");
            System.out.println("Groupname " + Groupname);
            if (Groupname.equalsIgnoreCase("null")) {
                Groupname = "";
            }
//            subid = it.getStringExtra("subid");
//            System.out.println("subid " + subid);
//            mobile = it.getStringExtra("mobile");
//            System.out.println("mobile " + mobile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void InitUIControl() {

        mBtnConnetBluetoothDevice = (Button) findViewById(R.id.btn_connect_bluetooth_device);
        mBtnConnetBluetoothDevice.setOnClickListener(mBtnConnetBluetoothDeviceOnClickListener);
        mBtnPrint = (Button) findViewById(R.id.btn_print);
        mBtnPrint.setOnClickListener(mBtnPrintOnClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
//            builddilogdecline();
            if (mChatService == null) setupChat();
        }
    }

    private void connectautomatic() {
        Intent serverIntent = null;
        serverIntent = new Intent(PrintActivityAdvance.this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void builddilogdecline() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PrintActivityAdvance.this, R.style.MyAlertDialogStyleDeclined);
        alertDialog.setTitle("Information");
//        alertDialog.setIcon(R.drawable.ic_print_red);
        alertDialog
                .setMessage("Connect Device to Print the Receipt");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");


        if (mChatService != null) {
            if (mChatService.getState() == BluetoothPrintDriver.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        mChatService = new BluetoothPrintDriver(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    @SuppressLint("NewApi")
    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }


    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:

                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:

                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                        case BluetoothPrintDriver.STATE_NONE:

                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String ErrorMsg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    float Voltage = 0;
                    if (D)
                        Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  readBuf[1]:" + readBuf[1] + "  readBuf[2]:" + readBuf[2]);
                    if (readBuf[2] == 0)
                        ErrorMsg = "NO ERROR!         ";
                    else {
                        if ((readBuf[2] & 0x02) != 0)
                            ErrorMsg = "ERROR: No printer connected!";
                        if ((readBuf[2] & 0x04) != 0)
                            ErrorMsg = "ERROR: No paper!  ";
                        if ((readBuf[2] & 0x08) != 0)
                            ErrorMsg = "ERROR: Voltage is too low!  ";
                        if ((readBuf[2] & 0x40) != 0)
                            ErrorMsg = "ERROR: Printer Over Heat!  ";
                    }
                    Voltage = (float) ((readBuf[0] * 256 + readBuf[1]) / 10.0);
                    //if(D) Log.i(TAG, "Voltage: "+Voltage);
                    DisplayToast(ErrorMsg + "                                        " + "Battery voltage��" + Voltage + " V");
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    mBtnPrint.setVisibility(View.VISIBLE);
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //��ʾ��Ϣ
    public void showMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }//showMessage

    // ��ʾToast
    public void DisplayToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        //����toast��ʾ��λ��
        toast.setGravity(Gravity.TOP, 0, 100);
        //��ʾ��Toast
        toast.show();
    }//DisplayToast

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    //Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    OnClickListener mBtnQuitOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Stop the Bluetooth chat services
            if (mChatService != null) mChatService.stop();
            finish();
        }
    };

    OnClickListener mBtnConnetBluetoothDeviceOnClickListener = new OnClickListener() {
        Intent serverIntent = null;

        public void onClick(View arg0) {
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(PrintActivityAdvance.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }
    };

    OnClickListener mBtnPrintOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean connected = pref.getBoolean("connected", Boolean.parseBoolean(""));
            if (!connected) {
                mBtnConnetBluetoothDevice.setVisibility(View.VISIBLE);
            } else {
                mBtnConnetBluetoothDevice.setVisibility(View.GONE);
                connectautomatic();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (BluetoothPrintDriver.IsNoConnection()) {
                            return;
                        }
                        BluetoothPrintDriver.Begin();

                        // Receiptno = pref.getString("receiptno", "");
                        //    ReceiptAmount = pref.getString("receiptamnt", "");

                        String Billname = Receiptno;
                        String Dateprint = "Date : " + date;


                        String owner_nme = "MPV Chit Funds", cmpny_street = pref.getString("B_Address", ""), cmpny_city = pref.getString("B_city", "");
                        String Toparty = "Name :" + Cusname;
                        String subtotal = Amount;
                        String payment = paymode;
                        String Group = Groupname;
                        String ticket = ticketno;

                        int lengthsubtotal = subtotal.length();
                        if (lengthsubtotal <= 32) {
                            int dumlength = 9 - lengthsubtotal;
                            for (int j = 0; j < dumlength; j++) {
                                subtotal = " " + subtotal;
                            }

                        } else {
                            //  itemname = itemname.substring(0, 19) + ".";
                        }

                        if (Receiptno.isEmpty()) {

                        } else {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("Receipt No : " + Receiptno + "\n");
                            BluetoothPrintDriver.LF();
                        }


                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write(Dateprint + "\n");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x01);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//����

                        BluetoothPrintDriver.BT_Write(owner_nme + "\n");
                        BluetoothPrintDriver.SetBold((byte) 0x00);//����
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write(cmpny_street + ",\n");
                        BluetoothPrintDriver.LF();

//            BluetoothPrintDriver.SetAlignMode((byte) 1);
//            BluetoothPrintDriver.BT_Write(cmpny_city + ", Pin:" + pref.getString("B_pincode", ""));
//            BluetoothPrintDriver.BT_Write("\n");
//            BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write(cmpny_city + ", Ph.No:" + pref.getString("B_brnchphone", ""));
                        BluetoothPrintDriver.BT_Write("\n");
                        BluetoothPrintDriver.LF();

//            BluetoothPrintDriver.SetAlignMode((byte) 1);
//            BluetoothPrintDriver.BT_Write("GST No: " + cmpny_gst);
//            BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write("\n");
                        BluetoothPrintDriver.LF();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//����
                        BluetoothPrintDriver.LF();
                        BluetoothPrintDriver.BT_Write(Toparty + "\n");
                        BluetoothPrintDriver.SetBold((byte) 0x00);//����
                        BluetoothPrintDriver.LF();
//            if (subid.isEmpty()) {
//
//            } else {
//                BluetoothPrintDriver.SetAlignMode((byte) 0);
//                BluetoothPrintDriver.BT_Write("Customer Id : ");
//                BluetoothPrintDriver.BT_Write(subid + "\n");
//                BluetoothPrintDriver.LF();
//            }
//            if (mobile.isEmpty()) {
//
//            } else {
//                BluetoothPrintDriver.SetAlignMode((byte) 0);
//                BluetoothPrintDriver.BT_Write("Mobile : ");
//                BluetoothPrintDriver.BT_Write(mobile + "\n");
//                BluetoothPrintDriver.LF();
//            }


                        BluetoothPrintDriver.BT_Write(String.format("--------------------------------\n"), true);
                        try {
                            if (Groupname.equalsIgnoreCase("")) {

                            } else {
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.BT_Write("Group/Ticket No : ");
                                //  int grouplen = Group.length();
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                                BluetoothPrintDriver.BT_Write(Groupname + " / " + ticketno + "\n");
                                BluetoothPrintDriver.LF();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
  /*
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Received Amount : ");
           int amntlen=Amount.length();
            if (amntlen <= 32) {
                int dumlength = 32 - amntlen;
                for (int j = 0; j < dumlength; j++) {
                    Amount = " "+Amount ;
                }
            } else {
            } *
            BluetoothPrintDriver.BT_Write(Amount + " Rs" + "\n");
            BluetoothPrintDriver.LF();
*/
          /*  BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Ticket No. : ");
           // int grouplen=Group.length();
            if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            }
            BluetoothPrintDriver.BT_Write(ticket+"\n");
            BluetoothPrintDriver.LF();*/

          /*  BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Installment : ");
     /*       int instllenth=instlmntamnt.length();
            if (instllenth <= 32) {
                int dumlength = 32 - instllenth;
                for (int j = 0; j < dumlength; j++) {
                    instlmntamnt = " "+instlmntamnt ;
                }

            } else {

            }
            BluetoothPrintDriver.BT_Write(pendingamnt+" Rs"+"\n");
            BluetoothPrintDriver.LF(); */
/*
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Penalty : ");
            int penlenth=penaltyamnt.length();
            if (penlenth <= 32) {
                int dumlength = 32 - penlenth;
                for (int j = 0; j < dumlength; j++) {
                    penaltyamnt = " "+penaltyamnt ;
                }
            } else {
            } *
            BluetoothPrintDriver.BT_Write(penaltyamnt + " Rs" + "\n");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Bonus : ");
      /*      int bonlen=bonusamnt.length();
            if (penlenth <= 32) {
                int dumlength = 32 - bonlen;
                for (int j = 0; j < dumlength; j++) {
                    bonusamnt = " "+bonusamnt ;
                }
            } else {
            }
            BluetoothPrintDriver.BT_Write(bonusamnt + " Rs" + "\n");
            BluetoothPrintDriver.LF();
 */

                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write("Received Amount : ");
       /*      int amntlen=Amount.length();
            if (amntlen <= 32) {
                int dumlength = 32 - amntlen;
                for (int j = 0; j < dumlength; j++) {
                    Amount = " "+Amount ;
                }
            } else {
            } */
                        String return_val_in_english = EnglishNumberToWords.convert(Integer.parseInt(Amount));
                        try {
                            Double d = Double.parseDouble(Amount);
                            String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                            Amount = moneyString1;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        BluetoothPrintDriver.BT_Write(Amount + " (" + return_val_in_english + " only)" + "\n");
                        BluetoothPrintDriver.LF();
                        if (totalamnt.equalsIgnoreCase("")) {

                        } else {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("Total Advance Amount : ");
                            // int grouplen=Group.length();
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                            String return_val_in_english1 = EnglishNumberToWords.convert(Integer.parseInt(totalamnt));
                            try {
                                Double d = Double.parseDouble(totalamnt);
                                String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                                totalamnt = moneyString1;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            BluetoothPrintDriver.BT_Write(totalamnt + " (" + return_val_in_english1 + " only)" + "\n");
                            BluetoothPrintDriver.LF();
                        }


                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write("Payment Mode : ");
       /*     int paylen=paymode.length();
            if (amntlen <= 32) {
                int dumlength = 32 - paylen;
                for (int j = 0; j < dumlength; j++) {
                    paymode = " "+paymode ;
                }
            } else {
            } */
                        BluetoothPrintDriver.BT_Write(paymode + "\n");
                        BluetoothPrintDriver.LF();
                        if (paymode.equalsIgnoreCase("cheque")) {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("Cheque No. : ");
                            // int grouplen=Group.length();
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                            BluetoothPrintDriver.BT_Write(cheno + "\n");
                            BluetoothPrintDriver.LF();

                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("Cheque Bank : ");
                            BluetoothPrintDriver.BT_Write(chebank + "\n");
                            BluetoothPrintDriver.LF();
//                BluetoothPrintDriver.SetAlignMode((byte) 0);
//                BluetoothPrintDriver.BT_Write("Cheque Branch : ");
//                BluetoothPrintDriver.BT_Write(Branch_Name + "\n");
//                BluetoothPrintDriver.LF();

                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("Cheque Date : ");
                            // int grouplen=Group.length();
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                            BluetoothPrintDriver.BT_Write(chedate + "\n");
                            BluetoothPrintDriver.LF();
                        } else if (paymode.equalsIgnoreCase("dd")) {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("DD No. : ");
                            // int grouplen=Group.length();
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                            BluetoothPrintDriver.BT_Write(cheno + "\n");
                            BluetoothPrintDriver.LF();

                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("DD Bank : ");
                            // int grouplen=Group.length();
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                            BluetoothPrintDriver.BT_Write(chebank + "\n");
                            BluetoothPrintDriver.LF();

                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("DD Date : ");
                            // int grouplen=Group.length();
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                            BluetoothPrintDriver.BT_Write(chedate + "\n");
                            BluetoothPrintDriver.LF();
                        }
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write(String.format("--------------------------------\n"), true);// ┏
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.BT_Write("*System generated Bill.\nNo signature Needed\n");
                        BluetoothPrintDriver.LF();
                        if (paymode.equalsIgnoreCase("cheque")) {
                            BluetoothPrintDriver.BT_Write("*Subject to Realization");
                            BluetoothPrintDriver.LF();
                        }
                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write("Prepared by : " + username);
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.BT_Write("\nThank You \n");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
                        BluetoothPrintDriver.BT_Write("\n\n");
                        BluetoothPrintDriver.LF();

                        Intent i = new Intent(PrintActivityAdvance.this, CollectionActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }, 1500);
//            updateprintedstatus();
            }
        }
    };
//    private void updateprintedstatus() {
//        StringRequest movieReq = new StringRequest(Request.Method.POST,
//                Config.updateprint_advance, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("Collection Activity", response.toString());
//
//                try {
//
//
//                    JSONObject object = new JSONObject(response);
//                    String status = object.getString("status");
//                    if (status.equalsIgnoreCase("1")) {
//                        System.out.println("updated");
//                        finish();
//                    } else {
//                        System.out.println("Not updated");
//                        finish();
//                    }
//
//
//                } catch (JSONException e) {
//                    finish();
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener()
//
//        {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                finish();
//
//            }
//        })
//
//        {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("receiptid", recid);
//                return params;
//
//
//            }
//
//        };
//        movieReq.setRetryPolicy(new DefaultRetryPolicy(10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(movieReq);
//    }

    OnClickListener mBtnTestOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (BluetoothPrintDriver.IsNoConnection()) {
                return;
            }
            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SelftestPrint();    //��ӡ�Բ�ҳ
        }
    };
    OnClickListener mBtnInquiryOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Log.i(TAG, "inquiry btn");
            if (BluetoothPrintDriver.IsNoConnection()) {
                return;
            }
            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.StatusInquiry();    // ��ѯ����״̬���ص�ѹ

        }
    };
}