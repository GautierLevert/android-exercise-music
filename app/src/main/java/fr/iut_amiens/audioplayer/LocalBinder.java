package fr.iut_amiens.audioplayer;

import android.app.Service;
import android.os.Binder;

public class LocalBinder extends Binder {

    private Service service;

    public LocalBinder(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
