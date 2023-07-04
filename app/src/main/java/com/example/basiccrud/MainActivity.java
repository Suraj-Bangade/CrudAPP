package com.example.basiccrud;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<MyDataModel> dataList;
    private CrudHelper crudHelper;
    private MyDataModel selectedDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listView = findViewById(R.id.listView);

        dataList = new ArrayList<>();
        crudHelper = new CrudHelper(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getNames());
        listView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View v) {
                    try {
                        String name = editTextName.getText().toString().trim();
                        String email = editTextEmail.getText().toString().trim();
                        if (!name.isEmpty() && !email.isEmpty()) {
                            MyDataModel dataModel = new MyDataModel();
                            dataModel.setName(name);
                            dataModel.setEmail(email);
                            long id = crudHelper.insert(dataModel);
                            if (id != -1) {
                                dataModel.setId((int) id);
                                dataList.add(dataModel);
                                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getNames());
                                listView.setAdapter(adapter);
                                clearFields();
                                Toast.makeText(MainActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                                refreshListData();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter name and email", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Unexpected error occured", Toast.LENGTH_SHORT).show();
                    }
                }
             });



        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (selectedDataModel != null) {
                        String name = editTextName.getText().toString().trim();
                        String email = editTextEmail.getText().toString().trim();
                        if (!name.isEmpty() && !email.isEmpty()) {
                            selectedDataModel.setName(name);
                            selectedDataModel.setEmail(email);
                            int rowsAffected = crudHelper.update(selectedDataModel);
                            if (rowsAffected > 0) {
                                adapter.notifyDataSetChanged();
                                clearFields();
                                Toast.makeText(MainActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                refreshListData();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter name and email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please select a data item to update", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Unexpected error occured", Toast.LENGTH_SHORT).show();
                }}
            });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (selectedDataModel != null) {
                        int rowsAffected = crudHelper.delete(selectedDataModel);
                        if (rowsAffected > 0) {
                            dataList.remove(selectedDataModel);
                            adapter.notifyDataSetChanged();
                            clearFields();
                            Toast.makeText(MainActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();

                            refreshListData();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please select a data item to delete", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "An error occurred while deleting data", Toast.LENGTH_SHORT).show();
                }
            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedDataModel = dataList.get(position);
                    editTextName.setText(selectedDataModel.getName());
                    editTextEmail.setText(selectedDataModel.getEmail());
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "Unexpected error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<String> getNames() {
        dataList.clear();
        List<String> names = new ArrayList<>();
        try {
            dataList = crudHelper.retrieve();
            for (MyDataModel dataModel : dataList) {
                names.add(dataModel.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "An error occurred while retrieving data", Toast.LENGTH_SHORT).show();
        }
        return names;
    }



    private void clearFields() {
        editTextName.setText("");
        editTextEmail.setText("");
    }
    private void refreshListData() {
        List<String> names = getNames();
        adapter.clear();
        adapter.addAll(names);
        adapter.notifyDataSetChanged();
    }

}
