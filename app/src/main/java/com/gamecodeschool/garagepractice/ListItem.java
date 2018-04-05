package com.gamecodeschool.garagepractice;

/**
 * Created by FIRAOL DIDA on 08-Mar-18.
 */

public class ListItem {
    private int _id;
    private String _nameList;
    private int _phone;
    private String _desc;
    private int _date;
    private int _month;
    private int _km;
    private String _plateNumber;

    public ListItem() {
    }

    public ListItem(int _id, String _nameList, int _phone, String _desc, int _date, int _month, int _km, String _plateNumber) {
        this._id = _id;
        this._nameList = _nameList;
        this._phone = _phone;
        this._desc = _desc;
        this._date = _date;
        this._month = _month;
        this._km = _km;
        this._plateNumber = _plateNumber;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_nameList() {
        return _nameList;
    }

    public void set_nameList(String _nameList) {
        this._nameList = _nameList;
    }

    public int get_phone() {
        return _phone;
    }

    public void set_phone(int _phone) {
        this._phone = _phone;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public int get_date() {
        return _date;
    }

    public void set_date(int _date) {
        this._date = _date;
    }

    public int get_month() {
        return _month;
    }

    public void set_month(int _month) {
        this._month = _month;
    }

    public String get_plateNumber() {
        return _plateNumber;
    }

    public void set_plateNumber(String _plateNumber) {
        this._plateNumber = _plateNumber;
    }

    public int get_km() {
        return _km;
    }

    public void set_km(int _km) {
        this._km = _km;
    }
}
