package com.mymxhbyf.dongjk.lattecore.net.rx;

import android.content.Context;

import com.mymxhbyf.dongjk.lattecore.net.HttpMethod;
import com.mymxhbyf.dongjk.lattecore.net.RestCreator;
import com.mymxhbyf.dongjk.lattecore.ui.loader.LatteLoader;
import com.mymxhbyf.dongjk.lattecore.ui.loader.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by DongJK on 2018/1/19.
 */

public class RxRestClient {

    private final String URL;
    private static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams() ;


    private final RequestBody BODY;
    private final LoaderStyle LOASER_STYLE;
    //upload
    private final File FILE;

    private final Context CONTEXT;

    public RxRestClient(String url,
                        Map<String, Object> params,
                        RequestBody body,
                        LoaderStyle loaderStyle,
                        File file,
                        Context context) {
        this.URL = url;
        PARAMS.putAll(params);
        this.BODY = body;
        this.LOASER_STYLE = loaderStyle;
        this.FILE = file;
        this.CONTEXT = context;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }

    private Observable<String> request(HttpMethod method){
        final RxRestService service = RestCreator.getRxRestService();
        Observable<String> observable = null;

        if (LOASER_STYLE != null){
            LatteLoader.showLoading(CONTEXT,LOASER_STYLE);
        }

        switch (method){
            case GET:
                observable = service.get(URL,PARAMS);
                break;
            case POST:
                observable = service.post(URL,PARAMS);
                break;
            case POST_RAW:
                observable = service.postRaw(URL,BODY);
                break;
            case PUT:
                observable = service.put(URL,PARAMS);
                break;
            case PUT_RAW:
                observable = service.putRaw(URL,BODY);
                break;
            case DELETE:
                observable = service.delete(URL,PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file",FILE.getName());
                observable = RestCreator.getRxRestService().upload(URL,body);
                break;
            default:
                break;
        }

        return observable;

    }


    public final Observable<String> get(){
        return request(HttpMethod.GET);
    }

    public final Observable<String> post(){
        if (BODY == null){
            return request(HttpMethod.POST);
        }else {
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.POST_RAW);
        }

    }

    public final Observable<String> put(){
        if (BODY == null){
            return request(HttpMethod.PUT);
        }else {
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.PUT_RAW);
        }

    }

    public final Observable<String> delete(){
        return request(HttpMethod.DELETE);
    }

    public final Observable<String> upload(){
        return request(HttpMethod.UPLOAD);
    }

    public final Observable<ResponseBody> download(){
        return RestCreator.getRxRestService().download(URL,PARAMS);
    }
}
