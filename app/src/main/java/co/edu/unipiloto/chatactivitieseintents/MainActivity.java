package co.edu.unipiloto.chatactivitieseintents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;
    private ListView listViewMessages;

    private ArrayList<String> messageList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        listViewMessages = findViewById(R.id.listViewMessages);

        // Inicializar la lista de mensajes y el adaptador
        messageList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        listViewMessages.setAdapter(adapter);

        // Recibir un mensaje de MainActivity2 (si se envió)
        String receivedMessage = getIntent().getStringExtra("message_back");
        if (receivedMessage != null) {
            messageList.add("Cuidadano: " + receivedMessage);
            adapter.notifyDataSetChanged();
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = editTextMessage.getText().toString();
                if (!mensaje.isEmpty()) {
                    messageList.add("Planeador: " + mensaje);
                    adapter.notifyDataSetChanged();

                    // Enviar el mensaje a la otra actividad
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("message", mensaje);
                    startActivityForResult(intent, 1);  // Aquí utilizamos startActivityForResult
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String responseMessage = data.getStringExtra("message_back");
            if (responseMessage != null) {
                messageList.add("Cuidadano: " + responseMessage);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
