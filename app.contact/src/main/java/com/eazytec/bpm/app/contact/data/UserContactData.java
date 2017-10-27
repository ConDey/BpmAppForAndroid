package com.eazytec.bpm.app.contact.data;


import com.eazytec.bpm.app.contact.utils.CnToSpell;

/**
 *
 * @author Beckett_W
 * @version Id: UserContactData, v 0.1 2017/7/6 9:20 Administrator Exp $$
 */
public class UserContactData implements Comparable<UserContactData>{

    private String name; // 姓名
    private String phoneNum; //电话号码
    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母

    public UserContactData(){

    }

    public UserContactData(String name ,String phoneNum){
     this.name = name;
     this.phoneNum = phoneNum;
     pinyin = CnToSpell.getPinYin(name); // 根据姓名获取拼音
     firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
     if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
     firstLetter = "#";
        }
    }


    public String getName()

    {
        if(name!=null){
        return name.trim();
        }else return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }


    @Override
    public int compareTo(UserContactData another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }
}
