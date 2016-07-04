package com.example.codru.stendensocial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
/*
 * Made by Marin Codrut, Tim Vaneker, Smith Chen and Flavian Gall
 */
public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView about = (TextView)  findViewById(R.id.about);
        about.setText("about stenden social:\n" +
                "\n" +
                "Stenden Social is here to help students and potential \n" +
                "1) students to connect with one and other\n" +
                "2) sell used books \n" +
                "3) being updated about events that are about to happen or going to happen.\n" +
                "\n" +
                "Made by:\n" +
                "G-T applications\n" +
                "\n" +
                "members:\n" +
                "Tim vaneker- Leader\n" +
                "Smith chen - Co- leader\n" +
                "Codrut Marin - Programer\n" +
                "Flavian - Secretary\n" +
                "Ana are mere!");
    }
}
