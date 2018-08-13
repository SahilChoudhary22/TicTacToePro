package com.revorg.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    var mFirebaseAuth: FirebaseAuth?=null

    var database = FirebaseDatabase.getInstance()
    var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mFirebaseAuth= FirebaseAuth.getInstance()
    }

    fun buLoginEvent(view:View){

        LoginToFirebase(etEmail.text.toString(),etPassword.text.toString())
    }

    fun LoginToFirebase(email:String,password:String){

        mFirebaseAuth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) {task ->

                    if(task.isSuccessful){
                        Toast.makeText(applicationContext,"Login Successful",Toast.LENGTH_SHORT).show()
                        var currentuser = mFirebaseAuth!!.currentUser
                        //save to Database
                        if(currentuser!=null) {
                            myRef.child("Users").child(splitString(currentuser.email.toString())).child("Request").setValue(currentuser.uid)
                        }

                        LoadMain()

                    } else {
                        Toast.makeText(applicationContext,"Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

    }

    override fun onStart() {
        super.onStart()

        LoadMain()
    }

    fun LoadMain(){

        var currentuser = mFirebaseAuth!!.currentUser

        if(currentuser!=null) {


            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentuser.email)
            intent.putExtra("uid", currentuser.uid)

            startActivity(intent)
        }
    }

    fun splitString(str:String):String{

        var split = str.split("@")
        return split[0]
    }
}
