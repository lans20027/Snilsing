package org.tfoms.snils.xmls;

import java.util.ArrayList;

public class IsFileExistsThread extends Thread {

    private final ArrayList<String> enps;

    public IsFileExistsThread(ArrayList<String> enps){
        this.enps = enps;
    }

    @Override
    public void run() {
        super.run();
    }
}
