package com.example.oogway

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import android.graphics.Bitmap


class ProfileFragment : Fragment() {

    private lateinit var imageView : ImageView
    private lateinit var usernameTV : TextView
    private lateinit var usernameET : EditText
    private lateinit var userUrlET : EditText
    private lateinit var updateBtn : Button
    private lateinit var logOutBtn : Button

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageView)
        usernameTV = view.findViewById(R.id.usernameTV)

        usernameET = view.findViewById(R.id.usernameET)
        userUrlET = view.findViewById(R.id.userUrlET)

        updateBtn = view.findViewById(R.id.updateBtn)
        logOutBtn = view.findViewById(R.id.logOutBtn)

        val transformation: Transformation = object : Transformation {
            override fun transform(source: Bitmap): Bitmap {
                val desiredWidth = 100
                val desiredHeight = 100

                val scaledBitmap = Bitmap.createScaledBitmap(source, desiredWidth, desiredHeight, true)
                if (scaledBitmap != source) {
                    source.recycle()
                }

                return scaledBitmap
            }

            override fun key(): String {
                return "resizeTransformation"
            }
        }

        getUser(FirebaseAuth.getInstance().uid!!) { user ->
            usernameTV.text = user.username
            Picasso.get().load(user.profileImage).transform(transformation).into(imageView)
        }
    }

    private fun getUser(documentId: String, callback: (User) -> Unit) {
        usersCollection.document(documentId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.getString("username").toString()
                    val profileImage = document.getString("image").toString()
                    val user = User(username, profileImage)
                    callback(user)
                }
            }
    }
}