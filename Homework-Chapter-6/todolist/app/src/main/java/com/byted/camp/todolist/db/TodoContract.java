package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

public final class TodoContract {

    // TODO 1. 定义创建数据库以及升级的操作
    public static final String SQL_CREATE="CREATE TABLE "+Noteentry.Note_title+"("+
            Noteentry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            Noteentry.Note_date+" INTEGER, "+
            Noteentry.Note_state + " INTEGER, "+
            Noteentry.Note_content + " TEXT, "+
            Noteentry.Note_priority + " INTEGER)";

    public static final String SQL_MODIFY =
            "ALTER TABLE "+Noteentry.Note_title+" ADD " + Noteentry.Note_priority + " INTEGER";

    private TodoContract() {

    }
    public static class Noteentry implements BaseColumns {
        // TODO 2.此处定义表名以及列明
        public static final String Note_title="note";
        public static final String Note_date="date";
        public static final String Note_state="state";
        public static final String Note_content="content";
        public static final String Note_priority="priority";
    }

}
