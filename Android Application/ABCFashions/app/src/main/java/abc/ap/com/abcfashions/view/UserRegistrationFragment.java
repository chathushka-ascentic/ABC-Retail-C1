package abc.ap.com.abcfashions.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Users;
import abc.ap.com.abcfashions.services.DataService;
import abc.ap.com.abcfashions.services.SyncService;
import abc.ap.com.abcfashions.services.VolleySingletonController;
import abc.ap.com.abcfashions.services.VolleyUtils;
import abc.ap.com.abcfashions.utils.LocalSharedPreferences;


/**
 * Created by: Aparna Prasad
 */

public class UserRegistrationFragment extends Fragment {

    private EditText _name, _email, _password, _reEnterPwd;
    private Button _signUp;
    private View view;
    private String name;
    private String eMail;
    private String password;
    private Users users;
    private TextInputLayout _nameWrapper, _emailWrapper, _pwdWrapper, _rePwdWrapper;
    private ProgressBar _progressBar;
    private InputMethodManager inputManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        users = new Users();
        try {

        view = inflater.inflate(R.layout.user_registration_view, container, false);


        _nameWrapper = (TextInputLayout) view.findViewById(R.id.nameWrapper);
        _emailWrapper = (TextInputLayout) view.findViewById(R.id.emailWrapper);
        _pwdWrapper = (TextInputLayout) view.findViewById(R.id.pwdWrapper);
        _rePwdWrapper = (TextInputLayout) view.findViewById(R.id.rePwdWrapper);
        _progressBar = (ProgressBar) view.findViewById(R.id.progressBarSpinner);

        _name = (EditText) view.findViewById(R.id.input_name);
        _email = (EditText) view.findViewById(R.id.input_email);
        _password = (EditText) view.findViewById(R.id.input_password);
        _reEnterPwd = (EditText) view.findViewById(R.id.input_reEnterPassword);
        _signUp = (Button) view.findViewById(R.id.btn_signup);

        _name.addTextChangedListener(new TextWatcher() {

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

        _email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _emailWrapper.setError(null);
                _emailWrapper.setErrorEnabled(false);

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

        _password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _pwdWrapper.setError(null);
                _pwdWrapper.setErrorEnabled(false);

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

        _reEnterPwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _rePwdWrapper.setError(null);
                _rePwdWrapper.setErrorEnabled(true);
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


        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    _progressBar.setVisibility(View.VISIBLE);
                    _signUp.setVisibility(View.INVISIBLE);
                    if (!validate()) {
                        _progressBar.setVisibility(View.INVISIBLE);
                        _signUp.setVisibility(View.VISIBLE);
                        inputManager = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        return;
                    }


                    users.setFullname(name);
                    users.setEmail(eMail);


                    Gson gson = new Gson();
                    String userJson = gson.toJson(users);
                    JSONObject jsonObject = new JSONObject(userJson);
                    jsonObject.put("cust_pwd",password);

                    JSONObject usersJsonObject1 = new JSONObject().put("CustomerInsertRequest", jsonObject);


                    VolleyUtils.makeJsonObjectRequestPOST(getActivity(), "insertCustomer.php", usersJsonObject1, "InsertCustomer", new VolleySingletonController.VolleyResponseListener() {

                                @Override
                                public void onError(String message) {
                                    try
                                    {
                                                _progressBar.setVisibility(View.INVISIBLE);
                                                _signUp.setVisibility(View.VISIBLE);
                                                 App.setSnackbar(view, getString(R.string.internetCheckMsg));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onResponse(String res) {

                                    try {

                                        DataService syncService = new DataService(getActivity().getApplicationContext());

                                        JsonObject jobject = ((JsonObject) new JsonParser().parse(res)).getAsJsonObject();
                                        String resS = jobject.get("status").getAsString();
                                        int uId = jobject.get("userId").getAsInt();

                                        syncService.ClearDb();
                                        LocalSharedPreferences.GetInstance(getContext()).ClearStoredUser();

                                        if (resS.equals("200")) {
                                            users.setUserId(uId);
                                            App.LoggedInUser = users;

                                            DataService dataService = new DataService(getActivity().getApplicationContext());
                                            dataService.insertUser(users);

                                            getData();


                                        } else if (resS.equals("201"))
                                        {
                                                    _progressBar.setVisibility(View.INVISIBLE);
                                                    _signUp.setVisibility(View.VISIBLE);
                                                    App.setSnackbar(view, getString(R.string.accountExsist));
                                        }
                                        else
                                        {
                                                    _progressBar.setVisibility(View.INVISIBLE);
                                                    _signUp.setVisibility(View.VISIBLE);
                                                    App.setSnackbar(view, getString(R.string.internetCheckMsg));
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();

                                        _progressBar.setVisibility(View.INVISIBLE);
                                        _signUp.setVisibility(View.VISIBLE);
                                        App.setSnackbar(view, getString(R.string.internetCheckMsg));
                                    }
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        }catch(Exception e){
            e.printStackTrace();
        }


        return view;
    }







    private boolean validate() {
        boolean valid = true;
        _nameWrapper.setError(null);

        if ((_name.getText()) != null) {
            name = _name.getText().toString();
        } else {
            name = "";
        }

        if ((_email.getText()) != null) {
            eMail = _email.getText().toString();
        } else {
            eMail = "";
        }

        if ((_password.getText()) != null) {
            password = _password.getText().toString();
        } else {
            password = "";
        }

        String reEnterPassword;
        if ((_reEnterPwd.getText()) != null) {
            reEnterPassword = _reEnterPwd.getText().toString();
        } else {
            reEnterPassword = "";
        }

        if (name.trim().isEmpty()) {
            _nameWrapper.setError(getString(R.string.nameValidation));
            valid = false;
        }
        else if (eMail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(eMail).matches()) {
            _emailWrapper.setError(getString(R.string.emailValidation));
            valid = false;
        }
        else if (password.trim().isEmpty() || password.trim().length() < 4 || password.trim().length() > 10) {
            _pwdWrapper.setError(getString(R.string.passwordValidation));
            valid = false;
        }
        else if ((!password.trim().equals(reEnterPassword))) {
                _rePwdWrapper.setError(getString(R.string.passwordMismatch));
                valid = false;
        }

        return valid;
    }

    private void getData()
    {
        SyncService.registerListener(new SyncService.SyncCompleteListener() {
            @Override
            public void onSuccess() {

                        LocalSharedPreferences localSharedPreferences = LocalSharedPreferences.GetInstance(getContext());
                        localSharedPreferences.StoreUser();

                        App.setSnackbar(view, getString(R.string.registrationSuccess));

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("gotoMenu",0);
                        intent.putExtra("branchId",0);
                        startActivity(intent);
                        getActivity().finish();
            }

            @Override
            public void onError() {

                try
                {

                    DataService syncService = new DataService(getActivity().getApplicationContext());
                    syncService.ClearDb();
                    LocalSharedPreferences.GetInstance(getContext()).ClearStoredUser();

                    _progressBar.setVisibility(View.INVISIBLE);
                    _signUp.setVisibility(View.VISIBLE);
                    App.setSnackbar(view, getString(R.string.internetCheckMsg));

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        SyncService syncService = new SyncService(getActivity().getApplicationContext());
        syncService.StartMainSync(getActivity(), App.LoggedInUser.getUserId());
    }
}
