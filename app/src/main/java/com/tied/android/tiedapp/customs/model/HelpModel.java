package com.tied.android.tiedapp.customs.model;

import com.tied.android.tiedapp.R;

/**
 * Created by greepon123 on 6/6/2016.
 */
public class HelpModel {

    public static final HelpModel[] helpLists = new HelpModel[] {
            new HelpModel("How does Tied work?","Lorem ipsum dolor sit amet, consectetur adipiscing " +
                    "elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ", R.mipmap.email_icon),
            new HelpModel("How can I add multiple clients?","Venice",R.mipmap.email_icon),
            new HelpModel("Who can use Tied?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",R.mipmap.email_icon),
            new HelpModel("Is Tied that helpful?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua.",R.mipmap.email_icon),
            new HelpModel("How much does it cost?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod",R.mipmap.email_icon),
    };

    private int image;
    private  String title;
    private String description;


    public HelpModel(String title, String description,int image) {
        this.image = image;
        this.title = title;
        this.description = description;

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
