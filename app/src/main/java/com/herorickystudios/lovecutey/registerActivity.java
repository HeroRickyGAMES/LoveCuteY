package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.local.PersistedInstallationEntry;

import java.util.HashMap;
import java.util.Map;

//Programado por HeroRickyGames

public class registerActivity extends AppCompatActivity {

    private EditText edit_Nome,edit_Email,edit_senha,reeditsenha, idade_text;
    private Button register_button;
    private RadioGroup radioGrup;
    String[] menssagens = {"Preencha todos os campos para continuar", "Cadastro feito com sucesso!"};
    String usuarioID;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;


    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //iniciar Componentes
        edit_Nome = findViewById(R.id.nome_register);
        edit_Email = findViewById(R.id.username_Register);
        edit_senha = findViewById(R.id.password_Register);
        reeditsenha = findViewById(R.id.repassword_Register);
        register_button = findViewById(R.id.register_button);
        idade_text = findViewById(R.id.idade_text);
        radioGrup = findViewById(R.id.radioGroup);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 30);
        locationRequest.setFastestInterval(1000 * 5);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //strings para verificação se ou não
                String nome = edit_Nome.getText().toString();
                String email = edit_Email.getText().toString();
                String senha = edit_senha.getText().toString();
                String resenha = reeditsenha.getText().toString();
                String idade = idade_text.getText().toString();

                int idadee =Integer.valueOf(idade);

                //E se foi cadastrado?
                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || resenha.isEmpty() || idade.isEmpty()){
                //Falta campos USUARIO!
                    Snackbar snackbar = Snackbar.make(view, menssagens[0],Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{

                    //Verificação de idade
                    if(idadee <= 15){

                        Toast.makeText(registerActivity.this, "Você é menor que 16 anos!", Toast.LENGTH_SHORT).show();

                    }else{

                        if(senha.intern() == resenha.intern()){
                            Toast.makeText(registerActivity.this, "Cadastro feito com sucesso!", Toast.LENGTH_SHORT).show();
                            //No caso ta tudo digitado certo e a idade bate, então vamos cadastrar o fulano de tal no banco de dados mlkada!
                            CadastrarUsuario(view);
                        }else{
                            Toast.makeText(registerActivity.this, "Senhas não iguais", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });

        //Esconde a action Bar
        getSupportActionBar().hide();
    }

    private void CadastrarUsuario(View view){

        //Cadastrando o fulano.
        String email = edit_Email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Snackbar snackbar = Snackbar.make(view, menssagens[1],Snackbar.LENGTH_LONG);
                    snackbar.show();

                    FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                    String getUID = usuarioLogado.getUid();

                    String nome = edit_Nome.getText().toString();
                    String idade = idade_text.getText().toString();
                    String email = edit_Email.getText().toString();



                    int selectID = radioGrup.getCheckedRadioButtonId();
                    final RadioButton radioButton = (RadioButton) findViewById(selectID);

                    if(radioButton.getText() == null){
                        return;
                    }

                    String genero = radioButton.getText().toString();

                    referencia.child(genero).child(getUID).child("Dados do Usuario").child("nome").setValue(nome);
                    referencia.child(genero).child(getUID).child("Dados do Usuario").child("email").setValue(email);
                    referencia.child(genero).child(getUID).child("Dados do Usuario").child("idade").setValue(idade);

                    //Lançar activity dps do cadastro
                    Intent intent = new Intent(registerActivity.this, ListUsersActivity.class);
                    startActivity(intent);

                }else{

                    String erro;
                    try {

                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no mínimo 6 caracteres!";
                    }catch (FirebaseAuthUserCollisionException e) {

                        erro = "Essa conta já foi criada, crie uma nova conta ou clique em esqueci minha senha na tela de login!";
                    }catch (FirebaseAuthInvalidCredentialsException e){

                        erro = "Seu email está digitado errado, verifique novamente!";

                    }catch (Exception e){
                        erro = "Ao cadastrar usuário!";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro,Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

            }
        });

    }
}