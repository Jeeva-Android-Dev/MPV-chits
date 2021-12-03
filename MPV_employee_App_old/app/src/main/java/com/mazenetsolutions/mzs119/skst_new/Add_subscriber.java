package com.mazenetsolutions.mzs119.skst_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdaptercustomer;
import com.mazenetsolutions.mzs119.skst_new.Model.BranchModel;
import com.mazenetsolutions.mzs119.skst_new.Model.Company_brancheModel;
import com.mazenetsolutions.mzs119.skst_new.Model.Custmodel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.NDSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Add_subscriber extends AppCompatActivity {
Spinner type_spinner,position_spinner,sourcefund_spinner,relationship_spinner,relationship_spinner_minor,house_type_spinner,
        office_type_spinner,house_offc_spinner,postal_corr_spinner,salutation_spinner;
RadioGroup member_type_radio_grp,father_spouse_radio,gender_radio_group;
EditText date_of_birth,date_of_registration,date_of_joining,person_age,company_age;
    DatePicker picker;
    int curryear;
    ImageView add_image;
    TextView save_subscriber;
    Uri imageUri;
    LinearLayout new_city,new_city1,new_city2;
    RadioButton male_radio,female_radio,shemale_radio;

    LinearLayout individual_layout,nonindividual_layout;
RadioButton indivi_radio,non_indivi_radio,father_radio,spouse_radio;
NDSpinner district_spinner1,state_spinner1,city_spinner1;
NDSpinner district_spinner2,state_spinner2,city_spinner2;
NDSpinner district_spinner_new,state_spinner_new,city_spinner_new;
NDSpinner district_spinner3,state_spinner3,city_spinner3,brach_spinner;
EditText pincode_spinner1,pincode_spinner2,pincode_spinner3,pincode_spinner_new,city_name;
    String selectedstate1="",selectedstate2="",selectedstate3="",selected_state_new="",selecteddistrict_new="";
    int selectedstatepos1=0,selectedstatepos2=0,selectedstatepos3=0;
    String selected_city1="",selected_city2="",selected_city3="";
    int selected_citypos1=0,selected_citypos2=0,selected_citypos3=0;
    String selecteddistrict1="",selecteddistrict2="",selecteddistrict3="";
    int selecteddistrictpos1=0,selecteddistrictpos2=0,selecteddistrictpos3=0;
//    String selectedpincode1,selectedpincode2,selectedpincode3;
//    int selectedpincodepos1=0,selectedpincodepos2=0,selectedpincodepos3=0;
    LinearLayout fathername_layout;
    LinearLayout source_fund_layout,position_name_layout;
    EditText source_fund_name,position_name,mobile_no,avg_monthly_income,nominee_relation;

    public static ArrayList<Company_brancheModel> branch_list;

    String[] types_array;
    String[] relationship_array_nominee;
    String[] position_array = { "Select","Proprietor","Working Partner", "Director","ChairPerson","Others" };
    String[] sourcefund_array = { "Select","Salary","Agriculture","Business Income","Invest Income","Others" };
    String[] relationship_array = { "Select","Aunt","Brother","Father","Grand Father","Grand Mother", "Sister", "Uncle","Mother"};
    String[] house_type_array = { "Select","Owned","Rented/Lease" };
    String[] postal_corr_array = { "Select","Residence","Office" };
    String[] salutation_array = { "Select","Mr","Mrs","Miss","M/S" };
    String selected_branch_name="",selected_branch_id;
    TextView father_spouse_heading;
    EditText name_of_father_spouse,gaurdian_name,org_name;
    LinearLayout minor_layout,major_layout;
    TextView ofc_address,permenet_address,perment_address_title,house_offc_heading;
    CheckBox same_address_checkbox;
    EditText present_address,present_landmark,present_since;
    EditText permanent_address,permanent_landmark,permanent_since;
    EditText office_address,office_landmark,office_since,ofc_designation;
    String selected_member_type="";
    String seleteced_customer_type="",seleced_org_type="",selected_salutation="",selected_gender="",selectedhouse_office_type="",
            selected_position="",selected_sorce_fund="",selected_house_type="",selected_office_type="",selected_postal_correspondence="",selected_gaurdian="",selected_noiminee_relation="",selected_gaurdian_relation="";
    TextView type_head;
    EditText firstname,initial,registered_name,org_cin_no,org_rep,phn_no,email_id,
            aadhar_id,pan_no,licence_no,voter_id,ration_card,passport_no,other_id_name,other_id_num,nominee_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscriber);


        save_subscriber = (TextView) findViewById(R.id.save_subscriber);
        firstname = (EditText) findViewById(R.id.firstname);
        initial = (EditText) findViewById(R.id.initial);
        gaurdian_name = (EditText) findViewById(R.id.gaurdian_name);
        org_name = (EditText) findViewById(R.id.org_name);
        registered_name = (EditText) findViewById(R.id.registered_name);
        org_cin_no = (EditText) findViewById(R.id.org_cin_no);
        org_rep = (EditText) findViewById(R.id.org_rep);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        phn_no = (EditText) findViewById(R.id.phn_no);
        email_id = (EditText) findViewById(R.id.email_id);
        aadhar_id = (EditText) findViewById(R.id.aadhar_id);
        pan_no = (EditText) findViewById(R.id.pan_no);
        licence_no = (EditText) findViewById(R.id.licence_no);
        voter_id = (EditText) findViewById(R.id.voter_id);
        ration_card = (EditText) findViewById(R.id.ration_card);
        passport_no = (EditText) findViewById(R.id.passport_no);
        other_id_name = (EditText) findViewById(R.id.other_id_name);
        other_id_num = (EditText) findViewById(R.id.other_id_num);
        avg_monthly_income = (EditText) findViewById(R.id.avg_monthly_income);
        nominee_name = (EditText) findViewById(R.id.nominee_name);
        ofc_designation = (EditText) findViewById(R.id.ofc_designation);
        ofc_designation = (EditText) findViewById(R.id.ofc_designation);

        new_city = (LinearLayout) findViewById(R.id.new_city);
        new_city1 = (LinearLayout) findViewById(R.id.new_city1);
        new_city2 = (LinearLayout) findViewById(R.id.new_city2);




        source_fund_layout = (LinearLayout) findViewById(R.id.source_fund_layout);
        position_name_layout = (LinearLayout) findViewById(R.id.position_name_layout);
        source_fund_name = (EditText) findViewById(R.id.source_fund_name);
        position_name = (EditText) findViewById(R.id.position_name_text);
        type_head = (TextView) findViewById(R.id.type_head);


        present_address = (EditText) findViewById(R.id.present_address);
        present_landmark = (EditText) findViewById(R.id.present_landmark);
        present_since = (EditText) findViewById(R.id.present_since);

        permanent_address = (EditText) findViewById(R.id.permanent_address);
        permanent_landmark = (EditText) findViewById(R.id.permanent_landmark);
        permanent_since = (EditText) findViewById(R.id.permanent_since);

        office_address = (EditText) findViewById(R.id.office_address);
        office_landmark = (EditText) findViewById(R.id.office_landmark);
        office_since = (EditText) findViewById(R.id.office_since);


        member_type_radio_grp = (RadioGroup) findViewById(R.id.member_type_radio_grp);
        father_spouse_radio = (RadioGroup) findViewById(R.id.father_spouse_radio);
        gender_radio_group = (RadioGroup) findViewById(R.id.gender_radio_group);
        indivi_radio = (RadioButton) findViewById(R.id.indivi_radio);
        non_indivi_radio = (RadioButton) findViewById(R.id.non_indivi_radio);
      //  indivi_radio.setChecked(true);

        spouse_radio = (RadioButton) findViewById(R.id.spouse_radio);
        father_radio = (RadioButton) findViewById(R.id.father_radio);
        individual_layout = (LinearLayout) findViewById(R.id.individual_layout);
        nonindividual_layout = (LinearLayout) findViewById(R.id.nonindividual_layout);
        major_layout = (LinearLayout) findViewById(R.id.major_layout);
        minor_layout = (LinearLayout) findViewById(R.id.minor_layout);
        father_spouse_heading = (TextView) findViewById(R.id.father_spouse_heading);
        name_of_father_spouse = (EditText) findViewById(R.id.name_of_father_spouse);
        ofc_address = (TextView) findViewById(R.id.ofc_address);
        house_offc_heading = (TextView) findViewById(R.id.house_offc_heading);
        same_address_checkbox = (CheckBox) findViewById(R.id.same_address_checkbox);
        fathername_layout = (LinearLayout) findViewById(R.id.fathername_layout);
