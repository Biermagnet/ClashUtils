package de.michi.clashutils.utils;

import java.util.ArrayList;

public class MessageSplitter {


    private String text;
    private ArrayList<String> paragraphs;
    private int length;

    public MessageSplitter(int length) {
        text = "";
        this.length = 2000;
        if (length != 0) this.length = length;
        paragraphs = new ArrayList<>();
    }


    public void add(String toAdd) {
        if (this.text.length() + toAdd.length() > this.length) {
            paragraphs.add(text);
            this.text = toAdd;
        } else {
            this.text += toAdd;
        }

    }

    public void addLineBreaker() {
        this.text += "\n";
    }

    public ArrayList<String> getMessages() {
        if (this.text != "") {
            paragraphs.add(text);
            text = "";
        }
        return paragraphs;
    }
}
