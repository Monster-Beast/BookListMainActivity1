package com.jnu.booklistmainactivity.data;

import static org.junit.Assert.*;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;

import com.jnu.booklistmainactivity.data.Book;
import com.jnu.booklistmainactivity.data.DataSaver;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DataSaverTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void save() {
        DataSaver dataSaver=new DataSaver();
    }

    @Test
    public void load() {
        DataSaver dataSaver=new DataSaver();
        ArrayList<Book> books=dataSaver.Load(ApplicationProvider.getApplicationContext());
        Assert.assertEquals(0,books.size());
    }
}