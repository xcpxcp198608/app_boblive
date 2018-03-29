package com.wiatec.boblive.utils.OkHttp.Listener;

import android.text.TextUtils;

import com.px.kotlin.utils.SPUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.Subject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by patrick on 2016/12/22.
 */

public abstract class StringListener implements Callback {

    public abstract void onSuccess (String s) throws IOException;
    public abstract void onFailure (String e);

    @Override
    public void onFailure(Call call, IOException e) {
        if(e.getMessage() == null){
            onFailure("unknown error");
            return;
        }
        Observable.just(e.getMessage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        onFailure(s);
                    }
                });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Headers headers = response.headers();
        List<String> cookies = headers.values("Set-Cookie");
        if(cookies != null && cookies.size() > 0 ) {
            String session = cookies.get(0);
            String cookie = session.substring(0, session.indexOf(";"));
//            SPUtil.put("cookie", cookie);
        }
        Observable.just(response)
                .map(new Function<Response, String>() {
                    @Override
                    public String apply(Response response) throws Exception {
                        try {
                            return response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "";
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subject<String>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            if(TextUtils.isEmpty(s)) {
                                onFailure("request result is empty");
                            }else{
                                onSuccess(s);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e.getMessage() != null){
                            onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
