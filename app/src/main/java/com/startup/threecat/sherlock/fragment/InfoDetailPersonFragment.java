package com.startup.threecat.sherlock.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.databasehelper.DatabaseUtil;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Person;
import com.startup.threecat.sherlock.util.LoadImage;

import java.util.ArrayList;

/**
 * Created by Dell on 20-Jul-16.
 */
public class InfoDetailPersonFragment extends Fragment {

    private Person person;
    private Context context;
    private ImageView imgPerson;
    private EditText edtName;
    private EditText edtAge;
    private EditText edtHeight;
    private EditText edtHairColor;
    private EditText edtAddress;
    private EditText edtComment;
    private RadioButton rbtMale;
    private RadioButton rbtFemale;
    private Button btnSave;

    public static InfoDetailPersonFragment newInstance(Person person) {
        Bundle arg = new Bundle();
        arg.putSerializable(ConstantData.PERSON_TAG, person);

        InfoDetailPersonFragment fragment = new InfoDetailPersonFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        this.person = (Person) arg.getSerializable(ConstantData.PERSON_TAG);
        this.context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_detail_person, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {

        imgPerson = (ImageView)view.findViewById(R.id.imgAddPerson);
        edtName = (EditText)view.findViewById(R.id.edtInputName);
        edtAge = (EditText)view.findViewById(R.id.edtInputAge);
        edtHeight = (EditText)view.findViewById(R.id.edtInputHeight);
        edtHairColor = (EditText)view.findViewById(R.id.edtInputHairColor);
        edtAddress = (EditText)view.findViewById(R.id.edtAddress);
        edtComment = (EditText)view.findViewById(R.id.edtAdditionalComment);
        rbtMale = (RadioButton)view.findViewById(R.id.rbtMale);
        rbtFemale = (RadioButton)view.findViewById(R.id.rbtFemale);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        LoadImage.loadImagePerson(context, imgPerson, person.getUrlImage());
        edtName.setText(person.getName());
        edtAge.setText(person.getAge() + "");
        edtHeight.setText(person.getHeight() + "");
        edtAddress.setText(person.getAddress());
        edtComment.setText(person.getComment());
        edtHairColor.setText(person.getHairColor());
        if(person.isGender() == Person.MALE) {
            rbtMale.setChecked(true);
        }else {
            rbtFemale.setChecked(true);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveInfo();
            }
        });
    }

    public void handleSaveInfo() {

        String newName = edtName.getText().toString().trim();
        String newSAge = edtAge.getText().toString();
        String newSHeight = edtHeight.getText().toString();
        String newHairColor = edtHairColor.getText().toString();
        String newAddress = edtAddress.getText().toString();
        String newComment = edtComment.getText().toString();
        int newGender = rbtMale.isChecked() ? Person.MALE : Person.FEMALE;

        if(checkEmpty(newName, newSAge, newSHeight)) {
            int newAge = Integer.parseInt(newSAge);
            float newHeight = Float.parseFloat(newSHeight);
            Person tempPerson = new Person(person.getId(), newName, newAge, newHeight, newGender,
                    newHairColor, newAddress, newComment, person.getMovements());
            tempPerson.setUrlImage(person.getUrlImage());

            if(isChange(tempPerson)) {
                saveInfoPerson(tempPerson);
            }else {
                Toast.makeText(context, "No any change", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveInfoPerson(Person tempPerson) {

        new AsyncTask<Person, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Person... params) {
                boolean isUpdated = DatabaseUtil.updateInfoPerson(context, params[0]);
                if(isUpdated) {
                    person.copy(params[0]);
                }
                return isUpdated;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean) {
                    Toast.makeText(context, "Update successful !", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context, "Update fail", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(tempPerson);
    }

    public boolean checkEmpty(String name, String sAge, String sHeight) {

        boolean check = true;
        ArrayList<EditText> editTexts = new ArrayList<>();
        String error = "Required Value !";
        if(name.equals("")) {
            edtName.setError(error);
            editTexts.add(edtName);
            check = false;
        }
        if(sAge.equals("")) {
            edtAge.setError(error);
            editTexts.add(edtAge);
            check = false;

        }
        if(sHeight.equals("")) {
            edtHeight.setError(error);
            editTexts.add(edtHeight);
            check = false;
        }

        if(editTexts.size() != 0) {
            editTexts.get(0).requestFocus();
        }
        return check;
    }

    public boolean isChange(Person temp) {
        boolean change = false;
        if(!temp.getName().equals(person.getName())) {
            change = true;
        }else if(temp.getAge() != person.getAge()) {
            change = true;
        }else if(temp.getHeight() != person.getHeight()) {
            change = true;
        }else if(!temp.getHairColor().equals(person.getHairColor())) {
            change = true;
        }else if(!temp.getAddress().equals(person.getAddress())) {
            change = true;
        }else if(!temp.getComment().equals(person.getComment())) {
            change = true;
        }else if(temp.isGender() != person.isGender()) {
            change = true;
        }
        return change;
    }
}