//        RadioButton male_radio,female_radio,shemale_radio;
        male_radio=(RadioButton)findViewById(R.id.male_radio);
        female_radio=(RadioButton)findViewById(R.id.female_radio);
        shemale_radio=(RadioButton)findViewById(R.id.shemale_radio);


        final Calendar cldr1 = Calendar.getInstance();
        curryear=cldr1.get(Calendar.YEAR);

        perment_address_title = (TextView) findViewById(R.id.perment_address_title);
        permenet_address = (TextView) findViewById(R.id.permenet_address);
        final LinearLayout designation_layout,ofc_type_layout;
 save_subscriber.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {

         save_subscriber.setEnabled(false);



         validation();




     }
 });






        new_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTeamPOPUP(getIntent());
            }
        });
        new_city1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTeamPOPUP(getIntent());
            }
        });
        new_city2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTeamPOPUP(getIntent());
            }
        });

        getstates1();
        getstates2();
        getstates3();

        designation_layout = (LinearLayout) findViewById(R.id.designation_layout);
        ofc_type_layout = (LinearLayout) findViewById(R.id.ofc_type_layout);


        district_spinner1 = (NDSpinner) findViewById(R.id.district_spinner1);
        district_spinner2 = (NDSpinner) findViewById(R.id.district_spinner2);
        district_spinner3 = (NDSpinner) findViewById(R.id.district_spinner3);



        pincode_spinner1 = (EditText) findViewById(R.id.pincode_spinner1);
        pincode_spinner2 = (EditText) findViewById(R.id.pincode_spinner2);
        pincode_spinner3 = (EditText) findViewById(R.id.pincode_spinner3);


        state_spinner1 = (NDSpinner) findViewById(R.id.state_spinner1);
        state_spinner2 = (NDSpinner) findViewById(R.id.state_spinner2);
        state_spinner3 = (NDSpinner) findViewById(R.id.state_spinner3);

        city_spinner1 = (NDSpinner) findViewById(R.id.city_spinner1);
        city_spinner2 = (NDSpinner) findViewById(R.id.city_spinner2);
        city_spinner3 = (NDSpinner) findViewById(R.id.city_spinner3);
        brach_spinner = (NDSpinner) findViewById(R.id.brach_spinner);
        getbranches();

        type_spinner = (Spinner) findViewById(R.id.type_spinner);


        position_spinner = (Spinner) findViewById(R.id.position_spinner);
        ArrayAdapter position_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, position_array);
        position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position_spinner.setAdapter(position_adapter);

        sourcefund_spinner = (Spinner) findViewById(R.id.sourcefund_spinner);
        ArrayAdapter sourcefund_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sourcefund_array);
        sourcefund_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourcefund_spinner.setAdapter(sourcefund_adapter);

        relationship_spinner = (Spinner) findViewById(R.id.relationship_spinner);
        relationship_spinner_minor = (Spinner) findViewById(R.id.relationship_spinner_minor);
        ArrayAdapter relation_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, relationship_array);
        relation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        relationship_spinner.setAdapter(relation_adapter);
        relationship_spinner_minor.setAdapter(relation_adapter);

        house_type_spinner = (Spinner) findViewById(R.id.house_type_spinner);
        ArrayAdapter housetype_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, house_type_array);
        housetype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        house_type_spinner.setAdapter(housetype_adapter);
        office_type_spinner = (Spinner) findViewById(R.id.office_type_spinner);
        housetype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        office_type_spinner.setAdapter(housetype_adapter);


        house_offc_spinner = (Spinner) findViewById(R.id.house_offc_spinner);
        ArrayAdapter houseofc_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, house_type_array);
        houseofc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        house_offc_spinner.setAdapter(houseofc_adapter);

        postal_corr_spinner = (Spinner) findViewById(R.id.postal_corr_spinner);
        ArrayAdapter posal_corr_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, postal_corr_array);
        posal_corr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postal_corr_spinner.setAdapter(posal_corr_adapter);

        salutation_spinner = (Spinner) findViewById(R.id.salutation_spinner);
        ArrayAdapter salutation_spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, salutation_array);
        salutation_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salutation_spinner.setAdapter(salutation_spinner_adapter);

        same_address_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                   if(isChecked){

                                                       if(indivi_radio.isChecked()){
                                                           permanent_address.setText(present_address.getText().toString());
                                                           permanent_landmark.setText(present_landmark.getText().toString());
                                                           permanent_since.setText(present_since.getText().toString());
                                                           state_spinner3.setSelection(selectedstatepos1);
//                                                           district_spinner3.setSelection(selecteddistrictpos1);
//                                                           city_spinner3.setSelection(selected_citypos1);
//                                                           pincode_spinner3.setSelection(selectedpincodepos1);
                                                           int hosue_position=house_type_spinner.getFirstVisiblePosition();
                                                           house_offc_spinner.setSelection(hosue_position);

                                                       }

                                                       if(non_indivi_radio.isChecked()){

                                                           permanent_address.setText(office_address.getText().toString());
                                                           permanent_landmark.setText(office_landmark.getText().toString());
                                                           permanent_since.setText(office_since.getText().toString());
                                                           state_spinner3.setSelection(selectedstatepos2);

                                                           int hosue_position=office_type_spinner.getSelectedItemPosition();
                                                           house_offc_spinner.setSelection(hosue_position);
                                                       }
                                                   }else {


                                                       permanent_address.setText(null);
                                                       permanent_landmark.setText(null);
                                                       permanent_since.setText(null);
                                                       state_spinner3.setSelection(0);
                                                       district_spinner3.setSelection(0);
                                                       city_spinner3.setSelection(0);
                                                       pincode_spinner3.setSelection(0);
                                                       house_offc_spinner.setSelection(0);
                                                   }

                                               }
                                           }
        );



        member_type_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (indivi_radio.isChecked()) {
                    type_head.setText("Customer Type *");

                    seleced_org_type="";
                    selected_member_type="ind";

                    types_array = new String[]{"Select","Major","Minor"};

                    individual_layout.setVisibility(View.VISIBLE);
                    nonindividual_layout.setVisibility(View.GONE);
                    ofc_address.setText("Office Address");
                    permenet_address.setText("Permanent Residence Address");
                    perment_address_title.setText("Permanent Residence Address");
                    house_offc_heading.setText("House Type *");
                    designation_layout.setVisibility(View.VISIBLE);
                    ofc_type_layout.setVisibility(View.GONE);
                    same_address_checkbox.setText(" if Permanent Address (Same as above).");


                    ArrayAdapter types_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, types_array);
                    types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type_spinner.setAdapter(types_adapter);

                }
                if (non_indivi_radio.isChecked()) {
                    type_head.setText("Organization Type *");
                    seleteced_customer_type="";

                            selected_member_type="non_ind";

                    types_array = new String[]{"Select","Proprietor", "Partnership", "Pvt / Ltd.co", "Society/Club/Tust", "Association"};


                    individual_layout.setVisibility(View.GONE);
                    nonindividual_layout.setVisibility(View.VISIBLE);
                    ofc_address.setText(" Local Office Address");
                    permenet_address.setText(" Registered Office Address");
                    perment_address_title.setText(" Registered Office Address");
                    house_offc_heading.setText("Office Type *");
                    designation_layout.setVisibility(View.GONE);
                    ofc_type_layout.setVisibility(View.VISIBLE);
                    same_address_checkbox.setText(" if Registered Office Address (Same as above).");

                    ArrayAdapter types_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, types_array);
                    types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type_spinner.setAdapter(types_adapter);

                }
            }
        });
        gender_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (male_radio.isChecked()) {
                    selected_gender="Male";
                }
                if (female_radio.isChecked()) {
                    selected_gender="Female";
                }
                if (shemale_radio.isChecked()) {
                    selected_gender="Shemale";
                }
            }
        });
        /*father_spouse_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (father_radio.isChecked()) {

                }
                if (spouse_radio.isChecked()) {

                }

            }
        });*/
        father_spouse_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (father_radio.isChecked()) {
                    selected_gaurdian="father";
                    father_spouse_heading.setText("Name of the father *");
                    name_of_father_spouse.setHint("Enter Father's name");
                    fathername_layout.setVisibility(View.VISIBLE);

                }
                if (spouse_radio.isChecked()) {
                    father_spouse_heading.setText("Name of the Spouse *");
                    name_of_father_spouse.setHint("Enter Spouse's name");
                    fathername_layout.setVisibility(View.VISIBLE);
                    selected_gaurdian="spouse";
                }
            }
        });



        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                String selected = parentView.getItemAtPosition(position).toString();

                if (selected.equalsIgnoreCase("Minor")) {
                    seleteced_customer_type="minor";

                    major_layout.setVisibility(View.GONE);
                    minor_layout.setVisibility(View.VISIBLE);
                    relationship_array_nominee =new String[] { "Select","Aunt","Brother","Father","Grand Father","Grand Mother", "Sister", "Uncle","Mother"};

                    ArrayAdapter types_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, relationship_array_nominee);
                    types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    relationship_spinner.setAdapter(types_adapter);
                }else {
                    relationship_array_nominee =new String[] {"Select", "Aunt","Brother","Father","Grand Father","Grand Mother", "Sister", "Uncle","Mother","Spouse"};

                    ArrayAdapter types_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, relationship_array_nominee);
                    types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    relationship_spinner.setAdapter(types_adapter);
                }

                if (selected.equalsIgnoreCase("Major")){

                     seleteced_customer_type="major";
                      major_layout.setVisibility(View.VISIBLE);
                      minor_layout.setVisibility(View.GONE);

                }
               // types_array = new String[]{"Proprietor", "Partnership", "Pvt / Ltd.co", "Scoiety / Club /Trust", "Association"};

                if (selected.equalsIgnoreCase("Proprietor")){

                    seleced_org_type="Proprietor";
                }
                if (selected.equalsIgnoreCase("Partnership")){

                    seleced_org_type="Partnership";
                }
                if (selected.equalsIgnoreCase("Pvt / Ltd.co")){

                    seleced_org_type="Pvt / Ltd.co";
                }
                if (selected.equalsIgnoreCase("Society/Club/Tust")){

                    seleced_org_type="Scoiety / Club /Trust";
                }
                if (selected.equalsIgnoreCase("Association")){

                    seleced_org_type="Association";
                }



                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        date_of_birth=(EditText) findViewById(R.id.date_of_birth);
        person_age=(EditText) findViewById(R.id.person_age);
        person_age.setInputType(InputType.TYPE_NULL);
        date_of_birth.setInputType(InputType.TYPE_NULL);

        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                cldr.set(day,month,year);
                // date picker dialog
                DatePickerDialog datepicker = new DatePickerDialog(Add_subscriber.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_of_birth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                Date currentDate = new Date();


                                int age=curryear-year;
                                person_age.setText(String.valueOf(age));
                                date_of_birth.setError(null);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        date_of_joining=(EditText) findViewById(R.id.date_of_joining);
        date_of_joining.setInputType(InputType.TYPE_NULL);
        date_of_joining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datepicker = new DatePickerDialog(Add_subscriber.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_of_joining.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                date_of_joining.setError(null);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        date_of_registration=(EditText) findViewById(R.id.date_of_registration);
        date_of_registration.setInputType(InputType.TYPE_NULL);
        company_age=(EditText) findViewById(R.id.company_age);
        company_age.setInputType(InputType.TYPE_NULL);
        date_of_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datepicker = new DatePickerDialog(Add_subscriber.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_of_registration.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                int age=curryear-year;
                                company_age.setText(String.valueOf(age));
                                date_of_registration.setError(null);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        add_image = (ImageView) findViewById(R.id.add_image);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (ActivityCompat.checkSelfPermission(Add_subscriber.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(Add_subscriber.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(Add_subscriber.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    openGallery();
                } else {


                    if (ActivityCompat.checkSelfPermission(Add_subscriber.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            init();
                    } else {
                        ActivityCompat.requestPermissions(Add_subscriber.this, new String[]{Manifest.permission.CAMERA}, 101);
                    }
                    if (ActivityCompat.checkSelfPermission(Add_subscriber.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        ActivityCompat.requestPermissions(Add_subscriber.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
                    }

                    if (ActivityCompat.checkSelfPermission(Add_subscriber.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                    } else {
                        ActivityCompat.requestPermissions(Add_subscriber.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 103);
                    }


                }


            }
        });


          position_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selected = parentView.getItemAtPosition(position).toString();
//               String[] salutation_array = { "Mr","Mrs","Miss","M/S" };

               if(selected.equalsIgnoreCase("Proprietor")){
                   selected_position="Proprietor";
               }
               if(selected.equalsIgnoreCase("Working Partner")){
                   selected_position="Working_Partner";
               }
               if(selected.equalsIgnoreCase("Director")){
                   selected_position="Director";
               }
               if(selected.equalsIgnoreCase("Chairperson")){
                   selected_position="Chairperson";
               }
               if(selected.equalsIgnoreCase("Others")){
                   selected_position="Others";
                   position_name_layout.setVisibility(View.VISIBLE);
               }else {
                   position_name_layout.setVisibility(View.GONE);

               }




                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
          sourcefund_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selected = parentView.getItemAtPosition(position).toString();

               if(selected.equalsIgnoreCase("Salary")){
                   selected_sorce_fund="Salary";
               }
               if(selected.equalsIgnoreCase("Agriculture")){
                   selected_sorce_fund="Agriculture";
               }
               if(selected.equalsIgnoreCase("Business Income")){
                   selected_sorce_fund="Business Income";
               }
               if(selected.equalsIgnoreCase("Invest Income")){
                   selected_sorce_fund="Investment Income";
               }

               if(selected.equalsIgnoreCase("Others")){
                   selected_sorce_fund="Others";
                   source_fund_layout.setVisibility(View.VISIBLE);
               }else {
                   source_fund_layout.setVisibility(View.GONE);

               }


                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




        salutation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                String selected = parentView.getItemAtPosition(position).toString();
//               String[] salutation_array = { "Mr","Mrs","Miss","M/S" };

               if(selected.equalsIgnoreCase("Mr")){
                   selected_salutation="Mr";
               }
               if(selected.equalsIgnoreCase("Mrs")){
                   selected_salutation="Mrs";
               }
               if(selected.equalsIgnoreCase("Miss")){
                   selected_salutation="Miss";
               }
               if(selected.equalsIgnoreCase("M/S")){
                   selected_salutation="M/S";
               }



                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        house_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                String selected = parentView.getItemAtPosition(position).toString();
//               String[] salutation_array = { "Mr","Mrs","Miss","M/S" };

               if(selected.equalsIgnoreCase("Owned")){
                   selected_house_type="Owned";
               }
               if(selected.equalsIgnoreCase("Rented")){
                   selected_house_type="Rented";
               }


                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        office_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                String selected = parentView.getItemAtPosition(position).toString();
//               String[] salutation_array = { "Mr","Mrs","Miss","M/S" };

               if(selected.equalsIgnoreCase("Owned")){
                   selected_office_type="Owned";
               }
               if(selected.equalsIgnoreCase("Rented")){
                   selected_office_type="Rented";
               }


                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        house_offc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                String selected = parentView.getItemAtPosition(position).toString();
//               String[] salutation_array = { "Mr","Mrs","Miss","M/S" };

               if(selected.equalsIgnoreCase("Owned")){
                   selectedhouse_office_type="Owned";
               }
               if(selected.equalsIgnoreCase("Rented")){
                   selectedhouse_office_type="Rented";
               }


                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        postal_corr_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                String selected = parentView.getItemAtPosition(position).toString();
//               String[] salutation_array = { "Mr","Mrs","Miss","M/S" };

               if(selected.equalsIgnoreCase("Residence")){
                   selected_postal_correspondence="Residence";
               }
               if(selected.equalsIgnoreCase("Office")){
                   selected_postal_correspondence="Office";
               }


                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        brach_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                String selected = parentView.getItemAtPosition(position).toString();
                selected_branch_id=branch_list.get(position).getBranch_id();
                selected_branch_name=branch_list.get(position).getBranch_name();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        relationship_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                selected_noiminee_relation = parentView.getItemAtPosition(position).toString();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        state_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selectedstate1 = parentView.getItemAtPosition(position).toString();
                 selectedstatepos1 = position;
                 getDistricts1();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        state_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selectedstate2 = parentView.getItemAtPosition(position).toString();

                    selectedstatepos2  =position;
               getDistricts2();
                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        state_spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selectedstate3 = parentView.getItemAtPosition(position).toString();
               selectedstatepos3=position;
               getDistricts3();
                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        district_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selecteddistrict1 = parentView.getItemAtPosition(position).toString();
                 selecteddistrictpos1=position;
                 getcity1();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        district_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selecteddistrict2 = parentView.getItemAtPosition(position).toString();
               selecteddistrictpos2=position;

               getcity2();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        district_spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selecteddistrict3 = parentView.getItemAtPosition(position).toString();
               selecteddistrictpos3=position;

               getcity3();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        city_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selected_city1 = parentView.getItemAtPosition(position).toString();
               selected_citypos1=position;
               getpincode1();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        city_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selected_city2 = parentView.getItemAtPosition(position).toString();
                 selected_citypos2=position;

                getpincode2();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        city_spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                 selected_city3 = parentView.getItemAtPosition(position).toString();
                 selected_citypos3=position;
               getpincode3();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



    }






    public void getbranches() {

      //  showDialog();
        String url = Config.branchmaster;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
                    branch_list= new ArrayList<Company_brancheModel>();

                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("branch");

                        for (int i = 0; i < branch_array.length(); i++) {
                            JSONObject jObj = branch_array.getJSONObject(i);


                            Company_brancheModel list_data =new Company_brancheModel();
                            list_data.setBranch_id(jObj.getString("Id"));
                            list_data.setBranch_name(jObj.getString("Name"));

                           branch_list.add(list_data);

                            categories.add(jObj.getString("Name"));


                        }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brach_spinner.setAdapter(dataAdapter2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("userid", pref.getString("userid", "0"));
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getstates1() {

      //  showDialog();
        String url = Config.getstates;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("State_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("StateName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    state_spinner1.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("userid", pref.getString("userid", "0"));
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getstates_new() {

      //  showDialog();
        String url = Config.getstates;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("State_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("StateName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    state_spinner_new.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("userid", pref.getString("userid", "0"));
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getstates2() {

      //  showDialog();
        String url = Config.getstates;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("State_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("StateName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    state_spinner2.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("userid", pref.getString("userid", "0"));
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getstates3() {

      //  showDialog();
        String url = Config.getstates;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("State_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("StateName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    state_spinner3.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("userid", pref.getString("userid", "0"));
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getDistricts1() {

      //  showDialog();
        String url = Config.getdistricts;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("District_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("DistrictName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district_spinner1.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("State_Name", selectedstate1);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getDistricts_new() {

      //  showDialog();
        String url = Config.getdistricts;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("District_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("DistrictName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district_spinner_new.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("State_Name", selected_state_new);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getDistricts2() {

      //  showDialog();
        String url = Config.getdistricts;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("District_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("DistrictName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district_spinner2.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("State_Name", selectedstate2);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getDistricts3() {

      //  showDialog();
        String url = Config.getdistricts;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("District_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("DistrictName"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district_spinner3.setAdapter(dataAdapter2);
                    if(same_address_checkbox.isChecked()){
                        if(indivi_radio.isChecked()){
                            district_spinner3.setSelection(selecteddistrictpos1);
                        }
                        if(non_indivi_radio.isChecked()){
                            district_spinner3.setSelection(selecteddistrictpos2);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("State_Name", selectedstate3);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getcity1() {

      //  showDialog();
        String url = Config.getdcity;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("City_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("City"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city_spinner1.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("District_Name", selecteddistrict1);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getcity2() {

      //  showDialog();
        String url = Config.getdcity;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("City_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("City"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city_spinner2.setAdapter(dataAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("District_Name", selecteddistrict2);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getcity3() {

      //  showDialog();
        String url = Config.getdcity;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("City_Name");

                    for (int i = 0; i < branch_array.length(); i++) {
                        JSONObject jObj = branch_array.getJSONObject(i);
//
//
//                        Company_brancheModel list_data =new Company_brancheModel();
//                        list_data.setBranch_id(jObj.getString("Id"));
//                        list_data.setBranch_name(jObj.getString("Name"));
//
//                        branch_list.add(list_data);

                        categories.add(jObj.getString("City"));


                    }
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city_spinner3.setAdapter(dataAdapter2);
                    if(same_address_checkbox.isChecked()){
                        if(indivi_radio.isChecked()){
                            city_spinner3.setSelection(selected_citypos1);
                        }
                        if(non_indivi_radio.isChecked()){
                            city_spinner3.setSelection(selected_citypos2);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("District_Name", selecteddistrict3);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getpincode1() {

      //  showDialog();
        String url = Config.getdpincode;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("Pincode");

                    String pin=branch_array.toString();
                    String pincode=pin.substring(2,8);

                    categories.add(pincode);
//                    for (int i = 0; i < branch_array.length(); i++) {
//                        JSONObject jObj = branch_array.getJSONObject(i);
////
////
////                        Company_brancheModel list_data =new Company_brancheModel();
////                        list_data.setBranch_id(jObj.getString("Id"));
////                        list_data.setBranch_name(jObj.getString("Name"));
////
////                        branch_list.add(list_data);
//
//                        categories.add(jObj.getString("City"));
//
//
//                    }

                    pincode_spinner1.setText(pincode);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("City_Name", selected_city1);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getpincode2() {

      //  showDialog();
        String url = Config.getdpincode;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("Pincode");

                    String pin=branch_array.toString();
                    String pincode=pin.substring(2,8);

                    categories.add(pincode);
//                    for (int i = 0; i < branch_array.length(); i++) {
//                        JSONObject jObj = branch_array.getJSONObject(i);
////
////
////                        Company_brancheModel list_data =new Company_brancheModel();
////                        list_data.setBranch_id(jObj.getString("Id"));
////                        list_data.setBranch_name(jObj.getString("Name"));
////
////                        branch_list.add(list_data);
//
//                        categories.add(jObj.getString("City"));
//
//
//
                    pincode_spinner2.setText(pincode);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("City_Name", selected_city2);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getpincode3() {

      //  showDialog();
        String url = Config.getdpincode;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    JSONArray branch_array = object.getJSONArray("Pincode");

                    String pin=branch_array.toString();
                    String pincode=pin.substring(2,8);

                    categories.add(pincode);
//                    for (int i = 0; i < branch_array.length(); i++) {
//                        JSONObject jObj = branch_array.getJSONObject(i);
////
////
////                        Company_brancheModel list_data =new Company_brancheModel();
////                        list_data.setBranch_id(jObj.getString("Id"));
////                        list_data.setBranch_name(jObj.getString("Name"));
////
////                        branch_list.add(list_data);
//
//                        categories.add(jObj.getString("City"));
//
//
//                    }
//                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_subscriber.this, android.R.layout.simple_spinner_item, categories);
//                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    pincode_spinner3.setAdapter(dataAdapter2);

                    pincode_spinner3.setText(pincode);
                    if(same_address_checkbox.isChecked()){
                        if(indivi_radio.isChecked()){
                            pincode_spinner3.setText(pincode_spinner1.getText().toString());
                        }
                        if(non_indivi_radio.isChecked()){
                            pincode_spinner3.setText(pincode_spinner2.getText().toString());

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("City_Name", selected_city3);
//                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void openGallery() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(Add_subscriber.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {

                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, 1);
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto , 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        add_image.setImageBitmap(selectedImage);


//                        if (globalValue.getString("edit_player").equalsIgnoreCase("no")) {
//                            add_image.setImageBitmap(selectedImage);
//
//                        } else {
//                            Players_Adapter.add_image.setImageBitmap(selectedImage);
//                        }

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && requestCode == 1) {
                        imageUri = data.getData();
                        add_image.setImageURI(imageUri);

//                        if (globalValue.getString("edit_player").equalsIgnoreCase("no")) {
//
//                            add_image.setImageURI(imageUri);
//                        } else {
//                            Players_Adapter.add_image.setImageURI(imageUri);
//
//                        }
                        break;
                    }

            }

        }

    }




    public void saveForm() {

        //  showDialog();
        String url = Config.saveform;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Save Form ", response.toString());
                System.out.println("save form url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                  if(object.getString("status").equalsIgnoreCase("1")){



                      AlertDialog alertDialog = new AlertDialog.Builder(Add_subscriber.this).create();
                      alertDialog.setTitle(object.getString("details"));
                      alertDialog.setMessage("Customer ID : " +object.getString("customer_id"));
                      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                              new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {
                                      dialog.dismiss();

                                      Intent i=new Intent(getApplicationContext(),MenuActivity.class);
                                      startActivity(i);
                                      finish();
                                  }
                              });
                      alertDialog.show();



                  }else {

                      Toast.makeText(getApplicationContext(),object.getString("details"),Toast.LENGTH_SHORT).show();

                  }
                    save_subscriber.setEnabled(true);



                } catch (JSONException e) {

                    save_subscriber.setEnabled(true);

                    e.printStackTrace();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Bitmap bitmap = ((BitmapDrawable) add_image.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] imageInByte = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

               params.put("user_img", encodedImage);
               params.put("branch_name", selected_branch_id);
               params.put("member_type", selected_member_type);
               params.put("cus_type", seleteced_customer_type);
               params.put("indv_salutation", selected_salutation);
               params.put("indv_first_name", firstname.getText().toString());
               params.put("indv_initial", initial.getText().toString());
               params.put("indv_dob", date_of_birth.getText().toString());
               params.put("indv_age", person_age.getText().toString());
               params.put("indv_gender",selected_gender);
               //
                params.put("father_or_spouse", selected_gaurdian);
                //
                if (seleteced_customer_type.equals("major")) {
                    if (father_radio.isChecked())
                        params.put("indv_father_name", name_of_father_spouse.getText().toString());
                    if (spouse_radio.isChecked())
                        params.put("indv_spouse_name", name_of_father_spouse.getText().toString());
                }
               params.put("guardian_name", gaurdian_name.getText().toString());
               params.put("guardian_relation", relationship_spinner_minor.getSelectedItem().toString());

               params.put("org_name", org_name.getText().toString());
               params.put("org_type",seleced_org_type );
               params.put("org_registration_name", registered_name.getText().toString());
               params.put("org_cin_no", org_cin_no.getText().toString());
               params.put("org_date_of_reg", date_of_registration.getText().toString());
               params.put("org_age", company_age.getText().toString());
               params.put("org_representativr", org_rep.getText().toString());
               params.put("org_position", selected_position);
               params.put("org_position_name", position_name.getText().toString());

               params.put("mobile_no", mobile_no.getText().toString());
               params.put("phone_no", phn_no.getText().toString());
               params.put("email_id", email_id.getText().toString());
               params.put("aadhar_no", aadhar_id.getText().toString());
               params.put("pan_no", pan_no.getText().toString());
               params.put("licence_no", licence_no.getText().toString());
               params.put("voter_id_no", voter_id.getText().toString());
               params.put("ration_card_no", ration_card.getText().toString());
               params.put("passport_no", passport_no.getText().toString());
               params.put("date_of_joining",date_of_joining.getText().toString());
               params.put("other_id_name", other_id_name.getText().toString());
               params.put("other_id_no", other_id_num.getText().toString());
               params.put("source_fund", selected_sorce_fund);
               params.put("source_fund_name", source_fund_name.getText().toString());
               params.put("avg_monthly_income", avg_monthly_income.getText().toString());

               params.put("nominee_name", nominee_name.getText().toString());
               params.put("nominee_relation",selected_noiminee_relation);

               params.put("present_address",present_address.getText().toString());
               params.put("present_landmark",present_landmark.getText().toString());
               params.put("present_state",selectedstate1);
               params.put("present_district",selecteddistrict1);
               params.put("present_city",selected_city1);
               params.put("present_pincode",pincode_spinner1.getText().toString());
               params.put("present_house_type",selected_house_type);
               params.put("present_since_year",present_since.getText().toString());

               params.put("reg_ofc_adress",office_address.getText().toString());
               params.put("reg_ofc_landmark",office_landmark.getText().toString());
               params.put("reg_ofc_state",selectedstate2);
               params.put("reg_ofc_district",selecteddistrict2);
               params.put("reg_ofc_city",selected_city2);
               params.put("reg_ofc_pincode",pincode_spinner2.getText().toString());
               params.put("reg_ofc_type",selected_office_type);
               params.put("reg_designation",ofc_designation.getText().toString());
               params.put("reg_ofc_since",office_since.getText().toString());


                params.put("permanent_address",permanent_address.getText().toString());
                params.put("permanent_landmark",permanent_landmark.getText().toString());
                params.put("permanent_state",selectedstate3);
                params.put("permanent_district",selecteddistrict3);
                params.put("permanent_city",selected_city3);
                params.put("permanent_pincode",pincode_spinner3.getText().toString());
                params.put("permanent_house_type",selectedhouse_office_type);
                params.put("permanent_since_year",permanent_since.getText().toString());
                params.put("postal_correspondence",selected_postal_correspondence);



                System.out.println("save_form_param  "+params);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    public   void AddTeamPOPUP(final Intent context) {


        ImageView  close1;
        TextView submit;
        TextView toolbar_title;

        final Dialog dialog = new Dialog(Add_subscriber.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_city);
        close1 = (ImageView) dialog.findViewById(R.id.close1);
        submit = (TextView) dialog.findViewById(R.id.submit_item);
        district_spinner_new = (NDSpinner)dialog.findViewById(R.id.district_spinner_new);
        city_name = (EditText) dialog.findViewById(R.id.city_name);
        pincode_spinner_new= (EditText) dialog.findViewById(R.id.pincode_spinner_new);
        state_spinner_new = (NDSpinner) dialog.findViewById(R.id.state_spinner_new);

        close1 = (ImageView) dialog.findViewById(R.id.close1);
        toolbar_title = (TextView) dialog.findViewById(R.id.toolbar_title);
        toolbar_title.setText("     Create New City     ");

        submit = (TextView) dialog.findViewById(R.id.submit_item);

        getstates_new();


        state_spinner_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                selected_state_new = parentView.getItemAtPosition(position).toString();

                getDistricts_new();

                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        district_spinner_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //   spinner123.isSpinnerDialogOpen = false;

                selecteddistrict_new = parentView.getItemAtPosition(position).toString();




                final  TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(selected_state_new.length()>0 && selecteddistrict_new.length()>0 && city_name.getText().toString().length()>0
                        && pincode_spinner_new.getText().toString().length()==6){
                    savecity();
                    dialog.dismiss();


                }else {
                    if(selected_state_new.length()<=0){
                        Toast.makeText(getApplicationContext(),"Please Select State",Toast.LENGTH_SHORT).show();
                    }
 if(selecteddistrict_new.length()<=0){
                        Toast.makeText(getApplicationContext(),"Please Select district",Toast.LENGTH_SHORT).show();
                    }

 if(city_name.getText().toString().length()<=0){

     city_name.setError("Enter City");
 }else {
     city_name.setError(null);

 }
 if(pincode_spinner_new.getText().toString().length()<6){

     pincode_spinner_new.setError("Enter Valid No");
 }else {
     pincode_spinner_new.setError(null);

 }




                }
            }
        });


        try
        {
            if(!(dialog.isShowing()))
            {
                dialog.show();
            }
            else
            {
                dialog.dismiss();
                //   filtered_date.setText(globalValue.getString("from_receipt")+" to "+globalValue.getString("to_receipt"));
            }
        }
        catch (Exception e)
        {

        }
        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //  filtered_date.setText(globalValue.getString("from_receipt")+" to "+globalValue.getString("to_receipt"));

            }
        });

    }

    public void savecity() {

        //  showDialog();
        String url = Config.add_city;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Save city ", response.toString());
                System.out.println("save city url" + response);
//                hidePDialog();


                try {
//                    branch_list= new ArrayList<Company_brancheModel>();
//
//                    branch_list.clear();
                    ArrayList<String> categories = new ArrayList<String>();

                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equalsIgnoreCase("1")){


                        Toast.makeText(getApplicationContext(),object.getString("details"),Toast.LENGTH_SHORT).show();


                        getstates1();
                        getstates2();
                        getstates3();

                    }else {

                        Toast.makeText(getApplicationContext(),object.getString("details"),Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {


                    e.printStackTrace();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("state", selected_state_new);
                params.put("district", selecteddistrict_new);
                params.put("city", city_name.getText().toString());
                params.put("pincode", pincode_spinner_new.getText().toString());

                System.out.println("save_city_param  "+params);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }




    public void validation() {
        String branch_validation, member_type_validation, cus_type_validation="", org_type_validation="", salutaion_valiation,
                first_name_validation, initial_validation, dob_validation,doj_validation, gender_validation, father_spouse_validation,
                father_spouse_name_validation="", gaurdian_name_validation, gaurdian_relation_validation, org_name_validation, registered_name_validation, org_cin_no_validation, date_of_registration_validation, org_rep_validation,
                rep_position_validation, mobile_no_validation, email_id_validation, proof_validation, sorce_fund_validation,
                avg_monthly_income_validation, nominee_name_validation, noiminee_relation_validation,present_address_validation,office_address_validation,permenet_address_validation,
        state1_validation,state2_validation,state3_validation,city1_validation,city2_validation,city3_validation
        ,district1_validation,district2_validation,district3_validation,pincode1_validation,pincode2_validation,pincode3_validation,
                present_since_validation,permanent_since_validation,office_since_validation,postal_corr_validation,ofctype_validation="",houseOFFC_type_validation="",ofc_designation_validation,present_house_type_validation="";
//1
        if (selected_branch_name.length() <= 0) {
            Toast.makeText(getApplicationContext(), "Select Branch", Toast.LENGTH_SHORT).show();
            branch_validation = "false";
        } else {
            branch_validation = "true";
        }
        //2
        if (indivi_radio.isChecked()) {
            member_type_validation = "true";
        } else if (non_indivi_radio.isChecked()) {
            member_type_validation = "true";

        } else {
            Toast.makeText(getApplicationContext(), "Select Member Type", Toast.LENGTH_SHORT).show();

            member_type_validation = "false";
        }
        //3

        if (selected_member_type.equals("ind")) {
            if (seleteced_customer_type.length() <= 0) {

                cus_type_validation = "false";
                Toast.makeText(getApplicationContext(), "Select customer Type", Toast.LENGTH_SHORT).show();

            } else {
                cus_type_validation = "true";
            }
            org_type_validation = "true";

        }
        if (selected_member_type.equals("non_ind")) {
            if (seleced_org_type.length() <= 0) {
                Toast.makeText(getApplicationContext(), "Select organisation Type", Toast.LENGTH_SHORT).show();

                org_type_validation = "false";
            } else {
                org_type_validation = "true";
            }
            cus_type_validation = "true";

        }
//4
        if (selected_member_type.equals("ind")) {

            if (selected_salutation.length() <= 0) {

                salutaion_valiation = "false";
                Toast.makeText(getApplicationContext(), "Select Salutation", Toast.LENGTH_SHORT).show();

            } else {
                salutaion_valiation = "true";
            }
        }else {
            salutaion_valiation = "true";

        }
//5
        if (selected_member_type.equals("ind")) {

            if (firstname.getText().toString().length() <= 0) {
                first_name_validation = "false";
                firstname.setError("* Required");

            } else {
                first_name_validation = "true";
                firstname.setError(null);

            }
        }else{
            first_name_validation = "true";

        }
//6
        if (selected_member_type.equals("ind")) {

            if (initial.getText().toString().length() <= 0) {
                initial_validation = "false";
                initial.setError("* Required");

            } else {
                initial_validation = "true";
                initial.setError(null);
            }

        }else {
            initial_validation = "true";

        }
        //7
        if (selected_member_type.equals("ind")) {

            if (date_of_birth.getText().toString().length() <= 0) {
                dob_validation = "false";
                date_of_birth.setError("*Required");

            } else {
                dob_validation = "true";
                date_of_birth.setError(null);

            }

        }else {

            dob_validation = "true";

        }
        if (date_of_joining.getText().toString().length() <= 0) {
            doj_validation = "false";
            date_of_joining.setError("*Required");

        } else {
            doj_validation = "true";
            date_of_joining.setError(null);

        }
//8
        if (selected_member_type.equals("ind")) {
            if (male_radio.isChecked()) {
                gender_validation = "true";
            } else if (female_radio.isChecked()) {
                gender_validation = "true";

            } else if (shemale_radio.isChecked()) {
                gender_validation = "true";

            } else {
                Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                gender_validation = "false";

            }
        }else {
            gender_validation = "true";

        }
        //

//    selected_house_type
    if (house_type_spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
        present_house_type_validation = "false";
        Toast.makeText(getApplicationContext(),"Select House Type",Toast.LENGTH_SHORT).show();
    } else {
        present_house_type_validation = "true";

    }


if (selected_member_type.equals("non_ind")) {


    if (office_type_spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
        ofctype_validation = "false";
        Toast.makeText(getApplicationContext(),"Select office Type",Toast.LENGTH_SHORT).show();
    } else {
        ofctype_validation = "true";

    }

        }else {
    ofctype_validation = "true";

        }


//    selected_house_typexs
    if (house_offc_spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
        houseOFFC_type_validation = "false";
        if (selected_member_type.equals("ind")) {
            Toast.makeText(getApplicationContext(),"Select House Type",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getApplicationContext(),"Select Office Type",Toast.LENGTH_SHORT).show();

        }

    } else {
        houseOFFC_type_validation = "true";

    }



        if (selected_member_type.equals("ind")) {

            if (ofc_designation.getText().toString().length() <= 0) {
                ofc_designation_validation = "false";
                Toast.makeText(getApplicationContext(),"Select Designation",Toast.LENGTH_SHORT).show();


            } else {
                ofc_designation_validation = "true";
                date_of_birth.setError(null);

            }

        }else {

            ofc_designation_validation = "true";

        }


//9

        if (seleteced_customer_type.equals("major")) {
            if (father_radio.isChecked()) {
                father_spouse_validation = "true";
            } else if (spouse_radio.isChecked()) {
                father_spouse_validation = "true";
            } else {
                father_spouse_validation = "false";
               Toast.makeText(getApplicationContext(),"Select Spouse/ Father",Toast.LENGTH_SHORT).show();


            }
        } else {

            father_spouse_validation = "true";

        }
        //10
        if (seleteced_customer_type.equals("major")) {
            if (name_of_father_spouse.getText().toString().length() <= 0) {
                father_spouse_name_validation = "false";
                name_of_father_spouse.setError("*Required");
            } else {
                father_spouse_name_validation = "true";

            }

        } else {
            father_spouse_name_validation = "true";

        }

        //11

        if (seleteced_customer_type.equals("minor")) {
            if (gaurdian_name.getText().toString().length() <= 0) {
                gaurdian_name_validation = "false";
                gaurdian_name.setError("*Required");
            } else {
                gaurdian_name_validation = "true";
                gaurdian_name.setError(null);


            }

        } else {
            gaurdian_name_validation = "true";
            gaurdian_name.setError(null);


        }
        //12

        if (seleteced_customer_type.equals("minor")) {
            if (relationship_spinner_minor.getSelectedItem().toString().equals("Select")) {
                gaurdian_relation_validation = "false";
                Toast.makeText(getApplicationContext(),"Select Guardian's Relationship",Toast.LENGTH_SHORT).show();
            } else {
                gaurdian_relation_validation = "true";

            }

        } else {
            gaurdian_relation_validation = "true";

        }

//13
        if (selected_member_type.equals("non_ind")) {
            if (org_name.getText().toString().length() <= 0) {
                org_name_validation = "false";
                org_name.setError("*Required");

            } else {
                org_name_validation = "true";
                org_name.setError(null);
            }
        } else {
            org_name_validation = "true";
            org_name.setError(null);
        }
//14
        if (selected_member_type.equals("non_ind")) {
            if (registered_name.getText().toString().length() <= 0) {
                registered_name_validation = "false";

                registered_name.setError("*Required");

            } else {

                registered_name_validation = "true";
                registered_name.setError(null);

            }
        } else {
            registered_name_validation = "true";
            registered_name.setError(null);

        }
//15
        if (selected_member_type.equals("non_ind")) {
            if (org_cin_no.getText().toString().length() <= 0) {
                org_cin_no_validation = "false";
                org_cin_no.setError("*Required");


            } else {

                org_cin_no_validation = "true";
                org_cin_no.setError(null);

            }
        } else {
            org_cin_no_validation = "true";
            org_cin_no.setError(null);

        }
//16
        if (selected_member_type.equals("non_ind")) {
            if (date_of_registration.getText().toString().length() <= 0) {
                date_of_registration_validation = "false";
                date_of_registration.setError("*Required");

            } else {

                date_of_registration_validation = "true";
                date_of_registration.setError(null);


            }
        } else {
            date_of_registration_validation = "true";
            date_of_registration.setError(null);

        }
//17
        if (selected_member_type.equals("non_ind")) {
            if (org_rep.getText().toString().length() <= 0) {
                org_rep_validation = "false";
                org_rep.setError("*Required");

            } else {

                org_rep_validation = "true";
                org_rep.setError(null);


            }
        } else {
            org_rep_validation = "true";
            org_rep.setError(null);

        }
//18
        if (selected_member_type.equals("non_ind")) {
            if (selected_position.length() <= 0) {
                rep_position_validation = "false";
                Toast.makeText(getApplicationContext(),"Select position",Toast.LENGTH_SHORT).show();

            } else {

                rep_position_validation = "true";

            }
        } else {
            rep_position_validation = "true";
        }
//19
        if (mobile_no.getText().toString().length() < 10) {

            mobile_no_validation = "false";
            mobile_no.setError("*Required");
        } else {
            mobile_no_validation = "true";
            mobile_no.setError(null);


        }
//20
        if (email_id.getText().toString().length() <= 0) {

            email_id_validation = "true";
            //email_id.setError("*Required");

        } else {
            email_id_validation = "true";
            email_id.setError(null);


        }

//21
        if (aadhar_id.getText().toString().length() > 0 || pan_no.getText().toString().length() > 0
                || licence_no.getText().toString().length() > 0 || voter_id.getText().toString().length() > 0
                || ration_card.getText().toString().length() > 0 || passport_no.getText().toString().length() > 0
                || other_id_num.getText().toString().length() > 0) {

            proof_validation = "true";
        } else {
            proof_validation = "false";
            Toast.makeText(getApplicationContext(),"Min 1 ID Proof needed",Toast.LENGTH_SHORT).show();



        }
        //22
        if (selected_sorce_fund.length() <= 0) {
            sorce_fund_validation = "false";
            Toast.makeText(getApplicationContext(),"Select Source Fund",Toast.LENGTH_SHORT).show();


        } else {
            sorce_fund_validation = "true";

        }

//23
        if (avg_monthly_income.getText().toString().length() <= 0) {
            avg_monthly_income_validation = "false";
            avg_monthly_income.setError("*Required");

        } else {
            avg_monthly_income_validation = "true";
            avg_monthly_income.setError(null);


        }
        //24
        if (nominee_name.getText().toString().length() <= 0) {
            nominee_name_validation = "false";
            nominee_name.setError("*Required");

        } else {
            nominee_name_validation = "true";
            nominee_name.setError(null);

        }
        //26

        if (selected_noiminee_relation.length() <= 0) {
            noiminee_relation_validation = "false";
            Toast.makeText(getApplicationContext(),"Select Nominee Relationship",Toast.LENGTH_SHORT).show();

        } else {
            noiminee_relation_validation = "true";

        }

//27
     if(present_address.getText().toString().length()<=0){
         present_address_validation = "false";
         present_address.setError("*Required");

     }else {
         present_address_validation = "true";
         present_address.setError(null);


     }
     if(permenet_address.getText().toString().length()<=0){
            permenet_address_validation = "false";
            permenet_address.setError("*Required");

     }else {
            permenet_address_validation = "true";
            permenet_address.setError(null);


     } if(office_address.getText().toString().length()<=0){
            office_address_validation = "false";
            office_address.setError("*Required");

     }else {
            office_address_validation = "true";
            office_address.setError(null);


     }
 //28

 if(selectedstate1.length()<=0){
     state1_validation = "false";
     Toast.makeText(getApplicationContext(),"Select State",Toast.LENGTH_SHORT).show();

 }else {
     state1_validation = "true";

 }    if(selectedstate2.length()<=0){
            state2_validation = "false";
            Toast.makeText(getApplicationContext(),"Select State",Toast.LENGTH_SHORT).show();


        }else {
            state2_validation = "true";

 }    if(selectedstate3.length()<=0){
            state3_validation = "false";
            Toast.makeText(getApplicationContext(),"Select State",Toast.LENGTH_SHORT).show();


        }else {
            state3_validation = "true";

 }

 //29
        if(selected_city1.length()<=0){
            city1_validation = "false";
            Toast.makeText(getApplicationContext(),"Select City",Toast.LENGTH_SHORT).show();

        }else {
            city1_validation = "true";

        }
        if(selected_city2.length()<=0){
            city2_validation = "false";
            Toast.makeText(getApplicationContext(),"Select City",Toast.LENGTH_SHORT).show();

        }else {
            city2_validation = "true";

        }
        if(selected_city3.length()<=0){
            city3_validation = "false";
            Toast.makeText(getApplicationContext(),"Select City",Toast.LENGTH_SHORT).show();


        }else {
            city3_validation = "true";

        }

 //29
        if(selecteddistrict1.length()<=0){
            district1_validation = "false";
            Toast.makeText(getApplicationContext(),"Select District",Toast.LENGTH_SHORT).show();


        }else {
            district1_validation = "true";

        }
        if(selecteddistrict2.length()<=0){
            district2_validation = "false";
            Toast.makeText(getApplicationContext(),"Select District",Toast.LENGTH_SHORT).show();


        }else {
            district2_validation = "true";

        }
        if(selecteddistrict3.length()<=0){
            district3_validation = "false";
            Toast.makeText(getApplicationContext(),"Select District",Toast.LENGTH_SHORT).show();


        }else {
            district3_validation = "true";

        }
 //29
        if(pincode_spinner1.getText().toString().length()<=0){
            pincode1_validation = "false";
            pincode_spinner1.setError("*Required");

        }else {
            pincode1_validation = "true";
            pincode_spinner1.setError(null);


        }
        if(pincode_spinner2.getText().toString().length()<=0){
            pincode2_validation = "false";
            pincode_spinner2.setError("*Required");


        }else {
            pincode2_validation = "true";
            pincode_spinner2.setError(null);


        }
        if(pincode_spinner3.getText().toString().length()<=0){
            pincode_spinner3.setError("*Required");

            pincode3_validation = "false";

        }else {
            pincode3_validation = "true";
            pincode_spinner3.setError(null);

        }


//30
 if(present_since.getText().toString().length()<=0){
     present_since_validation = "true";

//     present_since.setError("*Required");
        }else {
     present_since_validation = "true";
     present_since.setError(null);


 }

//31
 if(permanent_since.getText().toString().length()<=0){
     permanent_since_validation = "true";
//     permanent_since.setError("*Required");


 }else {
     permanent_since_validation = "true";
     permanent_since.setError(null);


 }
 
//32
 if(office_since.getText().toString().length()<=0){
     office_since_validation = "true";
//     office_since.setError("*Required");

        }else {
     office_since_validation = "true";
     office_since.setError(null);

        }


 if(selected_postal_correspondence.length()<=0){
     postal_corr_validation="false";
     Toast.makeText(getApplicationContext(),"Select Postal Correspondance",Toast.LENGTH_SHORT).show();

 }else {
     postal_corr_validation="true";
 }



 if(branch_validation.equals("true")&& member_type_validation.equals("true")
         && cus_type_validation.equals("true")&& org_type_validation.equals("true") && salutaion_valiation.equals("true")
         && first_name_validation.equals("true")&& initial_validation.equals("true")&& dob_validation.equals("true")&& doj_validation.equals("true")
         && gender_validation.equals("true")&& father_spouse_validation.equals("true")&& father_spouse_name_validation.equals("true")
         && gaurdian_name_validation.equals("true")&& gaurdian_relation_validation.equals("true")&& org_name_validation.equals("true")
         && registered_name_validation.equals("true")&& org_cin_no_validation.equals("true")&& date_of_registration_validation.equals("true")
         && org_rep_validation.equals("true")&& rep_position_validation.equals("true")
         && mobile_no_validation.equals("true")&& email_id_validation.equals("true")&& proof_validation.equals("true")
         && sorce_fund_validation.equals("true")&& avg_monthly_income_validation.equals("true")&& nominee_name_validation.equals("true")
         && noiminee_relation_validation.equals("true")&& present_address_validation.equals("true")
         && permenet_address_validation.equals("true")&& office_address_validation.equals("true") && state1_validation.equals("true")
         && state2_validation.equals("true")&& state3_validation.equals("true")&& city1_validation.equals("true")
         && city2_validation.equals("true")&& city3_validation.equals("true")&& district1_validation.equals("true")
         && district2_validation.equals("true")&& district3_validation.equals("true")&& pincode1_validation.equals("true")
         && pincode2_validation.equals("true")&& pincode3_validation.equals("true")&& present_since_validation.equals("true")
         && permanent_since_validation.equals("true")&& office_since_validation.equals("true")
         && postal_corr_validation.equals("true") && ofctype_validation.equals("true")&& houseOFFC_type_validation.equals("true")&& ofc_designation_validation.equals("true")
         && present_house_type_validation.equals("true"))
 {
     saveForm();

 }else {
     save_subscriber.setEnabled(true);
     Toast.makeText(getApplicationContext(),"Fill All mandatory field",Toast.LENGTH_SHORT).show();

 }

    }
}