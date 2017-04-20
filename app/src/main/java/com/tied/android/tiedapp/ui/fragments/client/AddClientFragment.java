package com.tied.android.tiedapp.ui.fragments.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.customs.model.DataModel;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.retrofits.services.TerritoryApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.SelectTerritoryActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.SelectDataDialog;
import com.tied.android.tiedapp.ui.dialogs.SimpleDialogSelector;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HTTPConnection;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class AddClientFragment extends Fragment implements View.OnClickListener, SelectDataDialog.SelectedListener{

    public static final String TAG = AddClientFragment.class
            .getSimpleName();

    int[] id_industry = {1, 2, 3, 4};
    String[] txt_industry = {"Food","Travel","Technology","Business"};
    Boolean[] industry_status = {false,false,false,false};
    ArrayList<DataModel> listIndustry;

    View addressLayout;
    int[] id_line = {1, 2};
    String[] txt_line = {"Line1","Line2"};
    Boolean[] line_status = {false,false};
    ArrayList<DataModel> listLine;
    ArrayList<Territory> selectedTerritories = new ArrayList<Territory>();


    public ImageView avatar;
    private EditText company,name, fax, phone, email, revenue, ytd_revenue, note;//, street, city, , zip, ;
    View uploadClients;
    private LinearLayout ok_but;
    private TextView industry, line, address;//, birthday;
    private Coordinate coordinate;

    Spinner stateSpinner;

    private String companyText, nameText, streetText, cityText, stateText, zipText, phoneText, faxText, noteText, birthdayText;
    private Location location;

    private int visit_frequency = 1;
    private LinearLayout weekly_layout, two_weeks_layout,monthly_layout,three_weeks_layout;
    LinearLayout visit_radio, camera;
    RelativeLayout industry_layout, territory_layout, name_layout;
    Integer industryId=null, lineId=null;
    View lineLayout;
    TextView txt_title, txt_territory;
    boolean addTerritory=false;

    //int industry_id = 1;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    private User user;
    private Client client;
    private Bundle bundle;
    private Uri uri;

    FragmentIterationListener mListener;
    Territory territory;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new AddClientFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_client, container, false);
        View focusView=view.findViewById(R.id.getFocus);
        focusView.requestFocus();
        focusView.setFocusableInTouchMode(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }



    public void initComponent(View view) {
        txt_title =(TextView) view.findViewById(R.id.txt_title);
        company = (EditText) view.findViewById(R.id.company);
        name = (EditText) view.findViewById(R.id.name);
        address =(TextView) view.findViewById(R.id.address_tv);
        email = (EditText) view.findViewById(R.id.email);
        txt_territory =(TextView) view.findViewById(R.id.territory_tv);

        lineLayout=view.findViewById(R.id.line_layout);
        lineLayout.setOnClickListener(this);

        addressLayout=view.findViewById(R.id.address_layout);
        addressLayout.setOnClickListener(this);

        uploadClients = view.findViewById(R.id.upload_clients);
        uploadClients.setOnClickListener(this);
        //   stateSpinner = (Spinner) view.findViewById(R.id.state);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.my_spinner_item, MyUtils.States.asArrayList());
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown);
        // stateSpinner.setAdapter(adapter);

        //stateSpinner.setSelection(adapter.getPosition("TX"));

        phone = (EditText) view.findViewById(R.id.phone);
        fax = (EditText) view.findViewById(R.id.fax);
        revenue = (EditText) view.findViewById(R.id.revenue);
        ytd_revenue = (EditText) view.findViewById(R.id.ytd_revenue);
        note = (EditText) view.findViewById(R.id.note);
        //birthday = (TextView) view.findViewById(R.id.birthday);
        //birthday_layout = (LinearLayout) view.findViewById(R.id.birthday_layout);
        //birthday_layout.setOnClickListener(this);

        industry_layout = (RelativeLayout) view.findViewById(R.id.industry_layout);
        industry = (TextView) view.findViewById(R.id.industry);
        industry.setOnClickListener(this);

        territory_layout = (RelativeLayout) view.findViewById(R.id.territory_layout);
        territory_layout.setOnClickListener(this);

        name_layout = (RelativeLayout) view.findViewById(R.id.name_layout);
        camera = (LinearLayout) view.findViewById(R.id.camera);
        camera.setOnClickListener(this);

        line = (TextView) view.findViewById(R.id.line);
        line.setOnClickListener(this);

        weekly_layout = (LinearLayout) view.findViewById(R.id.weekly_layout);
        monthly_layout = (LinearLayout) view.findViewById(R.id.monthly_layout);
        two_weeks_layout = (LinearLayout) view.findViewById(R.id.two_weeks_layout);
        three_weeks_layout = (LinearLayout) view.findViewById(R.id.three_weeks_layout);

        weekly_layout.setOnClickListener(this);
        monthly_layout.setOnClickListener(this);
        three_weeks_layout.setOnClickListener(this);
        two_weeks_layout.setOnClickListener(this);
        visit_radio = (LinearLayout) view.findViewById(R.id.visit_radio);

        selectRadio(visit_radio,0);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        ok_but = (LinearLayout) view.findViewById(R.id.ok_but);
        ok_but.setOnClickListener(this);
        avatar.setOnClickListener(this);

        listIndustry = new ArrayList<DataModel>();

        listLine = new ArrayList<DataModel>();
        for(int i = 0; i < id_line.length; i++){
            DataModel list_line = new DataModel(id_line[i],txt_line[i],line_status[i]);
            listLine.add(list_line);
        }

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);

            listIndustry=MyUtils.getIndustriesAsList();

            String client_json = bundle.getString(Constants.CLIENT_DATA);
            client = gson.fromJson(client_json, Client.class);
            if (client != null){
                txt_title.setText("Update Client");
                MyUtils.Picasso.displayImage(client.getLogo(), avatar);
                name.setText(client.getFull_name());
                company.setText(client.getCompany());
                view.findViewById(R.id.import_file).setVisibility(View.GONE);
                ///street.setText(client.getAddress().getStreet());
                //zip.setText(client.getAddress().getZip());
                //city.setText(client.getAddress().getCity());
               // stateSpinner.setSelection(adapter.getPosition(client.getAddress().getState()));
                location=client.getAddress();
                address.setText(location.getStreet()+", "+location.getCity()+", "+location.getState()+" "+location.getZip());
                phone.setText(client.getPhone());
                fax.setText(client.getFax());
                revenue.setText(client.getRevenue());
                ytd_revenue.setText(client.getYtd_revenue());
                note.setText(client.getNote());
                email.setText(client.getEmail());

                territory = client.getTerritory();
                if (territory != null) {
                    txt_territory.setText(territory.getCounty() + ", " + territory.getState());
                    selectedTerritories.add(territory);
                }

                industryId = client.getIndustry_id();
                for (int i = 0 ; i < listIndustry.size() ; i++) {
                    DataModel model = listIndustry.get(i);
                    if (model.getId() == client.getIndustry_id()) {
                        industry.setText(model.getName());
                        break;
                    }
                }

                if (user.getId().equals(client.getUser_id())) {
                    name_layout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.CreateSchedule, bundle);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SELECT_TERRITORY && resultCode == Activity.RESULT_OK) {

            selectedTerritories.clear();
            selectedTerritories.add( (Territory) (data.getSerializableExtra("selected")));

            txt_territory.setText(selectedTerritories.get(0).getCounty() + ", " + selectedTerritories.get(0).getState());
        }
    }

    private boolean validated() {
        companyText = company.getText().toString();
        if(companyText.isEmpty()) {
            MyUtils.showErrorAlert(getActivity(), "Company name is required");
            return false;
        }
       /* nameText = name.getText().toString();
        if(nameText.isEmpty()) {
            MyUtils.showErrorAlert(getActivity(), "Contact person's name is required");
            return false;
        }*/
       // streetText = street.getText().toString();
       // cityText = city.getText().toString();
        //zipText = zip.getText().toString();
        //stateText = stateSpinner.getSelectedItem().toString().trim();
        //location = new Location(cityText, zipText, stateText, streetText);
        if(location==null) {
            MyUtils.showErrorAlert(getActivity(), "Enter client location");
            return false;
        }

        /*if (selectedTerritories.size() == 0) {
            MyUtils.showErrorAlert(getActivity(), "You must select an territory");
            return false;
        }*/

        //birthdayText = birthday.getText().toString();
        if(industryId==null) {
            MyUtils.showErrorAlert(getActivity(), "You must select an industry");
            return false;
        }

        noteText = note.getText().toString();
        phoneText = phone.getText().toString();
        faxText = fax.getText().toString();

        return true;
       // return !streetText.equals("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_but:
                uri = ((AddClientActivity) getActivity()).outputUri;
               /* if(client == null){
                    Toast.makeText(getActivity(), "Upload user image", Toast.LENGTH_LONG).show();
                }
                else */
                if (validated()) {
//                    new GeocodeAsyncTask().execute();
                    if (client == null){
                        createClient();
                    }else{
                        editClient();
                    }
                }
                break;
            case R.id.weekly_layout:
                selectRadio(visit_radio,0);
                break;
            case R.id.two_weeks_layout:
                selectRadio(visit_radio,1);
                break;
            case R.id.three_weeks_layout:
                selectRadio(visit_radio,2);
                break;
            case R.id.monthly_layout:
                selectRadio(visit_radio,3);
                break;
           /* case R.id.birthday_layout:
                DialogFragment dateFragment = new ClientDatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;*/
            case R.id.industry:
                SimpleDialogSelector alert = new SimpleDialogSelector(getActivity(), listIndustry, new SimpleDialogSelector.SelectedListener() {
                    @Override
                    public void selected(DataModel dataModel) {
                        industryId=dataModel.getId();
                        industry.setText(dataModel.getName());
                    }
                });
                alert.showDialog();
                break;
            case R.id.line_layout:
               // SelectDataDialog alert_line = new SelectDataDialog(listLine,line,this);
               /* SimpleDialogSelector alert_line = new SimpleDialogSelector(getActivity(), listLine, new SimpleDialogSelector.SelectedListener() {
                    @Override
                    public void selected(DataModel dataModel) {
                        lineId=dataModel.getId();
                        line.setText(dataModel.getName());
                    }
                });
                alert_line.showDialog();*/
//                MyUtils.startRequestActivity(getActivity(), LinesListActivity.class, Constants.ADD_LINE, bundle);
                break;
            case R.id.avatar:
                showChooser();
                break;
            case R.id.address_layout:
                MyUtils.showAddressDialog(getActivity(), "Address", location , new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        location = (Location)response;
                        Logger.write("***************************", location.toJSONString());
                        address.setText(location.getLocationAddress());
                    }
                });
                break;
            case R.id.territory_layout:
                bundle.putInt(Constants.SHOW_TERRITORY, 0);
                bundle.putBoolean("single", true);
                MyUtils.startRequestActivity(getActivity(), SelectTerritoryActivity.class, Constants.SELECT_TERRITORY, bundle);
                break;
            case R.id.upload_clients:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.HOST+Constants.API_UPLOAD_CLIENTS+"?token="+MyUtils.getUserLoggedIn().getToken()));
                startActivity(i);

                /* Bundle bundle=new Bundle();
                bundle.putString("url", Constants.HOST+Constants.API_UPLOAD_CLIENTS+"?token="+MyUtils.getUserLoggedIn().getToken());
                bundle.putString("title", "Add Clients");
                MyUtils.startActivity(getActivity(), WebviewActivity.class, bundle);*/
                break;
        }
    }

    public void selectRadio(LinearLayout visit_radio, int position){
        int index = 0;
        for(int i = 0; i < visit_radio.getChildCount(); i++){
            if(visit_radio.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) visit_radio.getChildAt(i);
                ImageView img_radio = (ImageView) child.getChildAt(0);
                TextView title = (TextView) child.getChildAt(1);
                if(position != index){
                    img_radio.setBackgroundResource(R.mipmap.circle_uncheck);
                    title.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                }else{
                    img_radio.setBackgroundResource(R.mipmap.circle_check2);
                    title.setTextColor(getResources().getColor(R.color.button_bg));
                    visit_frequency = i+1;
                    Log.d(TAG, "radio_value"+visit_frequency);
                }
                index++;
            }
        }
    }

    @Override
    public void selectedNow(DataModel dataModel, TextView txt) {
        String text_industry = "- " + dataModel.getName() + " -";
        txt.setText(text_industry);
    }

    public void showChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Take a pics", "Browse Gallery"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                                ((AddClientActivity) getActivity()).imageUri = Uri.fromFile(photo);
                                ((AddClientActivity) getActivity()).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                                break;

                            case 1:
                                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                getIntent.setType("image/*");

                                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                pickIntent.setType("image/*");

                                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                                getActivity().startActivityForResult(chooserIntent, IMAGE_PICKER_SELECT);

                                break;

                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    class GeocodeAsyncTask extends MyAddressAsyncTask {

        String errorMessage = "";
        JSONObject jObject;
        JSONObject places = null;
        String lat;

        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(getActivity());
        }

        @Override
        protected Address doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

            try {
                Log.d(TAG, location.getLocationAddress());
                addresses = geocoder.getFromLocationName(location.getLocationAddress(), 1);
            } catch (IOException e) {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
            }

            if (addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if (getActivity() == null) return;
            if (address != null) {
                Coordinate coordinate = new Coordinate(address.getLatitude(), address.getLongitude());
                location.setCoordinate(coordinate);
                if (client == null){
                    createClient();
                }else{
                    editClient();
                }
            }else{
                DialogUtils.closeProgress();
                Toast.makeText(getActivity(), "sorry location cannot be found in map", Toast.LENGTH_LONG).show();
            }
        }
    }

    public Client initClient(Client client){
        client.setPhone(phoneText);
        client.setFull_name(nameText);
        client.setCompany(companyText);
        client.setAddress(location);
        if(lineId!=null) client.setLine_id(""+lineId);
        client.setIndustry_id(industryId);
        client.setVisit_id(visit_frequency);
        //client.setBirthday(birthdayText);
        client.setDescription(noteText);

        if(selectedTerritories.size()==0) {
            if(location.getCounty() !=null) {
                territory=new Territory();
                territory.setState(location.getState());
                territory.setCounty(location.getCounty());
                territory.setCountry(location.getCountry());
                client.setTerritory(territory);
            }
        }else      client.setTerritory(selectedTerritories.get(0));



        return client;
    }



    private void createClient() {
        Client client = new Client();
        client = initClient(client);
        File file = null;
        if(uri!=null) {
            file = new File(uri.getPath());


            Log.d("Uri", uri.getPath());
            Log.d("File path", file.getPath());
            Log.d("file Name", file.getName());
        }

        MultipartBody.Part body=null;
        if(file!=null) {

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
           body =
                    MultipartBody.Part.createFormData("logo", file.getName(), requestFile);
        }
        RequestBody clientReq =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), new Gson().toJson(client));


        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.createClient(user.getToken(), clientReq, body);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity() == null) return;
                try {
                    Log.d(TAG + " onResponse", resResponse.body().toString());
                    final ClientRes clientRes = resResponse.body();
                    if (clientRes.isAuthFailed()) {
                        User.LogOut(getActivity().getApplicationContext());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 201) {
                        Log.d(TAG + " client good", clientRes.getClient().toString());
                        bundle.putBoolean(Constants.NO_CLIENT_FOUND, false);
                        DialogUtils.closeProgress();
                        Client.clientCreated(getActivity().getApplicationContext());
                        MyUtils.showMessageAlert(getActivity(), "Client successfully created");
                        name.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Bundle bundle=new Bundle();
                                bundle.putSerializable(Constants.CLIENT_DATA, clientRes.getClient());
                                bundle.putSerializable(Constants.USER_DATA, user);
                                MyUtils.startActivity(getActivity(), ActivityClientProfile.class, bundle);
                                getActivity().finish();
                            }
                        }, 1000);
                       // MyUtils.startActivity(getActivity(), MainActivity.class, bundle);
                    } else {
                        DialogUtils.closeProgress();
                        //Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                        MyUtils.showErrorAlert(getActivity(), clientRes.getMessage());
                    }
                }catch (Exception e) {
                    MyUtils.showToast(getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ClientRes> ClientResponseCall, Throwable t) {
                //Toast.makeText(getActivity(), "On failure create: error encountered", Toast.LENGTH_LONG).show();
                //Log.d(TAG + " onFailure create", t.toString());
                MyUtils.showToast(getString(R.string.connection_error));
                DialogUtils.closeProgress();
            }
        });
        if(selectedTerritories.size()==0 && territory!=null) {
            //add this territory
            addTerritory();
        }
    }

    public static void createClient(final Client client, final Context context, final HTTPConnection.AjaxCallback callback) {

        MultipartBody.Part body=null;

        RequestBody clientReq =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), new Gson().toJson(client));
        User user=MyUtils.getUserLoggedIn();
        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.createClient(user.getToken(), clientReq, body);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (context == null) return;
                try {
                    Log.d(TAG + " onResponse", resResponse.body().toString());
                    ClientRes clientRes = resResponse.body();
                    if (clientRes.isAuthFailed()) {
                        User.LogOut(context);
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 201) {
                        Log.d(TAG + " client good", clientRes.getClient().toString());
                        //bundle.putBoolean(Constants.NO_CLIENT_FOUND, false);
                        DialogUtils.closeProgress();
                        Client.clientCreated(context.getApplicationContext());
                        MyUtils.showToast("Client successfully created");
                        callback.run(201, clientRes.toString());
                        // MyUtils.startActivity(getActivity(), MainActivity.class, bundle);
                    } else {
                        DialogUtils.closeProgress();
                        //Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                        MyUtils.showErrorAlert((Activity)context, clientRes.getMessage());
                    }
                }catch (Exception e) {
                    MyUtils.showToast(context.getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ClientRes> ClientResponseCall, Throwable t) {
                //Toast.makeText(getActivity(), "On failure create: error encountered", Toast.LENGTH_LONG).show();
                //Log.d(TAG + " onFailure create", t.toString());
                MyUtils.showToast(context.getString(R.string.connection_error));
                DialogUtils.closeProgress();
            }
        });
    }


    private void editClient() {
        client = initClient(client);

        DialogUtils.displayProgress(getActivity());
        ClientApi clientApi = MainApplication.createService(ClientApi.class);
        Call<ClientRes> response = null;
        if(uri != null){
            File file = new File(uri.getPath());

            Log.d("Uri", uri.getPath());
            Log.d("File path", file.getPath());
            Log.d("file Name", file.getName());

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            RequestBody clientReq =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), new Gson().toJson(client));

            // MultipartBody.Part is used to send also the actual file name
            final MultipartBody.Part body =
                    MultipartBody.Part.createFormData("logo", file.getName(), requestFile);

            response = clientApi.editClient(user.getToken(), client.getId(), clientReq, body);

        }else{
            response = clientApi.editNoAvatarClient(user.getToken(), client.getId(), client);
        }

        Log.d(TAG + " form ", user.getToken() +" "+client.toString());
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity() == null) return;
                Log.d(TAG + " onResponse", resResponse.body().toString());
                ClientRes clientRes = resResponse.body();
                if (clientRes.isAuthFailed()) {
                    User.LogOut(getActivity().getApplicationContext());
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                    Log.d(TAG + " client updated", clientRes.getClient().toString());
                    DialogUtils.closeProgress();

//                    bundle.putBoolean(Constants.NO_CLIENT_FOUND, false);
//                    Client.clientCreated(getActivity().getApplicationContext());
//                    bundle.putBoolean(Constants.CLIENT_EDITED, true);
//                    MyUtils.startActivity(getActivity(), MainActivity.class, bundle);

                    Intent intent = new Intent();

                    Bundle b =new Bundle();
                    b.putSerializable("client_id", client.getId());
                    intent.putExtras(b);

                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finishActivity(Constants.EDIT_CLIENT);
                    getActivity().finish();
                } else {
                    DialogUtils.closeProgress();
                    Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClientRes> ClientResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure edit: error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure edit", t.toString());
                DialogUtils.closeProgress();
            }
        });
        if(selectedTerritories.size()==0 && territory!=null) {
            //add this territory
            addTerritory();
        }
    }

    private void addTerritory() {
        User user=MyUtils.getUserLoggedIn();
        TerritoryApi territoryApi = MainApplication.createService(TerritoryApi.class);
        Call<ResponseBody> response = territoryApi.createUnique(user.getId(),territory);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (getActivity() == null) return;
                try {

                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Log.d(TAG + " onResponse", response.toString());
                        if (response.isAuthFailed()) {
                        User.LogOut(getActivity());
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {

                       // Log.d(TAG + " client good", clientRes.getClient().toString());
                        //bundle.putBoolean(Constants.NO_CLIENT_FOUND, false);
                       // DialogUtils.closeProgress();
                       // Client.clientCreated(context.getApplicationContext());
                      //  MyUtils.showToast("Client successfully created");
                       // callback.run(201, clientRes.toString());
                        // MyUtils.startActivity(getActivity(), MainActivity.class, bundle);
                    } else {
                        DialogUtils.closeProgress();
                        //Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                        //MyUtils.showErrorAlert(getActivity(), meta.getUser_message());
                    }
                }catch (Exception e) {
                    //MyUtils.showToast(getActivity().getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                //Toast.makeText(getActivity(), "On failure create: error encountered", Toast.LENGTH_LONG).show();
                //Log.d(TAG + " onFailure create", t.toString());
                //MyUtils.showToast(getActivity().getString(R.string.connection_error));
                //DialogUtils.closeProgress();
            }
        });
    }


}
