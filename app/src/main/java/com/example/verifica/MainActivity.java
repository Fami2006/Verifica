package com.example.verifica;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private double initialBudget = 0.0;
    private double totalExpenses = 0.0;
    private double totalIncome = 0.0;
    private StringBuilder expensesByCategory = new StringBuilder();
    private double foodExpenses = 0.0;
    private double transportExpenses = 0.0;
    private double otherExpenses = 0.0;
    private Button foodButton;
    private Button transportButton;
    private Button otherButton;
    private Button incomeButton;
    private EditText descriptionEditText;
    private EditText amountEditText;
    private Button sendButton;
    private TextView resultTextView;
    private String lastCategory = "";

    // Metodo per aggiornare le percentuali
    private void updatePercentages() {
        expensesByCategory.setLength(0); // Pulisco la stringa
        DecimalFormat df = new DecimalFormat("#.##");
        if (initialBudget > 0) {
            if (foodExpenses > 0) {
                double foodPercentage = (foodExpenses / initialBudget) * 100;
                expensesByCategory.append("Cibo: ").append(df.format(foodExpenses)).append("€ (").append(df.format(foodPercentage)).append("%)\n");
            }
            if (transportExpenses > 0) {
                double transportPercentage = (transportExpenses / initialBudget) * 100;
                expensesByCategory.append("Trasporti: ").append(df.format(transportExpenses)).append("€ (").append(df.format(transportPercentage)).append("%)\n");
            }
            if (otherExpenses > 0) {
                double otherPercentage = (otherExpenses / initialBudget) * 100;
                expensesByCategory.append("Altro: ").append(df.format(otherExpenses)).append("€ (").append(df.format(otherPercentage)).append("%)\n");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText budgetEditText = findViewById(R.id.editTextBudget);
        Button confirmBudgetButton = findViewById(R.id.button);
        foodButton = findViewById(R.id.button2);
        transportButton = findViewById(R.id.button3);
        otherButton = findViewById(R.id.button4);
        incomeButton = findViewById(R.id.button5);
        descriptionEditText = findViewById(R.id.editTextDescription);
        amountEditText = findViewById(R.id.editTextAmount);
        sendButton = findViewById(R.id.buttonSend);
        resultTextView = findViewById(R.id.textView3);

        confirmBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Controllo se il budget non è vuoto
                if (!budgetEditText.getText().toString().isEmpty()) {
                    initialBudget = Double.parseDouble(budgetEditText.getText().toString());
                    //Attivo gli altri bottoni
                    foodButton.setEnabled(true);
                    transportButton.setEnabled(true);
                    otherButton.setEnabled(true);
                    incomeButton.setEnabled(true);

                    //Disabilito i bottoni di budget e conferma
                    budgetEditText.setEnabled(false);
                    confirmBudgetButton.setEnabled(false);
                    resultTextView.setText("Budget Iniziale: " + initialBudget + "€\nTotale Spese: " + totalExpenses + "€\nTotale Ricavi: " + totalIncome + "€");
                }
            }
        });

        //Click listeners per Cibo, Trasporti, e Altro
        View.OnClickListener categoryButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rendo la descrizione e l'importo visibili
                descriptionEditText.setVisibility(View.VISIBLE);
                amountEditText.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                if (v.getId() == R.id.button2) {
                    lastCategory = "Cibo";
                } else if (v.getId() == R.id.button3) {
                    lastCategory = "Trasporti";
                } else if (v.getId() == R.id.button4) {
                    lastCategory = "Altro";
                } else if (v.getId() == R.id.button5) {
                    lastCategory = "Ricavo";
                }
            }
        };

        foodButton.setOnClickListener(categoryButtonClickListener);
        transportButton.setOnClickListener(categoryButtonClickListener);
        otherButton.setOnClickListener(categoryButtonClickListener);
        incomeButton.setOnClickListener(categoryButtonClickListener);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descrizione = descriptionEditText.getText().toString();
                String importoString = amountEditText.getText().toString();
                if (!importoString.isEmpty()) {
                    double importo = Double.parseDouble(importoString);
                    if (importo > 250) {
                        Toast.makeText(MainActivity.this, "Limite di 250 superato", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double categoryExpenses = 0.0;
                    double Totale = initialBudget - totalExpenses + totalIncome;
                    if (lastCategory.equals("Cibo")) {
                        if (Totale - importo < 0){
                            Toast.makeText(MainActivity.this, "Fondi mancanti", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        foodExpenses += importo;
                        categoryExpenses = foodExpenses;
                        totalExpenses += importo;
                    } else if (lastCategory.equals("Trasporti")) {
                        if (Totale - importo < 0){
                            Toast.makeText(MainActivity.this, "Fondi mancanti", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        transportExpenses += importo;
                        categoryExpenses = transportExpenses;
                        totalExpenses += importo;
                    } else if (lastCategory.equals("Altro")) {
                        if (Totale - importo < 0){
                            Toast.makeText(MainActivity.this, "Fondi mancanti", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        otherExpenses += importo;
                        categoryExpenses = otherExpenses;
                        totalExpenses += importo;
                    } else if (lastCategory.equals("Ricavo")) {
                        totalIncome += importo;
                        initialBudget += importo;
                        updatePercentages();
                    }
                    if (!lastCategory.equals("Ricavo")) {
                        double percentage = (categoryExpenses / initialBudget) * 100;
                        DecimalFormat df = new DecimalFormat("#.##");
                        expensesByCategory.append(lastCategory).append(": ").append(df.format(importo)).append("€ (").append(df.format(percentage)).append("%)\n");
                    } else {
                        expensesByCategory.append(lastCategory).append(": ").append(importo).append("€\n");
                    }
                    Totale = initialBudget - totalExpenses + totalIncome;
                    DecimalFormat df = new DecimalFormat("#.##");
                    resultTextView.setText("Budget Iniziale: " + initialBudget + "€\nTotale Spese: " + totalExpenses + "€\nTotale Ricavi: " + totalIncome + "€\n" + expensesByCategory.toString() + "Totale Generale: " + df.format(Totale) + "€");
                    descriptionEditText.setText("");
                    amountEditText.setText("");
                    descriptionEditText.setVisibility(View.GONE);
                    amountEditText.setVisibility(View.GONE);
                    sendButton.setVisibility(View.GONE);
                }
            }
        });
    }
}