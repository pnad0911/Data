package com.example.data;

import android.provider.BaseColumns;

public final class DataContract {
    private DataContract() {}

    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "Registration";
        public static final String COLUMN_DEVICE = "Device_ID";
        public static final String COLUMN_EMAIL = "Email_Address";
        public static final String COLUMN_IS_REG = "Is_Registered";
        public static final String COLUMN_SERVER = "Server_Address";
    }
}
