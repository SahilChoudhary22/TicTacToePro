package com.revorg.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    //DATABASE INSTANCE
    var database = FirebaseDatabase.getInstance()
    var myRef=database.reference

    var myEmail:String?=null

    var mFirebaseAnalytics:FirebaseAnalytics?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this)

        var b:Bundle= intent.extras
        myEmail = b.getString("email")

    }


    public  fun buClick(view:android.view.View){
        val buSelected= view as Button
        var cellID=0
        when(buSelected.id){
            R.id.bu1-> cellID=1
            R.id.bu2-> cellID=2
            R.id.bu3-> cellID=3
            R.id.bu4-> cellID=4
            R.id.bu5-> cellID=5
            R.id.bu6-> cellID=6
            R.id.bu7-> cellID=7
            R.id.bu8-> cellID=8
            R.id.bu9-> cellID=9

        }
        // Toast.makeText(this,"ID:"+ cellID, Toast.LENGTH_LONG).show()

        PlayGame(cellID,buSelected)
    }

    var player1=ArrayList<Int>()
    var player2=ArrayList<Int>()
    var ActivePlayer=1

    fun PlayGame(cellID:Int,buSelected:Button){

        if(ActivePlayer==1){
            buSelected.text="X"
            buSelected.setBackgroundResource(R.color.blue)
            player1.add(cellID)
            ActivePlayer=2
            AutoPlay()
        }else{
            buSelected.text="O"
            buSelected.setBackgroundResource(R.color.darkgreen)
            player2.add(cellID)
            ActivePlayer=1
        }


        buSelected.isEnabled=false
        CheckWiner()
    }



    fun  CheckWiner(){
        var winer=-1

        // row 1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3)){
            winer=1
        }
        if(player2.contains(1) && player2.contains(2) && player2.contains(3)){
            winer=2
        }


        // row 2
        if(player1.contains(4) && player1.contains(5) && player1.contains(6)){
            winer=1
        }
        if(player2.contains(4) && player2.contains(5) && player2.contains(6)){
            winer=2
        }




        // row 3
        if(player1.contains(7) && player1.contains(8) && player1.contains(9)){
            winer=1
        }
        if(player2.contains(7) && player2.contains(8) && player2.contains(9)){
            winer=2
        }



        // col 1
        if(player1.contains(1) && player1.contains(4) && player1.contains(7)){
            winer=1
        }
        if(player2.contains(1) && player2.contains(4) && player2.contains(7)){
            winer=2
        }



        // col 2
        if(player1.contains(2) && player1.contains(5) && player1.contains(8)){
            winer=1
        }
        if(player2.contains(2) && player2.contains(5) && player2.contains(8)){
            winer=2
        }


        // col 3
        if(player1.contains(3) && player1.contains(6) && player1.contains(9)){
            winer=1
        }
        if(player2.contains(3) && player2.contains(6) && player2.contains(9)){
            winer=2
        }


        if( winer != -1){

            if (winer==1){
                Toast.makeText(this," Player 1  win the game", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this," Player 2  win the game", Toast.LENGTH_LONG).show()

            }

        }

    }


    fun AutoPlay(){

        var emptyCells=ArrayList<Int>()
        for ( cellID in 1..9){

            if(!( player1.contains(cellID) || player2.contains(cellID))) {
                emptyCells.add(cellID)
            }
        }


        val r= Random()
        val randIndex=r.nextInt(emptyCells.size-0)+0
        val cellID= emptyCells[randIndex]

        var buSelect:Button?
        when(cellID){
            1-> buSelect=bu1
            2-> buSelect=bu2
            3-> buSelect=bu3
            4-> buSelect=bu4
            5-> buSelect=bu5
            6-> buSelect=bu6
            7-> buSelect=bu7
            8-> buSelect=bu8
            9-> buSelect=bu9
            else->{
                buSelect=bu1
            }
        }

        PlayGame(cellID,buSelect)

    }

    public fun buRequestEvent(view: View){
        var userEmail = etEmail.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
    }

    public fun buAcceptEvent(view: android.view.View){
        var userEmail = etEmail.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
    }

    fun incomingCalls(){

        myRef.child("Users").child(splitString(myEmail!!)).child("Request")
                .addValueEventListener(object:ValueEventListener{

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        try{
                            var td = dataSnapshot!!.value as HashMap<String,Any>
                            if(td!=null){
                                var value:String
                                for(key in td.keys){
                                    value = td[key] as String
                                    etEmail.setText(value)

                                    myRef.child("Users").child(splitString(myEmail!!)).child("Requests").setValue(true)

                                    break
                                }
                            }

                        }catch (ex:Exception){}
                    }

                    override fun onCancelled(dataSnapshot: DatabaseError?) {

                    }

                })
    }

    fun splitString(str:String):String{

        var split = str.split("@")
        return split[0]
    }
}
