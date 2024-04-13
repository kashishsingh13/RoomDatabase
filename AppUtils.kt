package com.example.practiceapplication

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Patterns
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern

class AppUtils {
     companion object{
        private const val PASSWORD_PATTERN =
             "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
         private const val PERSON_PATTERN=  "^[a-zA-Z]+$"

         fun  isvalidemail(email: String):Boolean{
             return Patterns.EMAIL_ADDRESS.matcher(email).matches()

         }
         fun isvalidpass(password:String):Boolean{
             return Pattern.matches(PASSWORD_PATTERN,password)
         }
         fun isvalidcontact(contact:String):Boolean{
            return contact.length == 10
         }
         fun isvalidname(name:String):Boolean{
             return Pattern.matches(PERSON_PATTERN,name)
         }
         fun getBitmapFromAbsolutePath(filePath: String): Bitmap? {
             return try {
                 val file = File(filePath)
                 val inputStream = FileInputStream(file)
                 val bitmap = BitmapFactory.decodeStream(inputStream)
                 inputStream.close()
                 bitmap
             } catch (e: IOException) {
                 e.printStackTrace()
                 null
             }
         }

         fun getImagePathFromBitmap(context: Context, bitmap: Bitmap?,imagePath: String): String? {
             if (imagePath.isEmpty()) {
                 var root: File = context.filesDir
                 var fileName = "${System.currentTimeMillis()}.png"

                 var file = File(root.path, fileName)
                 try {
                     val fileOutputStream = FileOutputStream(file)
                     bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                     fileOutputStream.flush()
                     fileOutputStream.close()
                 } catch (e: IOException) {
                     e.printStackTrace()
                     return null
                 }

                 return file.absolutePath


             }
             return imagePath
         }






     }
}