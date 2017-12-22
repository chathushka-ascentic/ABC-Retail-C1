package abc.ap.com.abcfashions.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Users;
import abc.ap.com.abcfashions.services.DataService;
import abc.ap.com.abcfashions.services.VolleySingletonController;
import abc.ap.com.abcfashions.services.VolleyUtils;


/**
 * Created by: Aparna Prasad
 */
public class UserProfileFragment extends Fragment {

    private EditText _fName;
    private EditText _contactNo;
    private EditText _eMail;
    private EditText _landNo;
    private EditText _address;
    private String fName;
    private String contactNo;
    private String landNo;
    private String address;
    private Users userObj;
    private Users existingUserObj;
    private DataService dataService;
    private TextInputLayout _nameWrapper, _contactNoWrapper, _landWrapper,_addressWrapper;
    private LinearLayout _edit, _update;

    private ProgressDialog dialog;
    private View view;
    private InputMethodManager inputManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userObj = new Users();

        view = inflater.inflate(R.layout.user_profile_content, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        try
        {
            dataService = new DataService(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        existingUserObj = App.LoggedInUser;

        _nameWrapper = (TextInputLayout) view.findViewById(R.id.nameWrapper);
        _contactNoWrapper = (TextInputLayout) view.findViewById(R.id.contactWrapper);
        _landWrapper = (TextInputLayout) view.findViewById(R.id.landWrapper);
        _addressWrapper = (TextInputLayout) view.findViewById(R.id.addressWrapper);
        _update = (LinearLayout) view.findViewById(R.id.saveLayout);
        _edit = (LinearLayout) view.findViewById(R.id.editLayout);



        _fName = (EditText) view.findViewById(R.id.input_fname);
        _fName.setText(existingUserObj.getFullname());

        _contactNo = (EditText) view.findViewById(R.id.input_contact);
        _contactNo.setText(existingUserObj.getContactNo1());

        _eMail = (EditText) view.findViewById(R.id.input_email);
        _eMail.setText(existingUserObj.getEmail());

        _landNo = (EditText) view.findViewById(R.id.input_land);
        _landNo.setText(existingUserObj.getContactNo2());


        _address = (EditText) view.findViewById(R.id.input_address);
        _address.setText(existingUserObj.getAddress());


        disableEditText();


        _fName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _nameWrapper.setError(null);
                _nameWrapper.setErrorEnabled(false);
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _contactNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _contactNoWrapper.setError(null);
                _contactNoWrapper.setErrorEnabled(false);
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _address.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _addressWrapper.setError(null);
                _addressWrapper.setErrorEnabled(false);

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _landNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _landWrapper.setError(null);
                _landWrapper.setErrorEnabled(false);

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        _edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                enableEditText();
                _edit.setVisibility(View.INVISIBLE);
                _update.setVisibility(View.VISIBLE);


            }
        });


        _update.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

               try {

                    dialog = new ProgressDialog(getContext(), R.style.AppThemeProgressDial);
                    dialog.setMessage(getString(R.string.updating));
                    dialog.setCancelable(false);
                    dialog.show();
                    Window window = dialog.getWindow();
                    if (window != null) {
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    }

                    if (!validate()) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            _update.setVisibility(View.VISIBLE);
                            _edit.setVisibility(View.INVISIBLE);

                        }
                        inputManager = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        return;
                    }

                    inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);


                    _update.setEnabled(false);
                    setUserData();
                    Gson gson = new Gson();
                    final String userJson = gson.toJson(userObj);
                    JSONObject jsonObject = new JSONObject(userJson);

                    JSONObject usersJson = new JSONObject().put("CustomerUpdateRequest", jsonObject);

                    VolleyUtils.makeJsonObjectRequestPOST(getActivity(), "updateCustomer.php", usersJson, "UpdateCustomer", new VolleySingletonController.VolleyResponseListener() {

                        @Override
                        public void onError(String message) {

                            try {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                _update.setEnabled(true);

                                _update.setVisibility(View.VISIBLE);
                                _edit.setVisibility(View.INVISIBLE);
                                enableEditText();
                                App.setSnackbar(view, getString(R.string.internetCheckMsg));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponse(String res) {
                            try {

                                JsonObject response = ((JsonObject) new JsonParser().parse(res)).getAsJsonObject();

                                if (response.get("status").getAsString().equals("200")) {
                                    _update.setEnabled(true);

                                    disableEditText();
                                    _update.setVisibility(View.INVISIBLE);
                                    _edit.setVisibility(View.VISIBLE);

                                    dataService.updateUsersTable(userObj);

                                    App.LoggedInUser = userObj;
                                    existingUserObj =userObj;

                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    inputManager = (InputMethodManager)
                                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);

                                    App.setSnackbar(view, getString(R.string.updateSuccess));


                                }
                                else
                                {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }

                                    App.setSnackbar(view, response.get("status_desc").getAsString());

                                    _update.setEnabled(true);
                                    _update.setVisibility(View.VISIBLE);
                                    _edit.setVisibility(View.INVISIBLE);
                                    enableEditText();

                                }

                            } catch (final Exception exception) {

                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                _update.setEnabled(true);

                                _update.setVisibility(View.VISIBLE);
                                _edit.setVisibility(View.INVISIBLE);
                                enableEditText();
                                App.setSnackbar(view, getString(R.string.internetCheckMsg));

                            }

                        }

                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void disableEditText() {
        _fName.setEnabled(false);
        _eMail.setEnabled(false);
        _landNo.setEnabled(false);
        _address.setEnabled(false);
        _contactNo.setEnabled(false);


    }

    private void enableEditText() {
        _fName.setEnabled(true);
        _contactNo.setEnabled(true);
        _landNo.setEnabled(true);
        _address.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        if ((_fName.getText()) != null) {
            fName = _fName.getText().toString();
        } else {
            fName = "";
        }

        if ((_contactNo.getText()) != null) {
            contactNo = _contactNo.getText().toString();
        } else {
            contactNo = "";
        }

        if ((_landNo.getText()) != null) {
            landNo = _landNo.getText().toString();
        } else {
            landNo = "";
        }


        if ((_address.getText()) != null) {
            address = _address.getText().toString();
        } else {
            address = "";
        }

        if (fName.trim().isEmpty()) {
            _nameWrapper.setError(getString(R.string.nameValidation));
            valid = false;
        } else {
            _nameWrapper.setError(null);
        }

        if (address.trim().isEmpty()) {
            _addressWrapper.setError(getString(R.string.addressValidation));
            valid = false;
        } else {
            _addressWrapper.setError(null);
        }

        if (!landNo.trim().isEmpty() && (landNo.length() != 10)) {
            _landWrapper.setError(getString(R.string.invalidmob));
            valid = false;

        } else {
            _landWrapper.setError(null);
        }

        if (contactNo.length() != 10) {
            _contactNoWrapper.setError(getString(R.string.invalidmob));
            valid = false;

        } else {
            _contactNoWrapper.setError(null);
        }


        return valid;
    }



    private void setUserData() {


        userObj.setUserId(App.LoggedInUser.getUserId());
        userObj.setFullname(fName);
        userObj.setContactNo1(contactNo);
        userObj.setAddress(address);
        userObj.setContactNo2(landNo);
        userObj.setEmail(App.LoggedInUser.getEmail());

    }

}
