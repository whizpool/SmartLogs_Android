package com.example.supportsystemdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.whizpool.supportsystem.utils.CoroutineCallback;
import com.whizpool.supportsystem.utils.SLogFileUtils;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;

public class Example extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        BuildersKt.launch(
                CoroutineScopeKt.MainScope(),
                (CoroutineContext) Dispatchers.getMain(),
                CoroutineStart.DEFAULT,
                (Function2<CoroutineScope, Continuation<? super Unit>, Unit>) (coroutineScope, continuation) -> {
                    // your code here
                    SLogFileUtils.INSTANCE.deleteFiles(true, () -> null, CoroutineCallback.Companion.call((aBoolean, error) -> {

                    }));

                    return Unit.INSTANCE;
                }
        );



    }

    public Example() {

    }


}
