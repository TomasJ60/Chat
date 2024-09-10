package co.edu.unipiloto.chatactivitieseintents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity2 extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;
    private ListView listViewMessages;

    private ArrayList<String> messageList;
    private ArrayAdapter<String> adapter;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        editTextMessage = findViewById(R.id.editTextMessage2);
        buttonSend = findViewById(R.id.buttonSend2);
        listViewMessages = findViewById(R.id.listViewMessages);


        sharedPreferences = getSharedPreferences("chat_prefs", MODE_PRIVATE);


        messageList = loadMessagesFromPrefs();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        listViewMessages.setAdapter(adapter);

        // Recibir el mensaje de MainActivity
        String receivedMessage = getIntent().getStringExtra("message");
        if (receivedMessage != null) {
            messageList.add("Planeador: " + receivedMessage);
            adapter.notifyDataSetChanged();
            saveMessagesToPrefs(messageList); // Guardar el historial actualizado
        }

        buttonSend.setOnClickListener(v -> {
            String mensaje = editTextMessage.getText().toString();
            if (!mensaje.isEmpty()) {
                messageList.add("Cuidadano: " + mensaje);
                adapter.notifyDataSetChanged();
                saveMessagesToPrefs(messageList);

                // Enviar el mensaje de vuelta a MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("message_back", mensaje);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Guardar mensajes en SharedPreferences
    private void saveMessagesToPrefs(ArrayList<String> messages) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder messagesString = new StringBuilder();
        for (String message : messages) {
            messagesString.append(message).append(";;");
        }
        editor.putString("messages", messagesString.toString());
        editor.apply();
    }

    // Cargar mensajes desde SharedPreferences
    private ArrayList<String> loadMessagesFromPrefs() {
        String savedMessages = sharedPreferences.getString("messages", "");
        ArrayList<String> messages = new ArrayList<>();
        if (!savedMessages.isEmpty()) {
            String[] messageArray = savedMessages.split(";;");
            messages.addAll(Arrays.asList(messageArray));
        }
        return messages;
    }
}

