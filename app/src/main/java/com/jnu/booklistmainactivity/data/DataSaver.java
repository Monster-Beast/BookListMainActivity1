package com.jnu.booklistmainactivity.data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataSaver {
    public void Save(Context context, ArrayList<Book> data){

        try {
            FileOutputStream fileOutputStream = context.openFileOutput("mydata.dat", Context.MODE_APPEND);
            ObjectOutputStream out=new ObjectOutputStream(fileOutputStream);
            out.writeObject(data);
            out.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Book> Load(Context context){
        ArrayList<Book> data=new ArrayList<>();
        try{
            FileInputStream fileInputStream=context.openFileInput("mydata.dat");
            ObjectInputStream in=new ObjectInputStream(fileInputStream);
            data=(ArrayList<Book>) in.readObject();
            in.close();
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
}
