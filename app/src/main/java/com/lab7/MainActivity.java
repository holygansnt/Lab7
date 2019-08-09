package com.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private EditText edusername;
    private EditText edpassword;
    private Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edusername = (EditText) findViewById(R.id.username);
        edpassword = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = edusername.getText().toString();
                final String password = edpassword.getText().toString();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                // truy vấn vào nhánh username mà người dùng nhập
                DatabaseReference users = firebaseDatabase.getReference(USERS).child(username);

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!username.isEmpty() && !password.isEmpty()) {
                            if (dataSnapshot.getValue() == null) {

                                Toast.makeText(MainActivity.this,
                                        getString(R.string.notify_user_is_not_exists), Toast.LENGTH_SHORT).show();
                            } else {

                                // lấy dữ liệu từ dataSnapshot gán vào model User,
                                // lưu ý : biến ở User cần trùng khớp với tên các giá trị trên firebase
                                User user = dataSnapshot.getValue(User.class);

                                if (user.password.equals(password)) {

                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));

                                } else {

                                    Toast.makeText(MainActivity.this, getString(R.string.notify_wrong_password), Toast.LENGTH_SHORT).show();

                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    public void SingUp(View view) {

        startActivity(new Intent(this, SignUpActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        edusername.setText(SignUpActivity.username);
        edpassword.setText(SignUpActivity.password);
    }
}
