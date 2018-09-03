package th.co.itg.ktb_demo;

import android.content.Context;
import android.net.Uri;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bikomobile.multipart.Multipart;
import com.google.gson.Gson;
import com.yanzhenjie.album.AlbumFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by ToCHe on 29/8/2018 AD.
 */
public class Service {

    ServiceListener listener;
    Context context;
    String idCard;
    String apiUploadSignature = "http://47.74.244.86/poc/api/Files/Upload_signatrue";
    String apiSendInformation = "http://47.74.244.86/poc/api/insert_info";
    String apiUploadDocument = "http://47.74.244.86/poc/api/Files/Upload_doc";
    String apiMapDocument = "http://47.74.244.86/poc/api/insert_otherimg_info";
    JSONObject information;
    ArrayList<AlbumFile> albumFiles;
    int countUpload = 0;

    public Service(ServiceListener listener, Context context, String idCard, JSONObject information,ArrayList<AlbumFile> files) {
        this.listener = listener;
        this.context = context;
        this.idCard = idCard;
        this.information = information;
        this.albumFiles = files;
    }

    public void uploadSignature(File file){
        Multipart multipart = new Multipart(context);
        multipart.addFile("image/jpeg",
                "image",file.getName(),
                Uri.fromFile(file));

        multipart.launchRequest(apiUploadSignature, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String json = null;
                try {
                    json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    System.out.println(json);
                    sendInformation(json,information);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

    }

    public void sendInformation(String urlSignature,JSONObject json){
        try {
            json.put("imageSignature_filename",urlSignature);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, apiSendInformation, json, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response);
                            uploadDocument(albumFiles);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error

                        }
                    });
            Volley.newRequestQueue(context).add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void uploadDocument(ArrayList<AlbumFile> albumFiles){
        for (AlbumFile album : albumFiles){
            File file = new File(album.getPath());
            Multipart multipart = new Multipart(context);
            multipart.addFile("image/jpeg",
                    "image",file.getName(),
                    Uri.fromFile(file));

            multipart.launchRequest(apiUploadDocument, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String json = null;
                    try {
                        json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        System.out.println(json);
                        mapDocumentWithCardID(json);
                        countUploadDocument();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    countUploadDocument();
                }
            });
        }
        listener.onSaveCaseComplete();
        System.out.println("send success");
    }

    public void mapDocumentWithCardID(String url){
        JSONObject json = new JSONObject();

        try {
            json.put("idCard",idCard);
            json.put("img",url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, apiMapDocument, json,
                            new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error

                        }
                    });
            Volley.newRequestQueue(context).add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void countUploadDocument(){
        countUpload++;
        if (albumFiles.size() == countUpload){
            listener.onSaveCaseComplete();
        }

    }


    interface ServiceListener{
        void onSaveCaseComplete();
    }
}
