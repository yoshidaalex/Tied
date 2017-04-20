package com.tied.android.tiedapp.objects.responses;

import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects._Meta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class ClientRes implements Serializable {
    private String id;
    private boolean success;
    private String message;
    private boolean authFailed;
    private _Meta _meta;
    private ArrayList<Client> clients;
    private Client client;

    public ClientRes(String id, boolean success, String message, boolean authFailed, _Meta _meta, ArrayList<Client> clients, Client client) {
        this.id = id;
        this.success = success;
        this.message = message;
        this.authFailed = authFailed;
        this._meta = _meta;
        this.clients = clients;
        this.client = client;
    }

    public _Meta get_meta() {
        return _meta;
    }

    public void set_meta(_Meta _meta) {
        this._meta = _meta;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAuthFailed() {
        return authFailed;
    }

    public void setAuthFailed(boolean authFailed) {
        this.authFailed = authFailed;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClientRes{" +
                "id='" + id + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", authFailed=" + authFailed +
                ", _meta=" + _meta +
                ", clients=" + clients +
                ", client=" + client +
                '}';
    }
}
