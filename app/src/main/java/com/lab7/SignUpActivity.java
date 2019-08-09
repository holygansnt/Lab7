package com.lab7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lab7.model.User;

import static com.lab7.firebase.FirebaseQuery.USERS;

public class SignUpActivity extends AppCompatActivity {
    private EditText edfirstName;
    private EditText edLastName;
    private EditText edUserName;
    private EditText edPassWord;
    private Button button;
    public static String username;
    public static String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edfirstName = (EditText) findViewById(R.id.firstName);
        edLastName = (EditText) findViewById(R.id.LastName);
        edUserName = (EditText) findViewById(R.id.UserName);
        edPassWord = (EditText) findViewById(R.id.PassWord);
        button = (Button) findViewById(R.id.button);


    }


    public void SignUp(View view) {
        final String firstName = edfirstName.getText().toString().trim();
        final String lastName = edLastName.getText().toString().trim();
        username = edUserName.getText().toString().trim();
        password = edPassWord.getText().toString().trim();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference(USERS).child(username);

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // chua co user voi username duoc nhap
                if (!username.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
                    if (dataSnapshot.getValue() == null) {

                        // hoc vien tu khoi tao model User
                        User user = new User();
                        user.firstName = firstName;
                        user.lastName = lastName;
                        user.password = password;


                        // them user vao nhanh Users
                        users.setValue(user, new DatabaseReference.CompletionListener() {

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                // hoc vien tu viet va kiem tra su kien loi va thanh cong
                                onBackPressed();
                                Toast.makeText(SignUpActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // username da ton tai, thong bao chon username khac
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                getString(R.string.notify_user_is_exits), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Nhập đủ nội dung", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
