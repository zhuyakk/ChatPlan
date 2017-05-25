package com.android.chatplan

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.Socket


class MainActivity : AppCompatActivity(){

    private var mEditView :EditText? = null
    private var mSendView :TextView? = null
    private var mListView :RecyclerView ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar) as Toolbar)
        if (supportActionBar != null) {
            supportActionBar?.title = ""
        }

        mEditView = findViewById(R.id.edit) as EditText
        mSendView = findViewById(R.id.send) as TextView
        mListView = findViewById(R.id.content) as RecyclerView

        mSendView!!.setOnClickListener(View.OnClickListener {
            if (mEditView!!.text == null){
                return@OnClickListener
            }

            Thread(Runnable {
                var sendMessage = mEditView!!.text.toString()
                var socket = Socket(Const.SERVER_IP, Const.SERVER_PORT)
                var writer = BufferedWriter(OutputStreamWriter(
                        socket.getOutputStream()))
                if (writer != null){
                    writer.write(sendMessage)
                    writer.flush()
                    socket.shutdownOutput()
                    writer.close()
                    socket.close()
                }
                mSendView!!.post {
                    mEditView!!.text = null
                    Toast.makeText(this, R.string.send_successful, Toast.LENGTH_SHORT).show()
                }
            }).start()
        })
    }
}
