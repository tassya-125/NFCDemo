package com.example.nfcdemo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.nfcdemo.R
import com.example.nfcdemo.activity.NFCActivity


class MainFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_main, container, false)
        val  nfc_button = view.findViewById<Button>(R.id.nfc_button)
        nfc_button.setOnClickListener(){
            val intent = Intent(view.context,NFCActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}